package com.myshopkirana.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.myshopkirana.R;
import com.myshopkirana.databinding.SkClusterItemBinding;
import com.myshopkirana.interfaces.ClusterSelectionInterface;
import com.myshopkirana.model.ClusterModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ClusterAdapter extends RecyclerView.Adapter<ClusterAdapter.ViewHolder> {


    private final ArrayList<ClusterModel> itemLists;
    private final ArrayList<ClusterModel> modelArrayList;
    private final Button btnDone;
    private final ClusterSelectionInterface selectionInterface;
    private final Context context;


    public ClusterAdapter(Activity activity, ArrayList<ClusterModel> itemLists, Button btnDone, ClusterSelectionInterface selectionInterfac) {

        this.itemLists = itemLists;
        this.context = activity;
        this.btnDone = btnDone;
        modelArrayList = new ArrayList<>();
        this.selectionInterface = selectionInterfac;
    }


    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NotNull ViewGroup viewGroup, int i) {
        return new ViewHolder(DataBindingUtil.inflate(LayoutInflater.from(viewGroup.getContext()),
                R.layout.sk_cluster_item, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NotNull ViewHolder viewHolder, @SuppressLint("RecyclerView") int i) {

        try {
            ClusterModel model = itemLists.get(i);
            viewHolder.mBinding.chbContent.setText(model.getClusterName());

            if (model.isSelected()) {
                modelArrayList.add(model);
                viewHolder.mBinding.chbContent.setChecked(true);
            } else {
                viewHolder.mBinding.chbContent.setChecked(false);


            }
            viewHolder.mBinding.chbContent.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    if (!model.isSelected()) {
                        model.setSelected(true);
                        modelArrayList.add(model);
//                        modelArrayList.add(new SelctedClusterModel(model.getClusterId(), model.getClusterName(), true));
                    } else {

                        if (model.isSelected()) {
                            for (int j = 0; j < modelArrayList.size(); j++) {
                                if (model.getClusterId() == modelArrayList.get(j).getClusterId()) {
                                    model.setSelected(false);
                                    modelArrayList.remove(model);
                                }
                            }

                        }
                    }

                    Log.e("modelArrayList", ">>" + modelArrayList.size());

                }
            });


            btnDone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (modelArrayList.size() > 0) {
                        selectionInterface.SelectedCluster(modelArrayList);
                    } else {
                        Toast.makeText(context, "Please Select Cluster", Toast.LENGTH_SHORT).show();
                    }


                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return itemLists == null ? 0 : itemLists.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        SkClusterItemBinding mBinding;


        public ViewHolder(SkClusterItemBinding binding) {
            super(binding.getRoot());
            this.mBinding = binding;
        }
    }
}
