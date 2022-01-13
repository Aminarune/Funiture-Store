package com.example.furniture.services;

import android.content.Context;
import android.os.AsyncTask;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.furniture.models.Cart;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class GetCart extends AsyncTask<Void, Void, Void> {

    Context view;

    String id_User;
    RequestQueue queue;

    String shippingAddressId;

    OnDataCartList onDataCartList;

    private String urlFav = Api.urlLocal + "cart";


    ArrayList<Cart> arrayList = new ArrayList<>();


    public GetCart(Context view, String id_User,String shippingAddressId, RequestQueue queue, OnDataCartList onDataCartList) {
        this.view = view;
        this.id_User = id_User;
        this.queue = queue;
        this.onDataCartList = onDataCartList;
        this.shippingAddressId=shippingAddressId;
    }

    @Override
    protected Void doInBackground(Void... voids) {

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(urlFav,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        if (response.length() > 0) {
                            for (int i = 0; i < response.length(); i++) {
                                try {
                                    JSONObject jsonObject = response.getJSONObject(i);
                                    String id = jsonObject.getString("Id");
                                    String idUser = jsonObject.getString("Id_User");
                                    if (idUser.equals(id_User)) {

                                        String idPro = jsonObject.getString("Id_Product");
                                        int quantity = jsonObject.getInt("Quantity");
                                        float price = Float.valueOf(jsonObject.getString("Price"));
                                        float total = Float.valueOf(jsonObject.getString("Total_Price"));

                                        Cart cart = new Cart(id, idUser, idPro, quantity, price, total);
                                        arrayList.add(cart);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            onDataCartList.onDataCartFound(view, arrayList);
                        }
                        else {
                            onDataCartList.onDataCartNotFound(view,"Do not have any item.");
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                onDataCartList.onDataCartNotFound(view, error.getMessage());
            }
        });


        queue.add(jsonArrayRequest);

        return null;
    }
}
