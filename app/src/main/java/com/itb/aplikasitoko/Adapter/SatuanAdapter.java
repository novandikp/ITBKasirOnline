package com.itb.aplikasitoko.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.itb.aplikasitoko.Model.ModelSatuan;
import com.itb.aplikasitoko.R;
import com.itb.aplikasitoko.ui.pengaturan.satuan.MasterSatuan;

import java.util.List;

public class SatuanAdapter extends RecyclerView.Adapter<SatuanAdapter.ViewHolder> {
    Context context;
    private List<ModelSatuan> data;

    public SatuanAdapter(Context context, List<ModelSatuan> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_satuan,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ModelSatuan satuan = data.get(position);
        holder.tSatuan.setText(satuan.getNama_satuan());
        holder.tHapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MasterSatuan)context).DeleteSat(satuan.getIdsatuan());
            }
        });

        holder.tEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MasterSatuan)context).dialogEditSatuan(satuan);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tSatuan, tHapus, tEdit;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tSatuan = (TextView)itemView.findViewById(R.id.txtSatuan);
            tHapus = (TextView)itemView.findViewById(R.id.txtHapus);
            tEdit = (TextView)itemView.findViewById(R.id.txtEdit);
        }
    }
}
