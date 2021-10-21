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
    @SerializedName("ShopFound")
    private String ShopFound;
    @SerializedName("CaptureImagePath")
    private String CaptureImagePath;
    @SerializedName("NewShippingAddress")
    private String NewShippingAddress;
    @SerializedName("Newlat")
    private String Newlat;
    @SerializedName("Newlg")
    private String Newlg;



    public CustomerModel(int customerId, String skcode, String shopName, String shippingAddress, String landMark, double lat, double lg, String shopFound, String captureImagePath, String newShippingAddress, String newlat, String newlg) {
        CustomerId = customerId;
        Skcode = skcode;
        ShopName = shopName;
        ShippingAddress = shippingAddress;
        LandMark = landMark;
        this.lat = lat;
        this.lg = lg;
        ShopFound = shopFound;
        CaptureImagePath = captureImagePath;
        NewShippingAddress = newShippingAddress;
        Newlat = newlat;
        Newlg = newlg;
    }

    public String getShopFound() {
        return ShopFound;
    }

    public void setShopFound(String shopFound) {
        ShopFound = shopFound;
    }

    public String getCaptureImagePath() {
        return CaptureImagePath;
    }

    public void setCaptureImagePath(String captureImagePath) {
        CaptureImagePath = captureImagePath;
    }

    public String getNewShippingAddress() {
        return NewShippingAddress;
    }

    public void setNewShippingAddress(String newShippingAddress) {
        NewShippingAddress = newShippingAddress;
    }

    public String getNewlat() {
        return Newlat;
    }

    public void setNewlat(String newlat) {
        Newlat = newlat;
    }

    public String getNewlg() {
        return Newlg;
    }

    public void setNewlg(String newlg) {
        Newlg = newlg;
    }

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
