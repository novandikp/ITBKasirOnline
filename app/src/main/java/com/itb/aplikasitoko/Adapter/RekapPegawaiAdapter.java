package com.itb.aplikasitoko.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.itb.aplikasitoko.R;
import com.itb.aplikasitoko.ViewModel.ViewModelRekapPegawai;
import com.itb.aplikasitoko.util.Modul;

import java.util.List;

public class RekapPegawaiAdapter extends RecyclerView.Adapter<RekapPegawaiAdapter.ViewHolder>{
    Context context;
    private List<ViewModelRekapPegawai> data;

    public RekapPegawaiAdapter(Context context, List<ViewModelRekapPegawai> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rekap_pegawai, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ViewModelRekapPegawai viewModelRekapPegawai = data.get(position);
        holder.nama.setText(viewModelRekapPegawai.getNama_pegawai());
        holder.alamat.setText(viewModelRekapPegawai.getAlamat_pegawai());
        holder.telp.setText(viewModelRekapPegawai.getNo_pegawai());
        holder.totalJual.setText(viewModelRekapPegawai.getTotal_jual());
        holder.pendapatan.setText("Rp. "+ Modul.removeE(viewModelRekapPegawai.getTotal_pendapatan()));
        holder.keuntungan.setText("Rp. "+ Modul.removeE(viewModelRekapPegawai.getTotal_keuntungan()));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView nama, telp, alamat, totalJual, pendapatan, keuntungan;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nama = itemView.findViewById(R.id.txtpegawai);
            telp = itemView.findViewById(R.id.txtnomor);
            alamat = itemView.findViewById(R.id.txtalamat);
            totalJual = itemView.findViewById(R.id.isitotaljual);
            pendapatan = itemView.findViewById(R.id.isipendapatan);
            keuntungan = itemView.findViewById(R.id.isiKeuntungan);
        }
    }
}
