package com.example.furniture.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.example.furniture.R;
import com.example.furniture.models.Favourite;
import com.example.furniture.models.Product;
import com.example.furniture.services.DownloadProductById;
import com.example.furniture.services.OnDataProductByID;

import java.util.ArrayList;
import java.util.Queue;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.ViewHD>{

    Context context;

    //id product in fav to bind view
    ArrayList<Product> productArrayList;

    //id fav to remove
    ArrayList<Favourite> favouriteArrayList;

    SetOnClickFav onClickFav;

    public FavoriteAdapter(Context context, ArrayList<Product> productArrayList, ArrayList<Favourite> favouriteArrayList, SetOnClickFav onClickFav) {
        this.context = context;
        this.productArrayList = productArrayList;
        this.favouriteArrayList = favouriteArrayList;
        this.onClickFav = onClickFav;
    }

    @NonNull
    @Override
    public ViewHD onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(context);
        View view=layoutInflater.inflate(R.layout.item_fav,parent,false);
        ViewHD viewHD=new ViewHD(view);
        return viewHD;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHD holder, int position) {
        Product product=productArrayList.get(position);
        holder.ivFavProduct.setImageBitmap(product.getPicture());
        holder.tvFavProduct.setText(product.getName());
        holder.tvPriceProduct.setText("$ "+product.getPrice());

        Favourite favourite=favouriteArrayList.get(position);
        holder.ivFavRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickFav.onRemoveItem(favourite.getId(),favourite.getIdUser(),favourite.getIdProduct());
            }
        });

        holder.ivAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickFav.onAddItem(favourite.getIdUser(),favourite.getIdProduct());
            }
        });
    }

    @Override
    public int getItemCount() {
        return productArrayList.size();
    }

    public class ViewHD extends RecyclerView.ViewHolder {

        ImageView ivFavProduct, ivAddProduct, ivFavRemove;

        TextView tvFavProduct, tvPriceProduct;


        public ViewHD(@NonNull View itemView) {
            super(itemView);

            ivFavProduct = itemView.findViewById(R.id.ivFavProduct);
            ivAddProduct = itemView.findViewById(R.id.ivAddProduct);
            ivFavRemove = itemView.findViewById(R.id.ivFavRemove);
            tvFavProduct = itemView.findViewById(R.id.tvFavProduct);
            tvPriceProduct = itemView.findViewById(R.id.tvPriceProduct);

        }
    }

    public interface SetOnClickFav{
        void onRemoveItem(String idFav,String idUser,String idProduct);
        void onAddItem(String idUser,String idProduct);
    }

}