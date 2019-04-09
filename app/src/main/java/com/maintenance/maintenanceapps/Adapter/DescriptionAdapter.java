package com.maintenance.maintenanceapps.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.maintenance.maintenanceapps.DescriptionList;
import com.maintenance.maintenanceapps.R;

import java.util.ArrayList;

public class DescriptionAdapter extends RecyclerView.Adapter<DescriptionAdapter.DescriptionViewHolder> {
    private ArrayList<DescriptionList> mdescList;

    @NonNull
    @Override
    public DescriptionViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.activity_description_list, viewGroup, false);
        DescriptionViewHolder dvh = new DescriptionViewHolder(v);
        return dvh;
    }

    @Override
    public void onBindViewHolder(@NonNull DescriptionViewHolder descriptionViewHolder, int i) {
        DescriptionList currentItem = mdescList.get(i);

        descriptionViewHolder.mTanggal.setText(currentItem.getTanggall());
        descriptionViewHolder.mNotes.setText(currentItem.getDescc());
        descriptionViewHolder.mUser.setText(currentItem.getUserr());
    }

    @Override
    public int getItemCount() {
        return mdescList.size();
    }

    public static class DescriptionViewHolder extends RecyclerView.ViewHolder{
        public TextView mTanggal;
        public  TextView mNotes;
        public TextView mUser;

        public DescriptionViewHolder(@NonNull View itemView) {
            super(itemView);
            mTanggal = itemView.findViewById(R.id.tglView);
            mNotes = itemView.findViewById(R.id.descView);
            mUser = itemView.findViewById(R.id.userView);
        }
    }

    public DescriptionAdapter(ArrayList<DescriptionList> descList){
        mdescList = descList;
    }


}
