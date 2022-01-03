package com.example.furniture.models;

import android.graphics.Bitmap;
import android.os.Parcelable;

import java.io.Serializable;
import java.math.BigDecimal;

public class Product implements Serializable {
    private String id;
    private String name;
    private String idCategory;
    private String price;
    private String desc;
    private Bitmap picture;
    private Boolean status;

    public Product(String id, String name, String idCategory, String price, String desc, Bitmap picture, Boolean status) {
        this.id = id;
        this.name = name;
        this.idCategory = idCategory;
        this.price = price;
        this.desc = desc;
        this.picture = picture;
        this.status = status;
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

    public String getIdCategory() {
        return idCategory;
    }

    public void setIdCategory(String idCategory) {
        this.idCategory = idCategory;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Bitmap getPicture() {
        return picture;
    }

    public void setPicture(Bitmap picture) {
        this.picture = picture;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}
