package com.myshopkirana.activity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.myshopkirana.BuildConfig;
import com.myshopkirana.R;
import com.myshopkirana.databinding.ActivityCustomerDetailBinding;
import com.myshopkirana.model.CustomerModel;
import com.myshopkirana.utils.CommonClassForAPI;
import com.myshopkirana.utils.DirectionsJSONParser;
import com.myshopkirana.utils.GPSTracker;
import com.myshopkirana.utils.Utils;
import com.nabinbhandari.android.permissions.PermissionHandler;
import com.nabinbhandari.android.permissions.Permissions;
import com.watermark.androidwm_light.WatermarkBuilder;
import com.watermark.androidwm_light.bean.WatermarkText;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import id.zelory.compressor.Compressor;
import io.reactivex.observers.DisposableObserver;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.functions.Action1;

//import rx.functions.Action1;

public class CustomerDetailActivity extends AppCompatActivity implements OnMapReadyCallback {
    GoogleMap googleMapMain;
    int shopFound = 0;
    private ActivityCustomerDetailBinding mBinding;

    private CustomerModel customerModel;
    private LatLng cLatLng;
    private PolylineOptions lineOptions;
    private String destLatLng;
    private GPSTracker gpsTracker;
    private String fProfile = "";
    private String uploadFilePath;
    private Utils utils;
    private String distance = "";
    private final DisposableObserver<String> imageObserver = new DisposableObserver<String>() {
        @Override
        public void onNext(@NotNull String response) {
            try {

                Utils.hideProgressDialog(CustomerDetailActivity.this);
                if (response != null) {
                    String mainURl = BuildConfig.apiEndpoint + response;
                    startActivity(new Intent(CustomerDetailActivity.this, CapcherImageActivity.class).
                            putExtra("model", customerModel).putExtra("ShopFound", shopFound).putExtra("imageurl", mainURl).putExtra("distance", distance));
                    Log.e("DaysBeatList_model", mainURl);
                } else {
                    Toast.makeText(CustomerDetailActivity.this, "Image Not Uploaded", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onError(Throwable e) {
            e.printStackTrace();
            Utils.hideProgressDialog(CustomerDetailActivity.this);
        }

        @Override
        public void onComplete() {
            Utils.hideProgressDialog(CustomerDetailActivity.this);
        }
    };
    private CommonClassForAPI commonClassForAPI;
    private ArrayList<LatLng> points = new ArrayList<>();

    public static Bitmap scaleBitmap(Bitmap bitmap, int newWidth, int newHeight, String skcode) {
        Bitmap scaledBitmap = Bitmap.createBitmap(newWidth, newHeight, Bitmap.Config.ARGB_8888);

        float scaleX = newWidth / (float) bitmap.getWidth();
        float scaleY = newHeight / (float) bitmap.getHeight();
        float pivotX = 0;
        float pivotY = 0;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(scaleX, scaleY, pivotX, pivotY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bitmap, 0, 0, new Paint(Paint.FILTER_BITMAP_FLAG));

        Paint color = new Paint();
        color.setTextSize(20);
        color.setColor(Color.WHITE);
        canvas.drawText(skcode, 30, 40, color);

        return scaledBitmap;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_customer_detail);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_route);
        mapFragment.getMapAsync(this);
        gpsTracker = new GPSTracker(this);
        commonClassForAPI = CommonClassForAPI.getInstance(this);
        utils = new Utils(this);
        customerModel = (CustomerModel) getIntent().getSerializableExtra("model");
        if (customerModel != null) {
            mBinding.llBottomSheet.name.setText("Name : " + customerModel.getShopName());
            mBinding.llBottomSheet.skCode.setText("Sk Code : " + customerModel.getSkcode());
            mBinding.llBottomSheet.txtAddValue.setText("Address : " + customerModel.getShippingAddress());
            mBinding.toolbar.title.setText(customerModel.getSkcode());
        }


        mBinding.toolbar.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mBinding.llBottomSheet.llmap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strUri = "http://maps.google.co.in/maps?q=" + customerModel.getShippingAddress() + " (" + customerModel.getShippingAddress() + ")";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(strUri));
                intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
                startActivity(intent);

            }
        });

        mBinding.llBottomSheet.llSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBinding.llBottomSheet.bottomSheet.setVisibility(View.GONE);
                mBinding.llBottomSheet.bottomSheetNotAble.setVisibility(View.VISIBLE);

            }
        });

        mBinding.llBottomSheet.llTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                callRunTimePermissions(1);
