package com.myshopkirana.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.material.tabs.TabLayout;
import com.myshopkirana.R;
import com.myshopkirana.adapter.CityAdapter;
import com.myshopkirana.adapter.ClusterAdapter;
import com.myshopkirana.adapter.Pager;
import com.myshopkirana.databinding.ActivityHomeBinding;
import com.myshopkirana.model.CityModel;
import com.myshopkirana.model.ClusterModel;
import com.myshopkirana.utils.CommonClassForAPI;

import java.lang.reflect.Array;
import java.util.ArrayList;

import io.reactivex.observers.DisposableObserver;

public class HomeActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener {
    ArrayList<String> listData = null;
    Pager adapter;
    private ActivityHomeBinding mBinding;
    private ArrayList<CityModel> cMainList;
    private final DisposableObserver<ArrayList<CityModel>> objCity = new DisposableObserver<ArrayList<CityModel>>() {

        @Override
        public void onNext(ArrayList<CityModel> cList) {
            try {

                cMainList = cList;
                CityAdapter adapter = new CityAdapter(HomeActivity.this,
                        R.layout.listitems_layout, R.id.cityname, cList);
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
    private CommonClassForAPI commonClassForAPI;
    private GoogleMap mMap;
    private PolygonOptions polygonOptions;
    private Polygon polygon;
    private ArrayList<ClusterModel> clusterList;
    private final DisposableObserver<ArrayList<ClusterModel>> objCluster = new DisposableObserver<ArrayList<ClusterModel>>() {

        @Override
        public void onNext(ArrayList<ClusterModel> cList) {
            try {
                clusterList = new ArrayList<>();
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

        inits();

    }

    private void inits() {

        polygonOptions = new PolygonOptions();
        commonClassForAPI = CommonClassForAPI.getInstance(this);
        cMainList = new ArrayList<>();
        clusterList = new ArrayList<>();
        //Adding the tabs using addTab() method
        mBinding.tabLayout.addTab(mBinding.tabLayout.newTab().setText("List View"));
        mBinding.tabLayout.addTab(mBinding.tabLayout.newTab().setText("Map View"));

        mBinding.tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        //Initializing viewPager

        adapter = new Pager(getSupportFragmentManager(), mBinding.tabLayout.getTabCount(), HomeActivity.this);
        mBinding.pager.setOffscreenPageLimit(1);
        //Adding adapter to pager
        mBinding.pager.setAdapter(adapter);


        //Adding onTabSelectedListener to swipe views
        mBinding.tabLayout.setOnTabSelectedListener(this);


        mBinding.pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                mBinding.tabLayout.setScrollPosition(position, positionOffset, true);
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mBinding.spnCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                int cityid = cMainList.get(pos).getCityid();

                callClusterApi(cityid);
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        mBinding.spnCluster.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                int clusterId = clusterList.get(pos).getClusterId();
                if (clusterId == 00) {
                    mBinding.llTabView.setVisibility(View.GONE);
                } else {
                    mBinding.llTabView.setVisibility(View.VISIBLE);
                    calCustomerList(clusterId);

                }
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        calcityApi();
    }

    private void calCustomerList(int clusterId) {

        int clusterValue = 0;
        Integer[] intArray = {clusterId};
        for(Integer val: intArray){
            clusterValue = val;
            System.out.print(val + " ");
        }
        commonClassForAPI.fetchCustomer(objCluster, clusterValue);
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
    }


    @Override
    public void onTabSelected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
}