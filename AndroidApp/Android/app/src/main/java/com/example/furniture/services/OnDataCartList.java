package com.example.furniture.services;

import android.content.Context;

import com.example.furniture.models.Cart;
import com.example.furniture.models.Favourite;

import java.util.ArrayList;

public interface OnDataCartList {
    void onDataCartFound(Context view, ArrayList<Cart> cartArrayList);
    void onDataCartNotFound(Context view,String error);
}
