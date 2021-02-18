package com.lenggogeni.appmading.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.lenggogeni.appmading.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    private EditText name, username, password;
    private Button tombolDaftar;
    private ImageView tombolKembali;

    private ProgressBar loading;

    private static String URL_REGISTER = "https://mading.soul-gh.xyz/register.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        loading = findViewById(R.id.loading);
        name = findViewById(R.id.name);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);

        tombolDaftar = findViewById(R.id.tombolDaftar);
        tombolKembali = findViewById(R.id.tombolKembali);

        tombolDaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mName = name.getText().toString().trim();
                String mUsername = username.getText().toString().trim();
                String mPassword = password.getText().toString().trim();

                if(mName.isEmpty() || mUsername.isEmpty() || mPassword.isEmpty()) {
                    if(mName.isEmpty()) {
                        name.setError("Harap isi nama");
                    }
                    if(mUsername.isEmpty()) {
                        username.setError("Harap isi username");
                    }
                    if(mPassword.isEmpty()) {
                        password.setError("Harap isi password");
                    }
                } else {
                    Daftar(mName, mUsername, mPassword);
                }
            }
        });

        tombolKembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void Daftar(final String name, final String username, final String password) {
        loading.setVisibility(View.VISIBLE);
        tombolDaftar.setVisibility(View.GONE);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_REGISTER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");

                            if (success.equals("1")) {
                                Toast.makeText(RegisterActivity.this, "Berhasil daftar", Toast.LENGTH_SHORT).show();

                                new android.os.Handler().postDelayed(
                                        new Runnable() {
                                            public void run() {
                                                finish();
                                            }
                                        }, 1000);

                                loading.setVisibility(View.GONE);
                            } else {
                                String pesanError = jsonObject.getString("error");

                                if(pesanError != null) {
                                    Toast.makeText(RegisterActivity.this, pesanError, Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(RegisterActivity.this, "Gagal daftar", Toast.LENGTH_SHORT).show();
                                }

                                loading.setVisibility(View.GONE);
                                tombolDaftar.setVisibility(View.VISIBLE);
                            }
                        } catch (JSONException e) {
                            loading.setVisibility(View.GONE);
                            tombolDaftar.setVisibility(View.VISIBLE);

                            Toast.makeText(RegisterActivity.this, "Terjadi kesalahan", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading.setVisibility(View.GONE);
                        tombolDaftar.setVisibility(View.VISIBLE);

                        Toast.makeText(RegisterActivity.this, "Tidak dapat terhubung ke server", Toast.LENGTH_SHORT).show();
                    }
                })
        {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("name", name);
                params.put("username", username);
                params.put("password", password);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}
