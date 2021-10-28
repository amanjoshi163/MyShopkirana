package com.myshopkirana.utils;

import android.app.Activity;
import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.myshopkirana.api.RestClient;
import com.myshopkirana.model.CityModel;
import com.myshopkirana.model.ClusterModel;
import com.myshopkirana.model.CustomerModel;
import com.myshopkirana.model.CustomerPostModel;
import com.myshopkirana.model.ImageResponse;

import java.util.ArrayList;
import java.util.Collections;

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

    public void fetchCluster(final DisposableObserver daysListDes,int cityId) {
        RestClient.getInstance(mActivity).getService().getCluster(cityId)
                .subscribeOn(Schedulers.io())
//                .doOnSubscribe(disposable -> Utils.showProgressDialog(mActivity))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ArrayList<ClusterModel>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(ArrayList<ClusterModel> o) {


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

    public void fetchCustomer(final DisposableObserver daysListDes, JsonArray clusterValue) {
        RestClient.getInstance(mActivity).getService().getCustList(clusterValue)
                .subscribeOn(Schedulers.io())
//                .doOnSubscribe(disposable -> Utils.showProgressDialog(mActivity))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ArrayList<CustomerModel>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(ArrayList<CustomerModel> o) {


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

    // upload profile image
    public void uploadImage(DisposableObserver<String> observer, MultipartBody.Part body) {
        RestClient.getInstance().getService().imageUpload(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull String s) {
                        observer.onNext(s);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        observer.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        observer.onComplete();
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

    //buy product
    public void updateCustomer(DisposableObserver fetchProductDes, CustomerPostModel model) {
        RestClient.getInstance().getService().getResponse(model)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> Utils.showProgressDialog(mActivity))
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Boolean object) {
                        fetchProductDes.onNext(object);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("TAG", "Error:" + e.toString());
                        fetchProductDes.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        fetchProductDes.onComplete();
                    }
                });
    }

}