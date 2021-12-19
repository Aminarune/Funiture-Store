package com.example.furniture.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.furniture.R;
import com.example.furniture.views.OrderHistoryActivity;
import com.example.furniture.views.PaymentMethodActivity;
import com.example.furniture.views.SettingActivity;
import com.example.furniture.views.ShippingAddressActivity;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AccountFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AccountFragment extends Fragment implements View.OnClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AccountFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AccountFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AccountFragment newInstance(String param1, String param2) {
        AccountFragment fragment = new AccountFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    private ImageButton btnPayment, btnOrderHistory, btnShippingAddress, btnSetting;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);


        btnOrderHistory = view.findViewById(R.id.ivOrderHisButton);
        btnOrderHistory.setOnClickListener(this::onClick);

        btnShippingAddress = view.findViewById(R.id.ivShippingAdButton);
        btnShippingAddress.setOnClickListener(this::onClick);

        btnPayment = view.findViewById(R.id.ivPaymentButton);
        btnPayment.setOnClickListener(this::onClick);

        btnSetting = view.findViewById(R.id.ivSettingButton);
        btnSetting.setOnClickListener(this::onClick);

        return view;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.ivOrderHisButton:
                Intent intentOrder = new Intent(v.getContext(), OrderHistoryActivity.class);
                startActivity(intentOrder);
                break;
            case R.id.ivShippingAdButton:
                Intent intentShip = new Intent(v.getContext(), ShippingAddressActivity.class);
                startActivity(intentShip);
                break;
            case R.id.ivPaymentButton:
                Intent intentPer = new Intent(v.getContext(), PaymentMethodActivity.class);
                startActivity(intentPer);
                break;
            case R.id.ivSettingButton:
                Intent intentSet = new Intent(v.getContext(), SettingActivity.class);
                startActivity(intentSet);
                break;
        }

    }
}