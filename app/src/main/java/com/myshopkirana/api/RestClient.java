package com.myshopkirana.api;

import android.app.Activity;
import android.widget.Toast;

import com.google.gson.Gson;
import com.myshopkirana.BuildConfig;

import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by User on 03-11-2018.
 */
public class RestClient {
    private static Activity mActivity;
    private static Retrofit retrofit;
    private static RestClient ourInstance;

    private RestClient() {
        try {


            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient client = new OkHttpClient.Builder()
                    .readTimeout(10, TimeUnit.MINUTES)
                    .connectTimeout(10, TimeUnit.MINUTES)
                    .writeTimeout(10, TimeUnit.MINUTES)
                    .addInterceptor(chain -> {
                        Request request = chain.request();
                        Response response = chain.proceed(request);
                        if (response.code() == 200) {
                            if (!request.url().toString().contains("UploadCustomerShopImage") ) {
                            try {
                                if (response.body() != null) {
                                    JSONObject jsonObject = new JSONObject();
                                    jsonObject.put("message", new JSONObject(response.body().string()));

                                    //Log.e("API_RESSSSS11",response.body().string());
                                    String data = "";
                                    MediaType contentType = null;

                                    data = jsonObject.getJSONObject("message").getString("Data");
//                                        destr = Aes256.decrypt(data, new SimpleDateFormat("yyyyMMdd", Locale.ENGLISH).format(new Date()) + "1201");
                                    printMsg(data);


                                    contentType = response.body().contentType();

                                    ResponseBody responseBody = ResponseBody.create(contentType, data);
                                    return response.newBuilder().body(responseBody).build();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            }
//                        }
                        }
                        return response;
                    })
                    .addInterceptor(chain -> {
                        Request request = chain.request().newBuilder()
                                .addHeader("authorization", "Bearer ")
                                .addHeader("noencryption", "1")
                                .build();
                        return chain.proceed(request);
                    })
                    .addInterceptor(interceptor)
                    .build();

            retrofit = new Retrofit.Builder()
.baseUrl(BuildConfig.apiEndpoint)
//                    .baseUrl("https://uat.shopkirana.in/api/")
                    .addConverterFactory(GsonConverterFactory.create(new Gson()))
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(client)
                    .client(client)
                    .build();
        } catch (Exception e) {
            Toast.makeText(mActivity, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public static RestClient getInstance(String url) {
//        if (BuildConfig.DEBUG) {
////            SharePrefs.getInstance(MyApplication.getInstance()).putStringBaseURL(SharePrefs.BASE_URL_TEST, url);
//            SharePrefs.getInstance(MyApplication.getInstance()).putStringBaseURL(SharePrefs.BASE_URL_TEST, BuildConfig.apiEndpoint);
//        } else {
//            SharePrefs.getInstance(MyApplication.getInstance()).putStringBaseURL(SharePrefs.BASE_URL_TEST, url);
////            SharePrefs.getInstance(MyApplication.getInstance()).putStringBaseURL(SharePrefs.BASE_URL_TEST, "https://uat.shopkirana.in");
//        }
        ourInstance = new RestClient();
        return ourInstance;

    }

    public static RestClient getInstance() {
        ourInstance = new RestClient();
        return ourInstance;

    }

    public static RestClient getInstance(Activity activity) {
        mActivity = activity;
        ourInstance = new RestClient();
        return ourInstance;
    }

    public APIServices getService() {
        return retrofit.create(APIServices.class);
    }

    private void printMsg(String msg) {
        if (msg.length() > 4050) {
            int chunkCount = msg.length() / 4050;     // integer division
            for (int i = 0; i <= chunkCount; i++) {
                int max = 4050 * (i + 1);
                if (max >= msg.length()) {
                    System.out.println(msg.substring(4050 * i));
                } else {
                    System.out.println(msg.substring(4050 * i, max));
                }
            }
        } else {
            System.out.println(msg);
        }
    }
}