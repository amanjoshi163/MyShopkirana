package com.myshopkirana.model;

import java.io.Serializable;

public class CityModel implements Serializable {


    private int Cityid;
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
