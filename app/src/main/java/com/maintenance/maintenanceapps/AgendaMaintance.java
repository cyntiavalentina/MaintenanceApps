package com.maintenance.maintenanceapps;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.maintenance.maintenanceapps.Adapter.AgendaAdapter;
import com.maintenance.maintenanceapps.Adapter.VendingAdapter;
import com.maintenance.maintenanceapps.Model.User;
import com.maintenance.maintenanceapps.Model.UserLocalStore;
import com.maintenance.maintenanceapps.R;
import com.maintenance.maintenanceapps.Service.MyService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AgendaMaintance extends AppCompatActivity{
    private RecyclerView mRecycleView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private UserLocalStore userLocalStore;
    private static User curUser;
    private static Context context;
    private int flag = 0;

    AlertDialog.Builder builder;

    List<AgendaItem> agendaitem = new ArrayList<>();
    private static AgendaAdapter agendaAdapter;
    private JSONArray vendings = new JSONArray();
    public static String url = BuildConfig.BASE_URL+"getListAgenda.php";
    private static final int PERMISSION_REQUEST_CODE = 1000;
    private FloatingActionButton fab;

    public SwipeRefreshLayout swipeRefreshLayout;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agenda_maintance);

        Toolbar mToolbar = findViewById(R.id.tbAgendaMaintanance);
        //mToolbar.setTitle("Maintenance Apps");
        //mToolbar.setLogo(R.drawable.icon_ungu);
        setSupportActionBar(mToolbar);
        builder = new AlertDialog.Builder(this);

        swipeRefreshLayout = findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                showListAgenda();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        userLocalStore = new UserLocalStore(getApplicationContext());
        curUser =userLocalStore.getLoggedInUser();
        context = AgendaMaintance.this;



        mRecycleView = findViewById(R.id.agendaRV);
        mRecycleView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecycleView.setLayoutManager(mLayoutManager);



        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AgendaMaintance.this, DetailCapture.class);
                startActivity(i);
            }
        });

//        if(curUser.roleID == 0 || curUser.roleID == 4){
//            fab.setVisibility(View.VISIBLE);
//        }else{
//            fab.setVisibility(View.GONE);
//        }

        verifyPermission();
        getUser();
        showListAgenda();
        swipeRefreshLayout.setRefreshing(false);

        Intent stickyService = new Intent(this, MyService.class);
        startService(stickyService);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.logout, menu);
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==R.id.logout){
            AlertDialog.Builder var_dialog = new AlertDialog.Builder(this);
            var_dialog.setMessage("Apakah Anda yakin ingin keluar dari Aplikasi?");
            var_dialog.setNegativeButton("TIDAK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            var_dialog.setPositiveButton("YA", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    userLocalStore.setUserLoggedIn(false);
                    userLocalStore.clearUserData();
                    startActivity(new Intent(AgendaMaintance.this, MainActivity.class));
                }
            });
            var_dialog.show();

        }

        return true;
    }

    public void getUser(){
        userLocalStore = new UserLocalStore(this);
        if(!userLocalStore.getUserLoggedIn()){
            Intent i = new Intent(AgendaMaintance.this, MainActivity.class);
            startActivity(i);
            swipeRefreshLayout.setRefreshing(true);
            finish();
        }
        curUser =userLocalStore.getLoggedInUser();
        Log.d("aaaa", String.valueOf(curUser.userID));
    }

    public void showListAgenda(){
        final JsonObjectRequest request = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    vendings = response.getJSONArray("result");
                    agendaitem.clear();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                for (int i = 0; i < vendings.length(); i++) {
                    try {
                        JSONObject obj = vendings.getJSONObject(i);
                        agendaitem.add(new AgendaItem(
                                        obj.getInt("PVM_UrgencyNumber"),
                                        obj.getString("PVM_Problem"),
                                        obj.getString("Out_Name"),
                                        obj.getString("TH_FinishYN"),
                                        obj.getString("TH_OutCode"),
                                        obj.getString("TH_FinishDate"),
                                        obj.getString("TH_TransactionID"),
                                        obj.getString("TH_OccurredDate"),
                                        obj.getString("TH_DueDate"),
                                        obj.getString("TH_ProblemDesc"),
                                        obj.getString("PVM_UrgencyStatus"),
                                        obj.getInt("TH_Rak"),
                                        obj.getString("TH_Motor"),
                                        obj.getInt("TH_ProblemID"),
                                        obj.getString("TH_TransactionID")

                                )
                        );
                        Log.d("qwert", obj.getString("TH_FinishYN"));
                        Log.d("hai", obj.toString());

                    } catch (JSONException e) {

                        e.printStackTrace();
                    }

                }
                agendaAdapter = new AgendaAdapter(context, agendaitem);
                mRecycleView.setAdapter(agendaAdapter);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AgendaMaintance.this, "Maaf sedang ada Gangguan", Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }


    public void verifyPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{
                        Manifest.permission.CAMERA
                }, PERMISSION_REQUEST_CODE);
            }
    }

 @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(this, "permission granted", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "permission denied", Toast.LENGTH_SHORT).show();
                }
            }
            break;
        }
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder var_dialog = new AlertDialog.Builder(this);
        var_dialog.setMessage("Apakah Anda yakin ingin keluar dari Aplikasi?");
        var_dialog.setNegativeButton("TIDAK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        var_dialog.setPositiveButton("YA", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                userLocalStore.setUserLoggedIn(false);
                userLocalStore.clearUserData();
                startActivity(new Intent(AgendaMaintance.this, MainActivity.class));
            }
        });
        var_dialog.show();
    }


}
