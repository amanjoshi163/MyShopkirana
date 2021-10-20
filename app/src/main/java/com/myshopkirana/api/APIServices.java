package com.myshopkirana.api;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.myshopkirana.model.CityModel;
import com.myshopkirana.model.ClusterModel;
import com.myshopkirana.model.CustomerModel;

import java.util.ArrayList;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by User on 03-11-2018.
 */
public interface APIServices {


    @GET("Test/GetActiveWarehouseCity")
    Observable<ArrayList<CityModel>> getCity();

    //https://uat.shopkirana.in/api/Test/GetClusterCityWise?cityid=37
    @GET("Test/GetClusterCityWise")
    Observable<ArrayList<ClusterModel>> getCluster(@Query("cityid") int cityid);

//    @Multipart
//    @POST("/api/imageupload")
//    Observable<JsonObject> uploadImage(@Part MultipartBody.Part body);


//    /https://uat.shopkirana.in/api/Test/GetClusterCustomers
    @POST("/api/Test/GetClusterCustomers")
    Observable<ArrayList<CustomerModel>> getCustList(@Body  JsonArray clusterValue);

}