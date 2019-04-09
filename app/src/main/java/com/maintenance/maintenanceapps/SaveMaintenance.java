package com.maintenance.maintenanceapps;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.support.v7.widget.Toolbar;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.maintenance.maintenanceapps.Adapter.VendingAdapter;
import com.maintenance.maintenanceapps.Model.User;
import com.maintenance.maintenanceapps.Model.UserLocalStore;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.annotation.Nullable;

public class SaveMaintenance extends AppCompatActivity {
    Button prosesBtn, ya, belum, close;
    Toolbar mToolbar;
    EditText notes;
    String url = BuildConfig.BASE_URL+"saveMaintenance.php";
    String status= "";
    String Trans, door;
    String doorStatus;
    String outletid;
    Button tutupBtn, btn_simpan, btn_batal;
    Dialog closedDoor;
    AlertDialog dialog;
    static int vendingOpenDoor;
    CountDownTimer countDownTimerDialog;
    private Boolean isTimerOn = false;
    private Boolean isProgressDialogClose = false;
    ProgressDialog progressDialog;

    private static UserLocalStore userLocalStore;
    static User currentUser;
    private FirebaseFirestore firestoreDB;
    private ListenerRegistration firestoreListener;
    AlertDialog dialogg;
    AlertDialog.Builder builder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_maintenance);

        mToolbar = findViewById(R.id.saveToolbar);
        //mToolbar.setTitle("Maintenance Apps");
        //mToolbar.setLogo(R.drawable.icon_ungu);
        Log.e("tool", mToolbar + "abc");

        ya = findViewById(R.id.yaBtn);
        belum = findViewById(R.id.belumBtn);
        notes = findViewById(R.id.notesMaintenance);

        firestoreDB = FirebaseFirestore.getInstance();
        outletid = getIntent().getStringExtra("KodeOutlet");
        Trans = getIntent().getStringExtra("KodeTrans");
        door = getIntent().getStringExtra("DoorYN");
        //Log.d("abc", Trans+"");
        builder = new AlertDialog.Builder(this);


        userLocalStore = new UserLocalStore(getApplicationContext());
        currentUser = userLocalStore.getLoggedInUser();

        progressDialog = new ProgressDialog(SaveMaintenance.this);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Mempersiapkan");
        progressDialog.setMessage("Mohon Menunggu...");

        closedDoor = new Dialog(this);
        closedDoor.requestWindowFeature(Window.FEATURE_NO_TITLE);
        closedDoor.setContentView(R.layout.dialong_closed_vending_door);
        closedDoor.setCanceledOnTouchOutside(false);
        closedDoor.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        ya.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ya.setBackgroundColor(Color.parseColor("#32CD32"));
                ya.setEnabled(false);
                belum.setBackgroundColor(Color.parseColor("#DCDCDC"));
                belum.setEnabled(true);
                prosesBtn.setVisibility(View.VISIBLE);
                status = "Y";


            }
        });

        belum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                belum.setBackgroundColor(Color.parseColor("#FF0000"));
                belum.setEnabled(false);
                ya.setBackgroundColor(Color.parseColor("#DCDCDC"));
                ya.setEnabled(true);
                prosesBtn.setVisibility(View.VISIBLE);
                status = "N";
            }
        });

        //Test();


//        if(ya.isClickable()){
//            ya.setBackgroundColor(Color.parseColor("#32CD32"));
//        }else if(belum.isClickable()){
//            belum.setBackgroundColor(Color.parseColor("#FF0000"));
//        }

