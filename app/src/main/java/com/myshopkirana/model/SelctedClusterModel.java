package com.myshopkirana.model;

public class SelctedClusterModel {

    private int clusterId;

    public String getClusterName() {
        return clusterName;
    }

    public void setClusterName(String clusterName) {
        this.clusterName = clusterName;
    }

    private String clusterName;
    private boolean isSelected;


    public int getClusterId() {
        return clusterId;
    }

    public SelctedClusterModel(int clusterId, String clusterName, boolean isSelected) {
        this.clusterId = clusterId;
        this.clusterName = clusterName;
        this.isSelected = isSelected;
    }

    public void setClusterId(int clusterId) {
        this.clusterId = clusterId;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
