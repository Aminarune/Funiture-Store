package com.example.furniture.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import com.example.furniture.services.RemoveCart;
import com.example.furniture.services.RemoveShippingAddress;
import com.example.furniture.services.UpdateDefaultAddress;
import com.example.furniture.utilities.AlbertDialogUtil;
import com.example.furniture.utilities.NetworkChangeReceiver;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class ShippingActivity extends AppCompatActivity{


    private FloatingActionButton btnAdd;

    private RecyclerView recycleViewShipping;

    private RequestQueue queue;

    private User user;

    private String from;

    //check connection state auto
    private NetworkChangeReceiver networkChangeReceiver;

    private ShippingAddressAdapter shippingAddressAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shipping);

        queue = Volley.newRequestQueue(ShippingActivity.this);

        user = (User) getIntent().getSerializableExtra("user");

        from = getIntent().getStringExtra("from");

        btnAdd = findViewById(R.id.btnCreateShipping);

        recycleViewShipping = findViewById(R.id.recycleViewShipping);


        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveToCreateAddShipping(user);
            }
        });


        Dialog dialog= AlbertDialogUtil.showAlertDialog(this);
        networkChangeReceiver = new NetworkChangeReceiver(dialog,R.raw.disconnected);



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
        shippingAddressAdapter = new ShippingAddressAdapter(ShippingActivity.this,
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

            @Override
            public void modifyShipping(ShippingAddress shippingAddress, int pos) {
                moveToUpdateShipping(shippingAddress,user);
            }

            @Override
            public void onRemoveItem(ShippingAddress shippingAddress, int pos) {
//                arrayList.remove(pos);
//                shippingAddressAdapter.notifyItemRemoved(pos);
//                RemoveShippingAddress removeShippingAddress = new RemoveShippingAddress(
//                        shippingAddress.getId(), queue);
//                removeShippingAddress.execute();
            }
        });
    }

    private void moveToUpdateShipping(ShippingAddress shippingAddress,User user){
        Intent intent = new Intent(ShippingActivity.this, AddShippingActivity.class);
        intent.putExtra("shipping",shippingAddress);
        intent.putExtra("from","from_edit");
        intent.putExtra("user", user);
        startActivity(intent);
    }


    private void moveToCreateAddShipping(User user) {
        Intent intent = new Intent(ShippingActivity.this, AddShippingActivity.class);
        intent.putExtra("from","from_create");
        intent.putExtra("user", user);
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        registerReceiver(networkChangeReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        getShippingAddress(ShippingActivity.this, user, queue);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(networkChangeReceiver);
    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(ShippingActivity.this,MainActivity.class);
        intent.putExtra("user",user);
        intent.putExtra("from","Shipping");
        startActivity(intent);
        finish();
    }
}