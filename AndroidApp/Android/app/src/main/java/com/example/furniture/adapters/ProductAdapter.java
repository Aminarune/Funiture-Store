package com.example.furniture.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.furniture.R;
import com.example.furniture.models.Product;

import java.util.ArrayList;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHD> {

    private Context context;
    private ArrayList<Product> productArrayList;
    private SetOnClickItemProduct onClickItemProduct;

    private SetOnClickItemPurchase setOnClickItemPurchase;

    public void setSetOnClickItemPurchase(SetOnClickItemPurchase setOnClickItemPurchase) {
        this.setOnClickItemPurchase = setOnClickItemPurchase;
    }

    public void setOnClickItemProduct(SetOnClickItemProduct onClickItemProduct) {
        this.onClickItemProduct = onClickItemProduct;
    }

    public ProductAdapter(Context context, ArrayList<Product> productArrayList) {
        this.context = context;
        this.productArrayList = productArrayList;
    }

    @NonNull
    @Override
    public ViewHD onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.item_product, parent, false);
        ViewHD viewHD = new ViewHD(view);
        return viewHD;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHD holder, @SuppressLint("RecyclerView") int position) {
        Product product = productArrayList.get(position);
        holder.imageView.setImageBitmap(product.getPicture());
        holder.textName.setText(product.getName());
        holder.textPrice.setText(product.getPrice());

        holder.ivItemPurchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setOnClickItemPurchase.setOnClickItemPurchase(view,position);
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickItemProduct.setOnClickItemProduct(view, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return productArrayList.size();
    }

    class ViewHD extends RecyclerView.ViewHolder {
        ImageView imageView,ivItemPurchase;
        TextView textName, textPrice;

        public ViewHD(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.ivItemProduct);
            textName = itemView.findViewById(R.id.tvNameProduct);
            textPrice = itemView.findViewById(R.id.tvPriceProduct);
            ivItemPurchase=itemView.findViewById(R.id.ivItemPurchase);
        }
    }


    public interface SetOnClickItemProduct {
        void setOnClickItemProduct(View view, int pos);
    }

    public interface SetOnClickItemPurchase{
        void setOnClickItemPurchase(View view,int pos);
    }

}
