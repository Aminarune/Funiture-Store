package com.example.furniture.utilities;

import com.example.furniture.models.Product;
import com.example.furniture.models.User;

public interface OnDataPassProduct {
    public void onDataPassProduct(User user,Product data, String tag);
}
