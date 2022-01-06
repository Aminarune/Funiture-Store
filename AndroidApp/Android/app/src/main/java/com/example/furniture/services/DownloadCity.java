package com.example.furniture.services;

import android.content.Context;
import android.os.AsyncTask;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.furniture.models.City;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DownloadCity extends AsyncTask<Void,Void,Void> {

    Context context;

    RequestQueue queue;

    GetCity getCity;

    public DownloadCity(Context context, RequestQueue queue) {
        this.context = context;
        this.queue = queue;
    }

    public void setGetCity(GetCity getCity) {
        this.getCity = getCity;
    }

    private static final String link=Api.urlProvince;

    @Override
    protected Void doInBackground(Void... voids) {

        ArrayList<City> city=new ArrayList<>();

        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(link, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray=response.getJSONArray("results");
                    for (int i = 0;i<jsonArray.length();i++){
                        JSONObject jsonObject=jsonArray.getJSONObject(i);
                        String id = jsonObject.getString("code");
                        String title = jsonObject.getString("name");
                        city.add(new City(id,title));
                    }
                    getCity.getCity(city);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(jsonObjectRequest);

        return null;
    }



    public  interface GetCity{
        void getCity(ArrayList<City> city);
    }
}
