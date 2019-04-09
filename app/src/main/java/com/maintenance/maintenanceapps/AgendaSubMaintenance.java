package com.maintenance.maintenanceapps;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.maintenance.maintenanceapps.Adapter.DescriptionAdapter;
import com.maintenance.maintenanceapps.Adapter.VendingAdapter;
import com.maintenance.maintenanceapps.Model.User;
import com.maintenance.maintenanceapps.Model.UserLocalStore;
import com.maintenance.maintenanceapps.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AgendaSubMaintenance extends AppCompatActivity {
    TextView textView, notifTxt;
    Context context;
    Button kerjaBtn;
    String kata;
    private UserLocalStore userLocalStore;
    private static User curUser;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private JSONArray descs = new JSONArray();
    public static String url = BuildConfig.BASE_URL+"getHistoryAgenda.php?transaction=";
    ArrayList<DescriptionList> descList = new ArrayList<>();
    AgendaItem agendaItem;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agenda_sub_maintenance);

        Toolbar mToolbar = findViewById(R.id.tbAgendaSub);
        mToolbar.setTitle("Detail");

        context = getApplicationContext();

        agendaItem = getIntent().getBundleExtra("Item_Detail").getParcelable("Bundle_Item");


        userLocalStore = new UserLocalStore(getApplicationContext());
        curUser =userLocalStore.getLoggedInUser();
        userLocalStore = new UserLocalStore(this);
        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);

        mRecyclerView.setLayoutManager(mLayoutManager);
        Log.d("cder",agendaItem.getImageResource()+"");
        notifTxt = findViewById(R.id.notifTxt);
        if(agendaItem.getTglText().equals("Y")){
            notifTxt.setVisibility(View.VISIBLE);
            notifTxt.setText("Selesai diperbaiki");
        }

        textView = findViewById(R.id.viewText);
        textView.setMovementMethod(new ScrollingMovementMethod());
        kata = "<b>Terjadi masalah Vending dengan rincian sebagai berikut: </b>";

        if(agendaItem.getProblemId()==2 ){
            textView.setText((Html.fromHtml(kata)) + "\n"+"\n"+
                            "Masalah    :   "+ agendaItem.getAgendaText() + "\n" +
                            "Lokasi        :   "+ agendaItem.getAgendaText2() + "\n" + "\n" +
                            "Deskripsi   : "+ "\n" +agendaItem.getProblem() + "\n" +
                    "Rak    : "+ agendaItem.getRak() + "\n" +"\n"+ "\n" +
                            "Tgl Terjadi     : "+ agendaItem.getOccuredate().substring(0,11) + "\n" +
                            "Batas Waktu : "+ agendaItem.getDuedate().substring(0,11) + "\n"
            );

        }else if(agendaItem.getRak()==0){
            textView.setText((Html.fromHtml(kata)) + "\n" + "\n" +
                            "Masalah    :   " + agendaItem.getAgendaText() + "\n" +
                            "Lokasi        :   " + agendaItem.getAgendaText2() + "\n" + "\n" +
                            "Deskripsi   : " + "\n" + agendaItem.getProblem() + "\n" + "\n" + "\n" +
                            "Tgl Terjadi     : " + agendaItem.getOccuredate().substring(0, 11) + "\n" +
                            "Batas Waktu : " + agendaItem.getDuedate().substring(0, 11) + "\n"
//                        + "Status      : "+ agendaItem.getStatus()
            );

        }else {

            textView.setText((Html.fromHtml(kata)) + "\n" + "\n" +
                            "Masalah    :   " + agendaItem.getAgendaText() + "\n" +
                            "Lokasi        :   " + agendaItem.getAgendaText2() + "\n" + "\n" +
                            "Deskripsi   : " + "\n" + agendaItem.getProblem() + "\n" +
                            "Rak     : "+ agendaItem.getRak() + "\n" +
                            "Motor  : "+ agendaItem.getMotor() +"\n" + "\n" + "\n" +
                            "Tgl Terjadi     : " + agendaItem.getOccuredate().substring(0, 11) + "\n" +
                            "Batas Waktu : " + agendaItem.getDuedate().substring(0, 11) + "\n"
//                        + "Status      : "+ agendaItem.getStatus()
            );
        }

        Log.d("aaa", agendaItem.getTransaksi());


//        descList.add(new DescriptionList("19 Maret 2019","Tidak ada Masalah","Rudi"));
//        descList.add(new DescriptionList("20 Maret 2019","Tidak ada Masalah","Rudi"));
//        descList.add(new DescriptionList("21 Maret 2019","Tidak ada Masalah","Rudi"));


        kerjaBtn = findViewById(R.id.kerjaBtn);
        if(agendaItem.getTglText().equals("N")) {
            kerjaBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("keluarga", agendaItem.getTransaksi());
                    VendingAdapter.openQr(true);
                    Intent i = new Intent(AgendaSubMaintenance.this, QRCode.class);
                    i.putExtra("KodeOutlet", agendaItem.getOutCode());
                    i.putExtra("KodeTrans", agendaItem.getTransaksiId());
                    Log.d("transss", agendaItem.getTransaksiId());
                    startActivity(i);
                    Log.d("tesmasuk", "masukQr");

                }
            });
        }else{
            kerjaBtn.setVisibility(View.INVISIBLE);
        }

        getListDescription();

    }

    public void getListDescription(){
        Log.d("agenda",agendaItem.getTransaksiId()+"");
        final JsonObjectRequest req = new JsonObjectRequest(url + agendaItem.getTransaksiId() , null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
//                Log.d("hasil", url+agendaItem.getTransaksiId());
                try {
                    descs = response.getJSONArray("result");
                    descList.clear();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                for (int i = 0; i < descs.length(); i++) {
                    try {
                        JSONObject obj = descs.getJSONObject(i);
                        descList.add(new DescriptionList(
                                obj.getString("TD_HandledDate"),
                                obj.getString("TD_Notes"),
                                obj.getString("AU_FullName")
                        ));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                mAdapter = new DescriptionAdapter(descList);
                mRecyclerView.addItemDecoration(
                        new DividerItemDecoration(getApplicationContext(), R.drawable.divider));
                mRecyclerView.setAdapter(mAdapter);
                Log.d("test1", descList+"");
                Log.d("test2", mAdapter+"");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
    });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(req);
    }
}
