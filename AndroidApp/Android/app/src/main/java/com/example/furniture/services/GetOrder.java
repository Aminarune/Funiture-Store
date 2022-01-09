package com.example.furniture.services;

import android.content.Context;
import android.os.AsyncTask;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.furniture.models.Order;
import com.example.furniture.models.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class GetOrder extends AsyncTask<Void, Void, Void> {


    Context context;

    RequestQueue queue;

    String stateInput;

    User user;

    OnDataGetOrder onDataGetOrder;

    public void setOnDataGetOrder(OnDataGetOrder onDataGetOrder) {
        this.onDataGetOrder = onDataGetOrder;
    }

    public GetOrder(Context context, RequestQueue queue, String stateInput, User user) {
        this.context = context;
        this.queue = queue;
        this.stateInput = stateInput;
        this.user = user;
    }

    private String link = Api.urlLocal + "order";


    @Override
    protected Void doInBackground(Void... voids) {


        ArrayList<Order> orderArrayList = new ArrayList<>();

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(link, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                if (response.length() > 0) {
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject jsonObject = response.getJSONObject(i);

                            String id_user = jsonObject.getString("Id_User");

                            String state = jsonObject.getString("State");


                            if (id_user.equals(user.getId()) && state.equals(stateInput)) {
                                String id = jsonObject.getString("Id");
                                String date = jsonObject.getString("Date");
                                String price = jsonObject.getString("Price");
                                String id_shipping = jsonObject.getString("Id_Shipping");
                                String status = jsonObject.getString("Status");
                                orderArrayList.add(new Order(id, date, price, id_user, state, id_shipping, status));
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    onDataGetOrder.onDataGetOrder(context,orderArrayList);

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

    public interface OnDataGetOrder {
        void onDataGetOrder(Context context,ArrayList<Order> orders);
    }
}
