package com.example.furniture.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
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
import java.util.List;

public class CartAdapter extends ArrayAdapter<Cart> {

    Context context;

    ArrayList<Product> productArrayList;


    SetOnClickCart setOnClickCart;

    String str = "###.###";

    OnDataPassProduct onDataPassProduct;

    public CartAdapter(@NonNull Context context, @NonNull List<Cart> objects) {
        super(context, 0, objects);
    }

    public void setOnDataPassProduct(OnDataPassProduct onDataPassProduct) {
        this.onDataPassProduct = onDataPassProduct;
    }

    public void setProductArrayList(ArrayList<Product> productArrayList) {
        this.productArrayList = productArrayList;
    }


    public void setSetOnClickCart(SetOnClickCart setOnClickCart) {
        this.setOnClickCart = setOnClickCart;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Cart cart = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_cart, parent, false);
        }


        ImageView ivCartProduct = convertView.findViewById(R.id.ivCartProduct);
        ImageView ivCartRemove = convertView.findViewById(R.id.ivCartRemove);
        TextView tvCartProduct = convertView.findViewById(R.id.tvCartProduct);
        TextView tvPriceProductCart = convertView.findViewById(R.id.tvPriceProductCart);
        TextView tvQuantityCart = convertView.findViewById(R.id.tvQuantityCart);
        TextView tvInscreaseCart = convertView.findViewById(R.id.tvInscreaseCart);
        TextView tvDescreaseCart = convertView.findViewById(R.id.tvDescreaseCart);
        TextView tvTotalPriceCart = convertView.findViewById(R.id.tvTotalPriceCart);


        Product product = productArrayList.get(position);
        ivCartProduct.setImageBitmap(product.getPicture());
        tvCartProduct.setText(product.getName());

        tvQuantityCart.setText(formatString(cart.getQuantity()));
        tvPriceProductCart.setText(String.valueOf(cart.getPrice()));
        tvTotalPriceCart.setText(String.valueOf(cart.getTotalPrice()));


        if (cart.getQuantity() == 10) {
            tvInscreaseCart.setEnabled(false);
        } else {
            tvInscreaseCart.setEnabled(true);
        }

        if (cart.getQuantity() == 1) {
            tvDescreaseCart.setEnabled(false);
        } else {
            tvDescreaseCart.setEnabled(true);
        }

        tvInscreaseCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int quantity = cart.getQuantity() + 1;

                float first = Float.valueOf(product.getPrice());
                float total = Float.valueOf(cart.getTotalPrice());
                float result = total + first;

                tvTotalPriceCart.setText(
                        NumberUtilities.getFloatDecimal(str).format(result));
                tvQuantityCart.setText(formatString(quantity));

                float temp = Float.parseFloat(NumberUtilities.getFloatDecimal(str).format(result));
                cart.setTotalPrice(temp);
                cart.setQuantity(quantity);

                setOnClickCart.onIncreaseItem(view, cart, quantity);
                notifyDataSetChanged();
            }
        });

        tvDescreaseCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int quantity = cart.getQuantity() - 1;

                float first = Float.valueOf(product.getPrice());
                float total = Float.valueOf(cart.getTotalPrice());
                float result = total - first;

                tvTotalPriceCart.setText(
                        NumberUtilities.getFloatDecimal(str).format(result));
                tvQuantityCart.setText(formatString(quantity));

                cart.setQuantity(quantity);
                float temp = Float.parseFloat(NumberUtilities.getFloatDecimal(str).format(result));
                cart.setTotalPrice(temp);

                setOnClickCart.onDecreaseItem(view, cart, quantity);
                notifyDataSetChanged();


            }
        });

        ivCartRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setOnClickCart.onRemoveItem(view, cart, position);
            }
        });

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setOnClickCart.onClickItemCart(view, product);
            }
        });


        return convertView;
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
