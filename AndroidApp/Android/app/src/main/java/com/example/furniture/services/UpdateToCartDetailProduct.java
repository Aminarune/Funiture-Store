package com.example.furniture.services;

import android.os.AsyncTask;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.furniture.models.Cart;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

public class UpdateToCartDetailProduct extends AsyncTask<Void, Void, Void> {


    int quantity;
    RequestQueue queue;
    Cart cart_found;


    OnDataSaveCart onDataSaveCart;

    private String urlCart = Api.urlLocal + "cart";


    public UpdateToCartDetailProduct(int quantity,
                                     RequestQueue queue, Cart cart_found, OnDataSaveCart onDataSaveCart) {
        this.quantity = quantity;
        this.queue = queue;
        this.cart_found = cart_found;
        this.onDataSaveCart = onDataSaveCart;
    }

    @Override
    protected Void doInBackground(Void... voids) {

        String link = urlCart + "/" + cart_found.getId();


        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);

        int quan = quantity + cart_found.getQuantity();

        float totalPrice = cart_found.getPrice() * quan;

        StringRequest stringRequest = new StringRequest(Request.Method.PUT, link, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Gson gson = new GsonBuilder().create();
                Cart cart = gson.fromJson(response, Cart.class);
                onDataSaveCart.onSuccess(true);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("Id", cart_found.getId());
                params.put("Id_User", cart_found.getIdUser());
                params.put("Id_Product", cart_found.getIdProduct());
                params.put("Quantity", String.valueOf(quan));
                params.put("Price", df.format(cart_found.getPrice()));
                params.put("Total_Price", df.format(totalPrice));
                return params;
            }
        };
        queue.add(stringRequest);


        return null;
    }


}
