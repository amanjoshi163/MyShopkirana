package com.myshopkirana.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.jsibbold.zoomage.ZoomageView;
import com.myshopkirana.R;
import com.myshopkirana.utils.ZoomableImageView;

public class ZoomImageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zoom_image);

        ZoomageView touch = (ZoomageView)findViewById(R.id.myZoomageView);
       String mainURl = getIntent().getStringExtra("imageurl");
        Log.e("Bhagwan ", "" + mainURl);
        if (mainURl != null) {

            Glide.with(ZoomImageActivity.this)
                    .load(mainURl)
                    .into(touch);
        }
    }
}