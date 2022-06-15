package com.itb.aplikasitoko.ui.pengaturan;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.itb.aplikasitoko.Component.LoadingDialog;
import com.itb.aplikasitoko.databinding.FragmentPengaturanBinding;
import com.itb.aplikasitoko.ui.pengaturan.kategori.MasterDaftarKategori;
import com.itb.aplikasitoko.ui.pengaturan.pegawai.MasterPegawai;
import com.itb.aplikasitoko.ui.pengaturan.pelanggan.MasterPelanggan;
import com.itb.aplikasitoko.ui.pengaturan.produk.MasterProduk;
import com.itb.aplikasitoko.ui.pengaturan.satuan.MasterSatuan;
import com.itb.aplikasitoko.ui.pengaturan.toko.IdentitasToko;

public class PengaturanFragment extends Fragment {

    // private FragmentPengaturanBinding binding;
    FragmentPengaturanBinding bind;
    private Context context;

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        PengaturanViewModel pengaturanViewModel = new ViewModelProvider(this).get(PengaturanViewModel.class);

        bind = FragmentPengaturanBinding.inflate(getLayoutInflater());
        View root = bind.getRoot();

        bind.masterDaftarKategori.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), MasterDaftarKategori.class)); // kalau mangil fragment, itu
                                                                                     // pakenya getContext() ,bukan this
            }
        });

        bind.masterSatuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), MasterSatuan.class));
            }
        });

        bind.masterPelanggan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), MasterPelanggan.class));
            }
        });

        bind.masterPegawai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), MasterPegawai.class));
            }
        });

        bind.masterProduk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), MasterProduk.class));
            }
        });

        bind.IdentitasToko.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), IdentitasToko.class));
            }
        });

//        TextView textView = bind.textPengaturan;
//        pengaturanViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;

    }
}