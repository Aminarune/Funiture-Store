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
import com.example.furniture.models.Cart;
import com.example.furniture.models.Favourite;
import com.example.furniture.models.OrderDetail;
import com.example.furniture.models.Product;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DownloadDataProduct extends AsyncTask<Void, Void, Void> {

    Context view;

    OnDataProductListener mlistener;

    ArrayList<Product> products = new ArrayList<Product>();

    ArrayList<Favourite> favourites;

    ArrayList<OrderDetail> orderDetails;

    ArrayList<Cart> carts;

    private static final String urlProduct = Api.urlLocal + "product";


    public DownloadDataProduct(Context view, OnDataProductListener mlistener, ArrayList<Favourite> favourites) {
        this.view = view;
        this.mlistener = mlistener;
        this.favourites = favourites;
    }


    public DownloadDataProduct(Context view, ArrayList<Cart> carts,OnDataProductListener mlistener) {
        this.view = view;
        this.mlistener = mlistener;
        this.carts = carts;
    }

    public DownloadDataProduct(Context view, OnDataProductListener mlistener, ArrayList<OrderDetail> orderDetails,int i) {
        this.view = view;
        this.mlistener = mlistener;
        this.orderDetails = orderDetails;
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
                                String id_product = jsonObject.getString("Id");
                                String name = jsonObject.getString("Name");
                                String id_category_product = jsonObject.getString("Id_Category");
                                String price = jsonObject.getString("Price");

                                String desc = jsonObject.getString("Description");
                                byte[] byteArray = Base64.decode(jsonObject.getString("Picture"), Base64.DEFAULT);
                                Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);


                                products.add(new Product(id_product, name, id_category_product, price, desc, bmp, status));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    mlistener.onCompleteDataFavProduct(view, products,favourites);
                    mlistener.onCompleteDataCartProduct(view,products,carts);
                    mlistener.onCompleteDataOrderDetailProduct(view,products,orderDetails);
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
