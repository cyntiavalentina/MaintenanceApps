package com.maintenance.maintenanceapps.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.maintenance.maintenanceapps.CONFIG;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class VendingAdapter {
    Object isMaintenance, isQRMT, doorStatus;
    static FirebaseFirestore firestoreDB = FirebaseFirestore.getInstance();
    static String nomorVending = CONFIG.getNomorVending();

//    public static void openQr(Boolean isQRMT) {
//        Map<String, Object> vending = new HashMap<>();
////       vending.put("isMaintenance", isMaintenance);
//        vending.put("isQRMT", isQRMT);
//
//
//
//        firestoreDB.collection("m_outlet").document(nomorVending)
//                .update(vending)
//                .addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void aVoid) {
//                        Log.d("addstatus", "DocumentSnapshot successfully written!");
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Log.w("addstatus", "Error writing document", e);
//                    }
//                });
//    }

    //untuk mengambil 2hal yang kita perlukan
    public static void openMaintenance(String doorStatus, Boolean isMaintenance) {
        firestoreDB = FirebaseFirestore.getInstance();
        Map<String, Object> vending = new HashMap<>();
            vending.put("doorStatus", doorStatus);
            vending.put("isMaintenance", isMaintenance);

        firestoreDB.collection("m_outlet").document(nomorVending)
                .update(vending)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("addstatus", "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("addstatus", "Error writing document", e);
                    }
                });
    }

    public static void openDoor(String doorStatus) {
        Map<String, Object> vending = new HashMap<>();
        vending.put("doorStatus", doorStatus);

        firestoreDB.collection("m_outlet").document(nomorVending)
                .update(vending)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("addstatus", "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("addstatus", "Error writing document", e);
                    }
                });
    }

    public static void openMaintenance(Boolean isMaintenance) {
        firestoreDB = FirebaseFirestore.getInstance();
        Map<String, Object> vending = new HashMap<>();
        vending.put("isMaintenance", isMaintenance);

        firestoreDB.collection("m_outlet").document(nomorVending)
                .update(vending)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("addstatus", "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("addstatus", "Error writing document", e);
                    }
                });
    }


    public static void openQr(Boolean isQRMT) {
        Map<String, Object> vending = new HashMap<>();
//        vending.put("isMaintenance", isMaintenance);
        vending.put("isQRMT", isQRMT);

        firestoreDB.collection("m_outlet").document(nomorVending)
                .update(vending)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("addstatus", "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("addstatus", "Error writing document", e);
                    }
                });
    }



}
