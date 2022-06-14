package com.itb.aplikasitoko.ui.pengaturan;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.itb.aplikasitoko.R;
import com.itb.aplikasitoko.ui.pengaturan.kategori.MasterDaftarKategori;

public class MyKategoriAdapter extends RecyclerView.Adapter<MyKategoriAdapter.ViewHolder> {
    MyKategori[] myKategori;
    Context context;
    public MyKategoriAdapter(MyKategori[] myKategori, MasterDaftarKategori activity){
        this.myKategori = myKategori;
        this.context=activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.fragment_master_list,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final MyKategori myKategoriList = myKategori[position];
        holder.textViewName.setText(myKategoriList.getKategori());
        holder.textViewName.setText(myKategoriList.getSubKategori());
        holder.kategoriIcon.setImageResource(myKategoriList.getIconKategori());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "myKategoriList", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return myKategori.length;
    }

    public  class ViewHolder extends RecyclerView.ViewHolder {
        ImageView kategoriIcon;
        TextView textViewName;
        TextView textViewDate;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            kategoriIcon = itemView.findViewById(R.id.imageViewKategori);
            textViewName = itemView.findViewById(R.id.textName);
            textViewDate = itemView.findViewById(R.id.textDate);
        }
    }
}
