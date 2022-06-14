package com.itb.aplikasitoko.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.itb.aplikasitoko.R;
import com.itb.aplikasitoko.ViewModel.ViewModelJual;
import com.itb.aplikasitoko.util.Modul;

import java.text.ParseException;
import java.util.List;

public class LapPendapatanAdapter extends RecyclerView.Adapter<LapPendapatanAdapter.ViewHolder> {
    Context context;
    private List<ViewModelJual> data;

    public LapPendapatanAdapter(Context context, List<ViewModelJual> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pendapatan, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ViewModelJual viewModelJual = data.get(position);

        try {
            holder.tanggal.setText(viewModelJual.tanggalku());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.pendapatan.setText("Rp. "+Modul.removeE(viewModelJual.getTotal()));
        holder.pelanggan.setText(viewModelJual.getNama_pelanggan());
        holder.pegawai.setText(viewModelJual.getNama_pegawai());
        holder.fakturJual.setText(viewModelJual.getFakturjual());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tanggal, jumlah, pendapatan, pelanggan, pegawai, fakturJual;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tanggal = itemView.findViewById(R.id.txtDate);
            pendapatan = itemView.findViewById(R.id.isiPendapatan);
            pelanggan = itemView.findViewById(R.id.isipelanggan);
            pegawai = itemView.findViewById(R.id.isiPegawai);
            fakturJual = itemView.findViewById(R.id.isiFakturJual);
        }
    }
}
