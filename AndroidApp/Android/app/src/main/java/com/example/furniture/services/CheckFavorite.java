package com.example.furniture.services;

import android.os.AsyncTask;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CheckFavorite extends AsyncTask<Void, Void, Void> {

    String id_User;
    String id_Product;
    RequestQueue queue;

    OnFavDataListener onFavDataListener;

    private String urlFav = Api.urlLocal + "favorite";

    public CheckFavorite(String id_User, String id_Product, RequestQueue queue, OnFavDataListener onFavDataListener) {
        this.id_User = id_User;
        this.id_Product = id_Product;
        this.queue = queue;
        this.onFavDataListener = onFavDataListener;
    }

    @Override
    protected Void doInBackground(Void... voids) {

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(urlFav,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject jsonObject = response.getJSONObject(i);
                                String id = jsonObject.getString("Id");
                                String idUser = jsonObject.getString("Id_User");
                                String idPro = jsonObject.getString("Id_Product");
                                if (id_User.equals(idUser)) {
                                    if (id_Product.equals(idPro)) {
                                        //already save
                                        onFavDataListener.onFoundFav(true, id);
                                        break;
                                    } else {
                                        //not save
                                        onFavDataListener.onNotFoundFav(false);
                                    }
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
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
