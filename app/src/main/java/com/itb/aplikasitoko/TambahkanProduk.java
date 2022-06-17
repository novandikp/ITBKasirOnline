package com.itb.aplikasitoko;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.itb.aplikasitoko.ViewModel.ViewModelBarang;
import com.itb.aplikasitoko.Response.RegisBarangResponse;
import com.itb.aplikasitoko.databinding.ActivityTambahkanProdukBinding;
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

                    startActivity(new Intent(TambahkanProduk.this, HomePage.class));
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