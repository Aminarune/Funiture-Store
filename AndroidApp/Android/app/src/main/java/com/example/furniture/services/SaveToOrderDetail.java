package com.example.furniture.services;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.furniture.models.Order;
import com.example.furniture.utilities.NumberUtilities;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SaveToOrderDetail extends AsyncTask<Void, Void, Void> {

    Context context;

    RequestQueue queue;

    String idOrder;

    String idProduct;

    int quantity;

    float price;

    float totalPrice;

    boolean status;

    OnDataSaveOrderDetail onDataSaveOrderDetail;

    public SaveToOrderDetail(Context context, RequestQueue queue, String idOrder, String idProduct,
                       int quantity, float price, float totalPrice, boolean status) {
        this.context = context;
        this.queue = queue;
        this.idOrder = idOrder;
        this.idProduct = idProduct;
        this.quantity = quantity;
        this.price = price;
        this.totalPrice = totalPrice;
        this.status = status;
    }


    public void setOnDataSaveOrderDetail(OnDataSaveOrderDetail onDataSaveOrderDetail) {
        this.onDataSaveOrderDetail = onDataSaveOrderDetail;
    }

    private String url = Api.urlLocal + "orderdetail";

    @Override
    protected Void doInBackground(Void... voids) {

        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson = new GsonBuilder().create();
                Order order = gson.fromJson(response, Order.class);
                onDataSaveOrderDetail.onDataSaveOrderDetail();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                onDataSaveOrderDetail.onFailDataSaveOrderDetail(error.getMessage());
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("Id_Order", idOrder);
                params.put("Id_Product", idProduct);
                params.put("Quantity", String.valueOf(quantity));
                params.put("Price", NumberUtilities.getFloatDecimal("###.##").format(price));
                params.put("Total_Price",NumberUtilities.getFloatDecimal("###.##").format(totalPrice));
                params.put("Status", String.valueOf(status));
                return params;
            }
        };

        queue.add(stringRequest);

        return null;
    }

    public interface OnDataSaveOrderDetail{
        void onDataSaveOrderDetail();
        void onFailDataSaveOrderDetail(String error);
    }


}
