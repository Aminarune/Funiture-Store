package com.example.furniture.services;

public interface OnFavDataListener {
    void onFoundFav(boolean result,String id);
    void onNotFoundFav(boolean result);
}
