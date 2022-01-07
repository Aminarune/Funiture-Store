package com.example.furniture.services;

import android.os.AsyncTask;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;

public class RemoveShippingAddress extends AsyncTask<Void,Void,Void> {

    String id;
    RequestQueue queue;


    private String urlShip = Api.urlLocal + "shippingaddress";

    public RemoveShippingAddress(String id,RequestQueue queue) {
        this.id = id;
        this.queue = queue;
    }

    @Override
    protected Void doInBackground(Void... voids) {

        String link = urlShip+"/"+id;

        JsonArrayRequest jsonArrayRequest=new JsonArrayRequest(Request.Method.DELETE,link,
                null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

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
