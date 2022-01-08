package com.example.furniture.services;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Base64;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.furniture.models.Product;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DownloadDataProductByCategory extends AsyncTask<Void, Void, Void> {

    Context view;

    OnDataProductListener mlistener;


    String id;

    ArrayList<Product> products = new ArrayList<Product>();

    private static final String urlProduct = Api.urlLocal + "product";

    public DownloadDataProductByCategory(Context view, OnDataProductListener mlistener, String id) {
        this.view = view;
        this.mlistener = mlistener;
        this.id = id;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        RequestQueue queue = Volley.newRequestQueue(view);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(urlProduct, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                ArrayList<Product> products = new ArrayList<>();
                if (response != null) {
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject jsonObject = (JSONObject) response.getJSONObject(i);
                            boolean status = jsonObject.getBoolean("Status");
                            if (status) {
                                String id_category_product = jsonObject.getString("Id_Category");
                                if (id_category_product.equals(id)) {
                                    String id_product = jsonObject.getString("Id");
                                    String name = jsonObject.getString("Name");
                                    String price = jsonObject.getString("Price");
                                    String desc = jsonObject.getString("Description");

                                    byte[] byteArray = Base64.decode(jsonObject.getString("Picture"), Base64.DEFAULT);
                                    Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);

                                    products.add(new Product(id_product, name, id_category_product, price, desc, bmp, status));
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    mlistener.onCompleteDataProduct(view, products);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mlistener.onErrorDataProduct(view, error.getMessage());
            }
        });
        queue.add(jsonArrayRequest);
        return null;
    }

}
