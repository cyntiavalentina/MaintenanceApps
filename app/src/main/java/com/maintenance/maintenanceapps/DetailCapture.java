package com.maintenance.maintenanceapps;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DetailCapture extends AppCompatActivity {
    private Toolbar mToolbar;
    Spinner spinner, spinRak, spinMotor, spinMasalah;
    String url = BuildConfig.BASE_URL;
    ArrayAdapter<String> vendingAdapter;
    ArrayAdapter<String> problemAdapter;
    ArrayAdapter<String> rakAdapter;
    ArrayAdapter<String> listMotor;
    List<String> listVending = new ArrayList<>();
    List<String> listMasalah = new ArrayList<>();
    List<String> listRak = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_capture);

        mToolbar = findViewById(R.id.tbDetail);
        mToolbar.setTitle("DETAIL");

        spinner = findViewById(R.id.spinner);
        spinRak = findViewById(R.id.spinRak);
        spinMotor = findViewById(R.id.spinMotor);
        spinMasalah = findViewById(R.id.spinMasalah);



        vendingAdapter = new ArrayAdapter<>(DetailCapture.this, android.R.layout.simple_spinner_dropdown_item, listVending);
        vendingAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(vendingAdapter);
        showListVending();
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String vendingTxt = spinner.getSelectedItem().toString();
                Log.d("aaa12", vendingTxt);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
//        String vendingTxt = spinner.getSelectedItem().toString();
//        Log.d("aaa12", vendingTxt);

        problemAdapter = new ArrayAdapter<>(DetailCapture.this, android.R.layout.simple_spinner_dropdown_item, listMasalah);
        problemAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinMasalah.setAdapter(problemAdapter);
        showListMasalah();

        rakAdapter = new ArrayAdapter<>(DetailCapture.this, android.R.layout.simple_spinner_dropdown_item, listRak);
        rakAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinRak.setAdapter(rakAdapter);
        //showListRak();



    }

    public void showListVending(){
        final JsonObjectRequest request = new JsonObjectRequest(url+"showVending.php", null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    listVending.clear();
                    JSONArray vendings = response.getJSONArray("result");
                    for (int i = 0; i < vendings.length(); i++) {
                        try {
                            JSONObject vending = vendings.getJSONObject(i);
                            listVending.add(vending.getString("Out_Name"));
                        } catch (JSONException e) {
                            Log.d("error_",e+"");
                            e.printStackTrace();
                        }
                    }
                    vendingAdapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("error_",e+"");
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(DetailCapture.this, error+"", Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);

    }

    public void showListMasalah(){
        final JsonObjectRequest request = new JsonObjectRequest(url+"showProblem.php", null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    listMasalah.clear();
                    JSONArray vendings = response.getJSONArray("result");
                    for (int i = 0; i < vendings.length(); i++) {
                        try {
                            JSONObject vending = vendings.getJSONObject(i);
                            listMasalah.add(vending.getString("PVM_Problem"));
                        } catch (JSONException e) {
                            Log.d("error_",e+"");
                            e.printStackTrace();
                        }
                    }
                    problemAdapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("error_",e+"");
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(DetailCapture.this, error+"", Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }

    public void showListRak(){
        final JsonObjectRequest request = new JsonObjectRequest(url+"showRakMaintenance.php", null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    listRak.clear();

                    JSONArray vendings = response.getJSONArray("result");
                    for (int i = 0; i < vendings.length(); i++) {
                        try {
                            JSONObject vending = vendings.getJSONObject(i);
                            listRak.add(vending.getString("RAK"));
                        } catch (JSONException e) {
                            Log.d("error_",e+"");
                            e.printStackTrace();
                        }
                    }
                    rakAdapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("error_",e+"");
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(DetailCapture.this, error+"", Toast.LENGTH_LONG).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);

    }
}
