package com.example.furniture.models;

import android.graphics.Bitmap;

public class Category {
    private String id;
    private String name;
    private Bitmap picture;

    public Category(String id, String name, Bitmap picture) {
        this.id = id;
        this.name = name;
        this.picture = picture;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Bitmap getPicture() {
        return picture;
    }

    public void setPicture(Bitmap picture) {
        this.picture = picture;
    }
}
