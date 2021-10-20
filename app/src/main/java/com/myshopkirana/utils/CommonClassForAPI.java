package com.myshopkirana.utils;

import android.app.Activity;
import android.util.Log;

import com.google.gson.JsonObject;
import com.myshopkirana.api.RestClient;
import com.myshopkirana.model.CityModel;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MultipartBody;

public class CommonClassForAPI {
    private static Activity mActivity;
    private static CommonClassForAPI CommonClassForAPI;

    public static CommonClassForAPI getInstance(Activity activity) {
        if (CommonClassForAPI == null) {
            CommonClassForAPI = new CommonClassForAPI();
        }
        mActivity = activity;
        return CommonClassForAPI;
    }




    public void fetchCityList(final DisposableObserver daysListDes) {
        RestClient.getInstance(mActivity).getService().getCity()
                .subscribeOn(Schedulers.io())
//                .doOnSubscribe(disposable -> Utils.showProgressDialog(mActivity))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ArrayList<CityModel>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(ArrayList<CityModel> o) {
                        daysListDes.onNext(o);
                    }


                    @Override
                    public void onError(Throwable e) {
                        // customDialog.dismiss();
                        daysListDes.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        //customDialog.dismiss();
                        daysListDes.onComplete();
                    }
                });
    }

//
//
//    // chart API
//    public void setMpAndroidGraph(final DisposableObserver graphDes, String day, String skcode) {
//        RestClient.getInstance(mActivity).getService().getMpGraphData(day, skcode)
//                .subscribeOn(Schedulers.io())
//                //.doOnSubscribe(disposable -> Utils.showProgressDialog(mActivity))
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Observer<List<MpGraphModel>>() {
//                    @Override
//                    public void onSubscribe(Disposable d) {
//                    }
//
//                    @Override
//                    public void onNext(List<MpGraphModel> o) {
//                        graphDes.onNext(o);
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        // customDialog.dismiss();
//                        graphDes.onError(e);
//                    }
//
//                    @Override
//                    public void onComplete() {
//                        //customDialog.dismiss();
//                        graphDes.onComplete();
//                    }
//                });
//    }
//


}