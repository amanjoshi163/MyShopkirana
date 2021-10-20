package com.myshopkirana.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.myshopkirana.R;
import com.myshopkirana.activity.HomeActivity;
import com.myshopkirana.model.CityModel;

import java.util.ArrayList;

public class CityAdapter extends ArrayAdapter<CityModel> {

    private ArrayList<CityModel> cList;
    private Activity context;
    private LayoutInflater flater;



    public CityAdapter(HomeActivity activity, int listitems_layout, int title, ArrayList<CityModel> cList) {
        super(activity, listitems_layout, title, cList);
        this.context=activity;
        this.cList=cList;
        flater = context.getLayoutInflater();
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        return rowview(convertView, position);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return rowview(convertView, position);
    }

    private View rowview(View convertView, int position) {

        CityModel rowItem = cList.get(position);

        viewHolder holder;
        View rowview = convertView;
        if (rowview == null) {

            holder = new viewHolder();
            flater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowview = flater.inflate(R.layout.listitems_layout, null, false);

            holder.txtTitle = rowview.findViewById(R.id.cityname);

            rowview.setTag(holder);
        } else {
            holder = (viewHolder) rowview.getTag();
        }

        holder.txtTitle.setText(rowItem.getCityName());

        return rowview;
    }

    private class viewHolder {
        TextView txtTitle;
    }

}