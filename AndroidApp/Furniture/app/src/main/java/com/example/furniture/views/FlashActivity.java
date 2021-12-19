package com.example.furniture.views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;


import com.example.furniture.R;
import com.google.android.material.button.MaterialButton;

public class FlashActivity extends AppCompatActivity {

    private MaterialButton button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*hide status bar*/
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_flash);

        button = findViewById(R.id.btnGetStart);

        button.setOnClickListener(v -> {
            Intent intent = new Intent(FlashActivity.this, SignInActivity.class);
            startActivity(intent);

            finish();/*destroy acti when leave*/
        });


    }
}