package com.myshopkirana.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

/**
 *
 */
public class MarshmallowPermissions {
    final public static int PERMISSION_REQUEST_CODE_LOCATION = 01;
    final public static int PERMISSION_REQUEST_CODE_WRITE_EXTERNAL_STORAGE = 03;
    final public static int PERMISSION_REQUEST_CODE_CAMERA_AND_STORAGE = 05;

    public static boolean checkPermissionCamera(Activity activity) {
        Log.e("call", "checkPermissionCamera");
        int result = ContextCompat.checkSelfPermission(activity,
                Manifest.permission.CAMERA);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    public static void requestPermissionCamera(Activity activity, int requestCode) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.CAMERA)) {
            Toast.makeText(activity,
                    "CAMERA permission allows us to capture images. Please allow in App Settings for additional functionality.",
                    Toast.LENGTH_LONG).show();
            //--onBackPressed();
        } else {
            Log.e("call", "ndroid.Manifest.permission.CAMERA");
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CAMERA},
                    requestCode);
        }
    }

    public static boolean checkPermissionWriteExternalStorage(Activity activity) {
        int result = ContextCompat.checkSelfPermission(activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    public static void requestPermissionWriteExternalStorage(Activity activity, int requestCode) {

        if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Toast.makeText(activity,
                    "Write storage permission allows us to save capture images. Please allow in App Settings for additional functionality.",
                    Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, requestCode);
        }
    }

    public static boolean checkPermissionReadExternalStorage(Activity activity) {
        int result = ContextCompat.checkSelfPermission(activity,
                Manifest.permission.READ_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    public static void requestPermissionReadExternalStorage(Activity activity, int requestCode) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                Manifest.permission.READ_EXTERNAL_STORAGE)) {
            Toast.makeText(activity,
                    "Read storage permission allows us to save capture images. Please allow in App Settings for additional functionality.",
                    Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, requestCode);
        }
    }

    public static boolean checkPermissionRecordAudio(Activity activity) {
        int result = ContextCompat.checkSelfPermission(activity,
                Manifest.permission.RECORD_AUDIO);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    public static void requestPermissionRecordAudio(Activity activity, int requestCode) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                Manifest.permission.RECORD_AUDIO)) {
            Toast.makeText(activity,
                    "Read storage permission allows us to save capture images. Please allow in App Settings for additional functionality.",
                    Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.RECORD_AUDIO}, requestCode);
        }
    }

    public static void startInstalledAppDetailsActivity(final Activity context) {
        if (context == null) {
            return;
        }
        final Intent i = new Intent();
        i.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        i.addCategory(Intent.CATEGORY_DEFAULT);
        i.setData(Uri.parse("package:" + context.getPackageName()));
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        context.startActivity(i);
    }

    final public static int REQUEST_CODE_LOCATION_GOOGLE_MAP = 202;

    public static boolean checkPermissionLocation(Activity mActivity) {
        int result = ContextCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_FINE_LOCATION);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    public static void requestPermissionLocation(Activity activity, int requestCode) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.ACCESS_FINE_LOCATION)) {
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    requestCode);
        } else {
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    requestCode);
        }
    }

    public static void requestPermissionCameraAndWriteExternalStorage(Activity activity, int requestCode) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                Manifest.permission.CAMERA)) {
            ActivityCompat
                    .requestPermissions(activity,
                            new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            requestCode);
        } else {
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    requestCode);
        }
    }
}