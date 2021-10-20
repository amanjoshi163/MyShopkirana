package com.myshopkirana.utils;



import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.multidex.MultiDex;


import com.myshopkirana.activity.HomeActivity;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import io.reactivex.observers.DisposableObserver;

public class MyApplication extends Application implements LifecycleObserver {
    private static MyApplication mInstance;
    private static MediaRecorder mediaRecorder;
    public HomeActivity homeActivity;


    public Activity activity;



    public static synchronized MyApplication getInstance() {
        return mInstance;
    }

    @Override
    protected void attachBaseContext(Context base) {

        super.attachBaseContext(LocaleHelper.onAttach(base, "en"));
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;


    }




}