package com.itb.aplikasitoko.ui.pengaturan.pegawai;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.itb.aplikasitoko.Api;
import com.itb.aplikasitoko.Component.ErrorDialog;
import com.itb.aplikasitoko.Component.LoadingDialog;
import com.itb.aplikasitoko.Component.SuccessDialog;
import com.itb.aplikasitoko.Database.Repository.PegawaiRepository;
import com.itb.aplikasitoko.Model.ModelPegawai;
import com.itb.aplikasitoko.R;
import com.itb.aplikasitoko.Response.PegawaiResponse;
import com.itb.aplikasitoko.databinding.EditPegawaiBinding;
import com.itb.aplikasitoko.util.Modul;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditPegawai extends AppCompatActivity {
    EditPegawaiBinding bind;
    PegawaiRepository pr;
    private EditText inNama, inAlamat, inTelp, inPin;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        bind = EditPegawaiBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Edit Data Pegawai");
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        setContentView(bind.getRoot());

        //inisiasi database
        pr = new PegawaiRepository(getApplication());

        init();

        bind.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int id = getIntent().getIntExtra("idpegawai", 0); //ini hrs pake default valu, kalau gk bakal eror
                String nama = inNama.getText().toString();
                String alamat = inAlamat.getText().toString();
                String telp = Modul.PhoneFormat(inTelp.getText().toString());
                String pin = inPin.getText().toString();

                if (nama.isEmpty() || alamat.isEmpty() || telp.isEmpty() || pin.isEmpty() || pin.length() < 4 || pin.length() > 4) {
                    inNama.setError("Harap isi dengan benar");
                    inAlamat.setError("Harap isi dengan benar");
                    inTelp.setError("Harap isi dengan benar");
                    inPin.setError("Harap isi dengan benar");

                } else {
                    ModelPegawai mp = new ModelPegawai(nama, alamat, telp, pin);
                    mp.setIdpegawai(id);
                    UpdatePeg(mp.getIdpegawai(), mp);
                }
            }
        });

    }

    public void init(){
        //Mendefinisikan variabel
        inNama = bind.addNamaPagawai;
        inAlamat = bind.addAlamatPagawai;
        inTelp = bind.addNomorPagawai;
        inPin = bind.addPinPegawai;

        //Mengambil & menerima data d intent
        inNama.setText(getIntent().getStringExtra("nama_pegawai"));
        inAlamat.setText(getIntent().getStringExtra("alamat_pegawai"));
        inTelp.setText(getIntent().getStringExtra("no_pegawai"));
        inPin.setText(getIntent().getStringExtra("pin"));
    }

    public void UpdatePeg(int id, ModelPegawai modelPegawai){
        LoadingDialog.load(EditPegawai.this);
        Call<PegawaiResponse> pegawaiResponseCall = Api.Pegawai(EditPegawai.this).updatePeg(id, modelPegawai);
        pegawaiResponseCall.enqueue(new Callback<PegawaiResponse>() {
            @Override
            public void onResponse(Call<PegawaiResponse> call, Response<PegawaiResponse> response) {
                LoadingDialog.close();
                if (response.isSuccessful()){
                    if (response.body().isStatus()) {
                        pr.update(modelPegawai);

                        finish();
                        SuccessDialog.message(EditPegawai.this, getString(R.string.updated_success), bind.getRoot());
                    }
                } else {
                    ErrorDialog.message(EditPegawai.this, getString(R.string.updated_error), bind.getRoot());
//                    Toast.makeText(EditPegawai.this, response.toString(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PegawaiResponse> call, Throwable t) {
                LoadingDialog.close();
                Toast.makeText(EditPegawai.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
