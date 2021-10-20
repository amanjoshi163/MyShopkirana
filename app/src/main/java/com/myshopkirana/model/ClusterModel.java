package com.myshopkirana.model;

import com.google.maps.model.LatLng;

import java.io.Serializable;
import java.util.ArrayList;

public class ClusterModel implements Serializable {
    private int ClusterId;
    private String ClusterName;
    private ArrayList<LatLng> clusterLatLngList;

    public int getClusterId() {
        return ClusterId;
    }

    public void setClusterId(int clusterId) {
        ClusterId = clusterId;
    }

    public String getClusterName() {
        return ClusterName;
    }

    public void setClusterName(String clusterName) {
        ClusterName = clusterName;
    }

    public ArrayList<LatLng> getClusterLatLngList() {
        return clusterLatLngList;
    }

    public void setClusterLatLngList(ArrayList<LatLng> clusterLatLngList) {
        this.clusterLatLngList = clusterLatLngList;
    }
}
