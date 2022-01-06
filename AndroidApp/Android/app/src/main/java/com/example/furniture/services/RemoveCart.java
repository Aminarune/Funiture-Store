package com.example.furniture.services;

import android.os.AsyncTask;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;

public class RemoveCart extends AsyncTask<Void,Void,Void> {

    String id;
    RequestQueue queue;


    private String urlCart = Api.urlLocal + "cart";

    public RemoveCart(String id,RequestQueue queue) {
        this.id = id;
        this.queue = queue;
    }

    @Override
    protected Void doInBackground(Void... voids) {


        String url = urlCart + "/" + id;
        JsonArrayRequest jsonArrayRequest=new JsonArrayRequest(Request.Method.DELETE,url,
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
