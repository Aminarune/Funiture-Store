package com.example.furniture.utilities;

import com.example.furniture.models.Order;
import com.example.furniture.models.User;

public interface SendDetailToActivity {
    void sendDetailToActivity(Order order, User user);
}
