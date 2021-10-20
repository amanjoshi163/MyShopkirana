//package com.myshopkirana.utils;
//
//import static android.content.Context.MODE_PRIVATE;
//
//import android.app.Activity;
//import android.app.Dialog;
//import android.content.Context;
//import android.content.SharedPreferences;
//import android.graphics.PorterDuff;
//import android.net.ConnectivityManager;
//import android.net.NetworkInfo;
//import android.preference.PreferenceManager;
//import android.util.Log;
//import android.view.Gravity;
//import android.view.LayoutInflater;
//import android.view.MotionEvent;
//import android.view.View;
//import android.view.inputmethod.InputMethodManager;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.google.gson.Gson;
//import com.myshopkirana.R;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//
//import java.text.DateFormat;
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//import java.util.Locale;
//import java.util.TimeZone;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//
////import com.crashlytics.android.Crashlytics;
//
//public class Utils {
//    public static Dialog customDialog;
//    public static String myFormat = "yyyy-MM-dd'T'HH:mm:ss";
//    private static Context context;
//    public static boolean isSKseleted = false;
//
//
//    public static String pattern = "##.##";
//    public static int WAREHOUSEID=0;
//    public static int PEOPLEID=0;
//
//    public Utils(Context _mContext) {
//        context = _mContext;
//
//        WAREHOUSEID= SharePrefs.getInstance(_mContext).getInt(SharePrefs.WAREHOUSE_ID);
//        PEOPLEID= SharePrefs.getInstance(_mContext).getInt(SharePrefs.PEOPLE_ID);
//    }
//
//    public static void setToast(Context _mContext, String str) {
//        Toast toast = Toast.makeText(_mContext, str, Toast.LENGTH_SHORT);
//        toast.setGravity(Gravity.CENTER, 0, 0);
//
//        toast.show();
//
//
//    }
//
//    public boolean isValidGSTNo(String str) {
//        // Regex to check valid
//        // GST (Goods and Services Tax) number
//        String regex = "^[0-9]{2}[A-Z]{5}[0-9]{4}"
//                + "[A-Z]{1}[1-9A-Z]{1}"
//                + "Z[0-9A-Z]{1}$";
//
//        // Compile the ReGex
//        Pattern p = Pattern.compile(regex);
//
//        // If the string is empty
//        // return false
//        if (str == null) {
//            return false;
//        }
//
//        // Pattern class contains matcher()
//        // method to find the matching
//        // between the given string
//        // and the regular expression.
//        Matcher m = p.matcher(str);
//
//        // Return if the string
//        // matched the ReGex
//        return m.matches();
//    }
//
//
//    public static void setToastSize(Context _mContext, String str) {
//
//
//        Toast toast = Toast.makeText(_mContext, str, Toast.LENGTH_SHORT);
//        toast.setGravity(Gravity.CENTER, 0, 0);
//        LinearLayout toastLayout = (LinearLayout) toast.getView();
//        TextView toastTV = (TextView) toastLayout.getChildAt(0);
//        toastTV.setTextSize(12);
//        toast.show();
//    }
//
//    public static void showProgressDialog(Context activity) {
//        if (customDialog != null) {
//            if (customDialog.isShowing()) {
//                customDialog.dismiss();
//            }
//            customDialog = null;
//        }
//        customDialog = new Dialog(activity, R.style.CustomDialog);
//        customDialog.setCancelable(false);
//        LayoutInflater inflater = LayoutInflater.from(activity);
//        View mView = inflater.inflate(R.layout.progress_dialog, null);
//        customDialog.setContentView(mView);
//        if (!customDialog.isShowing()) {
//            customDialog.show();
//        }
//    }
//
//    public static void hideProgressDialog(Context activity) {
//        if (customDialog != null) {
//            if (customDialog.isShowing()) {
//                customDialog.dismiss();
//            }
//        }
//    }
//
//
//    public static void buttonEffect(View button) {
//        button.setOnTouchListener((v, event) -> {
//            switch (event.getAction()) {
//                case MotionEvent.ACTION_DOWN: {
//                    v.getBackground().setColorFilter(0xe0f47521, PorterDuff.Mode.SRC_ATOP);
//                    v.invalidate();
//                    break;
//                }
//                case MotionEvent.ACTION_UP: {
//                    v.getBackground().clearColorFilter();
//                    v.invalidate();
//                    break;
//                }
//            }
//            return false;
//        });
//    }
//
//    public static String getDateFormate(String ServerDate) {
//        // 2018-12-24T15:48:15.707+05:30
//        if (!ServerDate.equalsIgnoreCase("") && !ServerDate.equalsIgnoreCase(null)) {
//            DateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());//These format come to server
//            originalFormat.setTimeZone(TimeZone.getDefault());
//            DateFormat targetFormat = new SimpleDateFormat("E, dd MMM yyyy", Locale.getDefault());  //change to new format here  //dd-MM-yyyy HH:mm:ss
//
//            Date date = null;
//            String formattedDate = "";
//            try {
//                date = originalFormat.parse(ServerDate);
//
//                formattedDate = targetFormat.format(date);  //result
//
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
//            return formattedDate;
//
//
//        } else {
//            return "null";
//        }
//
//    }
//
//
//
//    public static String getDateFormat(String ServerDate) {
//        // 2018-12-24T15:48:15.707+05:30
//        if (!ServerDate.equalsIgnoreCase("") && !ServerDate.equalsIgnoreCase(null)) {
//            DateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());//These format come to server
//            originalFormat.setTimeZone(TimeZone.getDefault());
//            DateFormat targetFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());  //change to new format here  //dd-MM-yyyy HH:mm:ss
//            Date date;
//            String formattedDate = "";
//            try {
//                date = originalFormat.parse(ServerDate);
//                formattedDate = targetFormat.format(date);  //result
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
//            return formattedDate;
//        } else {
//            return "null";
//        }
//    }
//
//    public static String getSimpleDateFormat(String ServerDate) {
//        // 2018-12-24T15:48:15.707+05:30
//        if (!ServerDate.equalsIgnoreCase("") && !ServerDate.equalsIgnoreCase(null)) {
//            DateFormat originalFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());//These format come to server
//            originalFormat.setTimeZone(TimeZone.getDefault());
//            DateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());  //change to new format here  //dd-MM-yyyy HH:mm:ss
//            Date date = null;
//            String formattedDate = "";
//            try {
//                date = originalFormat.parse(ServerDate);
//                formattedDate = targetFormat.format(date);  //result
//
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
//            return formattedDate;
//        } else {
//            return "null";
//        }
//    }
//
//    public static String getTimeFromServerDate(String ServerDate) {
//
//        if (!ServerDate.equalsIgnoreCase("") && !ServerDate.equalsIgnoreCase(null)) {
//            DateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());//These format come to server
//            originalFormat.setTimeZone(TimeZone.getTimeZone("IST"));
//            DateFormat targetFormat = new SimpleDateFormat("dd-MM-yyyy hh:mma", Locale.getDefault());  //change to new format here  //dd-MM-yyyy HH:mm:ss
//            Date date;
//            String formattedDate = "";
//            try {
//                date = originalFormat.parse(ServerDate);
//                formattedDate = targetFormat.format(date);  //result
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
//            return formattedDate;
//        } else {
//            return "null";
//        }
//    }
//
//    public static String getHeader(Activity activity) {
//        String header = "";
//        String custMobileNumber = SharePrefs.getInstance(activity).getString(SharePrefs.MOBILE_NUMBER).trim();
//        String custPeopleName = SharePrefs.getInstance(activity).getString(SharePrefs.PEOPLE_FIRST_NAME).trim();
//        String custPeopleLastName = SharePrefs.getInstance(activity).getString(SharePrefs.PEOPLE_LAST_NAME).trim();
//        if (!TextUtils.isNullOrEmpty(custPeopleLastName)) {
//            header = custMobileNumber + "-" + custPeopleName.trim() + " " + custPeopleLastName.trim();
//        } else {
//            header = custMobileNumber + "-" + custPeopleName;
//        }
//
//        return header;
//    }
//
//    public static String getToken(Activity activity) {
//        return SharePrefs.getInstance(activity).getString(SharePrefs.TOKEN);
//    }
//
//
//    public static void hideKeyboard(Activity context, View view) {
//        // Then just use the following:
//        if (view != null) {
//            InputMethodManager imm = (InputMethodManager) context
//                    .getSystemService(Context.INPUT_METHOD_SERVICE);
//            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
//        }
//    }
//
//    public static void hideKeyboard(Activity activity) {
//        try {
//            InputMethodManager inputManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
//            inputManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
//        } catch (Exception e) {
//            // Ignore exceptions if any
//            Log.e("KeyBoardUtil", e.toString(), e);
//        }
//    }
//
//    public boolean isValidNumber(String mobno) {
//        String Nopattern = "^[5-9][0-9]{9}$";
//        Pattern pattern = Pattern.compile(Nopattern);
//        Matcher matcher = pattern.matcher(mobno);
//        return matcher.matches();
//    }
//
//    public boolean isNetworkAvailable() {
//        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
//        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
//    }
//
//    //save Cart item data in Complex preference
//    public CartItemBean getCartItem() {
//        if (mCartItem == null) {
//            ComplexPreferences mCartItemArraylistPref = ComplexPreferences.getComplexPreferences(context, Constant.CART_ITEM_ARRAYLIST_PREF, MODE_PRIVATE);
//            mCartItem = mCartItemArraylistPref.getObject(Constant.CART_ITEM_ARRAYLIST_PREF_OBJ, CartItemBean.class);
//            if (mCartItem == null) {
//                mCartItem = new CartItemBean(new ArrayList<CartItemInfo>(), 0, 0, 0, 0, 0, 0, "", "");
//            }
//        }
//        return mCartItem;
//    }
//
//    public void clearLocalData(Activity activity) {
//        try {
//            SharePrefs.getInstance(activity).putBoolean(SharePrefs.IS_API_CALL_HOME, true);
//            SharePrefs.getInstance(activity).putBoolean(SharePrefs.IS_API_CALL_ALL_CATEGORY, true);
//            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
//            SharedPreferences.Editor prefEditor = prefs.edit();
//            prefEditor.remove(SharePrefs.CATEGORY_BY_ID + "1");
//            prefEditor.remove(SharePrefs.CATEGORY_BY_ID + "2");
//            prefEditor.remove(SharePrefs.CATEGORY_BY_ID + "3");
//            prefEditor.remove(SharePrefs.CATEGORY_BY_ID + "5");
//            prefEditor.remove(SharePrefs.CATEGORY_BY_ALL);
//            prefEditor.remove(SharePrefs.HOME_SCREEN_DATA);
//            prefEditor.remove(SharePrefs.DILIVERY_CHARGE);
//            prefEditor.apply();
//            prefEditor.clear();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void clearCartData(Activity activity) {
//        ComplexPreferences mCartItemArraylistPref = ComplexPreferences.getComplexPreferences(activity, Constant.CART_ITEM_ARRAYLIST_PREF, MODE_PRIVATE);
//        mCartItemArraylistPref.clear();
//        mCartItemArraylistPref.removeObject(Constant.CART_ITEM_ARRAYLIST_PREF_OBJ);
//        mCartItemArraylistPref.commit();
//    }
//
//    public CartItemBean getCartItem(Activity activity) {
//        if (mCartItem == null) {
//            ComplexPreferences mCartItemArraylistPref = ComplexPreferences.getComplexPreferences(activity, Constant.CART_ITEM_ARRAYLIST_PREF, MODE_PRIVATE);
//            mCartItem = mCartItemArraylistPref.getObject(Constant.CART_ITEM_ARRAYLIST_PREF_OBJ, CartItemBean.class);
//            if (mCartItem == null) {
//                mCartItem = new CartItemBean(new ArrayList<>(), 0, 0, 0, 0, 0, 0, "", "");
//            }
//        }
//        return mCartItem;
//    }
//
//
//}