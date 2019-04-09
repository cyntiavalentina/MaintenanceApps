package com.maintenance.maintenanceapps;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;
import com.maintenance.maintenanceapps.Adapter.VendingAdapter;
import com.maintenance.maintenanceapps.Model.User;
import com.maintenance.maintenanceapps.Model.UserLocalStore;
import com.maintenance.maintenanceapps.R;
import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class QRCode extends AppCompatActivity implements ZXingScannerView.ResultHandler{
    private ZXingScannerView scannerView;
    TextView barcodeTxt;

    String Outcode;
    String TransNum;
    Dialog wrongVending;
    Button wrongBtn;
    ProgressDialog progressDialog;
    private Boolean isTimerOn = false;
    CountDownTimer countDownTimer;

    private UserLocalStore userLocalStore;
    private static User user;
    private static Context context;
    FirebaseFirestore firestoreDB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);
        scannerView = new ZXingScannerView(this);
        setContentView(scannerView);

        userLocalStore = new UserLocalStore(getApplicationContext());
        user = userLocalStore.getLoggedInUser();
        context = QRCode.this;

        barcodeTxt = findViewById(R.id.barcodeTxt);

        Outcode = getIntent().getStringExtra("KodeOutlet");
        TransNum = getIntent().getStringExtra("KodeTrans");
        Log.d("abc", TransNum);


        Toast.makeText(this, "Sihlakan scan QR Code disini", Toast.LENGTH_LONG).show();

        //vendingID = getIntent().getStringExtra("Out_code");

        wrongVending = new Dialog(this);
        wrongVending.requestWindowFeature(Window.FEATURE_NO_TITLE);
        wrongVending.setContentView(R.layout.dialog_wrong_vending);
        wrongVending.setCanceledOnTouchOutside(false);
        wrongVending.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        firestoreDB = FirebaseFirestore.getInstance();
//        Intent i = new Intent(QRCode.this, ProsesMaintenace.class);
//        i.putExtra("KodeOutlet", Outcode);
//        i.putExtra("KodeTrans", TransNum );
//        startActivity(i);
    }


    @Override
    protected void onResume() {
        super.onResume();
        scannerView.setResultHandler(this);
        scannerView.startCamera();

    }

    @Override
    protected void onPause() {
        super.onPause();
        scannerView.stopCamera();
    }

    @Override
    public void handleResult(Result rawResult) {
        //Toast.makeText(this, rawResult.getText(), Toast.LENGTH_SHORT).show();
        //progressDialog.show();
        if(Outcode.equals(rawResult.getText())){
            Intent i = new Intent(QRCode.this, ProsesMaintenace.class);
            i.putExtra("KodeOutlet", Outcode);
            i.putExtra("KodeTrans", TransNum );
            startActivity(i);
        }else{
            VendingAdapter.openQr(false);
            wrongVending.show();
            wrongBtn = wrongVending.findViewById(R.id.btn_ok_salahVending);
            wrongBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(QRCode.this, AgendaMaintance.class);
                    startActivity(i);
                }
            });

        }
        //progressDialog.cancel();
        if (isTimerOn) {
            countDownTimer.cancel();
            isTimerOn = false;
        }
        //scannerView.resumeCameraPreview(this);
    }

    @Override
    public void onBackPressed(){
        VendingAdapter.openMaintenance(false);
        if(isTimerOn){
            countDownTimer.cancel();
            isTimerOn = false;
        }
        super.onBackPressed();
    }



}