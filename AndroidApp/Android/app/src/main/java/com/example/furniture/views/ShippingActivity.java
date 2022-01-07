package com.example.furniture.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.Image;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.furniture.R;
import com.example.furniture.adapters.ShippingAddressAdapter;
import com.example.furniture.models.ShippingAddress;
import com.example.furniture.models.User;
import com.example.furniture.services.GetShippingAddress;
import com.example.furniture.services.OnDataShipAddList;
import com.example.furniture.services.UpdateDefaultAddress;
import com.example.furniture.utilities.AlbertDialogUtil;
import com.example.furniture.utilities.NetworkChangeReceiver;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class ShippingActivity extends AppCompatActivity{


    private FloatingActionButton btnAdd;

    private RecyclerView recycleViewShipping;

    private ImageView ivBackToShipping;

    private RequestQueue queue;

    //check connection state auto
    private NetworkChangeReceiver networkChangeReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shipping);

        User user = (User) getIntent().getSerializableExtra("user");

        btnAdd = findViewById(R.id.btnCreateShipping);

        recycleViewShipping = findViewById(R.id.recycleViewShipping);

        ivBackToShipping = findViewById(R.id.ivBackToShipping);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveToAddShipping(user);
            }
        });

        ivBackToShipping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        AlertDialog alertDialog= AlbertDialogUtil.showAlertDialog(this);
        networkChangeReceiver = new NetworkChangeReceiver(alertDialog);

        queue = Volley.newRequestQueue(ShippingActivity.this);

        getShippingAddress(ShippingActivity.this, user, queue);
    }

    private void getShippingAddress(Context context, User user, RequestQueue queue) {
        GetShippingAddress shippingAddress = new GetShippingAddress(user, queue);
        shippingAddress.execute();
        shippingAddress.setOnDataShipAddList(new OnDataShipAddList() {
            @Override
            public void onSuccess(ArrayList<ShippingAddress> arrayList) {
                createUIAddress(arrayList,user,queue);
            }

            @Override
            public void onFail(String error) {

            }
        });
    }

    private void createUIAddress(ArrayList<ShippingAddress> arrayList,User user,RequestQueue queue) {
        ShippingAddressAdapter shippingAddressAdapter = new ShippingAddressAdapter(ShippingActivity.this,
                arrayList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(ShippingActivity.this, RecyclerView.VERTICAL, false);
        recycleViewShipping.setLayoutManager(layoutManager);
        recycleViewShipping.setAdapter(shippingAddressAdapter);


        shippingAddressAdapter.setOnCheck(new ShippingAddressAdapter.OnCheck() {
            @Override
            public void setOnCheck(ShippingAddress shippingAddress) {
                UpdateDefaultAddress updateDefaultAddress=new UpdateDefaultAddress(
                        ShippingActivity.this,user,shippingAddress,
                        queue,true);
                updateDefaultAddress.execute();
            }

            @Override
            public void setOnUnCheck(ShippingAddress shippingAddress) {
                UpdateDefaultAddress updateDefaultAddress=new UpdateDefaultAddress(
                        ShippingActivity.this,user,shippingAddress,
                        queue,false);
                updateDefaultAddress.execute();
            }
        });
    }


    private void moveToAddShipping(User user) {
        Intent intent = new Intent(ShippingActivity.this, AddShippingActivity.class);
        intent.putExtra("user", user);
        startActivity(intent);
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



}