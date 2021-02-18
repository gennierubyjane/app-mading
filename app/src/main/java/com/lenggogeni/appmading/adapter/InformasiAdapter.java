package com.lenggogeni.appmading.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lenggogeni.appmading.R;
import com.lenggogeni.appmading.activity.DetailInformasiActivity;
import com.lenggogeni.appmading.model.Informasi;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class InformasiAdapter extends RecyclerView.Adapter<InformasiAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<Informasi> informasi;

    public InformasiAdapter(Context context, ArrayList<Informasi> informasi) {
        this.context = context;
        this.informasi = informasi;
    }

    @NonNull
    @Override
    public InformasiAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        view = layoutInflater.inflate(R.layout.informasi_list, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InformasiAdapter.MyViewHolder holder, final int position) {
        holder.judul.setText(informasi.get(position).getJudul());
        holder.waktu.setText(informasi.get(position).getDiunggah_oleh() + ", " + informasi.get(position).getWaktu());
        Picasso.get().load("http://mading.soul-gh.xyz/images/" + informasi.get(position).getImage()).into(holder.gambar);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailInformasiActivity.class);

                intent.putExtra("id", "" + informasi.get(position).getId());
                intent.putExtra("judul", informasi.get(position).getJudul());
                intent.putExtra("waktu", (informasi.get(position).getWaktu()));
                intent.putExtra("diunggah_oleh", (informasi.get(position).getDiunggah_oleh()));
                intent.putExtra("detail", (informasi.get(position).getDetail()));
                intent.putExtra("image", (informasi.get(position).getImage()));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return informasi.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView judul, waktu;
        private ImageView gambar;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            judul = (TextView) itemView.findViewById(R.id.judul);
            waktu = (TextView) itemView.findViewById(R.id.waktu);
            gambar = (ImageView) itemView.findViewById(R.id.gambar);
        }
    }
}
