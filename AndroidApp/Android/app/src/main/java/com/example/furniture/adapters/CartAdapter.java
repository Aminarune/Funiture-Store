package com.example.furniture.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.furniture.R;
import com.example.furniture.models.Cart;
import com.example.furniture.models.Product;
import com.example.furniture.services.CheckCart;
import com.example.furniture.services.OnDataGetCart;
import com.example.furniture.services.OnDataSaveCart;
import com.example.furniture.services.UpdateToCart;
import com.example.furniture.utilities.NumberUtilities;
import com.example.furniture.utilities.OnDataPassProduct;
import com.example.furniture.views.DetailProductActivity;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHD> {

    Context context;

    ArrayList<Product> productArrayList;

    ArrayList<Cart> cartArrayList;

    SetOnClickCart setOnClickCart;

    String str = "###.###";

    OnDataPassProduct onDataPassProduct;

    public void setOnDataPassProduct(OnDataPassProduct onDataPassProduct) {
        this.onDataPassProduct = onDataPassProduct;
    }

    public CartAdapter(Context context, ArrayList<Product> productArrayList,
                       ArrayList<Cart> cartArrayList) {
        this.context = context;
        this.productArrayList = productArrayList;
        this.cartArrayList = cartArrayList;
    }

    public void setSetOnClickCart(SetOnClickCart setOnClickCart) {
        this.setOnClickCart = setOnClickCart;
    }

    @NonNull
    @Override
    public ViewHD onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.item_cart, parent, false);
        CartAdapter.ViewHD viewHD = new CartAdapter.ViewHD(view);
        return viewHD;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHD holder, @SuppressLint("RecyclerView") int position) {
        Product product = productArrayList.get(position);
        holder.ivCartProduct.setImageBitmap(product.getPicture());
        holder.tvCartProduct.setText(product.getName());


        Cart cart = cartArrayList.get(position);
        holder.tvQuantityCart.setText(formatString(cart.getQuantity()));
        holder.tvPriceProductCart.setText(String.valueOf(cart.getPrice()));
        holder.tvTotalPriceCart.setText(String.valueOf(cart.getTotalPrice()));



        if (cartArrayList.get(position).getQuantity() == 10) {
            holder.tvInscreaseCart.setEnabled(false);
        } else {
            holder.tvInscreaseCart.setEnabled(true);
        }

        if (cartArrayList.get(position).getQuantity() == 1) {
            holder.tvDescreaseCart.setEnabled(false);
        } else {
            holder.tvDescreaseCart.setEnabled(true);
        }
        holder.tvInscreaseCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int quantity = cartArrayList.get(position).getQuantity() + 1;

                float first =Float.valueOf(product.getPrice());
                float total = Float.valueOf(cart.getTotalPrice());
                float result = total+first;

                holder.tvTotalPriceCart.setText(
                        NumberUtilities.getFloatDecimal(str).format(result));
                holder.tvQuantityCart.setText(formatString(quantity));

                float temp = Float.parseFloat(NumberUtilities.getFloatDecimal(str).format(result));
                cart.setTotalPrice(temp);
                cart.setQuantity(quantity);

                setOnClickCart.onIncreaseItem(view, cart, quantity);
                notifyDataSetChanged();
            }
        });

        holder.tvDescreaseCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int quantity = cartArrayList.get(position).getQuantity() - 1;

                float first =Float.valueOf(product.getPrice());
                float total = Float.valueOf(cart.getTotalPrice());
                float result = total-first;

                holder.tvTotalPriceCart.setText(
                        NumberUtilities.getFloatDecimal(str).format(result));
                holder.tvQuantityCart.setText(formatString(quantity));

                cart.setQuantity(quantity);
                float temp = Float.parseFloat(NumberUtilities.getFloatDecimal(str).format(result));
                cart.setTotalPrice(temp);

                setOnClickCart.onDecreaseItem(view, cart, quantity);
                notifyDataSetChanged();


            }
        });

        holder.ivCartRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setOnClickCart.onRemoveItem(view, cart, position);
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setOnClickCart.onClickItemCart(view,product);
            }
        });

    }

    @Override
    public int getItemCount() {
        return cartArrayList.size();
    }

    class ViewHD extends RecyclerView.ViewHolder {

        ImageView ivCartProduct, ivCartRemove;

        TextView tvCartProduct, tvPriceProductCart, tvQuantityCart, tvInscreaseCart, tvDescreaseCart,tvTotalPriceCart;

        public ViewHD(@NonNull View itemView) {
            super(itemView);
            ivCartProduct = itemView.findViewById(R.id.ivCartProduct);
            ivCartRemove = itemView.findViewById(R.id.ivCartRemove);
            tvCartProduct = itemView.findViewById(R.id.tvCartProduct);
            tvPriceProductCart = itemView.findViewById(R.id.tvPriceProductCart);
            tvQuantityCart = itemView.findViewById(R.id.tvQuantityCart);
            tvInscreaseCart = itemView.findViewById(R.id.tvInscreaseCart);
            tvDescreaseCart = itemView.findViewById(R.id.tvDescreaseCart);
            tvTotalPriceCart=itemView.findViewById(R.id.tvTotalPriceCart);
        }
    }

    public interface SetOnClickCart {
        void onRemoveItem(View view, Cart cart, int pos);

        void onIncreaseItem(View view, Cart cart, int quantity);

        void onDecreaseItem(View view, Cart cart, int quantity);

        void onClickItemCart(View view, Product product);
    }

    private String formatString(int number) {
        if (number >= 10) {
            return String.valueOf(number);
        }
        return "0" + String.valueOf(number);
    }


}
