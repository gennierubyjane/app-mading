package com.lenggogeni.appmading.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.lenggogeni.appmading.R;
import com.lenggogeni.appmading.model.Informasi;
import com.lenggogeni.appmading.utils.SessionManager;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class DetailInformasiActivity extends AppCompatActivity {
    private TextView judul, waktu, detail;
    private ImageView tombolHapus, tombolKembali, gambar;

    SessionManager sessionManager;

    private static String URL_HAPUS = "https://mading.soul-gh.xyz/informasi-hapus.php";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_informasi);

        sessionManager = new SessionManager(this);

        tombolKembali = findViewById(R.id.tombolKembali);
        tombolKembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        judul = findViewById(R.id.judul);
        waktu = findViewById(R.id.waktu);
        detail = findViewById(R.id.detail);
        gambar = findViewById(R.id.gambar);

        Intent intent = getIntent();
        final String extraId = intent.getStringExtra("id");
        String extraJudul = intent.getStringExtra("judul");
        String extraWaktu = intent.getStringExtra("diunggah_oleh") + ", " + intent.getStringExtra("waktu");
        String extraDiunggah_oleh = intent.getStringExtra("diunggah_oleh");
        String extraDetail = intent.getStringExtra("detail");
        String extraImage = intent.getStringExtra("image");

        judul.setText(extraJudul);
        waktu.setText(extraWaktu);
        detail.setText(extraDetail);
        Picasso.get().load("http://mading.soul-gh.xyz/images/" + extraImage).into(gambar);

        tombolHapus = findViewById(R.id.tombolHapus);

        HashMap<String, String> user = sessionManager.getUserDetail();
        String level = user.get(sessionManager.LEVEL);

        if(level.equals("admin")) {
            tombolHapus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Hapus(extraId);
                }
            });
        } else {
            String name = user.get(sessionManager.NAME);

            if(name.toLowerCase().equals(extraDiunggah_oleh.toLowerCase())) {
                tombolHapus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Hapus(extraId);
                    }
                });
            } else {
                tombolHapus.setVisibility(View.GONE);
            }
        }
    }

    private void Hapus(final String id) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_HAPUS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");

                            if (success.equals("1")) {
                                Toast.makeText(DetailInformasiActivity.this, "Berhasil menghapus data", Toast.LENGTH_SHORT).show();

                                finish();
                            } else {
                                Toast.makeText(DetailInformasiActivity.this, "Gagal menghapus data", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            Toast.makeText(DetailInformasiActivity.this, "Terjadi kesalahan", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(DetailInformasiActivity.this, "Tidak dapat terhubung ke server", Toast.LENGTH_SHORT).show();
                    }
                })
        {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id", id);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}
