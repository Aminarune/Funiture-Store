package com.example.furniture.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;

import com.example.furniture.R;
import com.example.furniture.adapters.ViewPagerAdapter;
import com.example.furniture.models.User;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class MyOrderActivity extends AppCompatActivity {

    private TabLayout tabLayout;

    private ViewPager2 viewPager;

    private ViewPagerAdapter viewPagerAdapter;

    private User user;

    private static final String[] title = {"Pending", "Delivering", "Completed"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order);

        user= (User) getIntent().getSerializableExtra("user");

        tabLayout = findViewById(R.id.tabLayoutOrder);

        viewPager = findViewById(R.id.viewPager);

        viewPagerAdapter = new ViewPagerAdapter(this);
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
}