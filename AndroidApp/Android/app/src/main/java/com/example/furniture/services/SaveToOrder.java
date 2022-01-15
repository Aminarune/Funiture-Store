package com.example.furniture.services;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.furniture.models.Order;
import com.example.furniture.models.User;
import com.example.furniture.utilities.NumberUtilities;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SaveToOrder extends AsyncTask<Void, Void, Void> {

    Context context;

    RequestQueue queue;

    float totalPrice;

    String userId;

    String shippingId;

    public SaveToOrder(Context context, RequestQueue queue, float totalPrice, String userId, String shippingId) {
        this.context = context;
        this.queue = queue;
        this.totalPrice = totalPrice;
        this.userId = userId;
        this.shippingId = shippingId;
    }

    private String url = Api.urlLocal + "order";

    OnDataSaveOrder onDataSaveOrder;

    public void setOnDataSaveOrder(OnDataSaveOrder onDataSaveOrder) {
        this.onDataSaveOrder = onDataSaveOrder;
    }


    @Override
    protected Void doInBackground(Void... voids) {

        UUID uuid = UUID.randomUUID();
        String id = uuid.toString();

        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateTime = simpleDateFormat.format(calendar.getTime()).toString();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson = new GsonBuilder().create();
                Order order = gson.fromJson(response, Order.class);
                onDataSaveOrder.onDataSaveOrder(id);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                onDataSaveOrder.onDataFailOrder(error.networkResponse.statusCode+"");
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError   {
                HashMap<String, String> params = new HashMap<>();
//                "2099-12-31T00:00:00"

                params.put("Id", id);
                params.put("Date",dateTime);
                params.put("Price", NumberUtilities.getFloatDecimal("###.##").format(totalPrice));
                params.put("Id_User", userId);
                params.put("Id_Manager","");
                params.put("State", "Pending");
                params.put("Id_Shipping", shippingId);
                params.put("Status", String.valueOf(true));

                return params;
            }


        };




        queue.add(stringRequest);

        return null;
    }

    public interface OnDataSaveOrder {
        void onDataSaveOrder(String idOrder);

        void onDataFailOrder(String error);
    }

}
