package com.maintenance.maintenanceapps.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.maintenance.maintenanceapps.AgendaItem;
import com.maintenance.maintenanceapps.AgendaSubMaintenance;
import com.maintenance.maintenanceapps.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class AgendaAdapter extends RecyclerView.Adapter<AgendaAdapter.AgendaViewHolder> {
    private List<AgendaItem> mAgendaList;
    Context context;
    Date date;
    String dateFormat;
    //static FirebaseFirestore firestoreDB;
    //static String noVending = CONFIG.getNomorVending();

    public static class AgendaViewHolder extends RecyclerView.ViewHolder{
        public ImageView imageView;
        public TextView agendaView;
        public TextView agendaView2;
        public TextView tglView;
        public ConstraintLayout relativeLayoutAgenda;


        public AgendaViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            agendaView = itemView.findViewById(R.id.agendaTxt);
            agendaView2 = itemView.findViewById(R.id.agendaTxt2);
            tglView = itemView.findViewById(R.id.tglTxt);
            relativeLayoutAgenda = itemView.findViewById(R.id.rlAgendaList);
        }
    }

    public AgendaAdapter(Context context,List<AgendaItem> agendaList){
        mAgendaList = agendaList;
        this.context = context;
    }

//    public static void openQR(Boolean isQRMT){
//        Map<String, Object> vending = new HashMap<>();
//        vending.put("isQRMT", isQRMT);
//
//        firestoreDB.collection("m_outlet").document(noVending)
//                .update(vending)
//                .addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void aVoid) {
//                        Log.d("addstatus", "Document succesfully written!");
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Log.d("addstatus", "Error writing document",e);
//                    }
//                });
//
//    }
//
//    public static void openDoor(String doorStatus){
//        Map<String, Object> vending = new HashMap<>();
//        vending.put("doorStatus", doorStatus);
//
//        firestoreDB.collection("m_outlet").document(noVending)
//                .update(vending)
//                .addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void aVoid) {
//                        Log.d("addstatus", "Document succesfully written!");
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Log.d("addstatus", "Error writing document",e);
//                    }
//                });
//    }

//    public static void openMaintenance(Boolean isMaintenance){
//        Map<String, Object> vending = new HashMap<>();
//        vending.put("isMaintance", isMaintenance);
//
//        firestoreDB.collection("m_outlet").document(noVending)
//                .update(vending)
//                .addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void aVoid) {
//                        Log.d("addstatus", "Document succesfully written!");
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Log.d("addstatus", "Error writing document",e);
//                    }
//                });
//    }

    @NonNull
    @Override
    public AgendaViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.activity_agenda_item, viewGroup, false);
        AgendaViewHolder avh = new AgendaViewHolder(v);
        return avh;
    }

    @Override
    public void onBindViewHolder(@NonNull AgendaViewHolder agendaViewHolder, int position) {
        final AgendaItem currentItem = mAgendaList.get(position);
        //firestoreDB = FirebaseFirestore.getInstance();


        date = new Date();
        dateFormat = new SimpleDateFormat("dd MMM yyy").format(date);

//        Log.d("qwe", currentItem.getTglText());
//        Log.d("qwe1", currentItem.getOccuredate());
//        Log.d("qwe2", currentItem.getTransaksi());
//        Log.d("qwe3", currentItem.getTglPerbaiki());
//        Log.d("qwe4", currentItem.getOutCode());
//        Log.d("qwe5", currentItem.getAgendaText());
//        Log.d("qwe6", currentItem.getAgendaText2());
//        Log.d("qwe7", currentItem.getDuedate());
//        Log.d("qwe8", currentItem.getImageResource()+"");

        agendaViewHolder.agendaView.setText(currentItem.getAgendaText() );
        agendaViewHolder.agendaView2.setText(currentItem.getAgendaText2());
       // agendaViewHolder.tglView.setText(currentItem.getTglText());

        Log.d("outcodeaaa", currentItem.getOutCode());

        if(currentItem.getImageResource()==1)
        {
            agendaViewHolder.imageView.setImageResource(R.drawable.redwarning);
        }
        if(currentItem.getImageResource()==2)
        {
            agendaViewHolder.imageView.setImageResource(R.drawable.orangewarning);
        }
        if(currentItem.getImageResource()==3)
        {
            agendaViewHolder.imageView.setImageResource(R.drawable.bluewarning);
        }
        if(currentItem.getImageResource()==4)
        {
            agendaViewHolder.imageView.setImageResource(R.drawable.completeicon);
        }

        Log.d("bbb", currentItem.getOccuredate());
        Log.d("ccc", currentItem.getTglText());
        if(currentItem.getTglText().equals("N")){
            agendaViewHolder.tglView.setText(currentItem.getOccuredate().substring(0,6));
            agendaViewHolder.tglView.setTextColor(Color.parseColor("#FF0000"));
            Log.d("abc", currentItem.getOccuredate());
        }else if (currentItem.getTglText().equals("Y")){
            agendaViewHolder.imageView.setImageResource(R.drawable.completeicon);
            agendaViewHolder.tglView.setText("Selesai" + "\n" + currentItem.getTglPerbaiki().substring(0,6));
            agendaViewHolder.tglView.setTextColor(Color.parseColor("#0B6623"));

        }



//        if(agendaViewHolder.tglView.getText().equals("Perbaiki")){
//            agendaViewHolder.tglView.setTextColor(Color.parseColor("#FF0000"));
//        }else if(agendaViewHolder.tglView.getText().equals("Sudah Perbaiki")){
//            agendaViewHolder.tglView.setTextColor(Color.parseColor("#0B6623"));
//        }

        agendaViewHolder.relativeLayoutAgenda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putParcelable("Bundle_Item", currentItem);
                Intent i = new Intent(context, AgendaSubMaintenance.class);
                i.putExtra("Item_Detail", bundle);
                i.putExtra("Transaction", bundle);
                context.startActivity(i);

            }
        });


    }

    @Override
    public int getItemCount() {
        return mAgendaList.size();
    }


}
