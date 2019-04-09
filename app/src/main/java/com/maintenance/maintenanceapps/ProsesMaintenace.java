package com.maintenance.maintenanceapps;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.maintenance.maintenanceapps.Adapter.VendingAdapter;
import com.maintenance.maintenanceapps.Model.DialogProgress;
import com.maintenance.maintenanceapps.Model.User;
import com.maintenance.maintenanceapps.Model.UserLocalStore;
import com.maintenance.maintenanceapps.R;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ProsesMaintenace extends AppCompatActivity {
    private UserLocalStore userLocalStore;
    private static User currUser;
    private static Context context;
    private SwipeRefreshLayout swipeRefreshLayout;
    private String outletid, Transaction;
    Button selesaiBtn;
    public static String VENDING_STATUS = "Vending_status";
    public static String NUM_DO = "Num_DO";
    public static String ALLOW_CONFIRM = "Allow_Confirm";

    CountDownTimer countDownTimer;
    private Boolean isTimerOn = false;
    private FirebaseFirestore firestoreDB;
    private ProgressDialog progressDialog, progressDialogtutupPintu;
    Dialog wrongVending, failedOpenDoor, successOpenDoor;
    private static RequestQueue queue;
    static String userID;
    private JSONArray status = new JSONArray();
    static int vendingOpenDoor;
    String doorStatus;
    String doorYN = "N";
    Button btnok, btnsukses;
    Boolean isMaintenance;
    private ListenerRegistration firestoreListener;
    private String tempVendingStatus = "nothing";
    private String tempAllowConfirmYN = "yn";
    private JSONArray maintenance = new JSONArray();
    String urldate = "http://vending.apodoc.id/maintenance/php/development/updateDoorStatus.php";
    String url = BuildConfig.BASE_URL;
    Button bukaPintu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proses_maintenance);
        outletid = getIntent().getStringExtra("KodeOutlet");
        Transaction = getIntent().getStringExtra("KodeTrans");
        Log.d("abc", outletid);
        VendingAdapter.openDoor("CLOSED");
        firestoreDB = FirebaseFirestore.getInstance();

        bukaPintu = findViewById(R.id.bukaPintu);
        bukaPintu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //bukaPintu.setBackgroundColor(Color.parseColor("#989898"));

                cekStatusPintu();
                Log.d("testoutlet", outletid);

            }
        });

        selesaiBtn = findViewById(R.id.selesaiBtn);
        selesaiBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), SaveMaintenance.class);
                i.putExtra("KodeTrans", Transaction);
                i.putExtra("KodeOutlet", outletid);
                i.putExtra("DoorYN",doorYN);
                Log.d("hehe", doorYN);
                startActivity(i);
            }
        });



        failedOpenDoor = new Dialog(this);
        failedOpenDoor.requestWindowFeature(Window.FEATURE_NO_TITLE);
        failedOpenDoor.setContentView(R.layout.dialog_failed_open_door);
        failedOpenDoor.setCanceledOnTouchOutside(false);
        failedOpenDoor.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        successOpenDoor = new Dialog(this);
        successOpenDoor.requestWindowFeature(Window.FEATURE_NO_TITLE);
        successOpenDoor.setContentView(R.layout.dialog_success_open_door);
        successOpenDoor.setCanceledOnTouchOutside(false);
        successOpenDoor.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//
        progressDialog = new ProgressDialog(ProsesMaintenace.this);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Mempersiapkan");
        progressDialog.setMessage("Mohon Menunggu...");

        VendingAdapter.openQr(false);
        VendingAdapter.openMaintenance(false);


    }


//    public void cekStatusPintu(){
//        firestoreListener = firestoreDB.document("m_outlet/" + outletid)
//                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
//                    @Override
//                    public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
//                        doorStatus = String.valueOf(documentSnapshot.getString("doorStatus"));
//                        isMaintenance = documentSnapshot.getBoolean("isMaintenance");
//
//                        VendingAdapter.openMaintenance("OPENED", true);
//                        Toast.makeText(ProsesMaintenace.this, "Pintu Terbuka!!", Toast.LENGTH_SHORT).show();
//                        cekMaintenance();
//                    }
//                });
//    }


        public interface MyCallBack {
            void onCallback(int vendingOpenDoor);
        }
        //untuk hitung waktu
        public void readVendingOpenTimer(final MyCallBack myCallBack) {
            final DocumentReference documentReference = firestoreDB.collection("configuration").document("timer");
            documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        final DocumentSnapshot document = task.getResult();

                        vendingOpenDoor = document.getLong("VendingOpenDoor").intValue();
                        Log.d("vendingOpenDoor", "vendingOpenDoor :" + vendingOpenDoor);
                        myCallBack.onCallback(vendingOpenDoor);
                    }
                }
            });
        }
    //mengecek status pintu open=false & closed=true (Dari firebasenya)
    public void cekStatusPintu(){
        progressDialog.show();
        Log.d("dwaaaa", outletid+"");
        firestoreListener = firestoreDB.document("m_outlet/" + outletid)
                .addSnapshotListener(ProsesMaintenace.this,new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                        String doorstatus1 = documentSnapshot.getString("doorStatus");
                        doorStatus = String.valueOf(documentSnapshot.getString("doorStatus"));

                        Log.d("dwaaaaaaaa", doorstatus1+"");
                        Log.d("dwaaaaaaaa", doorStatus+"");

                        if(String.valueOf(doorStatus).equals("OPENED")){
                            VendingAdapter.openMaintenance(false);
//                            Intent i = new Intent(ProsesMaintenace.this, SaveMaintenance.class);
//                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                            i.putExtra("KodeOutlet", outletid);

                            progressDialog.cancel();
                            successOpenDoor.show();

                            if (isTimerOn) {
                                countDownTimer.cancel();
                                isTimerOn = false;
                            }

                            btnsukses = successOpenDoor.findViewById(R.id.btn_succesDoor);
                            btnsukses.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    successOpenDoor.cancel();
                                    bukaPintu.setEnabled(false);
                                    bukaPintu.setBackgroundColor(Color.parseColor("#989898"));
                                    doorYN = "Y";

                                }
                            });

                            //finish();
                            //firestoreListener.remove();
                        }else if(String.valueOf(doorStatus).equals("CLOSED")){
                            //failedOpenDoor.show();
                            isMaintenance = documentSnapshot.getBoolean("isQRMT").equals("false");
                            readVendingOpenTimer(new MyCallBack() {
                                @Override
                                public void onCallback(int vendingOpenDoor) {
                                    TimerForDialogDoorStatus();
                                    //ubahStatusPintu();
                                }
                            });
                        }

