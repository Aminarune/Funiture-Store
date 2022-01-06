package com.example.furniture.services;

import android.content.Context;
import android.os.AsyncTask;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.furniture.models.Favourite;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class GetFavorite extends AsyncTask<Void,Void,Void> {

    Context view;

    String id_User;

    RequestQueue queue;

    OnDataFavList onDataFavList;

    private String urlFav = Api.urlLocal + "favorite";


    ArrayList<Favourite> arrayList=new ArrayList<>();


    public GetFavorite(Context view, String id_User, RequestQueue queue, OnDataFavList onDataFavList) {
        this.view = view;
        this.id_User = id_User;
        this.queue = queue;
        this.onDataFavList = onDataFavList;
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
                                String idUser = jsonObject.getString("Id_User");
                                if(idUser.equals(id_User)){
                                    String id = jsonObject.getString("Id");
                                    String idPro = jsonObject.getString("Id_Product");
                                    Favourite favourite=new Favourite(id,idUser,idPro);
                                    arrayList.add(new Favourite(id,idUser,idPro));
                                }
                                else {
                                    continue;
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        onDataFavList.onDataFavFound(view,arrayList);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                onDataFavList.onDataFavNotFound(view,error.getMessage());
            }
        });


        queue.add(jsonArrayRequest);

        return null;
    }
}
