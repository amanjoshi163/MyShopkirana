package com.myshopkirana.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.myshopkirana.R;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Marker;
import com.myshopkirana.R;
import com.myshopkirana.databinding.FragmentListViewBinding;


public class ListViewFragment extends Fragment {
    private String mParam1;
    private String mParam2;
    FragmentListViewBinding mBinding;

    public ListViewFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_list_view, container, false);
        return mBinding.getRoot();
    }


}