package com.example.furniture.services;

import com.example.furniture.models.Cart;

public interface OnDataGetCart {
    void onFoundCartItem(Cart cart);
    void onNotFoundItem(String error);
}
