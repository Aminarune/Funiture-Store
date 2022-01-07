package com.example.furniture.services;

import android.os.AsyncTask;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.furniture.models.ShippingAddress;
import com.example.furniture.models.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Queue;

public class GetShippingAddress extends AsyncTask<Void, Void, Void> {


    User user;

    RequestQueue queue;

    OnDataShipAddList onDataShipAddList;

    public GetShippingAddress(User user, RequestQueue queue) {
        this.user = user;
        this.queue = queue;
    }

    public void setOnDataShipAddList(OnDataShipAddList onDataShipAddList) {
        this.onDataShipAddList = onDataShipAddList;
    }

    String url = Api.urlLocal + "shippingaddress";

    @Override
    protected Void doInBackground(Void... voids) {

        ArrayList<ShippingAddress> shippingAddresses = new ArrayList<>();

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);
                        String id = jsonObject.getString("Id");
                        String idUser = jsonObject.getString("Id_User");
                        String address = jsonObject.getString("Address");
                        String province = jsonObject.getString("Province");
                        String district = jsonObject.getString("District");
                        String ward = jsonObject.getString("Ward");
                        Boolean status = jsonObject.getBoolean("Status");
                        if (idUser.equals(user.getId())) {
                            shippingAddresses.add(new ShippingAddress(
                                    id, idUser, address, province, district, ward, status
                            ));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                onDataShipAddList.onSuccess(shippingAddresses);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                onDataShipAddList.onFail(error.getMessage());
            }
        });

        queue.add(jsonArrayRequest);

        return null;
    }
}
