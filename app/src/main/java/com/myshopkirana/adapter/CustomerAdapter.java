package com.myshopkirana.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.myshopkirana.R;
import com.myshopkirana.activity.CustomerDetailActivity;
import com.myshopkirana.databinding.SkListviewItemBinding;
import com.myshopkirana.model.CustomerModel;

import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class
CustomerAdapter extends RecyclerView.Adapter<CustomerAdapter.ViewHolder> {


    private   Activity activity;
    private   ArrayList<CustomerModel> itemLists;


    public CustomerAdapter(Activity activity, ArrayList<CustomerModel> itemLists) {
        this.activity = activity;
        this.itemLists = itemLists;
    }

    public void updateList(ArrayList<CustomerModel> list){
      this.itemLists = list;
        notifyDataSetChanged();
    }


    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NotNull ViewGroup viewGroup, int i) {
        return new ViewHolder(DataBindingUtil.inflate(LayoutInflater.from(viewGroup.getContext()),
                R.layout.sk_listview_item, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NotNull ViewHolder viewHolder, final int i) {

        try {
            CustomerModel model = itemLists.get(i);
            viewHolder.mBinding.skCode.setText(activity.getResources().getString(R.string.sk_code) + ": " + model.getSkcode());


            viewHolder.mBinding.name.setText(activity.getResources().getString(R.string.txt_Shop_Name)+": " + model.getShopName());



            viewHolder.mBinding.txtAddValue.setText(activity.getResources().getString(R.string.txt_Shop_Address)+": " + model.getShippingAddress());


            viewHolder.mBinding.llDetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    activity.startActivity(new Intent(activity, CustomerDetailActivity.class).putExtra("model", model));
                }
            });


        } catch (Exception e) {
            e.printStackTrace();
//            Crashlytics.logException(e);
        }
    }

    @Override
    public int getItemCount() {
        return itemLists == null ? 0 : itemLists.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        SkListviewItemBinding mBinding;


        public ViewHolder(SkListviewItemBinding binding) {
            super(binding.getRoot());
            this.mBinding = binding;
        }
    }
}
