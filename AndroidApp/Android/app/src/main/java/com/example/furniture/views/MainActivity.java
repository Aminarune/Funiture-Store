package com.example.furniture.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

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

import pl.droidsonroids.gif.GifImageView;

public class MainActivity extends AppCompatActivity implements
        NavigationBarView.OnItemSelectedListener, View.OnClickListener, OnDataPassProduct,
        OnDataPassUser, OnDataPassCart {


    //check connection state auto
    private NetworkChangeReceiver networkChangeReceiver;

    private BottomNavigationView bottomNav;


    private User userLogin;

    private FloatingActionButton btnCart;

    private RequestQueue queue;

    private String tagFrom;

    private int exiteState = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        queue = Volley.newRequestQueue(this);

        userLogin = (User) getIntent().getSerializableExtra("user");


        btnCart = findViewById(R.id.btnCart);
        btnCart.setOnClickListener(this);


        bottomNav = findViewById(R.id.bottom_nav_view);

        //disable space menu holder
        bottomNav.getMenu().getItem(2).setEnabled(false);

        bottomNav.setBackground(null);

        bottomNav.setOnItemSelectedListener(this);

        Dialog dialog = AlbertDialogUtil.showAlertDialog(this);
        networkChangeReceiver = new NetworkChangeReceiver(dialog,R.raw.disconnected);

        tagFrom = getIntent().getStringExtra("from");
        if (tagFrom == null) {
            /*show home screen first*/
            bottomNav.setSelectedItemId(R.id.bottom_home_nav);
        } else if (tagFrom.equals("Maker")) {
            bottomNav.setSelectedItemId(R.id.bottom_maker_nav);
        } else if (tagFrom.equals("Home")) {
            bottomNav.setSelectedItemId(R.id.bottom_home_nav);
        } else if (tagFrom.equals("Shipping") || tagFrom.equals("MyOrder")) {
            bottomNav.setSelectedItemId(R.id.bottom_person_nav);
        } else if (tagFrom.equals("Cart")) {
            createFragment(new CartFragment(userLogin));
        }

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
                createFragment(new HomeFragment(userLogin));
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
                createFragment(new HomeFragment(userLogin));
                break;
        }
        return true;
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
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int) event.getRawX(), (int) event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent(event);
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
    public void onDataPassProduct(User user, Product data, String tag) {
        //receive data from fragment and send to detail
        Intent intent = new Intent(MainActivity.this, DetailProductActivity.class);
        intent.putExtra("productID", data.getId());
        intent.putExtra("user", user);
        intent.putExtra("from", tag);
        startActivity(intent);
        finish();
    }




    @Override
    public void onDataPassUser(User user, String tag) {

        if (tag.equals("Account")) {
            Intent intent = new Intent(MainActivity.this, ShippingActivity.class);
            intent.putExtra("user", user);
            intent.putExtra("from", tag);
            startActivity(intent);
            finish();
        } else if (tag.equals("MyOrder")) {
            Intent intent = new Intent(MainActivity.this, MyOrderActivity.class);
            intent.putExtra("user", user);
            intent.putExtra("from", tag);
            startActivity(intent);
            finish();
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
        intent.putExtra("from", "check_out");
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


    private void createExitDialog(Context context, int sourceGif, String mess) {



        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_layout_exit_dialog_gif);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView textView = dialog.findViewById(R.id.textMessDialogExit);
        textView.setText(mess);
        GifImageView imageView = dialog.findViewById(R.id.ivDialogExit);
        imageView.setImageResource(sourceGif);

        TextView tvNo = dialog.findViewById(R.id.tvNo);
        tvNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        TextView tvYes = dialog.findViewById(R.id.tvYes);
        tvYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                finish();
            }
        });


        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);

        dialog.show();
    }

    @Override
    public void onBackPressed() {


        //check current fragment

        Fragment f = getSupportFragmentManager().findFragmentById(R.id.frameLayoutHome);
        if (f instanceof CartFragment) {
            bottomNav.setSelectedItemId(R.id.bottom_home_nav);
        } else {
            int checkedItemId = bottomNav.getSelectedItemId();

            switch (checkedItemId) {
                case R.id.bottom_home_nav:
                    String mess = "Do you really want to quit?";
                    createExitDialog(MainActivity.this,R.raw.exit,mess);
                    break;
                case R.id.bottom_maker_nav:
                    bottomNav.setSelectedItemId(R.id.bottom_home_nav);
                    break;
                case R.id.bottom_bell_nav:
                    bottomNav.setSelectedItemId(R.id.bottom_home_nav);
                    break;
                case R.id.bottom_person_nav:
                    bottomNav.setSelectedItemId(R.id.bottom_home_nav);
                    break;
                default:
                    bottomNav.setSelectedItemId(R.id.bottom_home_nav);
                    break;
            }


        }


    }


}
