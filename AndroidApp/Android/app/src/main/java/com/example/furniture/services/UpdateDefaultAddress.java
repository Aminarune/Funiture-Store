package com.example.furniture.services;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.furniture.models.ShippingAddress;
import com.example.furniture.models.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;
import java.util.Map;

public class UpdateDefaultAddress extends AsyncTask<Void,Void,Void> {

    Context context;

    User user;

    ShippingAddress shippingAddress;

    RequestQueue queue;

    boolean status;

    public UpdateDefaultAddress(Context context, User user, ShippingAddress shippingAddress,
                                RequestQueue queue, boolean status) {
        this.context = context;
        this.user = user;
        this.shippingAddress = shippingAddress;
        this.queue = queue;
        this.status = status;
    }

    String url = Api.urlLocal;


    @Override
    protected Void doInBackground(Void... voids) {

        String link = url+"shippingaddress"+"/"+shippingAddress.getId();

        StringRequest stringRequest=new StringRequest(Request.Method.PUT, link, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson=new GsonBuilder().create();
                ShippingAddress shippingAddress=gson.fromJson(response,ShippingAddress.class);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> params = new HashMap<>();
                params.put("Id",shippingAddress.getId());
                params.put("Id_User",user.getId());
                params.put("Address",shippingAddress.getAddress());
                params.put("Province",shippingAddress.getProvince());
                params.put("District",shippingAddress.getDistrict());
                params.put("Ward",shippingAddress.getWard());
                params.put("Status",String.valueOf(status));
                return params;
            }
        };

        queue.add(stringRequest);

        return null;
    }
}
