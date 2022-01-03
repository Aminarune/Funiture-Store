package com.example.furniture.views;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.example.furniture.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ShippingAddressActivity extends AppCompatActivity implements View.OnClickListener {


    private FloatingActionButton icAddShipping;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shipping_address);

        initView();
    }

    private void initView(){
        icAddShipping=findViewById(R.id.ic_add_shipping);
        icAddShipping.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ic_add_shipping:
                createAlertDiaglog();
                break;
        }
    }

    private void createAlertDiaglog() {

    }
}