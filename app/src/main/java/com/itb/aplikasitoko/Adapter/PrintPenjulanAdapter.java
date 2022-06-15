package com.itb.aplikasitoko.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.itb.aplikasitoko.Component.LoadingDialog;
import com.itb.aplikasitoko.R;
import com.itb.aplikasitoko.ViewModel.ViewModelJual;
import com.itb.aplikasitoko.ui.home.PrintStruk;
import com.itb.aplikasitoko.util.Modul;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

public class PrintPenjulanAdapter extends RecyclerView.Adapter<PrintPenjulanAdapter.ViewHolder>{
    Context context;
    private List<ViewModelJual> data;

    public PrintPenjulanAdapter(Context context, List<ViewModelJual> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_print_penjualan, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ViewModelJual viewModelJual = data.get(position);
        String inputPattern = "yyyy-MM-dd HH:mm";
        String OutputPattern = "dd-MMM-yyyy HH:mm";

        SimpleDateFormat date = new SimpleDateFormat("dd-MMM-yyyy HH:mm");
        try {
            holder.tanggal.setText(viewModelJual.tanggalku());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.pendapatan.setText("Rp. "+ Modul.removeE(viewModelJual.getTotal()));
        holder.pegawai.setText(viewModelJual.getNama_pegawai());
        holder.pelanggan.setText(viewModelJual.getNama_pelanggan());
        holder.fakturJual.setText(viewModelJual.getFakturjual());
        holder.print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alert = new AlertDialog.Builder(context);
                alert.setTitle("Konfirmasi").setMessage("Anda akan melakukan print struk ?").setPositiveButton("Iya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        LoadingDialog.load(context);
                        Intent intent = new Intent(context, PrintStruk.class);
                        intent.putExtra("idjual", viewModelJual.getFakturjual());
                        context.startActivity(intent);
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
        TextView tanggal, jumlah, pendapatan,pegawai, fakturJual, pelanggan;
        ImageView print;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tanggal = itemView.findViewById(R.id.txtDate);
            pendapatan = itemView.findViewById(R.id.isiPendapatan);
            pegawai = itemView.findViewById(R.id.isiPegawai);
            fakturJual = itemView.findViewById(R.id.isiFakturJual);
            print = itemView.findViewById(R.id.ic_print);
            pelanggan = itemView.findViewById(R.id.txtPelanggan);
        }
    }
}
