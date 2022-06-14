package com.itb.aplikasitoko.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.itb.aplikasitoko.Model.ModelPelanggan;
import com.itb.aplikasitoko.R;
import com.itb.aplikasitoko.ui.pengaturan.pelanggan.EditPelanggan;
import com.itb.aplikasitoko.ui.pengaturan.pelanggan.MasterPelanggan;

import java.util.List;

public class PelangganAdapter extends RecyclerView.Adapter<PelangganAdapter.ViewHolder>{
    Context context;
    private List<ModelPelanggan> data;

    public PelangganAdapter(Context context, List<ModelPelanggan> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pelanggan, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ModelPelanggan mp = data.get(position);
        holder.tNama.setText(mp.getNama_pelanggan());
        holder.tAlamat.setText(mp.getAlamat_pelanggan());
        holder.tTelp.setText(mp.getNo_telepon());
        holder.tHapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MasterPelanggan)context).DeletePel(mp.getIdpelanggan());
            }
        });
        holder.tEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, EditPelanggan.class);
                i.putExtra("idpelanggan", mp.getIdpelanggan());
                i.putExtra("nama_pelanggan", mp.getNama_pelanggan()); //putextra utk mengirim data ke activity yg dituju, ini yg dikirim id barang
                i.putExtra("alamat_pelanggan", mp.getAlamat_pelanggan()); //ini namenya hrs sesuai kolom yg ada di database
                i.putExtra("no_telepon", mp.getNo_telepon());
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tNama, tTelp, tAlamat, tHapus, tEdit;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tNama = (TextView)itemView.findViewById(R.id.txtPelanggan);
            tAlamat = (TextView)itemView.findViewById(R.id.txtAlamat);
            tTelp = (TextView)itemView.findViewById(R.id.txtTelp);
            tHapus = (TextView)itemView.findViewById(R.id.txtHapus);
            tEdit = (TextView)itemView.findViewById(R.id.txtEdit);
        }
    }
}
