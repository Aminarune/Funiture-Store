package com.example.furniture.views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.furniture.R;
import com.example.furniture.adapters.SpinnerCityAdapter;
import com.example.furniture.adapters.SpinnerDistrictAdapter;
import com.example.furniture.adapters.SpinnerWardAdapter;
import com.example.furniture.models.City;
import com.example.furniture.models.District;
import com.example.furniture.models.User;
import com.example.furniture.models.Ward;
import com.example.furniture.services.DownloadCity;
import com.example.furniture.services.DownloadDistrict;
import com.example.furniture.services.DownloadWard;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;

public class AddShippingActivity extends AppCompatActivity implements
        DownloadCity.GetCity,
        View.OnClickListener,
        DownloadDistrict.GetDistrict,
        DownloadWard.GetWard {


    private Spinner spinnerCity, spinnerDistrict, spinnerWard;

    private RequestQueue queue;

    private EditText editAddress;

    private Button btnSaveAddress;

    private User user;

    private ImageView ivBackToAddShipping;

    private LinearLayout layoutAddShipping;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_shipping);

        editAddress = findViewById(R.id.editAddressDetail);

        spinnerCity = findViewById(R.id.spinner_city);

        spinnerDistrict = findViewById(R.id.spinner_district);

        spinnerWard = findViewById(R.id.spinner_ward);

        btnSaveAddress = findViewById(R.id.btnAddToShipping);
        btnSaveAddress.setOnClickListener(this::onClick);

        ivBackToAddShipping = findViewById(R.id.ivBackToAddShipping);
        ivBackToAddShipping.setOnClickListener(this::onClick);

        queue = Volley.newRequestQueue(this);


        user = (User) getIntent().getSerializableExtra("user");

        layoutAddShipping=findViewById(R.id.layoutAddShipping);


        downloadCityList();

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

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnAddToShipping:
                getAddressInfor();
                break;
            case R.id.ivBackToAddShipping:
                moveToShipping(user);
                break;

        }
    }

    private void getAddressInfor() {
        String address = editAddress.getText().toString();
        City city = (City) spinnerCity.getSelectedItem();
        District district = (District) spinnerDistrict.getSelectedItem();
        Ward ward = (Ward) spinnerWard.getSelectedItem();
    }


    private void moveToShipping(User user) {
        Intent intent = new Intent(AddShippingActivity.this, ShippingActivity.class);
        startActivity(intent);
        finish();
    }


    @Override
    public void onBackPressed() {
        moveToShipping(user);
    }


}