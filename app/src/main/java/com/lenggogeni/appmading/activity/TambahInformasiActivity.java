package com.lenggogeni.appmading.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.lenggogeni.appmading.R;
import com.lenggogeni.appmading.utils.SessionManager;
import com.lenggogeni.appmading.utils.VolleyMultipartRequest;

public class TambahInformasiActivity extends AppCompatActivity {
    private EditText judul, detail;
    private Button tombolKirim, bUpload;
    private ImageView tombolKembali;

    private ProgressBar loading;

    SessionManager sessionManager;

    private static String URL_KIRIM = "https://mading.soul-gh.xyz/informasi-tambah.php";
    private static String URL_FILE = "https://mading.soul-gh.xyz/informasi-file.php";

    private static final int REQUEST_PERMISSIONS = 100;
    private static final int PICK_IMAGE_REQUEST =1 ;
    private Bitmap bitmap;
    private String filePath;
    private ImageView previewImg;

    private String imagename;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_informasi);

        sessionManager = new SessionManager(this);

        tombolKembali = findViewById(R.id.tombolKembali);
        tombolKembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        judul = findViewById(R.id.judul);
        detail = findViewById(R.id.detail);

        loading = findViewById(R.id.loading);

        tombolKirim = findViewById(R.id.tombolKirim);
        tombolKirim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mJudul = judul.getText().toString().trim();
                String mDetail = detail.getText().toString().trim();

                if(mJudul.isEmpty() || mDetail.isEmpty()) {
                    if(mJudul.isEmpty()) {
                        judul.setError("Harap isi judul");
                    }
                    if(mDetail.isEmpty()) {
                        detail.setError("Harap isi detail");
                    }
                } else {
                    HashMap<String, String> user = sessionManager.getUserDetail();
                    String mDiunggah_oleh = user.get(sessionManager.NAME);
                    Kirim(mJudul, mDetail, mDiunggah_oleh);
                }
            }
        });

        previewImg = findViewById(R.id.previewImg);

        bUpload = findViewById(R.id.bUpload);
        bUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((ContextCompat.checkSelfPermission(getApplicationContext(),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) && (ContextCompat.checkSelfPermission(getApplicationContext(),
                        Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {
                    ActivityCompat.requestPermissions(TambahInformasiActivity.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                            REQUEST_PERMISSIONS);
                } else {
                    showFileChooser();
                }
            }
        });
    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri picUri = data.getData();
            filePath = getPath(picUri);

            if (filePath != null) {
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), picUri);

                    HashMap<String, String> user = sessionManager.getUserDetail();
                    String mDiunggah_oleh = user.get(sessionManager.USERNAME);
                    imagename = mDiunggah_oleh + "_" + System.currentTimeMillis();
                    uploadBitmap(imagename, bitmap);

                    previewImg.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(TambahInformasiActivity.this,"no image selected", Toast.LENGTH_LONG).show();
            }
        }
    }

    public String getPath(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = getContentResolver().query(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();

        return path;
    }


    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    private void uploadBitmap(final String imagename, final Bitmap bitmap) {
        loading.setVisibility(View.VISIBLE);
        tombolKirim.setVisibility(View.GONE);

        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, URL_FILE,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        try {
                            JSONObject jsonObject = new JSONObject(new String(response.data));
                            String success = jsonObject.getString("success");

                            if (success.equals("0")) {
                                String pesanError = jsonObject.getString("error");

                                if(pesanError != null) {
                                    Toast.makeText(TambahInformasiActivity.this, pesanError, Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(TambahInformasiActivity.this, pesanError, Toast.LENGTH_SHORT).show();
                                }
                            }
                            loading.setVisibility(View.GONE);
                            tombolKirim.setVisibility(View.VISIBLE);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            loading.setVisibility(View.GONE);
                            tombolKirim.setVisibility(View.VISIBLE);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading.setVisibility(View.GONE);
                        tombolKirim.setVisibility(View.VISIBLE);

                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }) {


            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
//                long imagename = System.currentTimeMillis();
                params.put("image", new DataPart(imagename + ".png", getFileDataFromDrawable(bitmap)));
                return params;
            }
        };

        //adding the request to volley
        Volley.newRequestQueue(this).add(volleyMultipartRequest);
    }

    private void Kirim(final String judul, final String detail, final String diunggah_oleh) {
        loading.setVisibility(View.VISIBLE);
        tombolKirim.setVisibility(View.GONE);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_KIRIM,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");

                            if (success.equals("1")) {
                                Toast.makeText(TambahInformasiActivity.this, "Berhasil menambah data", Toast.LENGTH_SHORT).show();

                                finish();

                                loading.setVisibility(View.GONE);
                            } else {
                                Toast.makeText(TambahInformasiActivity.this, "Gagal menambah data", Toast.LENGTH_SHORT).show();

                                loading.setVisibility(View.GONE);
                                tombolKirim.setVisibility(View.VISIBLE);
                            }
                        } catch (JSONException e) {
                            loading.setVisibility(View.GONE);
                            tombolKirim.setVisibility(View.VISIBLE);

                            Toast.makeText(TambahInformasiActivity.this, "Terjadi kesalahan", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading.setVisibility(View.GONE);
                        tombolKirim.setVisibility(View.VISIBLE);

                        Toast.makeText(TambahInformasiActivity.this, "Tidak dapat terhubung ke server", Toast.LENGTH_SHORT).show();
                    }
                })
        {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("judul", judul);
                params.put("detail", detail);
                params.put("diunggah_oleh", diunggah_oleh);
                params.put("image", imagename);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}
