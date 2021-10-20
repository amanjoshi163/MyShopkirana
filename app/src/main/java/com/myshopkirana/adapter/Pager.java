package com.myshopkirana.adapter;

import android.app.Activity;
import android.util.Log;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.google.maps.model.LatLng;
import com.myshopkirana.activity.HomeActivity;
import com.myshopkirana.fragment.ListViewFragment;
import com.myshopkirana.fragment.MapViewFragment;
import com.myshopkirana.model.ClusterLatLngModel;
import com.myshopkirana.model.ClusterModel;

import java.util.ArrayList;


public class Pager extends FragmentStatePagerAdapter {

    //integer to count number of tabs
    int tabCount;
    Activity activity;
    private ArrayList<ClusterLatLngModel> clusterLatLngList;
    //Constructor to the class
    public Pager(FragmentManager fm, int tabCount, HomeActivity homeActivity, ArrayList<ClusterLatLngModel> clusterList) {
        super(fm);
        //Initializing tab count
        this.tabCount = tabCount;
        this.activity = homeActivity;
        this.clusterLatLngList=clusterList;
        Log.e("CLUSTERLISSSS1111",">> "+clusterList.size());

    }

    //Overriding method getItem
    @Override
    public Fragment getItem(int position) {
       // Returning the current tabs
        switch (position) {
            case 0:
                ListViewFragment tab1 = new ListViewFragment();
                return tab1;
            case 1:

                MapViewFragment tab2 = new MapViewFragment(clusterLatLngList);
                return tab2;


            default:
                return null;
        }

    }

    //Overriden method getCount to get the number of tabs
    @Override
    public int getCount() {
        return tabCount;
    }


}
