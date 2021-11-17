package com.myshopkirana;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.myshopkirana.activity.HomeActivity;
import com.myshopkirana.utils.GpsUtils;
import com.myshopkirana.utils.SharePrefs;
import com.nabinbhandari.android.permissions.PermissionHandler;
import com.nabinbhandari.android.permissions.Permissions;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new GpsUtils(this).turnGPSOn(isGPSEnable -> {

        });
        SharePrefs.getInstance(MainActivity.this).putInt(SharePrefs.CLUSTER_ID,0);
        SharePrefs.getInstance(MainActivity.this).putInt(SharePrefs.CITY_ID,0);

        callRunTimePermissions();

    }

    public void callRunTimePermissions() {
        String[] permissions = {android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
        Permissions.check(MainActivity.this/*context*/, permissions, null/*rationale*/, null/*options*/, new PermissionHandler() {
            @Override
            public void onGranted() {
                new Handler().postDelayed(new Runnable(){
                    @Override
                    public void run() {
                        /* Create an Intent that will start the Menu-Activity. */
                        Intent mainIntent = new Intent(MainActivity.this, HomeActivity.class);
                        mainIntent.putExtra("where","splash");
                        startActivity(mainIntent);
                        finish();
                    }
                }, 3000);

            }

            @Override
            public void onDenied(Context context, ArrayList<String> deniedPermissions) {
                super.onDenied(context, deniedPermissions);
                Log.e("onDenied", "onDenied" + deniedPermissions);


            }
        });
    }
}