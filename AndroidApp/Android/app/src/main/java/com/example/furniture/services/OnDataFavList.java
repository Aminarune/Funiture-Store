package com.example.furniture.services;

import android.content.Context;
import android.view.View;

import com.example.furniture.models.Favourite;

import java.util.ArrayList;

public interface OnDataFavList {
    void onDataFavFound(Context view, ArrayList<Favourite> favourites);
    void onDataFavNotFound(Context view,String error);
}
