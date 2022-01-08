package com.example.furniture.utilities;

import com.example.furniture.models.Cart;
import com.example.furniture.models.Product;
import com.example.furniture.models.User;

import java.util.ArrayList;

public interface OnDataPassCart {
    public void onDataPassCart(User user, ArrayList<String> carts, ArrayList<String> products);
}
