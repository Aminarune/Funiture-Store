package com.example.furniture.services;

import android.os.AsyncTask;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.furniture.models.Favourite;
import com.example.furniture.models.Product;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SaveToFavorite extends AsyncTask<Void, Void, Void> {

    String id_User;
    String id_Product;
    RequestQueue queue;

    OnDataSaveListener saveListener;

    private String urlFav = Api.url + "favorite";


    public SaveToFavorite(String id_User, String id_Product, RequestQueue queue, OnDataSaveListener saveListener) {
        this.id_User = id_User;
        this.id_Product = id_Product;
        this.queue = queue;
        this.saveListener = saveListener;
    }

    @Override
    protected Void doInBackground(Void... voids) {

        UUID uuid = UUID.randomUUID();
        String id = uuid.toString();

        StringRequest stringRequest=new StringRequest(Request.Method.POST, urlFav, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson=new GsonBuilder().create();
                Favourite favourite=gson.fromJson(response,Favourite.class);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> params=new HashMap<>();
                params.put("Id",id);
                params.put("Id_User",id_User);
                params.put("Id_Product",id_Product);
                return params;
            }
        };
        queue.add(stringRequest);

        return null;
    }
}
