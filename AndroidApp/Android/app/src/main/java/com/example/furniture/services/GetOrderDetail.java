package com.example.furniture.services;

import android.os.AsyncTask;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.furniture.models.OrderDetail;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class GetOrderDetail extends AsyncTask<Void,Void,Void> {

    String idOrder;

    RequestQueue queue;

    String link = Api.urlLocal+"orderdetail";

    private GetOrderDetailInter getOrderDetailInter;

    public GetOrderDetail(String idOrder, RequestQueue queue) {
        this.idOrder = idOrder;
        this.queue = queue;
    }

    public void setGetOrderDetailInter(GetOrderDetailInter getOrderDetailInter) {
        this.getOrderDetailInter = getOrderDetailInter;
    }

    @Override
    protected Void doInBackground(Void... voids) {


        ArrayList<OrderDetail> arrayList=new ArrayList<>();

        JsonArrayRequest jsonArrayRequest=new JsonArrayRequest(link, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                if(response.length()>0){
                    for(int i=0;i<response.length();i++) {
                        try {
                            JSONObject jsonObject = response.getJSONObject(i);
                            String id = jsonObject.getString("Id_Order");
                            String idProduct = jsonObject.getString("Id_Product");
                            int quantity = jsonObject.getInt("Quantity");
                            String total = jsonObject.getString("Total_Price");
                            if(id.equals(idOrder)){
                                arrayList.add(new OrderDetail(
                                        id,idProduct,quantity,total
                                ));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    getOrderDetailInter.getOrderDetail(arrayList);
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

    public interface GetOrderDetailInter{
        void getOrderDetail(ArrayList<OrderDetail> orderDetails);
    }
}
