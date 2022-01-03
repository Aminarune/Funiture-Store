package com.example.furniture.services;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Base64;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.furniture.R;
import com.example.furniture.models.Category;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DownloadDataCategory extends AsyncTask<Void, Void, Void> {

    View view;

    OnDataCategoryListener mlistener;

    ArrayList<Category> categories = new ArrayList<Category>();

    RecyclerView recycleViewCategory;


    private static final String urlCategory = Api.url+"category";


    public DownloadDataCategory(View view, OnDataCategoryListener mlistener, RecyclerView recycleViewCategory) {
        this.view = view;
        this.mlistener = mlistener;
        this.recycleViewCategory = recycleViewCategory;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        RequestQueue queue = Volley.newRequestQueue(view.getContext());

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(urlCategory, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                ArrayList<Category> categories = new ArrayList<>();
                if (response != null) {
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject jsonObject = (JSONObject) response.get(i);

                            String id = jsonObject.getString("Id");
                            String name = jsonObject.getString("Category_Name");

                            byte[] byteArray = Base64.decode(jsonObject.getString("Picture"), Base64.DEFAULT);
                            Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);

                            categories.add(new Category(id, name, bmp));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    mlistener.onCompleteDataCategory(view, categories);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mlistener.onErrorDataCategory(view, error.getMessage());
            }
        });
        queue.add(jsonArrayRequest);
        return null;
    }
}
