package com.example.furniture.services;

import android.content.Context;
import android.os.AsyncTask;

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
import java.util.UUID;

public class UpdateToAddress extends AsyncTask<Void,Void,Void> {

    Context context;

    User user;

    String id;
    String address;
    String city ;
    String district;
    String ward;

    boolean status;

    RequestQueue queue;

    OnDataSaveAddress dataSaveAddress;

    public void setDataSaveAddress(OnDataSaveAddress dataSaveAddress) {
        this.dataSaveAddress = dataSaveAddress;
    }

    public UpdateToAddress(Context context, User user, String id, String address,
                           String city, String district, String ward, boolean status, RequestQueue queue) {
        this.context = context;
        this.user = user;
        this.id = id;
        this.address = address;
        this.city = city;
        this.district = district;
        this.ward = ward;
        this.status = status;
        this.queue = queue;
    }

    private String url = Api.urlLocal;

    @Override
    protected Void doInBackground(Void... voids) {

        String link = url+"shippingaddress"+"/"+id;

        StringRequest stringRequest=new StringRequest(Request.Method.PUT, link, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson=new GsonBuilder().create();
                ShippingAddress shippingAddress=gson.fromJson(response,ShippingAddress.class);
                dataSaveAddress.onSuccess(id);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dataSaveAddress.onFail();
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> params = new HashMap<>();
                params.put("Id",id);
                params.put("Id_User",user.getId());
                params.put("Address",address);
                params.put("Province",city);
                params.put("District",district);
                params.put("Ward",ward);
                params.put("Status",String.valueOf(status));
                return params;
            }
        };

        queue.add(stringRequest);

        return null;
    }
}
