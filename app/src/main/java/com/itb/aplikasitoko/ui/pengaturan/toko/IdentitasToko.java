package com.itb.aplikasitoko.ui.pengaturan.toko;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import com.itb.aplikasitoko.Api;
import com.itb.aplikasitoko.Component.ErrorDialog;
import com.itb.aplikasitoko.Component.LoadingDialog;
import com.itb.aplikasitoko.Component.SuccessDialog;
import com.itb.aplikasitoko.Database.Repository.TokoRepository;
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
    private TokoRepository tokoRepository;
        
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        bind = ActivityIdentitasBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        tokoRepository = new TokoRepository(getApplication());
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Identitas Toko");
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        setContentView(bind.getRoot());
        SpHelper sp = new SpHelper(IdentitasToko.this);
        bind.ukuranPrinter.setText(sp.getPrinter());



        tokoRepository.getToko().observe(this, new Observer<ModelToko>() {
            @Override
            public void onChanged(ModelToko modelToko) {
                if(modelToko!=null){
                    data = modelToko;
                    bind.namaPemilik.setText(modelToko.getNama_pemilik());
                    bind.namaUsaha.setText(modelToko.getNama_toko());

                    bind.JenisUsaha.setText(modelToko.getJenis_toko());
                    bind.lokasiUsaha.setText(modelToko.getAlamat_toko());
                }
            }
        });
        refreshData();

        AutoCompleteTextView JenisUsaha = bind.JenisUsaha;
        AutoCompleteTextView ukuranPrinter = bind.ukuranPrinter;

        bind.JenisUsaha.setClickable(true);
        bind.JenisUsaha.setFocusable(false);
        bind.JenisUsaha.setFocusableInTouchMode(false);
        bind.ukuranPrinter.setClickable(true);
        bind.ukuranPrinter.setFocusable(false);
        bind.ukuranPrinter.setFocusableInTouchMode(false);
        bind.JenisUsaha.setOnTouchListener((view, motionEvent) -> {
            bind.JenisUsaha.showDropDown();
            return true;
        });
        bind.ukuranPrinter.setOnTouchListener((view, motionEvent) -> {
            bind.ukuranPrinter.showDropDown();
            return true;
        });


        ArrayAdapter<String> myAdapter2 = new ArrayAdapter<String>(IdentitasToko.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.ukuranPrinter));
        myAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ukuranPrinter.setAdapter(myAdapter2);
        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(IdentitasToko.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.jenisUsaha));
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bind.JenisUsaha.setAdapter(myAdapter);
        myAdapter.notifyDataSetChanged();


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


    public void refreshData(){
        LoadingDialog.load(IdentitasToko.this);
        Call<IdentitasGetResp> identitasGetRespCall = Api.Identitas(IdentitasToko.this).getIdentitas();
        identitasGetRespCall.enqueue(new Callback<IdentitasGetResp>() {
            @Override
            public void onResponse(Call<IdentitasGetResp> call, Response<IdentitasGetResp> response) {
                LoadingDialog.close();
                if (response.isSuccessful()){
                    ModelToko modelToko = response.body().getData();
                    tokoRepository.insert(modelToko);
                    bind.namaPemilik.setText(modelToko.getNama_pemilik());
                    bind.namaUsaha.setText(modelToko.getNama_toko());

                    bind.JenisUsaha.setText(modelToko.getJenis_toko());
                    bind.lokasiUsaha.setText(modelToko.getAlamat_toko());

                }
            }

            @Override
            public void onFailure(Call<IdentitasGetResp> call, Throwable t) {
                LoadingDialog.close();
                Toast.makeText(IdentitasToko.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void updateProfil(ModelToko modelToko){
        LoadingDialog.load(IdentitasToko.this);
        Call<IdentitasResponse> identitasResponseCall = Api.Identitas(IdentitasToko.this).postIdentitas(modelToko);
        identitasResponseCall.enqueue(new Callback<IdentitasResponse>() {
            @Override
            public void onResponse(Call<IdentitasResponse> call, Response<IdentitasResponse> response) {
                LoadingDialog.close();
                tokoRepository.insert(modelToko);
                if (response.isSuccessful()){
                    SuccessDialog.message(IdentitasToko.this,getString(R.string.updated_success),bind.getRoot());
                } else {
                    ErrorDialog.message(IdentitasToko.this,getString(R.string.updated_error),bind.getRoot());
                }
            }

            @Override
            public void onFailure(Call<IdentitasResponse> call, Throwable t) {
                LoadingDialog.close();
                Toast.makeText(IdentitasToko.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
