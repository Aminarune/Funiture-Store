package com.example.furniture.views;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.furniture.R;
import com.example.furniture.models.Cart;
import com.example.furniture.models.Favourite;
import com.example.furniture.models.OrderDetail;
import com.example.furniture.models.Product;
import com.example.furniture.models.ShippingAddress;
import com.example.furniture.models.User;
import com.example.furniture.services.DownloadDataProduct;
import com.example.furniture.services.GetCart;
import com.example.furniture.services.GetShippingAddress;
import com.example.furniture.services.GetShippingAddressId;
import com.example.furniture.services.OnDataCartList;
import com.example.furniture.services.OnDataProductListener;
import com.example.furniture.services.OnDataSaveAddress;
import com.example.furniture.services.RemoveCart;
import com.example.furniture.services.SaveToAddress;
import com.example.furniture.services.SaveToOrder;
import com.example.furniture.services.SaveToOrderDetail;
import com.example.furniture.utilities.AlbertDialogUtil;
import com.example.furniture.utilities.DialogUtil;
import com.example.furniture.utilities.NetworkChangeReceiver;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class CheckOutActivity extends AppCompatActivity implements View.OnClickListener, OnDataCartList {

    private TextView tvName, tvPhone, tvAddress;

    private Button btnCheckOut;

    private User user;

    private String shippingAddressId;

    private ArrayList<String> cartArrayList;

    private ArrayList<String> productArrayList;

    private RequestQueue queue;

    private Dialog dialog;

    //check connection state auto
    private NetworkChangeReceiver networkChangeReceiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_out);

        AlertDialog alertDialog = AlbertDialogUtil.showAlertDialog(this);
        networkChangeReceiver = new NetworkChangeReceiver(alertDialog);

        dialog = DialogUtil.showDialog(CheckOutActivity.this
                , R.raw.loading, " Please wait a minute.");

        queue = Volley.newRequestQueue(this);

        user = (User) getIntent().getSerializableExtra("user");

        shippingAddressId = getIntent().getStringExtra("shippingId");

        cartArrayList = (ArrayList<String>) getIntent().getSerializableExtra("cart");

        productArrayList = (ArrayList<String>) getIntent().getSerializableExtra("product");

        tvName = findViewById(R.id.tvNameCheckOut);
        tvPhone = findViewById(R.id.tvPhoneCheckOut);
        tvAddress = findViewById(R.id.tvAddressCheckOut);
        btnCheckOut = findViewById(R.id.btnCheckOut);
        btnCheckOut.setOnClickListener(this);

        setData(user, shippingAddressId, queue);

    }

    @Override
    protected void onStart() {
        super.onStart();
        registerReceiver(networkChangeReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(networkChangeReceiver);
    }

    private void setData(User user, String shippingId, RequestQueue queue) {

        String from = getIntent().getStringExtra("from");
        if (from != null && from.equals("add_shipping")) {

            User u = (User) getIntent().getSerializableExtra("user");
            String address = getIntent().getStringExtra("address");
            String city = getIntent().getStringExtra("city");
            String district = getIntent().getStringExtra("district");
            String ward = getIntent().getStringExtra("ward");


            String fullAddress =
                    address + ", " +
                            ward + ", " +
                            district + ", " +
                            city;
            tvAddress.setText(fullAddress);

            tvName.setText(u.getName());
            tvPhone.setText(u.getPhone());

        } else {
            tvName.setText(user.getName());
            tvPhone.setText(user.getPhone());

            GetShippingAddressId getShippingAddressId = new GetShippingAddressId(user, shippingId, queue);
            getShippingAddressId.execute();
            getShippingAddressId.setOnDataAddressObject(new GetShippingAddressId.OnDataAddressObject() {
                @Override
                public void onDataAddressObject(ShippingAddress shippingAddress) {
                    String fullAddress =
                            shippingAddress.getAddress() + ", " +
                                    shippingAddress.getWard() + ", " +
                                    shippingAddress.getDistrict() + ", " +
                                    shippingAddress.getProvince();
                    tvAddress.setText(fullAddress);
                }
            });
        }

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnCheckOut:

                String from = getIntent().getStringExtra("from");
                if (from != null && from.equals("add_shipping")) {
                    saveAddress();
                } else {
                    saveOrder("");
                }
                break;
        }
    }

    private void saveAddress(){
        String from = getIntent().getStringExtra("from");
        if (from != null && from.equals("add_shipping")) {

            User u = (User) getIntent().getSerializableExtra("user");
            String address = getIntent().getStringExtra("address");
            String city = getIntent().getStringExtra("city");
            String district = getIntent().getStringExtra("district");
            String ward = getIntent().getStringExtra("ward");


            SaveToAddress saveToAddress=new SaveToAddress(CheckOutActivity.this,u,address,city,district,ward,true,queue);
            saveToAddress.execute();


            saveToAddress.setDataSaveAddress(new OnDataSaveAddress() {
                @Override
                public void onSuccess(String id) {
                    saveOrder(id);
                }

                @Override
                public void onFail() {

                }
            });

        }
    }

    private void saveOrder(String shipId) {

        if(shipId.isEmpty()==false){
            shippingAddressId=shipId;
        }

        dialog.show();
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        GetCart getCart = new GetCart(CheckOutActivity.this, user.getId(),shippingAddressId, queue, this);
        getCart.execute();
    }


    @Override
    public void onDataCartFound(Context view, ArrayList<Cart> cartArrayList) {



        DownloadDataProduct downloadDataProduct = new DownloadDataProduct(CheckOutActivity.this, cartArrayList, new OnDataProductListener() {
            @Override
            public void onCompleteDataProduct(Context view, ArrayList<Product> arrayList) {

            }

            @Override
            public void onErrorDataProduct(Context view, String error) {

            }

            @Override
            public void onCompleteDataFavProduct(Context view, ArrayList<Product> products, ArrayList<Favourite> favourites) {

            }

            @Override
            public void onCompleteDataCartProduct(Context view, ArrayList<Product> products, ArrayList<Cart> carts) {
                saveOrderDetail(carts);
            }

            @Override
            public void onCompleteDataOrderDetailProduct(Context view, ArrayList<Product> products, ArrayList<OrderDetail> carts) {

            }
        });
        downloadDataProduct.execute();
    }

    private void saveOrderDetail(ArrayList<Cart> carts) {
        float total = 0;

        for (int i = 0; i < carts.size(); i++) {
            total += (carts.get(i).getTotalPrice());
        }

        //save to order
        SaveToOrder saveToOrder = new SaveToOrder(CheckOutActivity.this, queue,
                total, user.getId(), shippingAddressId);
        saveToOrder.execute();
        saveToOrder.setOnDataSaveOrder(new SaveToOrder.OnDataSaveOrder() {
            @Override
            public void onDataSaveOrder(String idOrder) {
                //save to order detail

                for (int j = 0; j < carts.size(); j++) {

                    String id = carts.get(j).getIdProduct();
                    int quantity = carts.get(j).getQuantity();
                    float price = carts.get(j).getPrice();
                    float totalPrice = carts.get(j).getTotalPrice();

                    SaveToOrderDetail saveToOrderDetail = new SaveToOrderDetail(
                            CheckOutActivity.this,
                            queue,
                            idOrder,
                            id,
                            quantity,
                            price,
                            totalPrice,
                            true
                    );
                    saveToOrderDetail.execute();
                    saveToOrderDetail.setOnDataSaveOrderDetail(new SaveToOrderDetail.OnDataSaveOrderDetail() {
                        @Override
                        public void onDataSaveOrderDetail() {

                        }

                        @Override
                        public void onFailDataSaveOrderDetail(String error) {

                        }
                    });
                }


                dialog.dismiss();
                Dialog dia = DialogUtil.showDialog(CheckOutActivity.this, R.raw.wrong,
                        "Success.");
                dia.show();
                dia.setCancelable(false);
                dia.setCanceledOnTouchOutside(false);

                for (int j = 0; j < carts.size(); j++) {
                    String id = carts.get(j).getId();
                    RemoveCart removeCart = new RemoveCart(id, queue);
                    removeCart.execute();
                }

                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        dia.dismiss();
                        moveToMainActivity(user, "Cart");
                    }
                }, 1500);


            }

            @Override
            public void onDataFailOrder(String error) {
                dialog.dismiss();
                Dialog dia = DialogUtil.showDialog(CheckOutActivity.this, R.raw.wrong,
                        "Something went wrong.");
                dia.show();
                dia.setCancelable(false);
                dia.setCanceledOnTouchOutside(false);
                dia.dismiss();
                moveToMainActivity(user, "Cart");
            }
        });
    }

    @Override
    public void onDataCartNotFound(Context view, String error) {

    }

    private void moveToMainActivity(User user, String tag) {
        Intent intent = new Intent(CheckOutActivity.this, MainActivity.class);
        intent.putExtra("user", user);
        intent.putExtra("from", tag);
        startActivity(intent);
        finish();

    }

    @Override
    public void onBackPressed() {
        moveToMainActivity(user, "Cart");
    }
}