package com.myshopkirana.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.myshopkirana.R;
import com.myshopkirana.adapter.CityAdapter;
import com.myshopkirana.adapter.ClusterAdapter;
import com.myshopkirana.adapter.CustomerAdapter;
import com.myshopkirana.databinding.ActivityHomeBinding;
import com.myshopkirana.interfaces.ClusterSelectionInterface;
import com.myshopkirana.model.CityModel;
import com.myshopkirana.model.ClusterLatLngModel;
import com.myshopkirana.model.ClusterModel;
import com.myshopkirana.model.CustomerModel;
import com.myshopkirana.utils.CommonClassForAPI;
import com.myshopkirana.utils.GPSTracker;
import com.myshopkirana.utils.GpsUtils;
import com.myshopkirana.utils.SharePrefs;
import com.myshopkirana.utils.Utils;
import com.nabinbhandari.android.permissions.PermissionHandler;
import com.nabinbhandari.android.permissions.Permissions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.reactivex.observers.DisposableObserver;

public class HomeActivity extends AppCompatActivity implements ClusterSelectionInterface {
    CustomerAdapter customerAdapter;
    ArrayList<CustomerModel> custMainList;
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
    List<Address> addresses;
    GPSTracker gpsTracker;
    Geocoder geocoder;
    Dialog dialog;
    String clusterName = "";
    String cityName = "";
    private String where = "";
    private Utils utils;
    private ActivityHomeBinding mBinding;
    private ArrayList<CityModel> cMainList;
    private final DisposableObserver<ArrayList<CityModel>> objCity = new DisposableObserver<ArrayList<CityModel>>() {

        @Override
        public void onNext(ArrayList<CityModel> cList) {
            try {


                cMainList.addAll(cList);
                CityAdapter adapter = new CityAdapter(HomeActivity.this,
                        R.layout.listitems_layout, R.id.cityname, cMainList);
                mBinding.spnCity.setAdapter(adapter);


                try {
                    addresses = geocoder.getFromLocation(gpsTracker.getLatitude(), gpsTracker.getLongitude(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                    if (addresses != null) {
                        cityName = "" + addresses.get(0).getLocality();
                    }
                    System.out.println(cityName);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                int pos = 0;
                boolean b = false;
                int CityId = SharePrefs.getInstance(HomeActivity.this).getInt(SharePrefs.CITY_ID);
                if (CityId == 0) {
                    for (int i = 0; i < cMainList.size(); i++) {
                        if (!cityName.isEmpty()) {
                            if (cityName.equalsIgnoreCase(cMainList.get(i).getCityName())) {
                                SharePrefs.getInstance(HomeActivity.this).putInt(SharePrefs.CITY_ID, cMainList.get(i).getCityid());

                                b = true;
                                pos = i;
                                break;
                            }
                        }
                    }
                    if (b) {
                        mBinding.spnCity.setSelection(pos);
                    }
                } else {
                    for (int i = 0; i < cMainList.size(); i++) {
//                        if (!cityName.isEmpty()) {
                        if (CityId == cMainList.get(i).getCityid()) {
                            SharePrefs.getInstance(HomeActivity.this).putInt(SharePrefs.CITY_ID, cMainList.get(i).getCityid());

                            b = true;
                            pos = i;
                            break;
                        }
//                        }
                    }
                    if (b) {
                        mBinding.spnCity.setSelection(pos);
                    }
                }
                mBinding.llmainview.setVisibility(View.VISIBLE);
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
    private ArrayList<ArrayList<ClusterLatLngModel>> clusterLatLngList;
    private ArrayList<ClusterModel> clusterList;
    private final DisposableObserver<ArrayList<ClusterModel>> objCluster = new DisposableObserver<ArrayList<ClusterModel>>() {

        @Override
        public void onNext(ArrayList<ClusterModel> cList) {
            try {
                clusterName="";
                clusterList = new ArrayList<>();
                clusterList.addAll(cList);
                JsonArray clusterIds=new JsonArray();
                Utils.hideProgressDialog(HomeActivity.this);
                if (where.equals("splash")) {
//                    callCheckPop(clusterList);

                            if (clusterList.size()>0) {
                                clusterList.get(0).setSelected(true);
                                if (clusterName.equals("")) {
                                    clusterName = clusterList.get(0).getClusterName();
                                } else {
                                    clusterName = clusterName + "," + clusterList.get(0).getClusterName();
                                }
                                mBinding.spnCluster.setText(clusterName);

                                clusterIds.add(clusterList.get(0).getClusterId());
                                commonClassForAPI.fetchCustomer(objcutomer, clusterIds);
                                SharePrefs.getInstance(HomeActivity.this).putString(SharePrefs.CLUSTER_ID, new Gson().toJson(clusterIds));


                    }
                }else {
                    clusterName="";
                    String jsonString = SharePrefs.getInstance(HomeActivity.this).getString(SharePrefs.CLUSTER_ID);
                    if (!jsonString.isEmpty()) {
                           clusterIds = new Gson().fromJson(jsonString, new TypeToken<JsonArray>() {
                        }.getType());
                        commonClassForAPI.fetchCustomer(objcutomer, clusterIds);
                        SharePrefs.getInstance(HomeActivity.this).putString(SharePrefs.CLUSTER_ID, new Gson().toJson(clusterIds));

                        for (int i = 0; i < clusterList.size(); i++) {
                            for (int j = 0; j < clusterIds.size(); j++) {
                                if (clusterList.get(i).getClusterId() == Integer.parseInt(clusterIds.get(j)+"")) {
                                    clusterList.get(i).setSelected(true);
                                    if (clusterName.equals("")) {
                                        clusterName = clusterList.get(i).getClusterName();
                                    } else {
                                        clusterName = clusterName + "," + clusterList.get(i).getClusterName();
                                    }
                                    mBinding.spnCluster.setText(clusterName);
                                }
                            }


                        }
                    }




                }


//
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

    private void callCheckPop(ArrayList<ClusterModel> clusterList) {
        dialog = new Dialog(this);
        View view = getLayoutInflater().inflate(R.layout.dialog_cluster_main, null);
        RecyclerView lv = view.findViewById(R.id.custom_list);
        Button btnCancel = view.findViewById(R.id.btn_cancel);
        Button btnDone = view.findViewById(R.id.btn_done);

        lv.setLayoutManager(new LinearLayoutManager(HomeActivity.this));
        ClusterAdapter adapter = new ClusterAdapter(HomeActivity.this,
                clusterList, btnDone, this);


        lv.setAdapter(adapter);
        dialog.setContentView(view);
        dialog.show();
        Window window = dialog.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_home);
        new GpsUtils(this).turnGPSOn(isGPSEnable -> {

        });
        where = getIntent().getStringExtra("where");
        callRunTimePermissions();

    }


    public void callRunTimePermissions() {
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA,};
        Permissions.check(HomeActivity.this/*context*/, permissions, null/*rationale*/, null/*options*/, new PermissionHandler() {
            @Override
            public void onGranted() {
                Log.e("onDenied", "onGranted");
                Utils.showProgressDialog(HomeActivity.this);
                inits();
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
        gpsTracker = new GPSTracker(this);
        geocoder = new Geocoder(this, Locale.getDefault());
        clusterList = new ArrayList<>();
        custMainList = new ArrayList<>();
        utils = new Utils(HomeActivity.this);
        mBinding.toolbarMyLead.back.setVisibility(View.INVISIBLE);
        mBinding.rvRecycle.setLayoutManager(new LinearLayoutManager(HomeActivity.this));
        customerAdapter = new CustomerAdapter(HomeActivity.this, custMainList);
        mBinding.rvRecycle.setAdapter(customerAdapter);
        gpsTracker = new GPSTracker(HomeActivity.this);


        mBinding.spnCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

                int cityid = cMainList.get(pos).getCityid();
                SharePrefs.getInstance(HomeActivity.this).putInt(SharePrefs.CITY_ID, cityid);

                Utils.showProgressDialog(HomeActivity.this);
                callClusterApi(cityid);
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        mBinding.llreset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharePrefs.getInstance(HomeActivity.this).putString(SharePrefs.CLUSTER_ID, "");
                SharePrefs.getInstance(HomeActivity.this).putInt(SharePrefs.CITY_ID, 0);

                callRunTimePermissions();

            }
        });

        mBinding.spnCluster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String jsonString = SharePrefs.getInstance(HomeActivity.this).getString(SharePrefs.CLUSTER_ID);
                if (!jsonString.isEmpty()) {
                    ArrayList<Integer> clusterIds = new Gson().fromJson(jsonString, new TypeToken<ArrayList<Integer>>() {
                    }.getType());

                    for (int i = 0; i < clusterList.size(); i++) {
                        for (int j = 0; j < clusterIds.size(); j++) {
                            if (clusterList.get(i).getClusterId() == clusterIds.get(j)) {
                                clusterList.get(i).setSelected(true);



                            }
                        }


                    }
                }


                callCheckPop(clusterList);
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
            } else if (d.getName() != null && d.getName().toLowerCase().contains(text.toLowerCase())) {

                temp.add(d);
            } else if (d.getSkcode() != null && d.getSkcode().toLowerCase().contains(text.toLowerCase())) {
                temp.add(d);
            } else if (d.getShopName() != null && d.getShopName().toLowerCase().contains(text.toLowerCase())) {
                temp.add(d);
            }

        }
        //update recyclerview
        customerAdapter.updateList(temp);
    }

    @Override
    public void SelectedCluster(ArrayList<ClusterModel> list) {
       callCustomerMethod(list);
    }

    private void callCustomerMethod(ArrayList<ClusterModel> list) {
        Utils.showProgressDialog(HomeActivity.this);
        clusterName="";
        JsonArray clusterValue = new JsonArray();
        for (int i = 0; i < list.size(); i++) {
            clusterValue.add(list.get(i).getClusterId());

            if (clusterName.equals("")) {
                clusterName = list.get(i).getClusterName();
            } else {
                clusterName = clusterName + "," + list.get(i).getClusterName();
            }
            clusterLatLngList.add(list.get(i).getClusterLatLngList());
        }

        mBinding.spnCluster.setText(clusterName);
        if (dialog!=null && dialog.isShowing()) {
            dialog.dismiss();
        }
        SharePrefs.getInstance(HomeActivity.this).putString(SharePrefs.CLUSTER_ID, new Gson().toJson(clusterValue));
        commonClassForAPI.fetchCustomer(objcutomer, clusterValue);
    }
}