package com.myshopkirana.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
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

import com.bumptech.glide.Glide;
import com.myshopkirana.BuildConfig;
import com.myshopkirana.R;
import com.myshopkirana.databinding.ActivityCapcherImageBinding;
import com.myshopkirana.model.CustomerModel;
import com.myshopkirana.utils.CommonClassForAPI;
import com.myshopkirana.utils.GPSTracker;
import com.myshopkirana.utils.Utils;
import com.watermark.androidwm_light.WatermarkBuilder;
import com.watermark.androidwm_light.bean.WatermarkText;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import id.zelory.compressor.Compressor;
import io.reactivex.observers.DisposableObserver;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.functions.Action1;
//import rx.functions.Action1;
//import rx.functions.Action1;

public class CapcherImageActivity extends AppCompatActivity {
    ActivityCapcherImageBinding mBinding;
    GPSTracker gpsTracker;
    Geocoder geocoder;
    // upload image
    private final DisposableObserver<String> imageObserver = new DisposableObserver<String>() {
        @Override
        public void onNext(@NotNull String response) {
            try {

                calltimeandAddress();
                Utils.hideProgressDialog(CapcherImageActivity.this);
                if (response != null) {
                    mainURl = BuildConfig.apiEndpoint + response;

                    Log.e("DaysBeatList_model", mainURl);
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
    String fullAddress = "";
    String localAdress, landmarkArea, shopFoundValue, mainURl;
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

    private void calltimeandAddress() {
        try {
            gpsTracker = new GPSTracker(CapcherImageActivity.this);
            List<Address> addresses;
            addresses = geocoder.getFromLocation(gpsTracker.getLatitude(), gpsTracker.getLongitude(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            String address = "" + addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            mBinding.txtAddress.setVisibility(View.VISIBLE);
            mBinding.txtAddress.setText("Current Address :" + address);

        } catch (Exception e) {

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_capcher_image);
        customerModel = (CustomerModel) getIntent().getSerializableExtra("model");
        shopFoundValue = getIntent().getStringExtra("ShopFound");
        mainURl = getIntent().getStringExtra("imageurl");

        initView();
    }

    private void initView() {
        utils = new Utils(this);
        commonClassForAPI = CommonClassForAPI.getInstance(this);
        geocoder = new Geocoder(this, Locale.getDefault());

        gpsTracker = new GPSTracker(this);
        mBinding.skCode.setText(getString(R.string.sk_code) + ": " + customerModel.getSkcode());
        mBinding.name.setText(getString(R.string.txt_Shop_Name) + ": " + customerModel.getShopName());
        mBinding.txtAddValue.setText(getString(R.string.txt_Shop_Address) + ": " + customerModel.getShippingAddress());


        mBinding.toolbar.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        if (mainURl != null) {
            calltimeandAddress();
            Glide.with(CapcherImageActivity.this)
                    .load(mainURl)
                    .into(mBinding.ivShop);
        }

        if (shopFoundValue.equals("false")) {
            mBinding.txtTakepic.setText("Not Able To Track");
            mBinding.cbShopNotFound.setVisibility(View.VISIBLE);
        } else {
            mBinding.cbShopNotFound.setVisibility(View.GONE);
        }
        mBinding.llTakeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage(CapcherImageActivity.this);
            }
        });
        mBinding.ivShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mainURl != null) {
                    Intent intent = new Intent(CapcherImageActivity.this, ZoomImageActivity.class);
                    intent.putExtra("imageurl", mainURl);
                    startActivity(intent);
                }
            }
        });

        mBinding.btSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mainURl == null) {
                    Utils.setToast(getApplicationContext(), "Please Upload Shop Image");
                } else if (shopFoundValue.equals("false")) {
                    if (!mBinding.cbShopNotFound.isChecked()) {
                        Utils.setToast(getApplicationContext(), "Please Checked Shop not Found");
                    } else {
                        callApi();
                    }

                } else {

                    callApi();


                }

            }
        });
    }

    private void callApi() {
        if (customerModel.getLandMark() != null) {
            landmarkArea = customerModel.getLandMark();
        } else {
            landmarkArea = "";
        }
        if (customerModel.getShippingAddress() != null) {
            localAdress = customerModel.getShippingAddress();
        } else {
            localAdress = "";
        }

        CustomerModel model = new CustomerModel(customerModel.getCustomerId(),
                customerModel.getSkcode(),
                customerModel.getShopName(),
                localAdress,
                landmarkArea,
                customerModel.getLat(),
                customerModel.getLg(),
                shopFoundValue,
                mainURl,
                fullAddress,
                gpsTracker.getLatitude(), gpsTracker.getLongitude());
        if (utils.isNetworkAvailable()) {
            if (commonClassForAPI != null) {
                commonClassForAPI.updateCustomer(updateCustomer, model);
            }
        } else {
            Utils.setToast(getApplicationContext(), "No internet Connection");
        }
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

    //    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode != RESULT_CANCELED) {
//            switch (requestCode) {
//                case 0:
//                    Uri selectedImage = Uri.parse(uploadFilePath);
//
//
//                    mBinding.ivShop.setImageURI(selectedImage);
//                    if (utils.isNetworkAvailable()) {
//                        uploadMultipart();
//                    } else {
//                        Utils.setToast(this, "No Internet Connection");
//                    }
//                    Log.e("Bhagwan ", "" + selectedImage.toString());
//
//                    break;
//                case 1:
//                    if (resultCode == RESULT_OK && data != null) {
//
//                    }
//                    break;
//            }
//        }
//    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case 0:
                    Uri selectedImage = Uri.fromFile(new File(uploadFilePath));
                    try {
                        SimpleDateFormat df = new SimpleDateFormat("dd/MMM/yyyy,hh:mm aaa");
                        String date = df.format(Calendar.getInstance().getTime());
                        Log.e("TIMEEeee", date);

                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
                        WatermarkText watermarkText = new WatermarkText("Time : " + date)
                                .setPositionX(0.1)
                                .setPositionY(0.8)
                                .setTextColor(getResources().getColor(R.color.status_orange))
                                .setTextShadow(0.1f, 5, 5, Color.BLACK)
                                .setTextFont(R.font.segoeuib)
                                .setTextAlpha(150)
                                .setRotation(0)
                                .setTextSize(50);
                        WatermarkBuilder
                                .create(this, bitmap)
                                .loadWatermarkText(watermarkText) // use .loadWatermarkImage(watermarkImage) to load an image.
                                .getWatermark()
                                .setToImageView(mBinding.ivShop);
                        BitmapDrawable drawable = (BitmapDrawable) mBinding.ivShop.getDrawable();
                        Bitmap watermarkBitmap = drawable.getBitmap();
                        try (FileOutputStream out = new FileOutputStream(uploadFilePath)) {
                            watermarkBitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

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
        File fileToUpload = new File(uploadFilePath);

        //uploadImagePath(fileToUpload);
        Compressor.getDefault(this)
                .compressToFileAsObservable(fileToUpload)
                ///.subscribeOn(Schedulers.io())
                ///.observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<File>() {
                    @Override
                    public void call(File file) {
                        ///compressedImage = file;
                        uploadImagePath(fileToUpload);
                    }
                }, throwable -> showError(throwable.getMessage()));
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
