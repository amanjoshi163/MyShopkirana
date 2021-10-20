package com.myshopkirana.utils;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by User on 13-11-2018.
 */
public class SharePrefs {

    public static final String BILL_DISCOUNT_OFFER = "BillDiscountOfferData";
    public static final String IS_LOCATION_START = "is_location_start";
    public static final String IS_LOCATION_RESUME = "is_location_resume";
    public static final String IS_LOCATION_TIMER = "is_location_timer";
    public static final String BASE_URL_TEST = "base_url_test";
    //Offline data store
    public static final String IS_LOGOUT = "false";
    public static final String IS_LOCATION = "is_location";
    public static String LANGUAGE = "LANGUAGE";
    public static String DEVICE_ID = "device_id";
    public static String SHARED_PREF_NAME = "fcmsharedprefname";
    public static String KEY_ACCESS_TOKEN = "token";
    public static String MOBILE_NUMBER = "mobile";
    public static String CUSTOMER_MOBILE_NUMBER = "cust_mobile";
    public static String MINIMUM_AMOUNT_LIMIT = "AvailDial";
    public static String IS_LOGIN = "is_login";
    public static String CUST_ACTIVE = "cust_active";
    public static String IN_PROGRESS = "in_progress";
    public static String ISKPP = "is_kpp";
    public static String WHEEL_AMOUNT = "wheel_amount";
    public static String PEOPLE_LAT = "people_lat";
    public static String PEOPLE_LNG = "people_lng";
    public static String TRIP_START_TIME = "trip_start_time";
    public static String TOTAL_TRAVEL_TIME = "total_travel_time";
    public static String PEOPLE_ID = "people_id";
    public static String CURRENT_DATE = "current_date";
    public static String CURRENT_DATE_LOCATION = "current_date_location";
    public static String RECORDING_FILE = "recording_file";
    public static String SHOP_IMAGE = "shop_image";
    public static String ROLE = "role";
    public static String PEOPLE_FIRST_NAME = "people_first_name";
    public static String PEOPLE_LAST_NAME = "people_last_name";
    public static String PICHING_ID = "piching_id";
    public static String RECODING_STARTG = "start_recoding";
    public static String SHOP_NAME = "shop_name";
    public static String COMPANY_ID = "company_id";
    public static String CUSTOMER_ID = "Customer_Id";
    public static String DASHBOARD_CUSTOMER_ID = "dashboard_Customer_Id";
    public static String IS_DASHBOARD = "is_dashboard";
    public static String DEAULT_CUSTOMER_ID = "Default_Customer_Id";
    public static String WAREHOUSE_ID = "warehouse_id";
    public static String PEOPLE_WAREHOUSE_ID = "people_warehouse_id";
    public static String CITY_ID = "city_id";
    public static String DATE_OF_BIRTH = "DOB";
    public static String CITY_NAME = "CITY_NAME";
    public static String ITEM_FAV_JSON = "ItemFavJson";
    public static String EMAIL = "email_id";
    public static String WALLET_POINT = "WALLET_POINT";
    public static String PASSWORD = "PASSWORD";
    public static String USER_PROFILE_IMAGE = "user_profile_image";
    public static String TOTAL_ABC_ITEM = "TotalItem";
    public static String AVAIL_DIAL_WHEEL = "AvailDialwheel";
    public static String CURRENT_LANGUAGE = "CurrentLanguage";
    public static String BEAT_SK_CODE = "beat_sk_code";
    public static String BEAT_SHOP_NAME = "ShopName";
    public static String SK_JSON = "SkJson";
    public static String BID_JSON = "BidJson";
    public static String BEAT_SK_CODE_CUSTOMER_NAME = "BeatSkCodeCName";
    public static String SK_WALLET_AMOUNT = "SkWalletAmount";
    public static String PX = "px";
    public static String RX = "rx";
    public static String ENTER_REWARD_POINT = "EnterRewardPoint";
    public static String AMOUNT_TO_REDUCE = "AmountToReduct";
    public static String DILIVERY_CHARGE = "delivery_charge";
    public static String IS_RECODING = "IsRecording";
    public static String CATEGORY_BY_ID = "category_by_id";
    public static String IS_API_CALL_HOME = "is_api_call_home";
    public static String IS_API_CALL_ALL_CATEGORY = "is_api_call_all_category";
    public static String CATEGORY_BY_ALL = "category_by_all";
    public static String HOME_SCREEN_DATA = "home_screen_data";
    public static String APPLIED_OFFER_DATA = "applied_offer_data";
    public static String PREFERENCE = "SkRetailer";
    public static String ITEM_FLASH_DEAL_USED_JSON = "ItemFlashDealUsedJson";
    public static String APP_HOME_ITEM_JSON = "AppHomeItemJson";
    public static String TOKEN = "token";
    public static String TOKEN_NAME = "TokenName";
    public static String TOKEN_PASSWORD = "Token_password";
    public static String DREAM_POINT_LIMIT = "dream_point_limit";
    public static String IS_RECODING_START = "IsRecording_start";
    public static Context context1;
    private static SharePrefs instance;
    private final Context ctx;
    private final SharedPreferences sharedPreferences;
    private final SharedPreferences SharePreFBaseUrl;

