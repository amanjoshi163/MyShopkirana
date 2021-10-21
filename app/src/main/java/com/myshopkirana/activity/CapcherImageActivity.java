package com.myshopkirana.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.myshopkirana.BuildConfig;
import com.myshopkirana.R;
import com.myshopkirana.databinding.ActivityCapcherImageBinding;
import com.myshopkirana.model.CustomerModel;
import com.myshopkirana.model.ImageResponse;
import com.myshopkirana.utils.CommonClassForAPI;
import com.myshopkirana.utils.Utils;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import io.reactivex.observers.DisposableObserver;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class CapcherImageActivity extends AppCompatActivity {
    ActivityCapcherImageBinding mBinding;
    private CustomerModel customerModel;
    private Utils utils;
    private CommonClassForAPI commonClassForAPI;
    private String fProfile = "";
    private String uploadFilePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_capcher_image);
        customerModel = (CustomerModel) getIntent().getSerializableExtra("model");
        initView();
    }

    private void initView() {
        utils = new Utils(this);
        commonClassForAPI = CommonClassForAPI.getInstance(this);
        mBinding.skCode.setText(getString(R.string.sk_code) + ": " + customerModel.getSkcode());
        mBinding.name.setText(getString(R.string.txt_Shop_Name) + ": " + customerModel.getShopName());
        mBinding.txtAddValue.setText(getString(R.string.txt_Shop_Address) + ": " + customerModel.getShippingAddress());


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
                }else {
                    CustomerModel model = new CustomerModel(customerModel.getCustomerId(),
                            customerModel.getSkcode(),
                            customerModel.getShopName(),
                            customerModel.getShippingAddress(),
                            customerModel.getLandMark(),
                            customerModel.getLat(),
                            customerModel.getLg(),
                            customerModel.getShopFound(),
                            uploadFilePath,customerModel.getNewShippingAddress(),
                            customerModel.getNewlat(),customerModel.getNewlg());
                    if (utils.isNetworkAvailable()) {
                        if (commonClassForAPI != null) {
                            commonClassForAPI.updateCustomer(updateCustomer,model);
                        }
                    } else {
                        Utils.setToast(getApplicationContext(), "No internet Connection");
                    }

                }

            }
        });
    }

    // But item response
    DisposableObserver<JsonObject> updateCustomer = new DisposableObserver<JsonObject>() {
        @Override
        public void onNext(JsonObject jsonObject) {
            Utils.hideProgressDialog(CapcherImageActivity.this);
            if (jsonObject!=null){
                startActivity(new Intent(CapcherImageActivity.this,HomeActivity.class));
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


    private void chooseImage(Context context) {
        final CharSequence[] optionsMenu = {"Take Photo", "Choose from Gallery", "Exit"}; // create a menuOption Array
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
        uploadImagePath(fileToUpload);
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

}
