package com.example.furniture.services;

import com.example.furniture.models.ShippingAddress;


import java.util.ArrayList;

public interface OnDataShipAddList {
    void onSuccess(ArrayList<ShippingAddress> shippingAddresses);
    void onFail(String error);
}
