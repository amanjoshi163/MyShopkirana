package com.myshopkirana.model;

import com.google.gson.annotations.SerializedName;
import com.google.maps.model.LatLng;

import java.io.Serializable;
import java.util.ArrayList;

public class ClusterModel implements Serializable {
    @SerializedName("ClusterId")
    private int ClusterId;
    @SerializedName("ClusterName")
    private String ClusterName;
    @SerializedName("clusterlatlng")
    private ArrayList<ClusterLatLngModel> clusterLatLngList;

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

    public ArrayList<ClusterLatLngModel> getClusterLatLngList() {
        return clusterLatLngList;
    }

    public void setClusterLatLngList(ArrayList<ClusterLatLngModel> clusterLatLngList) {
        this.clusterLatLngList = clusterLatLngList;
    }
}
