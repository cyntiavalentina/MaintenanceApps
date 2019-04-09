package com.maintenance.maintenanceapps.Model;

import android.app.ProgressDialog;
import android.content.Context;

public class DialogProgress {

    ProgressDialog progressDialog;
    Context context;

    public DialogProgress(Context context) {
        this.context = context;
        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Processing");
        progressDialog.setMessage("Please wait...");
    }

    public void showDialog() {
        progressDialog.show();
    }

    public void hideDialog() {
        progressDialog.dismiss();
    }
}