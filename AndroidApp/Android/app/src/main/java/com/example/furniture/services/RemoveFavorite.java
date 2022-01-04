package com.example.furniture.services;

import android.os.AsyncTask;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONObject;

public class RemoveFavorite extends AsyncTask<Void, Void, Void> {

    String id;
    RequestQueue queue;


    private String urlFav = Api.url + "favorite";

    public RemoveFavorite(String id, RequestQueue queue) {
        this.id = id;
        this.queue = queue;
    }

    @Override
    protected Void doInBackground(Void... voids) {


        String url = urlFav + "/" + id;
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
