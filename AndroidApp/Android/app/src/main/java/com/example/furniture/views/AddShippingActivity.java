package com.example.furniture.views;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.furniture.R;
import com.example.furniture.adapters.SpinnerCityAdapter;
import com.example.furniture.adapters.SpinnerDistrictAdapter;
import com.example.furniture.adapters.SpinnerWardAdapter;
import com.example.furniture.models.City;
import com.example.furniture.models.District;
import com.example.furniture.models.ShippingAddress;
import com.example.furniture.models.User;
import com.example.furniture.models.Ward;
import com.example.furniture.services.DownloadCity;
import com.example.furniture.services.DownloadDistrict;
import com.example.furniture.services.DownloadWard;
import com.example.furniture.services.OnDataSaveAddress;
import com.example.furniture.services.SaveToAddress;
import com.example.furniture.services.SaveToCart;
import com.example.furniture.services.UpdateToAddress;
import com.example.furniture.utilities.AlbertDialogUtil;
import com.example.furniture.utilities.NetworkChangeReceiver;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;

public class AddShippingActivity extends AppCompatActivity implements
        DownloadCity.GetCity,
        DownloadDistrict.GetDistrict,
        DownloadWard.GetWard {


    private Spinner spinnerCity, spinnerDistrict, spinnerWard;

    private RequestQueue queue;

    private EditText editAddress;

    private Button btnSaveAddress;


    private User user;


    private LinearLayout layoutAddShipping;

    private ShippingAddress shippingAddress;

    private String from;

    //check connection state auto
    private NetworkChangeReceiver networkChangeReceiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_shipping);

        editAddress = findViewById(R.id.editAddressDetail);

        spinnerCity = findViewById(R.id.spinner_city);

        spinnerDistrict = findViewById(R.id.spinner_district);

        spinnerWard = findViewById(R.id.spinner_ward);

        layoutAddShipping = findViewById(R.id.layoutAddShipping);

        btnSaveAddress = findViewById(R.id.btnAddToShipping);

        queue = Volley.newRequestQueue(this);

        from = getIntent().getStringExtra("from");
        if (from.equals("from_create")) {
            user = (User) getIntent().getSerializableExtra("user");
        } else if (from.equals("from_edit")) {
            user = (User) getIntent().getSerializableExtra("user");
            shippingAddress = (ShippingAddress) getIntent().getSerializableExtra("shipping");
        }


        btnSaveAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getAddressInfor(user);
            }
        });


        AlertDialog alertDialog = AlbertDialogUtil.showAlertDialog(this);
        networkChangeReceiver = new NetworkChangeReceiver(alertDialog);

        downloadCityList();

    }

    private void setValue(User user, ShippingAddress shippingAddress) {

        editAddress.setText(shippingAddress.getAddress());

        setPosition(spinnerCity, shippingAddress.getProvince());

        setPosition(spinnerDistrict, shippingAddress.getProvince());

        setPosition(spinnerWard, shippingAddress.getProvince());

    }


    private void setPosition(Spinner spinner, String name) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equals(name)) {
                spinner.setSelection(i);
                break;
            }
        }
    }


    private void downloadCityList() {
        DownloadCity downloadCity = new DownloadCity(this, queue);
        downloadCity.execute();
        downloadCity.setGetCity(this::getCity);
    }

    @Override
    public void getCity(ArrayList<City> cities) {
        SpinnerCityAdapter spinnerCityAdapter = new SpinnerCityAdapter(AddShippingActivity.this,
                android.R.layout.simple_list_item_1, cities);
        spinnerCity.setAdapter(spinnerCityAdapter);

        spinnerCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                downloadDistrictList(cities, i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


    }

    private void downloadDistrictList(ArrayList<City> cities, int pos) {
        DownloadDistrict downloadDistrict = new DownloadDistrict(
                AddShippingActivity.this, queue,
                cities.get(pos).getId());
        downloadDistrict.execute();
        downloadDistrict.setGetDistrict(this::getDistrict);
    }


    @Override
    public void getDistrict(ArrayList<District> districts) {
        SpinnerDistrictAdapter spinnerDistrictAdapter = new SpinnerDistrictAdapter(AddShippingActivity.this,
                android.R.layout.simple_list_item_1, districts);
        spinnerDistrict.setAdapter(spinnerDistrictAdapter);

        spinnerDistrict.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                downloadWardList(districts, i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void downloadWardList(ArrayList<District> districts, int pos) {
        DownloadWard downloadWard = new DownloadWard(
                AddShippingActivity.this, queue,
                districts.get(pos).getId());
        downloadWard.execute();
        downloadWard.setGetWard(this::getWard);
    }

    @Override
    public void getWard(ArrayList<Ward> wards) {
        SpinnerWardAdapter spinnerWardAdapter = new SpinnerWardAdapter(AddShippingActivity.this,
                android.R.layout.simple_list_item_1, wards);
        spinnerWard.setAdapter(spinnerWardAdapter);

        if(from.equals("from_edit")){
            setValue(user, shippingAddress);
        }

    }

    private void getAddressInfor(User user) {
        String address = editAddress.getText().toString();
        City city = (City) spinnerCity.getSelectedItem();
        District district = (District) spinnerDistrict.getSelectedItem();
        Ward ward = (Ward) spinnerWard.getSelectedItem();
        if (from.equals("from_create")) {
            SaveToAddress saveToAddress = new SaveToAddress(AddShippingActivity.this,
                    user,
                    address,
                    city.getTitle(),
                    district.getTitle(),
                    ward.getTitle(),
                    queue);
            saveToAddress.execute();
            saveToAddress.setDataSaveAddress(new OnDataSaveAddress() {
                @Override
                public void onSuccess() {

                    moveToShipping(user);
                }

                @Override
                public void onFail() {

                }
            });
        } else if (from.equals("from_edit")) {
            UpdateToAddress updateToAddress = new UpdateToAddress(AddShippingActivity.this,
                    user,
                    shippingAddress.getId(),
                    address,
                    city.getTitle(),
                    district.getTitle(),
                    ward.getTitle(),
                    shippingAddress.isStatus(),
                    queue);
            updateToAddress.execute();
            updateToAddress.setDataSaveAddress(new OnDataSaveAddress() {
                @Override
                public void onSuccess() {
                    moveToShipping(user);
                }

                @Override
                public void onFail() {

                }
            });
        }

    }


    private void moveToShipping(User user) {
        finish();
    }


    @Override
    public void onBackPressed() {
        finish();
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