//                requestWritePermission();
            }
        });


        mBinding.llBottomSheet.llTakePhotoNotAble.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callRunTimePermissions(0);

            }
        });

        mBinding.llBottomSheet.llCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//              onBackPressed();
                mBinding.llBottomSheet.bottomSheet.setVisibility(View.VISIBLE);
                mBinding.llBottomSheet.bottomSheetNotAble.setVisibility(View.GONE);
            }
        });
    }


    public void callRunTimePermissions(int isshopFound) {

        String[] permissions = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        Permissions.check(CustomerDetailActivity.this/*context*/, permissions, null/*rationale*/, null/*options*/, new PermissionHandler() {
            @Override
            public void onGranted() {
                Log.e("onDenied", "onGranted");
//                startActivity(new Intent(CustomerDetailActivity.this,CapcherImageActivity.class).
//                        putExtra("model",customerModel).putExtra("ShopFound",shopFound));
                shopFound = isshopFound;
                chooseImage(CustomerDetailActivity.this);
            }

            @Override
            public void onDenied(Context context, ArrayList<String> deniedPermissions) {
                super.onDenied(context, deniedPermissions);
                Log.e("onDenied", "onDenied" + deniedPermissions);


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
    public void onMapReady(@NonNull GoogleMap googleMap) {
        googleMapMain = googleMap;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            googleMap.getUiSettings().setZoomControlsEnabled(true);
            googleMap.getUiSettings().setCompassEnabled(true);
            googleMap.getUiSettings().setMapToolbarEnabled(true);
            return;
        }


        cLatLng = new LatLng(gpsTracker.getLatitude(), gpsTracker.getLongitude());
//        destLatLng = new LatLng(customerModel.getLat(), customerModel.getLg());
        destLatLng = customerModel.getShippingAddress();
        drawRoute(cLatLng, destLatLng, customerModel);
    }

    private void drawRoute(LatLng origin, String dest, CustomerModel customerModel) {

        googleMapMain.addMarker(new MarkerOptions()
                .icon(BitmapDescriptorFactory.defaultMarker())
                .title("Current Location")
                .flat(true)
                .anchor(0.5f, 0.5f)
                .position(origin));
        Log.e("SHIPPP", ">> " + customerModel.getShippingAddress());
        if (customerModel.getShippingAddress() != null && !customerModel.getShippingAddress().equals("")) {
            Bitmap markerBitmap1 = BitmapFactory.decodeResource(getResources(), R.drawable.green_tag);
            markerBitmap1 = scaleBitmap(markerBitmap1, 150, 90, this.customerModel.getSkcode());

            LatLng lt = getLocationFromAddress(this, customerModel.getShippingAddress());
            if (lt != null) {
                googleMapMain.addMarker(new MarkerOptions()
                        .position(lt)
                        .anchor(0.5f, 0.5f)
                        .title(this.customerModel.getSkcode())
                        .icon(BitmapDescriptorFactory.fromBitmap(markerBitmap1)));
                String url = getUrl(origin, dest);

                FetchUrl FetchUrl = new FetchUrl();


                FetchUrl.execute(url);
            }
        }

    }

    private String getUrl(LatLng origin, String dest) {


        //  String url = "https://maps.googleapis.com/maps/api/directions/"+output+"?"+parameters + "&key=" + MY_API_KEY
        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        // Destination of route
        String str_dest = "destination=" + dest;


        // Sensor enabled
        String sensor = "sensor=false";


        String parameters = "";
        parameters = str_origin + "&" + str_dest + "&" + sensor;
        // Output format


        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/json" + "?" + parameters
                + "&key=" + getString(R.string.google_maps_key);
        url = url.replaceAll(" ", "%20");

        Log.e("Map_URL", url);
        return url;

    }

    public LatLng getLocationFromAddress(Context context, String strAddress) {
        Geocoder coder = new Geocoder(context);
        List<Address> address;
        LatLng p1 = null;

        try {
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }
            Address location = address.get(0);
            location.getLatitude();
            location.getLongitude();

            p1 = new LatLng(location.getLatitude(), location.getLongitude());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return p1;

    }

    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream inputStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.connect();

            inputStream = urlConnection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);

            }

            data = sb.toString();


            br.close();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null)
                inputStream.close();
            urlConnection.disconnect();
        }

        Log.d("data downlaod", data);
        return data;
    }

    void staticPolyLine() {
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (LatLng latLng : points) {
            builder.include(latLng);
        }
        LatLngBounds bounds = builder.build();
        CameraUpdate mCameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, 2);
        googleMapMain.animateCamera(mCameraUpdate);

        lineOptions = new PolylineOptions();
        lineOptions.color(Color.BLACK);
        lineOptions.width(5);
        lineOptions.addAll(points);
        googleMapMain.addPolyline(lineOptions);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case 0:

                    try {
                        Utils.showProgressDialog(CustomerDetailActivity.this);
                        Uri selectedImage = Uri.fromFile(new File(uploadFilePath));
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
                                .setToImageView(mBinding.llBottomSheet.demoIv);
                        BitmapDrawable drawable = (BitmapDrawable) mBinding.llBottomSheet.demoIv.getDrawable();
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

//        uploadImagePath(fileToUpload);

//        new Compressor(this)
//                .setQuality(10)
//                .compressToFileAsFlowable(fileToUpload)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Consumer<File>() {
//                    @Override
//                    public void accept(File file) throws Exception {
//                        uploadImagePath(file);
//                    }
//                });


//
//
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (imageObserver != null) {
            imageObserver.dispose();
        }
    }

    private void uploadImagePath(File file) {
        RequestBody requestFile =
                RequestBody.create(MediaType.parse("image/jpeg"), file);
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("file", file.getName(), requestFile);

        commonClassForAPI.uploadImage(imageObserver, body);
    }

    private class FetchUrl extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try {


                data = downloadUrl(url[0]);

            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();

//             Invokes the thread for parsing the JSON data
            parserTask.execute(result);

//            staticPolyLine(polyLine);

        }
    }

    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                Log.d("ParserTask", jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();


                // Starts parsing data
                routes = parser.parse(jObject);


            } catch (Exception e) {

                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            lineOptions = null;
            if (result != null && result.size() < 1) {
                Toast.makeText(CustomerDetailActivity.this, getResources().getString(R.string.cust_location_not), Toast.LENGTH_SHORT).show();
                return;
            }
            // Traversing through all the routes
            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList<>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    if (j == 0) {    // Get distance from the list
                        distance = point.get("distance");
                    }
                    if (point.get("lat") != null) {
                        double lat = Double.parseDouble(point.get("lat"));
                        double lng = Double.parseDouble(point.get("lng"));
                        LatLng position = new LatLng(lat, lng);
                        points.add(position);
                    }

                }


                if (!distance.equals(null)) {
                    String[] separated = distance.split(" ");
                    distance = separated[0]; // this will contain "Fruit"

                }
                Log.e("Distnedsfs", ">>>" + distance);
                // Adding all the points in the route to LineOptions
                staticPolyLine();
//                lineOptions.addAll(points);
//                lineOptions.width(10);
//                lineOptions.color(getResources().getColor(R.color.purple_500));
//                googleMapMain.animateCamera(CameraUpdateFactory.newLatLngZoom( cLatLng, 12.0f));
            }


        }
    }
}