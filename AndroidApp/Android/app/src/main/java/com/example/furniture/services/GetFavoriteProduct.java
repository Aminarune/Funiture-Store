package com.example.furniture.services;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Base64;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.furniture.models.Product;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class GetFavoriteProduct extends AsyncTask<Void,Void,Void> {


    Context context;


    RequestQueue queue;

    OnDataProductByID onDataProductByID;

    @Override
    protected Void doInBackground(Void... voids) {


        return null;
    }
}
