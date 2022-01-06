package com.example.furniture.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.furniture.R;
import com.example.furniture.models.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ShippingActivity extends AppCompatActivity implements View.OnClickListener {


    private FloatingActionButton btnAdd;

    private RecyclerView recycleViewShipping;

    private ImageView ivBackToShipping;

    private User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shipping);

        btnAdd = findViewById(R.id.btnCreateShipping);

        recycleViewShipping = findViewById(R.id.recycleViewShipping);

        ivBackToShipping = findViewById(R.id.ivBackToShipping);

        btnAdd.setOnClickListener(this::onClick);

        ivBackToShipping.setOnClickListener(this::onClick);

        user= (User) getIntent().getSerializableExtra("user");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnCreateShipping:
                moveToAddShipping(user);
                break;

            case R.id.ivBackToShipping:
                finish();
                break;
        }
    }

    private void moveToAddShipping(User user){
        Intent intent=new Intent(ShippingActivity.this,AddShippingActivity.class);
        intent.putExtra("user",user);
        startActivity(intent);
        finish();
    }
}