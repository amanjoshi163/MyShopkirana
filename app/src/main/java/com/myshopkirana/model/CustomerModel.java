package com.myshopkirana.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CustomerModel implements Serializable {
 @SerializedName("CustomerId")
    private int CustomerId;
    @SerializedName("Skcode")
    private String Skcode;

    @SerializedName("ShopName")
    private String ShopName;
    @SerializedName("ShippingAddress")
    private String ShippingAddress;
    @SerializedName("LandMark")
    private String LandMark;
    @SerializedName("lat")
    private double lat;
    @SerializedName("lg")
    private double lg;

    public int getCustomerId() {
        return CustomerId;
    }

    public void setCustomerId(int customerId) {
        CustomerId = customerId;
    }

    public String getSkcode() {
        return Skcode;
    }

    public void setSkcode(String skcode) {
        Skcode = skcode;
    }

    public String getShopName() {
        return ShopName;
    }

    public void setShopName(String shopName) {
        ShopName = shopName;
    }

    public String getShippingAddress() {
        return ShippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        ShippingAddress = shippingAddress;
    }

    public String getLandMark() {
        return LandMark;
    }

    public void setLandMark(String landMark) {
        LandMark = landMark;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLg() {
        return lg;
    }

    public void setLg(double lg) {
        this.lg = lg;
    }
}
