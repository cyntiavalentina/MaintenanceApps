package com.maintenance.maintenanceapps;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.maintenance.maintenanceapps.Model.DialogProgress;
import com.maintenance.maintenanceapps.Model.User;
import com.maintenance.maintenanceapps.Model.UserLocalStore;
import com.maintenance.maintenanceapps.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


// LOGIN

public class MainActivity extends AppCompatActivity {
    public static final String URL = BuildConfig.BASE_URL+"login.php";
    TextView nipLogin, passwordLogin;
    Button btnLogin;
    UserLocalStore userLocalStore;
    public static Activity activity;
    public User curUser;
    AlertDialog dialog;
    AlertDialog.Builder builder;
    DialogProgress dialogProgress;
    private static final String SALT_LOGIN = "MaintenanceApps";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        activity = this;



        nipLogin = findViewById(R.id.nipLogin);
        passwordLogin = findViewById(R.id.passLogin);
        btnLogin = findViewById(R.id.btnLogin);
        userLocalStore = new UserLocalStore(this);
        dialogProgress = new DialogProgress(MainActivity.this);
        builder = new AlertDialog.Builder(this);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = nipLogin.getText().toString().trim();
                String password = passwordLogin.getText().toString().trim();
                int appsid = 3;

                User user = new User(username, password, appsid);
                login(user);
            }
        });
    }


    public void login(final User user){
        if(nipLogin.getText().toString().equals("")){
            nipLogin.setError("NIP harap diisi");
            builder.setMessage("Mohon mengisi NIP terlebih dahulu");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            dialog = builder.show();
        }else if(passwordLogin.getText().toString().equals("")){
            passwordLogin.setError("Password harap diisi");
            builder.setMessage("Mohon mengisi passwotd terlebih dahulu");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            dialog = builder.show();
        } else {
            JSONObject object = new JSONObject();
            try {
                JSONArray arrData = new JSONArray();
                JSONObject objData = new JSONObject();

                objData.put("username", user.nip);
                objData.put("password", user.password);
                objData.put("appsid", user.appsid);

                arrData.put(objData);
                object.put("data", arrData);

            }catch (JSONException e){
                e.printStackTrace();
            }
            JsonObjectRequest rec;
            rec = new JsonObjectRequest(Request.Method.POST, URL, object,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            JSONArray data;
                            try{
                                if(response.getString("result").equals("OK")){
                                    Intent i = new Intent(getApplicationContext(), AgendaMaintance.class);
                                    JSONObject obj = response.getJSONArray("status").getJSONObject(0);

                                    if (obj.getString("msgLogin").equals("User not found")) {
                                        Toast.makeText(MainActivity.this, "User not found", Toast.LENGTH_SHORT).show();
                                    } else if (obj.getString("msgLogin").equals("User does not have access")) {
                                        Toast.makeText(MainActivity.this, "User does not have access", Toast.LENGTH_SHORT).show();
                                    } else if (obj.getString("msgLogin").equals("Invalid password")) {
                                        Toast.makeText(MainActivity.this, "Invalid password", Toast.LENGTH_SHORT).show();
                                    } else if (obj.getString("msgLogin").equals("User not active")) {
                                        Toast.makeText(MainActivity.this, "User not active", Toast.LENGTH_SHORT).show();
                                    }
                                    else if(obj.getString("msgLogin").equals("Success")){
                                        userLocalStore.setUserLoggedIn(true);
                                        userLocalStore.storeUserData(new User(
                                                obj.getString("validYN"),
                                                obj.getInt("userID"),
                                                obj.getString("msgLogin")

                                        ));
                                        Log.d("pesan", obj.getString("msgLogin"));
                                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(i);
                                        Log.d("asd", "onResponse: c");
//                                    finish();
                                    }}
                                } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                            },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("errors_", error + "");
                        }
                        });
            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            requestQueue.add(rec);
        }

    }

    public String get_SHA_512_SecurePassword(String passwordToHash, String salt) {
        String generatedPassword = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            md.update(salt.getBytes(StandardCharsets.UTF_8));
            byte[] bytes = md.digest(passwordToHash.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            generatedPassword = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return generatedPassword;
    }

    private void showErrorMessage() {
        android.support.v7.app.AlertDialog.Builder dialogBuilder = new android.support.v7.app.AlertDialog.Builder(MainActivity.this);
        dialogBuilder.setMessage("Incorrect user details");
        dialogBuilder.setPositiveButton("Ok", null);
        dialogBuilder.show();
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public void onBackPressed() {

    }
}
