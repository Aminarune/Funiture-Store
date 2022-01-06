package com.example.furniture.services;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.AdapterView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.furniture.models.District;
import com.example.furniture.models.Ward;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DownloadWard extends AsyncTask<Void, Void, Void> {

    Context context;

    RequestQueue queue;

    String districtId;

    GetWard getWard;

    public DownloadWard(Context context, RequestQueue queue, String districtId) {
        this.context = context;
        this.queue = queue;
        this.districtId = districtId;
    }

    public void setGetWard(GetWard getWard) {
        this.getWard = getWard;
    }

    private static final String url = Api.urlWard;


    @Override
    protected Void doInBackground(Void... voids) {

        String link = url + "?district=" + districtId;
        ArrayList<Ward> wards = new ArrayList<>();


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(link, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("results");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String id = jsonObject.getString("code");
                        String title = jsonObject.getString("name");
                        wards.add(new Ward(id, title));
                    }
                getWard.getWard(wards);
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



    public interface GetWard {
        void getWard(ArrayList<Ward> wards);
    }
}
