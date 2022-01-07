package com.example.furniture.models;

public class ShippingAddress {
    private String id;
    private String idUser;
    private String address;
    private String province;
    private String district;
    private String ward;
    private boolean status;

    public ShippingAddress(String id, String idUser, String address,
                           String province, String district, String ward, boolean status) {
        this.id = id;
        this.idUser = idUser;
        this.address = address;
        this.province = province;
        this.district = district;
        this.ward = ward;
        this.status = status;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getWard() {
        return ward;
    }

    public void setWard(String ward) {
        this.ward = ward;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
