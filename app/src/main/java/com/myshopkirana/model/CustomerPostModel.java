package com.myshopkirana.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CustomerPostModel  implements Serializable {

    @SerializedName("CustomerId")
    private String CustomerId;
    @SerializedName("Skcode")
    private String Skcode;
    @SerializedName("ShopName")
    private String ShopName;
    @SerializedName("ShippingAddress")
    private String ShippingAddress;
    @SerializedName("LandMark")
    private String LandMark;
    @SerializedName("lat")
    private String lat;
    @SerializedName("lg")
    private String lg;
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
    @SerializedName("Aerialdistance")
    private String Aerialdistance;

    public CustomerPostModel(String cid, String skcode, String shopname, String shipAddres, String landMark, String lat, String lng,
                             String shopFoundValue, String mainURl, String fullAddress, String newLat, String newLng,
                             String dist) {
        this.CustomerId=cid;
        this.Skcode=skcode;
        this.ShopName=shopname;
        this.ShippingAddress=shipAddres;
        this.LandMark=landMark;
        this.lat=lat;
        this.lg=lng;
        this.ShopFound=shopFoundValue;
        this.CaptureImagePath=mainURl;
        this.NewShippingAddress=fullAddress;
        this.Newlat=newLat;
        this.Newlg=newLng;
        this.Aerialdistance=dist;
            }


    public String getCustomerId() {
        return CustomerId;
    }

    public void setCustomerId(String customerId) {
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

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLg() {
        return lg;
    }

    public void setLg(String lg) {
        this.lg = lg;
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

    public String getAerialdistance() {
        return Aerialdistance;
    }

    public void setAerialdistance(String aerialdistance) {
        Aerialdistance = aerialdistance;
    }
}

