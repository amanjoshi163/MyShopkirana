package com.myshopkirana.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
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
import com.myshopkirana.R;
import com.myshopkirana.databinding.ActivityCustomerDetailBinding;
import com.myshopkirana.model.CustomerModel;
import com.myshopkirana.utils.DirectionsJSONParser;
import com.myshopkirana.utils.GPSTracker;
import com.nabinbhandari.android.permissions.PermissionHandler;
import com.nabinbhandari.android.permissions.Permissions;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CustomerDetailActivity extends AppCompatActivity implements OnMapReadyCallback {
    GoogleMap googleMapMain;
    private ActivityCustomerDetailBinding mBinding;
    private CustomerModel customerModel;
    private LatLng cLatLng, destLatLng;
    private PolylineOptions lineOptions;
    private GPSTracker gpsTracker;
    private ArrayList<LatLng> points = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_customer_detail);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_route);
        mapFragment.getMapAsync(this);
        gpsTracker=new GPSTracker(this);

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
                String strUri = "http://maps.google.com/maps?q=loc:" + customerModel.getLat() + "," + customerModel.getLg() + " (" + customerModel.getShippingAddress() + ")";
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

        mBinding.llBottomSheet.llTakeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callRunTimePermissions();
            }
        });

        mBinding.llBottomSheet.llTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callRunTimePermissions();

            }
        });

        mBinding.llBottomSheet.llCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              onBackPressed();

            }
        });
    }

    public void callRunTimePermissions() {
        String[] permissions = {Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE};
        Permissions.check(CustomerDetailActivity.this/*context*/, permissions, null/*rationale*/, null/*options*/, new PermissionHandler() {
            @Override
            public void onGranted() {
                Log.e("onDenied", "onGranted");
                startActivity(new Intent(CustomerDetailActivity.this,CapcherImageActivity.class).putExtra("model",customerModel));
            }

            @Override
            public void onDenied(Context context, ArrayList<String> deniedPermissions) {
                super.onDenied(context, deniedPermissions);
                Log.e("onDenied", "onDenied" + deniedPermissions);


            }
        });
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
        destLatLng = new LatLng(customerModel.getLat(), customerModel.getLg());
        drawRoute(cLatLng, destLatLng);
    }
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


    private void drawRoute(LatLng origin, LatLng dest) {

         googleMapMain.addMarker(new MarkerOptions()
                .icon(BitmapDescriptorFactory.defaultMarker())
                .title("Current Location")
                .flat(true)
                .anchor(0.5f, 0.5f)
                .position(origin));

        Bitmap markerBitmap1 = BitmapFactory.decodeResource(getResources(), R.drawable.green_tag);
        markerBitmap1 = scaleBitmap(markerBitmap1, 150, 90, customerModel.getSkcode());
        googleMapMain.addMarker(new MarkerOptions()
                .position(dest)
                .anchor(0.5f, 0.5f)
                .title(customerModel.getSkcode())
                .icon(BitmapDescriptorFactory.fromBitmap(markerBitmap1)));

        String url = getUrl(origin, dest);

        FetchUrl FetchUrl = new FetchUrl();


        FetchUrl.execute(url);
    }

    private String getUrl(LatLng origin, LatLng dest) {


        //  String url = "https://maps.googleapis.com/maps/api/directions/"+output+"?"+parameters + "&key=" + MY_API_KEY
        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;


        // Sensor enabled
        String sensor = "sensor=false";


        String parameters = "";
        parameters = str_origin + "&" + str_dest + "&" + sensor;
        // Output format


        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/json" + "?" + parameters
                + "&key=" + getString(R.string.google_maps_key);

        return url;

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


                    if (point.get("lat") != null) {
                        double lat = Double.parseDouble(point.get("lat"));
                        double lng = Double.parseDouble(point.get("lng"));
                        LatLng position = new LatLng(lat, lng);
                        points.add(position);
                    }

                }


                // Adding all the points in the route to LineOptions
                staticPolyLine();
//                lineOptions.addAll(points);
//                lineOptions.width(10);
//                lineOptions.color(getResources().getColor(R.color.purple_500));
//                googleMapMain.animateCamera(CameraUpdateFactory.newLatLngZoom( cLatLng, 12.0f));
            }




        }
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
}