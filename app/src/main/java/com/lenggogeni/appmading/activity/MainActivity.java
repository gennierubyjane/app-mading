package com.lenggogeni.appmading.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.lenggogeni.appmading.R;
import com.lenggogeni.appmading.adapter.InformasiAdapter;
import com.lenggogeni.appmading.model.Informasi;
import com.lenggogeni.appmading.utils.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    SessionManager sessionManager;

    private RequestQueue requestQueue;
    private SwipeRefreshLayout refresh;
    private ArrayList<Informasi> informasi = new ArrayList<>();
    private JsonArrayRequest arrayRequest;
    private RecyclerView recyclerView;
    private Dialog dialog;

    private InformasiAdapter informasiAdapter;

    private String URL = "https://mading.soul-gh.xyz/informasi.php";

    private TextView name, tombolLogout, belumAda;
    private ImageView tombolTambah, tombolCari;

    private EditText cari;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sessionManager = new SessionManager(this);
        sessionManager.checkLogin();

        HashMap<String, String> user = sessionManager.getUserDetail();

        if(user != null) {
            String mName = user.get(sessionManager.NAME);

            belumAda = findViewById(R.id.belumAda);

            name = findViewById(R.id.name);
            name.setText(mName);

            cari = findViewById(R.id.cari);

            refresh = (SwipeRefreshLayout) findViewById(R.id.swipedown);
            recyclerView = (RecyclerView) findViewById(R.id.informasi);

            dialog = new Dialog(this);

            refresh.setOnRefreshListener(this);
//            refresh.post(new Runnable() {
//                @Override
//                public void run() {
//                    informasi.clear();
//                    belumAda.setVisibility(View.GONE);
//                    getData();
//                }
//            });

            tombolLogout = findViewById(R.id.tombolLogout);
            tombolLogout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    informasi.clear();
                    sessionManager.logout();
                }
            });

            tombolTambah = findViewById(R.id.tombolTambah);
            tombolTambah.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, TambahInformasiActivity.class);
                    startActivity(intent);
                }
            });

            tombolCari = findViewById(R.id.tombolCari);
            tombolCari.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    informasi.clear();
                    belumAda.setVisibility(View.GONE);
                    getData();
                }
            });
        }
    }

    private void getData() {
        refresh.setRefreshing(true);

        String mCari = cari.getText().toString().trim();

        String URL_EXE = URL;

        if(mCari != null) {
            URL_EXE = URL + "?cari=" + mCari;
        }

        arrayRequest = new JsonArrayRequest(URL_EXE, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);

                        Informasi info = new Informasi();
                        info.setId(jsonObject.getInt("id"));
                        info.setJudul(jsonObject.getString("judul"));
                        info.setWaktu(jsonObject.getString("waktu"));
                        info.setDetail(jsonObject.getString("detail"));
                        info.setDiunggah_oleh(jsonObject.getString("diunggah_oleh"));
                        info.setImage(jsonObject.getString("image"));
                        informasi.add(info);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                if(informasi.size() < 1) {
                    belumAda.setVisibility(View.VISIBLE);
                }

                adapterPush(informasi);
                refresh.setRefreshing(false);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                refresh.setRefreshing(false);
            }
        });

        requestQueue = Volley.newRequestQueue(MainActivity.this);
        requestQueue.add(arrayRequest);
    }

    private void adapterPush(ArrayList<Informasi> informasi) {
        informasiAdapter = new InformasiAdapter(this, informasi);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(informasiAdapter);
    }

    @Override
    public void onRefresh() {
        informasi.clear();
        belumAda.setVisibility(View.GONE);
        getData();
    }

    public void onResume(){
        super.onResume();
        informasi.clear();
        belumAda.setVisibility(View.GONE);
        getData();
    }
}