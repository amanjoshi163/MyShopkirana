package com.myshopkirana.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;

import com.myshopkirana.R;
import com.myshopkirana.databinding.ActivityHomeBinding;

public class CordinateListActivity extends AppCompatActivity {
    ActivityCordinateBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_cordinate_list);

        initView();

    }

    private void initView() {

    }
}