//        if(ya.isPressed()){
//            ya.setBackgroundColor(Color.parseColor("#32CD32"));
//        }else if(belum.isPressed()){
//            belum.setBackgroundColor(Color.parseColor("FF0000"));
//        }

        prosesBtn = findViewById(R.id.prosesBtn);
        //prosesBtn.setVisibility(View.INVISIBLE);


        prosesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (notes.getText().toString().equals("")) {
                    notes.setError("Sihlakan diisi dulu!");
                    ya.setEnabled(false);
                    belum.setEnabled(false);
                } else {
                    ya.setEnabled(true);
                    belum.setEnabled(true);
                    cekStatusPintu();
                }


            }
        });

    }

    public void confirmKerjaan(String note, String YN, String ID, String user, String door) {
        JSONObject object = new JSONObject();
        try {
            JSONArray array = new JSONArray();
            JSONObject objdetail = new JSONObject();

            objdetail.put("note", note);
            objdetail.put("finish", YN);
            objdetail.put("transaction", ID);
            objdetail.put("user",user);
            objdetail.put("door", door);

            array.put(objdetail);
            object.put("data", array);
            Log.d("Objput", object + "");

        } catch (Exception e) {
            e.printStackTrace();
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, object,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        JSONArray data;
                        try {
                            if (response.getString("result").equals("OK")) {
                                if(isProgressDialogClose) {
                                    progressDialog.cancel();
                                    countDownTimerDialog.cancel();
                                }
                                Toast.makeText(SaveMaintenance.this, "DONE", Toast.LENGTH_SHORT).show();
                                Intent b = new Intent(getApplicationContext(), AgendaMaintance.class);
                                b.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                b.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(b);
                            }
                            else
                            {
                                Toast.makeText(SaveMaintenance.this, "Gagal", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.d("errorrespon", e + "");                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("errorrespon", error + "");
                    }
                }
        );
        RequestQueue requestQueue = Volley.newRequestQueue(SaveMaintenance.this);
        requestQueue.add(request);
    }

    public void closeTheDoor() {
        Log.d("HJKL", BuildConfig.BASE_URL + "closeTheDoor.php?outcode=" + outletid);
        JsonObjectRequest req = new JsonObjectRequest(BuildConfig.BASE_URL + "closeTheDoor.php?outcode=" + outletid, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                String status = null;

                try {
                    status = response.getString("status");
                    if (status.equals("OK")) {

                    } else {

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(SaveMaintenance.this);
        requestQueue.add(req);
        Log.d("urlreq", req.toString());
    }

    public void listenerCloseDoor(final ProsesMaintenace.MyCallBack myCallBack) {
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

    public void readVendingCloseTimer(final ProsesMaintenace.MyCallBack myCallBack) {
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
    public void checkDoorStatusVM(){
        final JsonObjectRequest req = new JsonObjectRequest(url + "checkDoorStatusVM.php?outcode=" + outletid, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    //stts = response.getJSONArray("Result");
                    if(response.getString("Result").equals("B")){
                        VendingAdapter.openMaintenance( false);
                        progressDialog.cancel();
                        if (isTimerOn) {
                            countDownTimerDialog.cancel();
                            isTimerOn = false;
                        }
                        finish();
                        firestoreListener.remove();
                    }
                    else{
                        progressDialog.cancel();
                        closedDoor.show();
                        if (isTimerOn) {
                            countDownTimerDialog.cancel();
                            isTimerOn = false;
                        }
                        close = closedDoor.findViewById(R.id.btn_ok_closedVending);
                            close.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    closedDoor.cancel();
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
                progressDialog.cancel();
                Log.d("err123", "onErrorResponse: " + error.toString());
                Toast.makeText(SaveMaintenance.this, "Gangguan", Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(req);
    }

    public void TimerForDialogDoorStatus() {
        final long finishtime = vendingOpenDoor;
        Log.d("timerfordialog", vendingOpenDoor + "");

        countDownTimerDialog = new CountDownTimer(finishtime * 1000, 1000) {
            @Override
            public void onTick(long l) {
                Log.d("ontick", l + "");
            }

            @Override
            public void onFinish() {
                checkDoorStatusVM();
             Log.d("JalanJalankan", "TimerForDialogDoorStatus:JalanCuy");

            }
        }.start();
        isTimerOn = true;

    }


    public void cekStatusPintu(){
        firestoreListener = firestoreDB.document("m_outlet/" + outletid)
                .addSnapshotListener(SaveMaintenance.this, new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                        doorStatus = documentSnapshot.getString("doorStatus");
                        if(doorStatus.equals("CLOSED")){
                            final Dialog dialogsimpan = new Dialog(SaveMaintenance.this);
                            dialogsimpan.setContentView(R.layout.dialog_simpan);
                            dialogsimpan.setCancelable(false);
                            dialogsimpan.show();

                            Button btn_simpan = dialogsimpan.findViewById(R.id.btn_simpan);
                            Button btn_batal = dialogsimpan.findViewById(R.id.btn_batal);

                            btn_simpan.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    confirmKerjaan(notes.getText().toString(),status, Trans, String.valueOf(currentUser.getUserID()), door);
                                    dialogsimpan.cancel();
                                }
                            });
                            btn_batal.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialogsimpan.cancel();
                                }
                            });

                        }else if(doorStatus.equals("OPENED")){
                            final Dialog dialogsimpan = new Dialog(SaveMaintenance.this);
                            dialogsimpan.setContentView(R.layout.dialog_simpan);
                            dialogsimpan.setCancelable(false);
                            dialogsimpan.show();

                            Button btn_simpan = dialogsimpan.findViewById(R.id.btn_simpan);
                            Button btn_batal = dialogsimpan.findViewById(R.id.btn_batal);

                            btn_simpan.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialogsimpan.cancel();
                                    closeTheDoor();
                                    firestoreListener.remove();
                                    listenerCloseDoor(new ProsesMaintenace.MyCallBack() {
                                        @Override
                                        public void onCallback(int vendingOpenDoor) {
                                            firestoreListener = firestoreDB.document("m_outlet/" + outletid)
                                                    .addSnapshotListener(SaveMaintenance.this, new EventListener<DocumentSnapshot>() {
                                                        @Override
                                                        public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                                                            doorStatus = documentSnapshot.getString("doorStatus");
                                                            Log.d("teeeeee","masuk_kesini "+doorStatus);
                                                            if (doorStatus.equals("CLOSED")) {
                                                                //Toast.makeText(SaveMaintenance.this, "haaaa", Toast.LENGTH_SHORT).show();
                                                                firestoreListener.remove();
                                                                confirmKerjaan(notes.getText().toString(),status, Trans, String.valueOf(currentUser.getUserID()), door);
                                                            }
                                                            else if (String.valueOf(doorStatus).equals("OPENED")) {
                                                                closedDoor.show();
                                                                    close = closedDoor.findViewById(R.id.btn_ok_closedVending);
                                                                    close.setOnClickListener(new View.OnClickListener() {
                                                                        @Override
                                                                        public void onClick(View v) {
                                                                            closedDoor.cancel();
                                                                            progressDialog.show();
                                                                            isProgressDialogClose=true;
                                                                        }
                                                                    });

                                                                if (String.valueOf(documentSnapshot.getBoolean("isQRMT")).equals("false")) {
                                                                    readVendingCloseTimer(new ProsesMaintenace.MyCallBack() {
                                                                        @Override
                                                                        public void onCallback(int vendingOpenDoor) {
                                                                            TimerForDialogDoorStatus();

                                                                        }
                                                                    });
                                                                }
                                                            }
                                                        }
                                                    });
                                        }
                                    });

                                }
                            });
                            btn_batal.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialogsimpan.cancel();
                                    VendingAdapter.openQr(false);
                                }
                            });
                        }
                    }
                });
