package com.maintenance.maintenanceapps.Service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.maintenance.maintenanceapps.Adapter.VendingAdapter;

public class MyService extends Service {
    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

        @Override
    public void onTaskRemoved(Intent rootIntent) {
        Toast.makeText(this, "onTaskRemoved", Toast.LENGTH_SHORT).show();
        VendingAdapter.openMaintenance(true);
        Log.d("removecoy", "onTaskRemoved: ");
//        super.onTaskRemoved(rootIntent);
        stopSelf();
    }
    @Override
    public void onCreate() {
        Toast.makeText(this, "Invoke background service onCreate method.", Toast.LENGTH_LONG).show();
        super.onCreate();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "Invoke background service onStartCommand method.", Toast.LENGTH_LONG).show();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "Invoke background service onDestroy method.", Toast.LENGTH_LONG).show();
    }

}

