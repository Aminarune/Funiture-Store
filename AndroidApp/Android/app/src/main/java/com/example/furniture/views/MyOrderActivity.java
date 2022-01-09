package com.example.furniture.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;

import com.example.furniture.R;
import com.example.furniture.adapters.ViewPagerAdapter;
import com.example.furniture.fragments.PendingFragment;
import com.example.furniture.models.Order;
import com.example.furniture.models.User;
import com.example.furniture.utilities.AlbertDialogUtil;
import com.example.furniture.utilities.NetworkChangeReceiver;
import com.example.furniture.utilities.SendDetailToActivity;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class MyOrderActivity extends AppCompatActivity implements SendDetailToActivity {

    private TabLayout tabLayout;

    private ViewPager2 viewPager;

    private ViewPagerAdapter viewPagerAdapter;

    private User userAccount;

    //check connection state auto
    private NetworkChangeReceiver networkChangeReceiver;


    private static final String[] title = {"Pending", "Delivering", "Completed"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order);

        AlertDialog alertDialog = AlbertDialogUtil.showAlertDialog(this);
        networkChangeReceiver = new NetworkChangeReceiver(alertDialog);

        userAccount = (User) getIntent().getSerializableExtra("user");

        tabLayout = findViewById(R.id.tabLayoutOrder);

        viewPager = findViewById(R.id.viewPager);

        viewPagerAdapter = new ViewPagerAdapter(this, userAccount);
        viewPager.setAdapter(viewPagerAdapter);

        new TabLayoutMediator(tabLayout, viewPager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch (position) {
                    case 0:
                        tab.setText(title[position]);
                        break;
                    case 1:
                        tab.setText(title[position]);
                        break;
                    case 2:
                        tab.setText(title[position]);
                        break;
                }
            }
        }).attach();
    }

    @Override
    public void sendDetailToActivity(Order order,User user) {
        Intent intent = new Intent(MyOrderActivity.this, DetailOrderActivity.class);
        intent.putExtra("idOder", order);
        intent.putExtra("user", userAccount);
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