package com.itb.aplikasitoko;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.itb.aplikasitoko.SharedPref.SpHelper;
import com.itb.aplikasitoko.ViewModel.ViewModelBarang;
import com.itb.aplikasitoko.Response.RegisBarangResponse;
import com.itb.aplikasitoko.databinding.ActivityTambahkanProdukBinding;
import com.itb.aplikasitoko.ui.pengaturan.pegawai.LoginPegawai;
import com.itb.aplikasitoko.util.Modul;
import com.itb.aplikasitoko.util.NumberTextWatcher;

import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TambahkanProduk extends AppCompatActivity {
    ActivityTambahkanProdukBinding bind;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bind = ActivityTambahkanProdukBinding.inflate(getLayoutInflater());
        setContentView(bind.getRoot());
        SpHelper spHelper = new SpHelper(this);
        spHelper.setValue(Config.lastPageSign,Config.PageSigned.DASHBOARD);
        bind.harga.addTextChangedListener(new NumberTextWatcher(bind.harga, new Locale("id","ID"),0));
        bind.hargaJual.addTextChangedListener(new NumberTextWatcher(bind.hargaJual, new Locale("id","ID"),0));
        EditText idBarang = bind.kodeProduk;
        EditText NamaBarang = bind.namaProduk;
        EditText Kategori = bind.kategori;
        EditText Satuan = bind.satuan;
        EditText Harga = bind.harga;
        EditText HargaJual = bind.hargaJual;
        EditText Stok = bind.stokAwal;


        bind.nextToMainPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                cek alll input is exist
                if (idBarang.getText().toString().isEmpty() || NamaBarang.getText().toString().isEmpty() || Kategori.getText().toString().isEmpty() || Satuan.getText().toString().isEmpty() || Harga.getText().toString().isEmpty() || HargaJual.getText().toString().isEmpty()) {
//                    set error input
                    if(idBarang.getText().toString().isEmpty()){
                        idBarang.setError("Kode Barang tidak boleh kosong");
                    }
                    if(NamaBarang.getText().toString().isEmpty()){
                        NamaBarang.setError("Nama Barang tidak boleh kosong");
                    }
                    if(Kategori.getText().toString().isEmpty()) {
                        Kategori.setError("Kategori tidak boleh kosong");
                    }
                    if(Satuan.getText().toString().isEmpty()) {
                        Satuan.setError("Satuan tidak boleh kosong");
                    }
                    if(Harga.getText().toString().isEmpty()) {
                        Harga.setError("Harga tidak boleh kosong");
                    }
                    if(HargaJual.getText().toString().isEmpty()) {
                        HargaJual.setError("Harga Jual tidak boleh kosong");
                    }
                    return;
                }
                ViewModelBarang modelVB = new ViewModelBarang();
                modelVB.setIdbarang(idBarang.getText().toString());
                modelVB.setBarang(NamaBarang.getText().toString());
                modelVB.setNama_kategori(Kategori.getText().toString());
                modelVB.setNama_satuan(Satuan.getText().toString());
                modelVB.setHarga(Modul.unnumberFormat(HargaJual.getText().toString()));
                modelVB.setHargabeli(Modul.unnumberFormat(Harga.getText().toString()));
                modelVB.setStok(Stok.getText().toString());
                RegisBarang(modelVB);
            }
        });
    }

    public void RegisBarang(ViewModelBarang viewModelBarang) {
        Call<RegisBarangResponse> RegisBarangResponseCall = Api.getService(TambahkanProduk.this).regisBarang(viewModelBarang);
        RegisBarangResponseCall.enqueue(new Callback<RegisBarangResponse>() {
            @Override
            public void onResponse(Call<RegisBarangResponse> call, Response<RegisBarangResponse> response) {
                if(response.isSuccessful()){
                    String message = "Data berhasil ditambahkan";
                    Toast.makeText(TambahkanProduk.this, message , Toast.LENGTH_LONG).show();

                    startActivity(new Intent(TambahkanProduk.this, LoginPegawai.class));
                    finish();
                } else {
                    String message = Api.getError(response).message;
                    Toast.makeText(TambahkanProduk.this, message , Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<RegisBarangResponse> call, Throwable t) {

            }
        });
    }
}