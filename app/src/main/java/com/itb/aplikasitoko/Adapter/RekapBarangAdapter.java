package com.itb.aplikasitoko.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.itb.aplikasitoko.R;
import com.itb.aplikasitoko.ViewModel.ViewModelRekapBarang;
import com.itb.aplikasitoko.util.Modul;

import java.util.List;

public class RekapBarangAdapter extends RecyclerView.Adapter<RekapBarangAdapter.ViewHolder>{
    Context context;
    private List<ViewModelRekapBarang> data;

    public RekapBarangAdapter(Context context, List<ViewModelRekapBarang> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rekap_barang, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ViewModelRekapBarang viewModelRekapBarang = data.get(position);
        holder.nama.setText(viewModelRekapBarang.getBarang());
        holder.idbarang.setText("id : "+viewModelRekapBarang.getIdbarang());
        holder.satuan.setText(viewModelRekapBarang.getNama_satuan());
        holder.totalJual.setText(viewModelRekapBarang.getTotal_jual());
        holder.pendapatan.setText("Rp. "+ Modul.removeE(viewModelRekapBarang.getTotal_pendapatan()));
        holder.keuntungan.setText("Rp. "+ Modul.removeE(viewModelRekapBarang.getTotal_keuntungan()));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView nama, idbarang, satuan, totalJual, pendapatan, keuntungan;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nama = itemView.findViewById(R.id.txtBarang);
            idbarang = itemView.findViewById(R.id.txtIdbarang);
            satuan = itemView.findViewById(R.id.isiSatuan);
            totalJual = itemView.findViewById(R.id.isitotaljual);
            pendapatan = itemView.findViewById(R.id.isipendapatan);
            keuntungan = itemView.findViewById(R.id.isiKeuntungan);
        }
    }
}
