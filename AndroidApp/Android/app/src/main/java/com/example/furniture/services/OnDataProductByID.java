package com.example.furniture.services;

import android.content.Context;
import android.view.View;

import com.example.furniture.models.Category;
import com.example.furniture.models.Product;

import java.util.ArrayList;

public interface OnDataProductByID {

    void onFound(Context context, Product product);

    void onNotFound(Context context, String error);
}
