package com.itb.aplikasitoko.ui.pengaturan.toko;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.itb.aplikasitoko.Api;
import com.itb.aplikasitoko.Component.ErrorDialog;
import com.itb.aplikasitoko.Component.SuccessDialog;
import com.itb.aplikasitoko.Model.ModelToko;
import com.itb.aplikasitoko.R;
import com.itb.aplikasitoko.Response.IdentitasGetResp;
import com.itb.aplikasitoko.Response.IdentitasResponse;
import com.itb.aplikasitoko.SharedPref.SpHelper;
import com.itb.aplikasitoko.databinding.ActivityIdentitasBinding;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class IdentitasToko extends AppCompatActivity {
    ActivityIdentitasBinding bind;
    private ModelToko data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        bind = ActivityIdentitasBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(bind.getRoot());
        SpHelper sp = new SpHelper(IdentitasToko.this);
        bind.ukuranPrinter.setText(sp.getPrinter());

        refreshData();

        AutoCompleteTextView JenisUsaha = bind.JenisUsaha;
        AutoCompleteTextView ukuranPrinter = bind.ukuranPrinter;

        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(IdentitasToko.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.jenisUsaha));
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        JenisUsaha.setAdapter(myAdapter);

        ArrayAdapter<String> myAdapter2 = new ArrayAdapter<String>(IdentitasToko.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.ukuranPrinter));
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ukuranPrinter.setAdapter(myAdapter2);

        bind.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ModelToko modelToko = new ModelToko();
                modelToko.setNama_pemilik(bind.namaPemilik.getText().toString());
                modelToko.setNama_toko(bind.namaUsaha.getText().toString());
                modelToko.setAlamat_toko(bind.lokasiUsaha.getText().toString());
                modelToko.setJenis_toko(JenisUsaha.getText().toString());
                sp.setPrinter(ukuranPrinter.getText().toString());
                updateProfil(modelToko);
            }
        });
    }

//    public void MasukProfil(ModelToko modelToko){
//        Call<InfoBisnisResponse> infoBisnisResponseCall = Api.getService(this).masukProfil(modelToko);
//        infoBisnisResponseCall.enqueue(new Callback<InfoBisnisResponse>() {
//            @Override
//            public void onResponse(Call<InfoBisnisResponse> call, Response<InfoBisnisResponse> response) {
//                if (response.isSuccessful()){
//                    SuccessDialog.message(IdentitasToko.this,getString(R.string.success_added),bind.getRoot());
//                } else {
//                    ErrorDialog.message(IdentitasToko.this,getString(R.string.add_kategori_error),bind.getRoot());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<InfoBisnisResponse> call, Throwable t) {
//                Toast.makeText(IdentitasToko.this, t.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
//    }

    public void refreshData(){

        Call<IdentitasGetResp> identitasGetRespCall = Api.Identitas(IdentitasToko.this).getIdentitas();
        identitasGetRespCall.enqueue(new Callback<IdentitasGetResp>() {
            @Override
            public void onResponse(Call<IdentitasGetResp> call, Response<IdentitasGetResp> response) {
                if (response.isSuccessful()){
                    ModelToko modelToko = response.body().getData();
                    bind.namaPemilik.setText(modelToko.getNama_pemilik());
                    bind.namaUsaha.setText(modelToko.getNama_toko());
                    bind.JenisUsaha.setText(modelToko.getJenis_toko());
                    bind.lokasiUsaha.setText(modelToko.getAlamat_toko());

                }
            }

            @Override
            public void onFailure(Call<IdentitasGetResp> call, Throwable t) {

            }
        });
    }

    public void updateProfil(ModelToko modelToko){
        Call<IdentitasResponse> identitasResponseCall = Api.Identitas(IdentitasToko.this).postIdentitas(modelToko);
        identitasResponseCall.enqueue(new Callback<IdentitasResponse>() {
            @Override
            public void onResponse(Call<IdentitasResponse> call, Response<IdentitasResponse> response) {
                if (response.isSuccessful()){
                    SuccessDialog.message(IdentitasToko.this,getString(R.string.success_added),bind.getRoot());
                } else {
                    ErrorDialog.message(IdentitasToko.this,getString(R.string.add_kategori_error),bind.getRoot());
                }
            }

            @Override
            public void onFailure(Call<IdentitasResponse> call, Throwable t) {
                Toast.makeText(IdentitasToko.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}