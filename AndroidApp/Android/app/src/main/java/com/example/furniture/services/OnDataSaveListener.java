package com.example.furniture.services;

import com.example.furniture.models.Favourite;

public interface OnDataSaveListener {
    void onSuccess(Favourite favourite);
    void onFail(String error);
}
