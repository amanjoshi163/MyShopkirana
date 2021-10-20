package com.myshopkirana.activity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.myshopkirana.R;
import com.myshopkirana.adapter.CityAdapter;
import com.myshopkirana.databinding.ActivityHomeBinding;
import com.myshopkirana.model.CityModel;
import com.myshopkirana.utils.CommonClassForAPI;

import java.util.ArrayList;

import io.reactivex.observers.DisposableObserver;

public class HomeActivity extends AppCompatActivity implements OnMapReadyCallback {
    private final DisposableObserver<ArrayList<CityModel>> objCity = new DisposableObserver<ArrayList<CityModel>>() {

        @Override
        public void onNext(ArrayList<CityModel> cList) {
            try {

                CityAdapter adapter = new CityAdapter(HomeActivity.this,
                        R.layout.listitems_layout, R.id.title, cList);
                mBinding.spnCity.setAdapter(adapter);
             } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onError(Throwable e) {
            e.printStackTrace();

            objCity.dispose();
        }

        @Override
        public void onComplete() {

        }
    };
    ActivityHomeBinding mBinding;
    private CommonClassForAPI commonClassForAPI;
    private GoogleMap mMap;
    private PolygonOptions polygonOptions;
    private Polygon polygon;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_home);

        inits();

    }

    private void inits() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        polygonOptions = new PolygonOptions();
        commonClassForAPI = CommonClassForAPI.getInstance(this);

        calcityApi();
    }

    private void calcityApi() {
        commonClassForAPI.fetchCityList(objCity);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        objCity.dispose();
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        // Add a marker in Sydney and move the camera
        LatLng TutorialsPoint = new LatLng(21, 57);
        mMap.addMarker(new
                MarkerOptions().position(TutorialsPoint).title("Tutorialspoint.com"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(TutorialsPoint));
    }
}