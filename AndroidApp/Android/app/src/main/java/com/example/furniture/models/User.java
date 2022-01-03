package com.example.furniture.models;

import java.io.Serializable;

public class User implements Serializable {


    private String id;
    private String name;
    private String email;
    private String pass;
    private int picture;
    private boolean status;
    private String phone;

    public User(String id, String name, String email, String pass, int picture, boolean status, String phone) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.pass = pass;
        this.picture = picture;
        this.status = status;
        this.phone = phone;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPass() {
        return pass;
    }

    public int getPicture() {
        return picture;
    }

    public boolean isStatus() {
        return status;
    }

    public String getPhone() {
        return phone;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public void setPicture(int picture) {
        this.picture = picture;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
