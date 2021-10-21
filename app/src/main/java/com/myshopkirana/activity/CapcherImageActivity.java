package com.myshopkirana.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;

import com.myshopkirana.BuildConfig;
import com.myshopkirana.R;
import com.myshopkirana.databinding.ActivityCapcherImageBinding;
import com.myshopkirana.model.CustomerModel;
import com.myshopkirana.model.ImageResponse;
import com.myshopkirana.utils.CommonClassForAPI;
import com.myshopkirana.utils.GPSTracker;
import com.myshopkirana.utils.Utils;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;


 import io.reactivex.observers.DisposableObserver;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class CapcherImageActivity extends AppCompatActivity {
    // upload image
    private final DisposableObserver<ImageResponse> imageObserver = new DisposableObserver<ImageResponse>() {
        @Override
        public void onNext(@NotNull ImageResponse response) {
            try {
                Utils.hideProgressDialog(CapcherImageActivity.this);
                if (response != null) {

                } else {
                    Toast.makeText(CapcherImageActivity.this, "Image Not Uploaded", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onError(Throwable e) {
            e.printStackTrace();
            Utils.hideProgressDialog(CapcherImageActivity.this);
        }

        @Override
        public void onComplete() {
            Utils.hideProgressDialog(CapcherImageActivity.this);
        }
    };
    ActivityCapcherImageBinding mBinding;
    GPSTracker gpsTracker;
    Geocoder geocoder;
     List<Address> addresses;

    // But item response
    DisposableObserver<Boolean> updateCustomer = new DisposableObserver<Boolean>() {
        @Override
        public void onNext(Boolean jsonObject) {
            Utils.hideProgressDialog(CapcherImageActivity.this);
            if (jsonObject) {
                startActivity(new Intent(CapcherImageActivity.this, HomeActivity.class));
                finish();
            }
        }

        @Override
        public void onError(Throwable e) {
            Utils.hideProgressDialog(CapcherImageActivity.this);
            e.printStackTrace();
        }

        @Override
        public void onComplete() {
            Utils.hideProgressDialog(CapcherImageActivity.this);
        }
    };
    private CustomerModel customerModel;
    private Utils utils;
    private CommonClassForAPI commonClassForAPI;
    private String fProfile = "";
    private String uploadFilePath;



    String localAdress,landmarkArea,shopFoundValue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_capcher_image);
        customerModel = (CustomerModel) getIntent().getSerializableExtra("model");
        shopFoundValue =getIntent().getStringExtra("ShopFound");
        initView();
    }

    private void initView() {
        geocoder = new Geocoder(this, Locale.getDefault());
        utils = new Utils(this);
        commonClassForAPI = CommonClassForAPI.getInstance(this);
        geocoder = new Geocoder(this, Locale.getDefault());

        gpsTracker = new GPSTracker(this);
        mBinding.skCode.setText(getString(R.string.sk_code) + ": " + customerModel.getSkcode());
        mBinding.name.setText(getString(R.string.txt_Shop_Name) + ": " + customerModel.getShopName());
        mBinding.txtAddValue.setText(getString(R.string.txt_Shop_Address) + ": " + customerModel.getShippingAddress());

        if (gpsTracker != null) {
              try {
                addresses = geocoder.getFromLocation(gpsTracker.getLatitude(), gpsTracker.getLongitude(), 1);
                localAdress = addresses.get(0).getLocality();
                landmarkArea = addresses.get(0).getSubLocality();
            } catch (IOException e) {
                e.printStackTrace();
            }
            gpsTracker.getLongitude();
        }

        mBinding.toolbar.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mBinding.llTakeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage(CapcherImageActivity.this);
            }
        });
        mBinding.btSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (uploadFilePath == null) {
                    Utils.setToast(getApplicationContext(), "Please Upload Shop Image");
                } else {
                    String    fullAddress="";

                    try {
                        addresses = geocoder.getFromLocation(gpsTracker.getLatitude(), gpsTracker.getLongitude(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                        Log.e("DaysBeatList_model", addresses.toString());
                        String address = "" + addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                        String city = "" + addresses.get(0).getLocality();
                        String state = "" + addresses.get(0).getAdminArea();
                        String country = "" + addresses.get(0).getCountryName();
                        String postalCode = "" + addresses.get(0).getPostalCode();
                        String knownName = "" + addresses.get(0).getFeatureName();
                         fullAddress = address + "," + city + "," + postalCode + "," + state + "," + knownName + "," + country;

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    CustomerModel model = new CustomerModel(customerModel.getCustomerId(),
                            customerModel.getSkcode(),
                            customerModel.getShopName(),
                            localAdress,
                            landmarkArea,
                            customerModel.getLat(),
                            customerModel.getLg(),
                            shopFoundValue,
                            uploadFilePath,
                            fullAddress,
                            gpsTracker.getLatitude(),
                            gpsTracker.getLongitude());

                    if (utils.isNetworkAvailable()) {
                        if (commonClassForAPI != null) {
                            commonClassForAPI.updateCustomer(updateCustomer, model);
                        }
                    } else {
                        Utils.setToast(getApplicationContext(), "No internet Connection");
                    }

                }

            }
        });
    }

    private void chooseImage(Context context) {
        final CharSequence[] optionsMenu = {"Take Photo", "Exit"}; // create a menuOption Array
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setItems(optionsMenu, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (optionsMenu[i].equals("Take Photo")) {
                    Intent pictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (pictureIntent.resolveActivity(context.getPackageManager()) != null) {
                        File photoFile;
                        photoFile = createImageFile();
                        Uri photoUri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", photoFile);
                        pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        pictureIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                    }
                    startActivityForResult(pictureIntent, 0);
                } else if (optionsMenu[i].equals("Exit")) {
                    dialogInterface.dismiss();
                }
            }
        });
        builder.show();
    }

    private File createImageFile() {
        File storageDir = this.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File file = new File(Environment.getExternalStorageDirectory() + "/ShopKirana");
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        fProfile = "photo_" + timeStamp + "_";
        if (!file.exists()) {
            file.mkdirs();
        }
        file = new File(storageDir, fProfile);
        uploadFilePath = file.getAbsolutePath();

        return file;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case 0:
                    Uri selectedImage = Uri.parse(uploadFilePath);
                    mBinding.ivShop.setImageURI(selectedImage);
                    if (utils.isNetworkAvailable()) {
                        uploadMultipart();
                    } else {
                        Utils.setToast(this, "No Internet Connection");
                    }
                    Log.e("Bhagwan ", "" + selectedImage.toString());

                    break;
                case 1:
                    if (resultCode == RESULT_OK && data != null) {

                    }
                    break;
            }
        }
    }

    private void uploadMultipart() {
        final File fileToUpload = new File(uploadFilePath);


        try {
//            File comprssFile=  new Compressor(this).compressToFile(fileToUpload);
            uploadImagePath(fileToUpload);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
//        new Compressor(CapcherImageActivity.this)
//                .compressToFileAsFlowable(fileToUpload)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Consumer<File>() {
//                    @Override
//                    public void accept(File file) {
//                        File compressedImage = file;
//
//                        uploadImagePath(compressedImage);
//                        //mBinding.scroll.fullScroll(View.FOCUS_DOWN);
//                    }
//                }, new Consumer<Throwable>() {
//                    @Override
//                    public void accept(Throwable throwable) {
//                        throwable.printStackTrace();
//                       /// Toast.makeText(CustomerSignActivity.this, "" + throwable.getMessage(), Toast.LENGTH_SHORT).show();
//                    }
//                });


    }

    private void showError(String errorMessage) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
    }

    private void uploadImagePath(File file) {
        RequestBody requestFile =
                RequestBody.create(MediaType.parse("image/jpeg"), file);
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("file", file.getName(), requestFile);
        Utils.showProgressDialog(this);
        commonClassForAPI.uploadImage(imageObserver, body);
    }

}
