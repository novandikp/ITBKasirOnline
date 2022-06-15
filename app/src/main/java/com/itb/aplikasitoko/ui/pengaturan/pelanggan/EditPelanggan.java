package com.itb.aplikasitoko.ui.pengaturan.pelanggan;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

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
import com.itb.aplikasitoko.databinding.EditPelangganBinding;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditPelanggan extends AppCompatActivity {
    EditPelangganBinding bind;
    PelangganRepository pr;
    EditText inNama, inAlamat, inTelp;

    @Override
    public void onCreate( Bundle savedInstanceState) {
        bind = EditPelangganBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Edit Data Pelanggan");
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        setContentView(bind.getRoot());

        //memanggil db sqlite
        pr = new PelangganRepository(getApplication());

        init();
        bind.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int id = getIntent().getIntExtra("idpelanggan", 0);
                String nama = inNama.getText().toString();
                String alamat = inAlamat.getText().toString();
                String telp = inTelp.getText().toString();
                if (nama.isEmpty() || alamat.isEmpty() || telp.isEmpty()){
                    inNama.setError("Harap isi dengan benar");
                    inAlamat.setError("Harap isi dengan benar");
                    inTelp.setError("Harap isi dengan benar");
                } else {
                    //setiap update butuh id, tapi gk bisa lgsg dimasukkan ke parameter, jd hrs dipisah saat di inisiasi
                    ModelPelanggan mp = new ModelPelanggan(nama, alamat, telp);
                    mp.setIdpelanggan(id);
                    UpdatePel(mp.getIdpelanggan(), mp);
                }

            }
        });
    }

    public void init(){
        //mendefinisikan variabel
        inNama = bind.inNama;
        inAlamat = bind.inAlamat;
        inTelp = bind.inTelp;

        //mengambil data dr intent
        inNama.setText(getIntent().getStringExtra("nama_pelanggan"));
        inAlamat.setText(getIntent().getStringExtra("alamat_pelanggan"));
        inTelp.setText(getIntent().getStringExtra("no_telepon"));
    }

    public void UpdatePel(int id, ModelPelanggan modelPelanggan){
        LoadingDialog.load(EditPelanggan.this);
        Call<PelangganResponse> pelangganResponseCall = Api.Pelanggan(EditPelanggan.this).updatePel(id, modelPelanggan);
        pelangganResponseCall.enqueue(new Callback<PelangganResponse>() {
            @Override
            public void onResponse(Call<PelangganResponse> call, Response<PelangganResponse> response) {
                LoadingDialog.close();
                if (response.isSuccessful()){
                    if (response.body().isStatus()){
                        pr.update(modelPelanggan);
                        finish();

                        SuccessDialog.message(EditPelanggan.this, getString(R.string.updated_success), bind.getRoot());
                    }
                } else {
                    ErrorDialog.message(EditPelanggan.this, getString(R.string.updated_error), bind.getRoot());
                }
            }

            @Override
            public void onFailure(Call<PelangganResponse> call, Throwable t) {
                LoadingDialog.close();
                Toast.makeText(EditPelanggan.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
