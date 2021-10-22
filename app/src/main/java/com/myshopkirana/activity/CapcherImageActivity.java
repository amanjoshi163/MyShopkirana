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

import com.bumptech.glide.Glide;
import com.myshopkirana.BuildConfig;
import com.myshopkirana.R;
import com.myshopkirana.databinding.ActivityCapcherImageBinding;
import com.myshopkirana.model.CustomerModel;
import com.myshopkirana.utils.CommonClassForAPI;
import com.myshopkirana.utils.GPSTracker;
import com.myshopkirana.utils.Utils;

import org.jetbrains.annotations.NotNull;

import java.io.File;
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

public class CapcherImageActivity extends AppCompatActivity {
    ActivityCapcherImageBinding mBinding;
    GPSTracker gpsTracker;
    Geocoder geocoder;
    String fullAddress = "";
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
            String city = "" + addresses.get(0).getLocality();
            String state = "" + addresses.get(0).getAdminArea();
            String country = "" + addresses.get(0).getCountryName();
            String postalCode = "" + addresses.get(0).getPostalCode();
            String knownName = "" + addresses.get(0).getFeatureName();
            fullAddress = address + "," + city + "," + postalCode + "," + state + "," + knownName + "," + country;
            mBinding.txtAddress.setVisibility(View.VISIBLE);
            mBinding.txtAddress.setText("Current Address :" + fullAddress);
            Date c = Calendar.getInstance().getTime();


            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat mdformat = new SimpleDateFormat("hh:mm aaa");
            String strDate = "" + mdformat.format(calendar.getTime());

            SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
            String formattedDate = df.format(c);
            String timeate = "Time : " + strDate + " Date : " + formattedDate;
            String device = "Device : " + Utils.getDeviceName();
            String s = timeate
                    + System.getProperty("line.separator")
                    + device
                    + System.getProperty("line.separator");

            mBinding.txtTime.setText(s);
            mBinding.txtTime.setVisibility(View.VISIBLE);
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

        //uploadImagePath(fileToUpload);
        Compressor.getDefault(this)
                .compressToFileAsObservable(fileToUpload)
                ///.subscribeOn(Schedulers.io())
                ///.observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<File>() {
                    @Override
                    public void call(File file) {
                        ///compressedImage = file;
                        uploadImagePath(file);
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
