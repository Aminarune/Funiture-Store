package com.example.furniture.services;

import android.os.AsyncTask;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.furniture.models.ShippingAddress;
import com.example.furniture.models.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class GetShippingAddressId extends AsyncTask<Void, Void, Void> {


    User user;

    String idShipping;

    RequestQueue queue;

    OnDataAddressObject onDataAddressObject;

    public GetShippingAddressId(User user, String idShipping, RequestQueue queue) {
        this.user = user;
        this.idShipping = idShipping;
        this.queue = queue;
    }

    public void setOnDataAddressObject(OnDataAddressObject onDataAddressObject) {
        this.onDataAddressObject = onDataAddressObject;
    }

    String url = Api.urlLocal + "shippingaddress";

    @Override
    protected Void doInBackground(Void... voids) {


        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET,
                url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                if (response.length() > 0) {
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject jsonObject = response.getJSONObject(i);
                            String id = jsonObject.getString("Id");
                            if (id.equals(idShipping)) {
                                String idUser = jsonObject.getString("Id_User");
                                String address = jsonObject.getString("Address");
                                String province = jsonObject.getString("Province");
                                String district = jsonObject.getString("District");
                                String ward = jsonObject.getString("Ward");
                                boolean status = jsonObject.getBoolean("Status");

                                ShippingAddress shippingAddress = new ShippingAddress(
                                        id, idUser, address, province, district, ward, status
                                );
                                onDataAddressObject.onDataAddressObject(shippingAddress);
                                break;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
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

    public interface OnDataAddressObject {
        void onDataAddressObject(ShippingAddress shippingAddress);
    }
}
