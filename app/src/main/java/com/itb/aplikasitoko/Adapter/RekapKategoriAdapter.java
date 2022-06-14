package com.itb.aplikasitoko.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.itb.aplikasitoko.R;
import com.itb.aplikasitoko.ViewModel.ViewModelRekapKategori;
import com.itb.aplikasitoko.util.Modul;

import java.util.List;

public class RekapKategoriAdapter extends RecyclerView.Adapter<RekapKategoriAdapter.ViewHolder>{
    Context context;
    private List<ViewModelRekapKategori> data;

    public RekapKategoriAdapter(Context context, List<ViewModelRekapKategori> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rekap_kategori, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ViewModelRekapKategori viewModelRekapKategori = data.get(position);
        holder.kategori.setText(viewModelRekapKategori.getNama_kategori());
        holder.totalJual.setText(viewModelRekapKategori.getTotal_jual());
        holder.pendapatan.setText("Rp. "+ Modul.removeE(viewModelRekapKategori.getTotal_pendapatan()));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView kategori, totalJual, pendapatan;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            kategori = itemView.findViewById(R.id.isiKategori);
            totalJual = itemView.findViewById(R.id.isijumlahjual);
            pendapatan = itemView.findViewById(R.id.isipendapatan);
        }
    
    }
}
