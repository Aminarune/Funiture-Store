package com.example.furniture.services;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.furniture.models.District;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DownloadDistrict extends AsyncTask<Void, Void, Void> {

    Context context;

    RequestQueue queue;

    String cityId;

    GetDistrict getDistrict;

    public DownloadDistrict(Context context, RequestQueue queue, String cityId) {
        this.context = context;
        this.queue = queue;
        this.cityId = cityId;
    }

    public void setGetDistrict(GetDistrict getDistrict) {
        this.getDistrict = getDistrict;
    }

    private static final String url = Api.urlDistrict;


    @Override
    protected Void doInBackground(Void... voids) {

        String link = url + "?province=" + cityId;

        ArrayList<District> districts = new ArrayList<>();


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, link, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("results");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String id = jsonObject.getString("code");
                                String title = jsonObject.getString("name");
                                districts.add(new District(id, title));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        getDistrict.getDistrict(districts);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(jsonObjectRequest);

        return null;
    }


    public interface GetDistrict {
        void getDistrict(ArrayList<District> districts);
    }
}
