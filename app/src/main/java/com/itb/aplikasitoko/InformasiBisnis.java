package com.itb.aplikasitoko;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.itb.aplikasitoko.Model.ModelToko;
import com.itb.aplikasitoko.Response.InfoBisnisResponse;
import com.itb.aplikasitoko.SharedPref.SpHelper;
import com.itb.aplikasitoko.databinding.ActivityInformasiBisnisBinding;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InformasiBisnis extends AppCompatActivity {
    private Button btnNext;
    private EditText NamaPemilik, NamaUsaha, Lokasi;
    private ProgressBar progressBar;
    ActivityInformasiBisnisBinding bind;
    SpHelper spHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bind = ActivityInformasiBisnisBinding.inflate(getLayoutInflater());
        setContentView(bind.getRoot());

        EditText NamaPemilik = bind.namaPemilik;
        EditText NamaUsaha = bind.namaUsaha;
        EditText Lokasi = bind.lokasiUsaha;
        //Spinner mySpinner = bind.jenisUsaha;
        AutoCompleteTextView JenisUsaha = bind.JenisUsaha;

        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(InformasiBisnis.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.jenisUsaha));
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //mySpinner.setAdapter(myAdapter);
        JenisUsaha.setAdapter(myAdapter);
         spHelper = new SpHelper(this);
        spHelper.setValue(Config.lastPageSign,Config.PageSigned.PROFIL);


        bind.nextToTambahProduk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ModelToko modelToko = new ModelToko();
                modelToko.setNama_pemilik(NamaPemilik.getText().toString());
                modelToko.setNama_toko(NamaUsaha.getText().toString());
                modelToko.setAlamat_toko(Lokasi.getText().toString());
                //modelToko.setJenis_toko(mySpinner.getSelectedItem().toString());
                modelToko.setJenis_toko(JenisUsaha.toString());
                MasukProfil(modelToko);
               
            }
        });
    }

    public void MasukProfil(ModelToko modelToko){
        Call<InfoBisnisResponse> infoBisnisResponseCall = Api.getService(this).masukProfil(modelToko);
        infoBisnisResponseCall.enqueue(new Callback<InfoBisnisResponse>() {
            @Override
            public void onResponse(Call<InfoBisnisResponse> call, Response<InfoBisnisResponse> response) {
                if (response.isSuccessful()){
                    spHelper.setUsername(modelToko.getNama_pemilik());
                    String message = "Data berhasil ditambahkan";
                    Toast.makeText(InformasiBisnis.this, message, Toast.LENGTH_SHORT).show();

                    startActivity(new Intent(InformasiBisnis.this, TambahkanProduk.class));
                    finish();
                } else {
                    String message = Api.getError(response).message;
                    Toast.makeText(InformasiBisnis.this, message, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<InfoBisnisResponse> call, Throwable t) {

            }
        });
    }


}