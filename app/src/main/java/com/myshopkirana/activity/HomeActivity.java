package com.myshopkirana.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.JsonArray;
import com.myshopkirana.R;
import com.myshopkirana.adapter.CityAdapter;
import com.myshopkirana.adapter.ClusterAdapter;
import com.myshopkirana.adapter.CustomerAdapter;
import com.myshopkirana.databinding.ActivityHomeBinding;
import com.myshopkirana.model.CityModel;
import com.myshopkirana.model.ClusterLatLngModel;
import com.myshopkirana.model.ClusterModel;
import com.myshopkirana.model.CustomerModel;
import com.myshopkirana.utils.CommonClassForAPI;
import com.myshopkirana.utils.GPSTracker;
import com.myshopkirana.utils.GpsUtils;
import com.myshopkirana.utils.Utils;
import com.nabinbhandari.android.permissions.PermissionHandler;
import com.nabinbhandari.android.permissions.Permissions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.reactivex.observers.DisposableObserver;

public class HomeActivity extends AppCompatActivity {
    CustomerAdapter customerAdapter;
    ArrayList<CustomerModel> custMainList;
    List<Address> addresses;
    private final DisposableObserver<ArrayList<CustomerModel>> objcutomer = new DisposableObserver<ArrayList<CustomerModel>>() {

        @Override
        public void onNext(ArrayList<CustomerModel> custList) {
            try {
                custMainList = new ArrayList<>();
                custMainList = custList;
                customerAdapter.updateList(custMainList);
                Utils.hideProgressDialog(HomeActivity.this);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onError(Throwable e) {
            e.printStackTrace();

            objcutomer.dispose();
        }

        @Override
        public void onComplete() {

        }
    };
    private Utils utils;
    private ActivityHomeBinding mBinding;
    private ArrayList<CityModel> cMainList;
    GPSTracker gpsTracker;
    Geocoder geocoder;
    String cityName="";
    private final DisposableObserver<ArrayList<CityModel>> objCity = new DisposableObserver<ArrayList<CityModel>>() {

        @Override
        public void onNext(ArrayList<CityModel> cList) {
            try {
//                cMainList = new ArrayList<>();
//                CityModel cityModel = new CityModel();
//                cityModel.setCityid(00);
//                cityModel.setCityName("Select City");
//                cList.add(cityModel);

                cMainList.addAll(cList);
                CityAdapter adapter = new CityAdapter(HomeActivity.this,
                        R.layout.listitems_layout, R.id.cityname, cMainList);
                mBinding.spnCity.setAdapter(adapter);


                try {
                    addresses = geocoder.getFromLocation(gpsTracker.getLatitude(), gpsTracker.getLongitude(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                    cityName = "" + addresses.get(0).getLocality();
                    System.out.println(cityName);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                int pos = 0;
                for (int i = 0; i <cMainList.size() ; i++) {
                    if(!cityName.isEmpty()){
                        if(cityName.equalsIgnoreCase(cMainList.get(i).getCityName().toLowerCase()))
                        {
                            pos = i;
                        }
                    }
                }
                mBinding.spnCity.setSelection(pos);

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
    private CommonClassForAPI commonClassForAPI;
    private ArrayList<ClusterLatLngModel> clusterLatLngList;
    private ArrayList<ClusterModel> clusterList;
    private final DisposableObserver<ArrayList<ClusterModel>> objCluster = new DisposableObserver<ArrayList<ClusterModel>>() {

        @Override
        public void onNext(ArrayList<ClusterModel> cList) {
            try {
                clusterList = new ArrayList<>();
//                ClusterModel clusterModel = new ClusterModel();
//                clusterModel.setClusterId(00);
//                clusterModel.setClusterName("Select Cluster");
//                clusterModel.setClusterLatLngList(new ArrayList<>());
//                cList.add(clusterModel);

                clusterList = cList;
                ClusterAdapter adapter = new ClusterAdapter(HomeActivity.this,
                        R.layout.listitems_layout, R.id.cityname, clusterList);
                mBinding.spnCluster.setAdapter(adapter);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onError(Throwable e) {
            e.printStackTrace();

            objCluster.dispose();
        }

        @Override
        public void onComplete() {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_home);
        new GpsUtils(this).turnGPSOn(isGPSEnable -> {

        });
        callRunTimePermissions();
        inits();

    }
    private int getIndex(Spinner spinner, String myString){
        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)){
                return i;

            }
        }

        return 0;
    }
    public void callRunTimePermissions() {
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA};
        Permissions.check(HomeActivity.this/*context*/, permissions, null/*rationale*/, null/*options*/, new PermissionHandler() {
            @Override
            public void onGranted() {
                Log.e("onDenied", "onGranted");
            }

            @Override
            public void onDenied(Context context, ArrayList<String> deniedPermissions) {
                super.onDenied(context, deniedPermissions);
                Log.e("onDenied", "onDenied" + deniedPermissions);


            }
        });
    }

    private void inits() {
        clusterLatLngList = new ArrayList<>();
        commonClassForAPI = CommonClassForAPI.getInstance(this);
        cMainList = new ArrayList<>();
        gpsTracker=new GPSTracker(this);
        geocoder = new Geocoder(this, Locale.getDefault());
        clusterList = new ArrayList<>();
        custMainList = new ArrayList<>();
        utils = new Utils(HomeActivity.this);
        mBinding.toolbarMyLead.back.setVisibility(View.INVISIBLE);
        mBinding.rvRecycle.setLayoutManager(new LinearLayoutManager(HomeActivity.this));
        customerAdapter = new CustomerAdapter(HomeActivity.this, custMainList);
        mBinding.rvRecycle.setAdapter(customerAdapter);


        gpsTracker = new GPSTracker(HomeActivity.this);
       


        
        // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()



        mBinding.spnCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

                int cityid = cMainList.get(pos).getCityid();

                Utils.showProgressDialog(HomeActivity.this);
                callClusterApi(cityid);
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        mBinding.spnCluster.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                int clusterId = clusterList.get(pos).getClusterId();
                clusterLatLngList = new ArrayList<>();
                if (clusterId == 00) {
                    mBinding.rvRecycle.setVisibility(View.GONE);
                } else {
                    Utils.showProgressDialog(HomeActivity.this);
                    mBinding.rvRecycle.setVisibility(View.VISIBLE);
                    clusterLatLngList = clusterList.get(pos).getClusterLatLngList();
                    calCustomerList(clusterId);
                }
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        mBinding.llMapView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, MapViewActivity.class);
                intent.putExtra("mylist", clusterLatLngList);
                startActivity(intent);
            }
        });
        if (utils.isNetworkAvailable()) {
            calcityApi();
        } else {
            Utils.hideProgressDialog(HomeActivity.this);
            Utils.setToast(HomeActivity.this, getString(R.string.internet_connection));
        }

        mBinding.etSearchCust.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (customerAdapter != null) {
                    filter(s.toString());
                }
                // TODO Auto-generated method stub
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {

                // filter your list from your input

                //you can use runnable postDelayed like 500 ms to delay search text
            }
        });
    }

    private void calCustomerList(int clusterId) {

        JsonArray clusterValue = new JsonArray();
        Integer[] intArray = {clusterId};
        for (Integer val : intArray) {
            clusterValue.add(val);
            System.out.print(val + " ");
        }
        commonClassForAPI.fetchCustomer(objcutomer, clusterValue);
    }

    private void callClusterApi(int cityId) {
        commonClassForAPI.fetchCluster(objCluster, cityId);
    }

    private void calcityApi() {
        commonClassForAPI.fetchCityList(objCity);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        objCluster.dispose();
        objCity.dispose();
        objcutomer.dispose();
    }

    void filter(String text) {

        ArrayList<CustomerModel> temp = new ArrayList();
        for (CustomerModel d : custMainList) {
            //or use .equal(text) with you want equal match
            //use .toLowerCase() for better matches

            if (d.getShippingAddress() != null && d.getShippingAddress().toLowerCase().contains(text.toLowerCase())) {
                temp.add(d);
            }
        }
        //update recyclerview
        customerAdapter.updateList(temp);
    }
}