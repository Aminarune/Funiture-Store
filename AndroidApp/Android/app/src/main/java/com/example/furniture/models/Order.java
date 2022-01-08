package com.example.furniture.models;

public class Order {
    private String id;
    private String date;
    private String price;
    private String idUser;
    private String state;
    private String idShipping;
    private String status;

    public Order(String id, String date, String price, String idUser, String state, String idShipping, String status) {
        this.id = id;
        this.date = date;
        this.price = price;
        this.idUser = idUser;
        this.state = state;
        this.idShipping = idShipping;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getIdShipping() {
        return idShipping;
    }

    public void setIdShipping(String idShipping) {
        this.idShipping = idShipping;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
