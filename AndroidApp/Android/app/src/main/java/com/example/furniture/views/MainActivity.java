package com.example.furniture.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.example.furniture.R;
import com.example.furniture.fragments.AccountFragment;
import com.example.furniture.fragments.BellFragment;
import com.example.furniture.fragments.CartFragment;
import com.example.furniture.fragments.HomeFragment;
import com.example.furniture.fragments.MakerFragment;
import com.example.furniture.models.Product;
import com.example.furniture.models.User;
import com.example.furniture.services.DownloadDataCategory;
import com.example.furniture.utilities.AlertDialogUtil;
import com.example.furniture.utilities.NetworkChangeReceiver;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity implements
        NavigationBarView.OnItemSelectedListener, HomeFragment.OnDataPassProduct, View.OnClickListener {


    //check connection state auto
    private NetworkChangeReceiver networkChangeReceiver;

    private BottomNavigationView bottomNav;

    private boolean checkBox;

    private User userLogin;

    private FloatingActionButton btnCart;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userLogin=getUserInfor();

        btnCart=findViewById(R.id.btnCart);
        btnCart.setOnClickListener(this);

        bottomNav = findViewById(R.id.bottom_nav_view);

        //disable space menu holder
        bottomNav.getMenu().getItem(2).setEnabled(false);

        bottomNav.setBackground(null);

        bottomNav.setOnItemSelectedListener(this);


        networkChangeReceiver = new NetworkChangeReceiver(
                AlertDialogUtil.showAlertDialog(MainActivity.this,
                        R.raw.disconnected, "Please check your connection and try again"));

        /*show home screen first*/
        bottomNav.setSelectedItemId(R.id.bottom_home_nav);


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


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.bottom_home_nav:
                createFragment(new HomeFragment());
                break;
            case R.id.bottom_maker_nav:
                createFragment(new MakerFragment(userLogin));
                break;
            case R.id.bottom_bell_nav:
                createFragment(new BellFragment());
                break;
            case R.id.bottom_person_nav:
                createFragment(new AccountFragment(userLogin, checkBox));
                break;
            default:
                createFragment(new HomeFragment());
                break;
        }
        return true;
    }

    private User getUserInfor() {
        Intent intent = getIntent();
        User user = (User) intent.getSerializableExtra("userObject");
        checkBox = intent.getBooleanExtra("checkBox", false);
        return user;
    }


    private void savePreferences(String email, String pass, boolean check) {
        SharedPreferences sharedPreferences = getSharedPreferences("caches", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("email", email);
        editor.putString("pass", pass);
        editor.putBoolean("check", check);
        editor.commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void createFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frameLayoutHome, fragment).commit();
    }


    @Override
    public void onDataPassProduct(Product data) {
        //receive data from fragment and send to detail
        Intent intent=new Intent(MainActivity.this, DetailProductActivity.class);
        intent.putExtra("productID",data.getId());
        intent.putExtra("user",userLogin);
        intent.putExtra("from","home_fragment");
        startActivity(intent);
    }




    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnCart:
                createFragment(new CartFragment(userLogin));
                break;
        }
    }
}
