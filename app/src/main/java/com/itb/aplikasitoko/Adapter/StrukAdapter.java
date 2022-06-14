package com.itb.aplikasitoko.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.itb.aplikasitoko.R;
import com.itb.aplikasitoko.ViewModel.ModelViewStruk;
import com.itb.aplikasitoko.util.Modul;

import java.util.List;

public class StrukAdapter extends RecyclerView.Adapter<StrukAdapter.ViewHolder> {
    Context context;
    private List<ModelViewStruk> modelDetailJualList;

    public StrukAdapter(Context context, List<ModelViewStruk> modelDetailJualList) {
        this.context = context;
        this.modelDetailJualList = modelDetailJualList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_struk, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ModelViewStruk detailJual = modelDetailJualList.get(position);
        holder.tNama.setText(detailJual.getBarang());
        holder.tHitung.setText(Modul.toString(detailJual.getJumlahjual())+" x "+"Rp. "+Modul.removeE(detailJual.getHargajual()));
        holder.tJumlah.setText(Modul.removeE(detailJual.getHargajual()*detailJual.getJumlahjual()));
    }

    @Override
    public int getItemCount() {
        return modelDetailJualList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tNama, tHitung, tJumlah;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tNama = itemView.findViewById(R.id.itemNamaBarang);
            tHitung = itemView.findViewById(R.id.jmlBarang);
            tJumlah = itemView.findViewById(R.id.totalHargaBarang);
        }
    }
}
