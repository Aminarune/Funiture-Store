package com.example.furniture.services;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Base64;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.furniture.models.Product;

import org.json.JSONException;
import org.json.JSONObject;

public class DownloadProductById extends AsyncTask<Void, Void, Void> {

    Context context;

    String id;

    RequestQueue queue;

    OnDataProductByID onDataProductByID;


    public DownloadProductById(Context context, String id, RequestQueue queue, OnDataProductByID onDataProductByID) {
        this.context = context;
        this.id = id;
        this.queue = queue;
        this.onDataProductByID = onDataProductByID;
    }


    private static final String urlProduct = Api.urlLocal + "product";

    @Override
    protected Void doInBackground(Void... voids) {

        String url = urlProduct + "/" + id;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if (response.length() > 0) {
                    try {
                        String id = response.getString("Id");
                        String name = response.getString("Name");
                        String id_category = response.getString("Id_Category");
                        String price = response.getString("Price");
                        String desc = response.getString("Description");
                        byte[] byteArray = Base64.decode(response.getString("Picture"), Base64.DEFAULT);
                        Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
                        boolean status = response.getBoolean("Status");
                        Product product = new Product(id, name, id_category, price, desc, bmp, status);
                        onDataProductByID.onFound(context, product);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    onDataProductByID.onNotFound(context, "This product was deleted.");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        queue.add(jsonObjectRequest);
        return null;
    }

}




