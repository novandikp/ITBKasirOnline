package com.itb.aplikasitoko.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.itb.aplikasitoko.Model.ModelPegawai;
import com.itb.aplikasitoko.R;
import com.itb.aplikasitoko.ui.pengaturan.pegawai.EditPegawai;
import com.itb.aplikasitoko.ui.pengaturan.pegawai.MasterPegawai;

import java.util.List;

public class PegawaiAdapter extends RecyclerView.Adapter<PegawaiAdapter.ViewHolder> {
    Context context;
    private List<ModelPegawai> data;

    public PegawaiAdapter(Context context, List<ModelPegawai> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pegawai, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ModelPegawai mp = data.get(position);
        holder.tNama.setText(mp.getNama_pegawai());
        holder.tAlamat.setText(mp.getAlamat_pegawai());
        holder.tTelp.setText(mp.getNo_pegawai());
        holder.txtHapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MasterPegawai)context).DeletePeg(mp.getIdpegawai());
            }
        });
        holder.txtEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, EditPegawai.class);
                i.putExtra("idpegawai", mp.getIdpegawai());
                i.putExtra("nama_pegawai", mp.getNama_pegawai());
                i.putExtra("alamat_pegawai", mp.getAlamat_pegawai());
                i.putExtra("no_pegawai", mp.getNo_pegawai());
                i.putExtra("pin", mp.getPin());
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tNama, tAlamat, tTelp, txtHapus, txtEdit;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tNama = (TextView) itemView.findViewById(R.id.txtPegawai);
            tAlamat = (TextView) itemView.findViewById(R.id.txtAlamat);
            tTelp = (TextView) itemView.findViewById(R.id.txtTelp);
            txtHapus = (TextView) itemView.findViewById(R.id.txtHapus);
            txtEdit = (TextView) itemView.findViewById(R.id.txtEdit);
        }
    }
}
