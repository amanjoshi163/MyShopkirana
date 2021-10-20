package com.myshopkirana.api;

import com.myshopkirana.model.CityModel;
import com.myshopkirana.model.ClusterModel;

import java.util.ArrayList;

import io.reactivex.Observable;
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


    //api/SalesApp/CustomerAddressUpdateRequest
    @POST("/api/SalesApp/CustomerAddressUpdateRequest")
    Observable<JsonObject> UpdateCustLocation(@Query("cityid") int cityid);

}