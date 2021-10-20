package com.myshopkirana.api;

import com.myshopkirana.model.CityModel;

import java.util.ArrayList;

import io.reactivex.Observable;
import retrofit2.http.GET;

/**
 * Created by User on 03-11-2018.
 */
public interface APIServices {


    @GET("Test/GetActiveWarehouseCity")
    Observable<ArrayList<CityModel>> getCity();

//    @Multipart
//    @POST("/api/imageupload")
//    Observable<JsonObject> uploadImage(@Part MultipartBody.Part body);


    //api/SalesApp/CustomerAddressUpdateRequest
//    @POST("/api/SalesApp/CustomerAddressUpdateRequest")
//    Observable<JsonObject> UpdateCustLocation(@Body updateCustLocationPostModel ages);

}