package com.example.furniture.models;

import java.io.Serializable;

public class Favourite implements Serializable {
    private String id;
    private String idUser;
    private String idProduct;

    public Favourite(String id, String idUser, String idProduct) {
        this.id = id;
        this.idUser = idUser;
        this.idProduct = idProduct;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(String idProduct) {
        this.idProduct = idProduct;
    }
}
