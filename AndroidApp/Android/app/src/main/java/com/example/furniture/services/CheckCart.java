package com.example.furniture.services;

import android.os.AsyncTask;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.furniture.models.Cart;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CheckCart extends AsyncTask<Void, Void, Void> {

    String idUser;
    String idProduct;
    RequestQueue queue;

    OnDataGetCart onDataGetCart;

    public CheckCart(String idUser, String idProduct, RequestQueue queue, OnDataGetCart onDataGetCart) {
        this.idUser = idUser;
        this.idProduct = idProduct;
        this.queue = queue;
        this.onDataGetCart = onDataGetCart;
    }

    String link = Api.urlLocal + "cart";

    @Override
    protected Void doInBackground(Void... voids) {

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(link, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                int temp=0;
                if (response.length() > 0) {
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject jsonObject = response.getJSONObject(i);
                            String id = jsonObject.getString("Id");
                            String id_user = jsonObject.getString("Id_User");
                            String id_product = jsonObject.getString("Id_Product");
                            if (id_user.equals(idUser) && id_product.equals(idProduct)) {
                                int quantity = Integer.parseInt(jsonObject.getString("Quantity"));
                                float price = Float.parseFloat(jsonObject.getString("Price"));
                                float total_price = Float.parseFloat(jsonObject.getString("Total_Price"));
                                Cart cart = new Cart(id, id_user, idProduct, quantity, price, total_price);
                                onDataGetCart.onFoundCartItem(cart);
                                temp=1;
                                break;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    if(temp==0){
                        onDataGetCart.onNotFoundItem("Item have not added.");
                    }
                } else {
                    onDataGetCart.onNotFoundItem("Do not have any item.");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        queue.add(jsonArrayRequest);

        return null;
    }
}
