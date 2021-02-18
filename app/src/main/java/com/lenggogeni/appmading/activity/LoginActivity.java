package com.lenggogeni.appmading.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.lenggogeni.appmading.R;
import com.lenggogeni.appmading.utils.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    private EditText username, password;
    private Button tombolLogin;
    private TextView tombolDaftar;
    private ProgressBar loading;
    private static String URL_LOGIN = "https://mading.soul-gh.xyz/login.php";

    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loading = findViewById(R.id.loading);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        tombolLogin = findViewById(R.id.tombolLogin);
        tombolDaftar = findViewById(R.id.tombolDaftar);

        sessionManager = new SessionManager(this);

        tombolLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mUsername = username.getText().toString().trim();
                String mPassword = password.getText().toString().trim();

                if(mUsername.isEmpty() || mPassword.isEmpty()) {
                    if(mUsername.isEmpty()) {
                        username.setError("Harap isi username");
                    }
                    if(mPassword.isEmpty()) {
                        password.setError("Harap isi password");
                    }
                } else {
                    Login(mUsername, mPassword);
                }
            }
        });

        tombolDaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    private void Login(final String username, final String password) {
        loading.setVisibility(View.VISIBLE);
        tombolLogin.setVisibility(View.GONE);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_LOGIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");

                            if (success.equals("1")) {
                                String strObject = jsonObject.getString("data");
                                JSONObject object = new JSONObject(strObject);

                                String name = object.getString("name");
                                String username = object.getString("username");
                                String id = object.getString("id");
                                String level = object.getString("level");

                                sessionManager.createSession(name, username, id, level);

                                Toast.makeText(LoginActivity.this, "Berhasil login", Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);

                                loading.setVisibility(View.GONE);
                            } else {
                                Toast.makeText(LoginActivity.this, "Gagal login, username atau password salah", Toast.LENGTH_SHORT).show();

                                loading.setVisibility(View.GONE);
                                tombolLogin.setVisibility(View.VISIBLE);
                            }
                        } catch (JSONException e) {
                            loading.setVisibility(View.GONE);
                            tombolLogin.setVisibility(View.VISIBLE);

                            Toast.makeText(LoginActivity.this, "Terjadi kesalahan", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading.setVisibility(View.GONE);
                        tombolLogin.setVisibility(View.VISIBLE);

                        Toast.makeText(LoginActivity.this, "Tidak dapat terhubung ke server", Toast.LENGTH_SHORT).show();
                    }
                })
        {
          protected Map<String, String> getParams() {
              Map<String, String> params = new HashMap<>();
              params.put("username", username);
              params.put("password", password);
              return params;
          }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}