//        firestoreListener = firestoreDB.document("m_outlet/" + outletid)
//                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
//                    @Override
//                    public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
//                        doorStatus = documentSnapshot.getString("doorStatus");
//                        Log.d("doorstatus", doorStatus);
////                        doorStatus = String.valueOf(documentSnapshot.getString("doorStatus"));
//                        if(String.valueOf(doorStatus).equals("OPENED")){
//                            VendingAdapter.openMaintenance(false);
//
//                            closedDoor.show();
//                            close = closedDoor.findViewById(R.id.btn_ok_closedVending);
//                            close.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    closedDoor.cancel();
//                                }
//                            });
//                        }else if(String.valueOf(doorStatus).equals("CLOSED")){
//                            final Dialog dialogsimpan = new Dialog(SaveMaintenance.this);
//                            dialogsimpan.setContentView(R.layout.dialog_simpan);
//                            dialogsimpan.show();
//
//                            Button btn_simpan = dialogsimpan.findViewById(R.id.btn_simpan);
//                            Button btn_batal = dialogsimpan.findViewById(R.id.btn_batal);
//
//                            btn_simpan.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    confirmKerjaan(notes.getText().toString(),status, Trans, String.valueOf(currentUser.userID), door);
//                                    dialogsimpan.cancel();
//                                    finish();
//
//                                }
//                            });
//                            btn_batal.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    dialogsimpan.cancel();
//                                }
//                            });
//                        }
//
//                    }
//
//                });

    }


    public void cekMaintenance(){
        if(String.valueOf(doorStatus).equals("OPENED")){
            tutupBtn.setVisibility(View.VISIBLE);
            tutupBtn.setEnabled(true);
            Log.d("test1", "lalalallaa");
            //firestoreListener.remove();
        }else if(String.valueOf(doorStatus).equals("CLOSED")){
            tutupBtn.setEnabled(false);
            tutupBtn.setBackgroundColor(Color.parseColor("#989898"));
            //tutupBtn.setVisibility(View.INVISIBLE);
            Log.d("test1", "kalalalaalf");
        }
    }

//    public void Test(){
//        firestoreListener = firestoreDB.document("m_outlet/"+outletid)
//                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
//                    @Override
//                    public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
//                        doorStatus = String.valueOf(documentSnapshot.getString("doorStatus"));
//                        Log.d("dddddd", doorStatus);
//                        if(String.valueOf(doorStatus).equals("OPENED")){
//                            tutupBtn.setVisibility(View.VISIBLE);
//                            tutupBtn.setEnabled(true);
//                            Log.d("awdd", "masuk");
//                        }else if(String.valueOf(doorStatus).equals("CLOSED")){
//                            tutupBtn.setBackgroundColor(Color.parseColor("#989898"));
//                            Log.d("awdd", "masuk11");
//                        }
//                    }
//                });
//    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}
