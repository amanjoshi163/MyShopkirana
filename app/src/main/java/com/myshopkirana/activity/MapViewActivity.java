package com.myshopkirana.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.myshopkirana.R;
import com.myshopkirana.databinding.ActivityMapViewBinding;
import com.myshopkirana.model.ClusterLatLngModel;
import com.myshopkirana.utils.TextUtils;

import java.util.ArrayList;
import java.util.Random;

public class MapViewActivity extends AppCompatActivity implements OnMapReadyCallback {
    GoogleMap googleMapMain;
    private ArrayList<ArrayList<ClusterLatLngModel>> clusterLatLngList;
    private ActivityMapViewBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_map_view);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        clusterLatLngList = (ArrayList<ArrayList<ClusterLatLngModel>>) getIntent().getSerializableExtra("mylist");
        mBinding.toolbar.title.setText("Map View");
        mBinding.toolbar.back.setVisibility(View.VISIBLE);
        Log.e("clusterLatLngList",">>>"+clusterLatLngList.size());
        mBinding.toolbar.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        googleMapMain = googleMap;

        for (int i = 0; i < clusterLatLngList.size(); i++) {
            setMarkerdata(clusterLatLngList.get(i));
        }



    }

    private void setMarkerdata(ArrayList<ClusterLatLngModel> cList) {

        ArrayList<LatLng> latLngList = new ArrayList<>();
        for (int k = 0; k < cList.size(); k++) {

            if (!TextUtils.isNullOrEmpty(String.valueOf(cList.get(k).getLat()))) {

                latLngList.add(new LatLng(cList.get(k).getLat(),
                        cList.get(k).getLng()));


            }
            googleMapMain.animateCamera(CameraUpdateFactory.newLatLngZoom(new
                    LatLng(cList.get(k).getLat(),
                    cList.get(k).getLng()), 12.0f));


        }

        Random rnd = new Random();
        int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
        Polygon polygon1 = googleMapMain.addPolygon(new PolygonOptions()
                .clickable(true)
                .strokeColor(color)
                .fillColor(color)
                .addAll(latLngList));


    }
}