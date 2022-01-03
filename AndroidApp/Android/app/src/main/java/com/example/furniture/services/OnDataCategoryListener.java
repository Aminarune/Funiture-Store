package com.example.furniture.services;

import android.view.View;

import com.example.furniture.models.Category;

import java.util.ArrayList;

public interface OnDataCategoryListener {

    void onCompleteDataCategory(View view, ArrayList<Category> arrayList);

    void onErrorDataCategory(View view, String error);

}
