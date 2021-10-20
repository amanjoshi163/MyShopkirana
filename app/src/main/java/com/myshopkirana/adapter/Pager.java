package com.myshopkirana.adapter;

import android.app.Activity;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.myshopkirana.activity.HomeActivity;
import com.myshopkirana.fragment.ListViewFragment;
import com.myshopkirana.fragment.MapViewFragment;


public class Pager extends FragmentStatePagerAdapter {

    //integer to count number of tabs
    int tabCount;
    Activity activity;

    //Constructor to the class
    public Pager(FragmentManager fm, int tabCount, HomeActivity homeActivity) {
        super(fm);
        //Initializing tab count
        this.tabCount = tabCount;
        this.activity = homeActivity;

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
                MapViewFragment tab2 = new MapViewFragment();
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
