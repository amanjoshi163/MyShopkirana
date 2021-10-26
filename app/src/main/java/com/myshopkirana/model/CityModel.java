package com.myshopkirana.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CityModel implements Serializable {

    @SerializedName("Cityid")
    private int Cityid;
    @SerializedName("CityName")
    private String CityName;

    public int getCityid() {
        return Cityid;
    }

    public void setCityid(int cityid) {
        Cityid = cityid;
    }

    public String getCityName() {
        return CityName;
    }

    public void setCityName(String cityName) {
        CityName = cityName;
    }
}
