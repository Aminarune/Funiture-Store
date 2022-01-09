package com.example.furniture.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.furniture.R;
import com.example.furniture.fragments.AccountFragment;
import com.example.furniture.fragments.BellFragment;
import com.example.furniture.fragments.CartFragment;
import com.example.furniture.fragments.HomeFragment;
import com.example.furniture.fragments.MakerFragment;
import com.example.furniture.models.Cart;
import com.example.furniture.models.Product;
import com.example.furniture.models.ShippingAddress;
import com.example.furniture.models.User;
import com.example.furniture.services.GetShippingAddress;
import com.example.furniture.services.GetShippingAddressCheckOut;
import com.example.furniture.services.OnDataShipAddList;
import com.example.furniture.utilities.AlbertDialogUtil;
import com.example.furniture.utilities.DialogUtil;
import com.example.furniture.utilities.NetworkChangeReceiver;
import com.example.furniture.utilities.OnDataPassCart;
import com.example.furniture.utilities.OnDataPassProduct;
import com.example.furniture.utilities.OnDataPassUser;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements
        NavigationBarView.OnItemSelectedListener, View.OnClickListener, OnDataPassProduct,
        OnDataPassUser, OnDataPassCart {


    //check connection state auto
    private NetworkChangeReceiver networkChangeReceiver;

    private BottomNavigationView bottomNav;


    private User userLogin;

    private FloatingActionButton btnCart;

    private RequestQueue queue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        queue = Volley.newRequestQueue(this);

        userLogin = (User) getIntent().getSerializableExtra("userObject");


        btnCart = findViewById(R.id.btnCart);
        btnCart.setOnClickListener(this);

        bottomNav = findViewById(R.id.bottom_nav_view);

        //disable space menu holder
        bottomNav.getMenu().getItem(2).setEnabled(false);

        bottomNav.setBackground(null);

        bottomNav.setOnItemSelectedListener(this);

        AlertDialog alertDialog = AlbertDialogUtil.showAlertDialog(this);
        networkChangeReceiver = new NetworkChangeReceiver(alertDialog);

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
                createFragment(new AccountFragment(userLogin));
                break;
            default:
                createFragment(new HomeFragment());
                break;
        }
        return true;
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
    public void onDataPassProduct(Product data, String tag) {
        //receive data from fragment and send to detail
        Intent intent = new Intent(MainActivity.this, DetailProductActivity.class);
        intent.putExtra("productID", data.getId());
        intent.putExtra("user", userLogin);
        intent.putExtra("from", tag);
        startActivity(intent);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnCart:
                createFragment(new CartFragment(userLogin));
                break;
        }
    }


    @Override
    public void onDataPassUser(User user, String tag) {

        if (tag.equals("account")) {
            Intent intent = new Intent(MainActivity.this, ShippingActivity.class);
            intent.putExtra("user", user);
            startActivity(intent);
        }else if(tag.equals("my_order")){
            Intent intent = new Intent(MainActivity.this, MyOrderActivity.class);
            intent.putExtra("user", user);
            startActivity(intent);
        }

    }


    @Override
    public void onDataPassCart(User user, ArrayList<String> carts, ArrayList<String> products) {
        GetShippingAddressCheckOut getShippingAddress = new GetShippingAddressCheckOut(user, queue);
        getShippingAddress.execute();
        getShippingAddress.setOnDataShipAddList(new OnDataShipAddList() {
            @Override
            public void onSuccess(ArrayList<ShippingAddress> shippingAddresses) {
                moveToCheckOut(shippingAddresses, user, carts, products);
            }

            @Override
            public void onFail(String error) {
                moveToAddShipping(user, carts, products);
            }
        });
    }

    private void moveToAddShipping(User user, ArrayList<String> carts, ArrayList<String> products) {
        Intent intent = new Intent(MainActivity.this, AddShippingActivity.class);
        intent.putExtra("user", user);
        intent.putExtra("cart", carts);
        intent.putExtra("from","check_out");
        intent.putExtra("product", products);
        startActivity(intent);
        finish();
    }

    private void moveToCheckOut(ArrayList<ShippingAddress> shippingAddresses, User user,
                                ArrayList<String> carts, ArrayList<String> products) {
        for (int i = 0; i < shippingAddresses.size(); i++) {
            if (shippingAddresses.get(i).isStatus()) {
                Intent intent = new Intent(MainActivity.this, CheckOutActivity.class);
                intent.putExtra("user", user);
                intent.putExtra("shippingId", shippingAddresses.get(i).getId());
                intent.putExtra("cart", carts);
                intent.putExtra("product", products);
                startActivity(intent);
                finish();
                break;
            }
        }

    }


}
