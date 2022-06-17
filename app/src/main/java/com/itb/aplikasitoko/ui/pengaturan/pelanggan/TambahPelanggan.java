package com.itb.aplikasitoko.ui.pengaturan.pelanggan;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.itb.aplikasitoko.Api;
import com.itb.aplikasitoko.Component.ErrorDialog;
import com.itb.aplikasitoko.Component.LoadingDialog;
import com.itb.aplikasitoko.Component.SuccessDialog;
import com.itb.aplikasitoko.Database.Repository.PelangganRepository;
import com.itb.aplikasitoko.Model.ModelPelanggan;
import com.itb.aplikasitoko.R;
import com.itb.aplikasitoko.Response.PelangganResponse;
import com.itb.aplikasitoko.databinding.InsertPelangganBinding;
import com.itb.aplikasitoko.util.Modul;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TambahPelanggan extends AppCompatActivity {
    InsertPelangganBinding bind;
    List<ModelPelanggan> data = new ArrayList<>();
    PelangganRepository pr;
    private EditText inNama, inAlamat, inTelp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        bind = InsertPelangganBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Tambah Pelanggan");
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        setContentView(bind.getRoot());

        inNama = bind.inNama;
        inAlamat = bind.inAlamat;
        inTelp = bind.inTelp;

        //memanggil db
        pr = new PelangganRepository(getApplication());

        bind.btnAddPelanggan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nama = inNama.getText().toString();
                String alamat = inAlamat.getText().toString();
                String telp = Modul.PhoneFormat(inTelp.getText().toString());

                if (nama.isEmpty() || alamat.isEmpty() || telp.isEmpty()){
                    inNama.setError("Harap isi dengan benar");
                    inAlamat.setError("Harap isi dengan benar");
                    inTelp.setError("Harap isi dengan benar");
                } else {
                    ModelPelanggan mp = new ModelPelanggan(nama, alamat, telp);
                    PostPel(mp);
                }
            }
        });
    }

    public void PostPel(ModelPelanggan modelPelanggan){
        inNama.setEnabled(false);
        inAlamat.setEnabled(false);
        inTelp.setEnabled(false);
        bind.btnAddPelanggan.setEnabled(false);

        LoadingDialog.load(this);
        Call<PelangganResponse> pelangganResponseCall = Api.Pelanggan(TambahPelanggan.this).postPel(modelPelanggan);
        pelangganResponseCall.enqueue(new Callback<PelangganResponse>() {
            @Override
            public void onResponse(Call<PelangganResponse> call, Response<PelangganResponse> response) {
                LoadingDialog.close();
                if (response.isSuccessful()){
                    SuccessDialog.message(TambahPelanggan.this, getString(R.string.success_added), bind.getRoot());
                    bind.inNama.getText().clear();
                    bind.inAlamat.getText().clear();
                    bind.inTelp.getText().clear();

                    pr.insert(modelPelanggan);

                    startActivity(new Intent(TambahPelanggan.this, MasterPelanggan.class));
                    finish();
                } else {
                    ErrorDialog.message(TambahPelanggan.this, getString(R.string.add_kategori_error), bind.getRoot());
                }
                inNama.setEnabled(true);
                inAlamat.setEnabled(true);
                inTelp.setEnabled(true);
                bind.btnAddPelanggan.setEnabled(true);
            }

            @Override
            public void onFailure(Call<PelangganResponse> call, Throwable t) {
                LoadingDialog.close();
                ErrorDialog.message(TambahPelanggan.this, getString(R.string.error_fetch), bind.getRoot());
                inNama.setEnabled(true);
                inAlamat.setEnabled(true);
                inTelp.setEnabled(true);
                bind.btnAddPelanggan.setEnabled(true);
            }
        });
    }
}
