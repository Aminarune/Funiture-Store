package com.example.furniture.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.furniture.R;
import com.example.furniture.fragments.AccountFragment;
import com.example.furniture.fragments.BellFragment;
import com.example.furniture.fragments.HomeFragment;
import com.example.furniture.fragments.MakerFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener {


    private BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNav = findViewById(R.id.navigationBottom);
        bottomNav.setOnItemSelectedListener(this);

        /*show home screen first*/
        bottomNav.setSelectedItemId(R.id.bottom_home_nav);


    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.bottom_home_nav:
                createFragment(new HomeFragment());
                break;
            case R.id.bottom_maker_nav:
                createFragment(new MakerFragment());
                break;
            case R.id.bottom_bell_nav:
                createFragment(new BellFragment());
                break;
            case R.id.bottom_person_nav:
                createFragment(new AccountFragment());
                break;
            default:
                createFragment(new HomeFragment());
                break;
        }
        return true;
    }

    private void createFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frameLayoutHome, fragment).commit();
    }
}