    public SharePrefs(Context context) {
        ctx = context;
        context1 = context;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharePreFBaseUrl = PreferenceManager.getDefaultSharedPreferences(context);

    }

    public static SharePrefs getInstance(Context ctx) {
        if (instance == null) {
            instance = new SharePrefs(ctx);
        }
        return instance;
    }

    public static String getStringSharedPreferences(Context context, String name) {
        SharedPreferences settings = context.getSharedPreferences(PREFERENCE, 0);
        return settings.getString(name, "");
    }

    public static void clearSharedPreferenceValue(Context context, String ClearValue) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor prefEditor = prefs.edit();
        prefEditor.remove(ClearValue);
        prefEditor.apply();
        prefEditor.clear();
    }

    // for username string preferences
    public static void setStringSharedPreference(String name, String value) {
        SharedPreferences settings = context1.getSharedPreferences(PREFERENCE, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(name, value);
        editor.apply();
    }

    public void putString(String key, String val) {
        sharedPreferences.edit().putString(key, val).apply();
    }

    public void putStringBaseURL(String key, String val) {
        //SharePreFBaseUrl.edit().putString(key, val).apply();

        SharedPreferences.Editor editor = context1.getSharedPreferences("MY_PREFS_NAME", MODE_PRIVATE).edit();
        editor.putString(key, val);

        editor.apply();

    }

    public String getStringBaseURL(String key) {
//        return SharePreFBaseUrl.getString(key, "https://internal.er15.xyz");

        SharedPreferences prefs = context1.getSharedPreferences("MY_PREFS_NAME", MODE_PRIVATE);
        String baseUrl = prefs.getString(key, "https://internal.er15.xyz");//"No name defined" is the default value.
        return baseUrl;
    }

    public String getString(String key) {
        return sharedPreferences.getString(key, "");
    }

    public void putInt(String key, Integer val) {
        sharedPreferences.edit().putInt(key, val).apply();
    }

    public void putBoolean(String key, Boolean val) {
        sharedPreferences.edit().putBoolean(key, val).apply();
    }

    public Boolean getBoolean(String key) {
        return sharedPreferences.getBoolean(key, false);
    }

    public int getInt(String key) {
        return sharedPreferences.getInt(key, 0);
    }

    public void clearSharePrefs() {
        sharedPreferences.edit().clear().commit();
    }

    public boolean storeToken(String Token) {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_ACCESS_TOKEN, Token);
        editor.apply();
        return true;
    }

    public String getToken() {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        return sharedPreferences.getString(KEY_ACCESS_TOKEN, null);
    }

}
