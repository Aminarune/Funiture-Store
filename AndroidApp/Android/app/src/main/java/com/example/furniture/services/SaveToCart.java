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
import com.example.furniture.models.Favourite;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SaveToCart extends AsyncTask<Void, Void, Void> {

    String id_User;
    String id_Product;
    int quantity;
    float price;
    float totalPrice;
    RequestQueue queue;

    OnDataSaveCart onDataSaveCart;

    private String urlCart = Api.url + "cart";


    public SaveToCart(String id_User, String id_Product, int quantity, float price, float totalPrice, RequestQueue queue, OnDataSaveCart onDataSaveCart) {
        this.id_User = id_User;
        this.id_Product = id_Product;
        this.quantity = quantity;
        this.price = price;
        this.totalPrice = totalPrice;
        this.queue = queue;
        this.onDataSaveCart = onDataSaveCart;
    }

    @Override
    protected Void doInBackground(Void... voids) {

        UUID uuid = UUID.randomUUID();
        String id = uuid.toString();

        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);


        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlCart, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson = new GsonBuilder().create();
                Cart cart = gson.fromJson(response, Cart.class);
                onDataSaveCart.onSuccess(true);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                onDataSaveCart.onFailure(error.getMessage());
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("Id", id);
                params.put("Id_User", id_User);
                params.put("Id_Product", id_Product);
                params.put("Quantity", String.valueOf(quantity));
                params.put("Price", df.format(price));
                params.put("Total_Price", df.format(totalPrice));
                return params;
            }
        };
        queue.add(stringRequest);


        return null;
    }


}
