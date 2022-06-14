package com.itb.aplikasitoko.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.itb.aplikasitoko.Model.ModelPelanggan;
import com.itb.aplikasitoko.R;
import com.itb.aplikasitoko.Service.OrderService;
import com.itb.aplikasitoko.ui.home.bottom_nav.PelangganOrder;

import java.util.List;

public class PelangganOrderAdapter extends RecyclerView.Adapter<PelangganOrderAdapter.ViewHolder>{
    Context context;
    private List<ModelPelanggan> data;

    public PelangganOrderAdapter(Context context, List<ModelPelanggan> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public PelangganOrderAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pelanggan, parent, false);
        return new PelangganOrderAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ModelPelanggan mp = data.get(position);
        holder.tNama.setText(mp.getNama_pelanggan());
        holder.tAlamat.setText(mp.getAlamat_pelanggan());
        holder.tTelp.setText(mp.getNo_telepon());
        holder.tHapus.setVisibility(View.GONE);
        holder.tEdit.setVisibility(View.GONE);
        holder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //holder.cv.setOutlineAmbientShadowColor(context.getColor(R.color.darkergrey));
                AlertDialog.Builder alert = new AlertDialog.Builder(context);
                alert.setTitle("Konfirmasi").setMessage("Apakah anda yakin untuk menambahkan "+mp.getNama_pelanggan()+" sebagai pelanggan?").setPositiveButton("Iya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        OrderService.getInstance().setPelanggan(mp); //menyimpan nama pelanggan
                        ((PelangganOrder)context).finish();
                    }
                }).setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).show();
            }
        });
    }


    @Override
    public int getItemCount() {
        return data.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tNama, tTelp, tAlamat, tHapus, tEdit;
        CardView cv;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tNama = (TextView)itemView.findViewById(R.id.txtPelanggan);
            tAlamat = (TextView)itemView.findViewById(R.id.txtAlamat);
            tTelp = (TextView)itemView.findViewById(R.id.txtTelp);
            tHapus = (TextView)itemView.findViewById(R.id.txtHapus);
            tEdit = (TextView)itemView.findViewById(R.id.txtEdit);
            cv = itemView.findViewById(R.id.cv);
        }
    }
}
