package com.itb.aplikasitoko.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.itb.aplikasitoko.R;
import com.itb.aplikasitoko.ViewModel.ViewModelDetailJual;
import com.itb.aplikasitoko.util.Modul;

import java.text.ParseException;
import java.util.List;

public class LapPenjualanAdapter extends RecyclerView.Adapter<LapPenjualanAdapter.ViewHolder>{
    Context context;
    private List<ViewModelDetailJual> data;

    public LapPenjualanAdapter(Context context, List<ViewModelDetailJual> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_penjualan, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ViewModelDetailJual viewModelDetailJual = data.get(position);
        try {
            holder.tanggal.setText(viewModelDetailJual.tanggalku());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.fakturjual.setText(viewModelDetailJual.getFakturjual());
        holder.pelanggan.setText(viewModelDetailJual.getNama_pelanggan());
        holder.pegawai.setText(viewModelDetailJual.getNama_pegawai());
        holder.barang.setText(viewModelDetailJual.getBarang());
        holder.keterangan.setText(Modul.intToStr(viewModelDetailJual.getJumlahjual())+" "+viewModelDetailJual.getNama_satuan());
        holder.keuntungan.setText(Modul.removeE(viewModelDetailJual.getLaba()));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView barang, keterangan, keuntungan, tanggal, pegawai, pelanggan, fakturjual;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            barang = itemView.findViewById(R.id.isiBarang);
            keterangan = itemView.findViewById(R.id.isiKeterangan);
            keuntungan = itemView.findViewById(R.id.isiKeuntungan);
            tanggal = itemView.findViewById(R.id.txtDate);
            pegawai = itemView.findViewById(R.id.isiPegawai);
            pelanggan = itemView.findViewById(R.id.isiPegawai);
            fakturjual = itemView.findViewById(R.id.isiFakturJual);
        }
    }
}