//                        VendingAdapter.openMaintenance("OPENED", true);
//                        Toast.makeText(ProsesMaintenace.this, "Pintu Terbuka!!", Toast.LENGTH_SHORT).show();
//                        cekMaintenance();
                    }
                });
    }

    public void TimerForDialogDoorStatus() {
        final long finishtime = vendingOpenDoor;
        Log.d("timerfordialog", vendingOpenDoor + "");

        countDownTimer = new CountDownTimer(finishtime * 1000, 1000) {
            @Override
            public void onTick(long l) {
                Log.d("ontick", l + "");
            }

            @Override
            public void onFinish() {
                Toast.makeText(ProsesMaintenace.this, "timeout", Toast.LENGTH_SHORT).show();
                checkDoorStatusVM();

            }
        }.start();
        isTimerOn = true;

    }

//    public void cekMaintenance(){
//        if(String.valueOf(doorStatus).equals("OPENED") && isMaintenance== true){
//            //.setEnabled(false);
//            //bukaPintu.setClickable(false);
//            Log.d("test1", "lalalallaa");
//            firestoreListener.remove();
//        }else if(String.valueOf(doorStatus).equals("CLOSED") && isMaintenance== false){
//            //bukaPintu.setEnabled(true);
//            //bukaPintu.setClickable(true);
//            Log.d("test1", "kalalalaalf");
//        }
    //}

//    public void ubahStatusPintu(){
//        JsonObjectRequest request = new JsonObjectRequest(url + "bukaPintu.php?outcode=" + outletid + "&userid=" + userID, null, new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//                String status = null;
//                try {
//                    status = response.getString("status");
//                    if (status.equals("OK")) {
//                        Log.d("ubahstatusPintu(", "OK");
//                        VendingAdapter.openQr(false);
//                        cekStatusPintu();
//                    } else {
//
//                        failedOpenDoor.show();
//                        btnok = failedOpenDoor.findViewById(R.id.btn_ok);
//                        btnok.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                failedOpenDoor.cancel();
//                            }
//                        });
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//            }
//
//    });
//        RequestQueue requestQueue = Volley.newRequestQueue(ProsesMaintenace.this);
//        requestQueue.add(request);
//        Log.d("urlreq", request.toString());
//    }

    public void checkDoorStatusVM(){
        final JsonObjectRequest req = new JsonObjectRequest(url + "checkDoorStatusVM.php?outcode=" + outletid, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.d("hedeked", response.getString("Result"));
                    //stts = response.getJSONArray("Result");
                    if(response.getString("Result").equals("S")){
                       VendingAdapter.openMaintenance( false);
                        successOpenDoor.show();
                        progressDialog.cancel();
                        if (isTimerOn) {
                            countDownTimer.cancel();
                            isTimerOn = false;
                        }
                        btnsukses = successOpenDoor.findViewById(R.id.btn_succesDoor);
                        btnsukses.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                successOpenDoor.cancel();
                            }
                        });
                        firestoreListener.remove();
                    }
                    else{
                        //progressDialog.cancel();

                        if (isTimerOn) {
                            countDownTimer.cancel();
                            isTimerOn = false;
                        }
                        failedOpenDoor.show();
                        btnok = failedOpenDoor.findViewById(R.id.btn_ok);
                        btnok.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                failedOpenDoor.cancel();
                                progressDialog.cancel();
                            }
                        });
                    }
                    //Log.d("hedek", response.getString("Result"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("err123", "onErrorResponse: " + error.toString());
                Toast.makeText(ProsesMaintenace.this, "Gangguan", Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(req);
    }

    @Override
    public void onBackPressed() {
        VendingAdapter.openQr(false);
        if (isTimerOn) {
            countDownTimer.cancel();
            isTimerOn = false;
        }
        super.onBackPressed();
    }

}
