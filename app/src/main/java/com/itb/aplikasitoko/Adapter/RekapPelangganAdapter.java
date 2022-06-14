package com.itb.aplikasitoko.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.itb.aplikasitoko.R;
import com.itb.aplikasitoko.ViewModel.ViewModelRekapPelanggan;
import com.itb.aplikasitoko.util.Modul;

import java.util.List;

public class RekapPelangganAdapter extends RecyclerView.Adapter<RekapPelangganAdapter.ViewHolder>{
    Context context;
    private List<ViewModelRekapPelanggan> data;

    public RekapPelangganAdapter(Context context, List<ViewModelRekapPelanggan> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rekap_pelanggan, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ViewModelRekapPelanggan viewModelRekapPelanggan = data.get(position);
        holder.nama.setText(viewModelRekapPelanggan.getNama_pelanggan());
        holder.alamat.setText(viewModelRekapPelanggan.getAlamat_pelanggan());
        holder.telp.setText(viewModelRekapPelanggan.getNo_telepon());
        holder.totalJual.setText(viewModelRekapPelanggan.getTotal_jual());
        holder.pendapatan.setText("Rp. "+ Modul.removeE(viewModelRekapPelanggan.getTotal_pendapatan()));
        holder.keuntungan.setText("Rp. "+ Modul.removeE(viewModelRekapPelanggan.getTotal_keuntungan()));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView nama, telp, alamat, totalJual, pendapatan, keuntungan;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nama = itemView.findViewById(R.id.txtPelanggan);
            telp = itemView.findViewById(R.id.txtnomor);
            alamat = itemView.findViewById(R.id.txtalamat);
            totalJual = itemView.findViewById(R.id.isitotaljual);
            pendapatan = itemView.findViewById(R.id.isipendapatan);
            keuntungan = itemView.findViewById(R.id.isiKeuntungan);
        }
    }
}
