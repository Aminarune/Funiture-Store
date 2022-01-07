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
import com.example.furniture.models.City;
import com.example.furniture.models.District;
import com.example.furniture.models.ShippingAddress;
import com.example.furniture.models.User;
import com.example.furniture.models.Ward;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SaveToAddress extends AsyncTask<Void,Void,Void> {

    Context context;

    User user;

    String address;
    String city ;
    String district;
    String ward;

    RequestQueue queue;

    OnDataSaveAddress dataSaveAddress;

    public void setDataSaveAddress(OnDataSaveAddress dataSaveAddress) {
        this.dataSaveAddress = dataSaveAddress;
    }

    public SaveToAddress(Context context, User user, String address, String city, String district, String ward, RequestQueue queue) {
        this.context = context;
        this.user = user;
        this.address = address;
        this.city = city;
        this.district = district;
        this.ward = ward;
        this.queue = queue;
    }

    String url = Api.urlLocal;
    @Override
    protected Void doInBackground(Void... voids) {

        UUID uuid = UUID.randomUUID();
        String id = uuid.toString();

        String link = url+"shippingaddress";

        StringRequest stringRequest=new StringRequest(Request.Method.POST, link, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson=new GsonBuilder().create();
                ShippingAddress shippingAddress=gson.fromJson(response,ShippingAddress.class);
                dataSaveAddress.onSuccess();
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
                params.put("Status",String.valueOf(false));
                return params;
            }
        };

        queue.add(stringRequest);

        return null;
    }
}
