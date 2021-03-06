package com.itb.aplikasitoko.ui.pengaturan.pegawai;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;

import com.itb.aplikasitoko.Api;
import com.itb.aplikasitoko.Component.ErrorDialog;
import com.itb.aplikasitoko.Component.LoadingDialog;
import com.itb.aplikasitoko.Component.SuccessDialog;
import com.itb.aplikasitoko.Database.Repository.PegawaiRepository;
import com.itb.aplikasitoko.HomePage;
import com.itb.aplikasitoko.Model.ModelLoginPegawai;
import com.itb.aplikasitoko.Model.ModelPegawai;
import com.itb.aplikasitoko.R;
import com.itb.aplikasitoko.Response.LoginPegawaiResponse;
import com.itb.aplikasitoko.Response.PegawaiGetResp;
import com.itb.aplikasitoko.SharedPref.SpHelper;
import com.itb.aplikasitoko.databinding.LoginPegawaiBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginPegawai extends AppCompatActivity {
    LoginPegawaiBinding bind;

    List<ModelPegawai> dataPegawai = new ArrayList<>();
    ArrayAdapter adapterPegawai ;
    List<String> pegawai = new ArrayList<>();
    List<String> idpegawai = new ArrayList<>();
    PegawaiRepository pegawaiRepository;
    SpHelper sp;





    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        LoadingDialog.close();
        bind = LoginPegawaiBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(bind.getRoot());

        sp = new SpHelper(LoginPegawai.this);
        SpHelper spHelper = sp;
        if (Objects.equals(spHelper.getValue("izinbt2", ""), "")){
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("Notifikasi").setMessage("ITBPOS Online collects location data to enable below features even when the app is closed or not in use.\n" +
                    "1. Find Device Printer when not pair\n" +
                    "2. Efficiency finding printer not pair\n" +
                    "When you allow background location, it will give you more earnings.");
            alert.setNegativeButton("Tutup", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    if (ContextCompat.checkSelfPermission(LoginPegawai.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(LoginPegawai.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);

                    }
                }
            }).setCancelable(false).show();

            spHelper.setValue("izinbt2","oke");
        }
        //Spinner prgawai
        pegawaiRepository = new PegawaiRepository(getApplication());
        pegawaiRepository.getAllPegawai().observe(this, new Observer<List<ModelPegawai>>() {
            @Override
            public void onChanged(List<ModelPegawai> modelPegawais) {
                dataPegawai.clear();
                dataPegawai.addAll(modelPegawais);
                pegawai.clear();
                idpegawai.clear();
                for(ModelPegawai modelPegawai : modelPegawais){
                    pegawai.add(modelPegawai.getNama_pegawai());
                    idpegawai.add(String.valueOf(modelPegawai.getIdpegawai()));
                }
                adapterPegawai = new ArrayAdapter(LoginPegawai.this, android.R.layout.simple_spinner_dropdown_item, pegawai);
                bind.namaPegawai.setAdapter(adapterPegawai);


            }
        });
        refreshPegawai();
        bind.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("idpegawai",String.valueOf(idpegawai.size()));
                String pegawai = idpegawai.get(bind.namaPegawai.getSelectedItemPosition());
                String pin = bind.pin.getText().toString();

                if (pin.isEmpty()) {
                    bind.pin.setError("Harap isi dengan benar");

                } else {
                    ModelLoginPegawai mLP = new ModelLoginPegawai(pegawai, pin);
                    LoginPeg(mLP);

                }
            }
        });


    }

    public void LoginPeg(ModelLoginPegawai modelLoginPegawai){
        LoadingDialog.load(LoginPegawai.this);
        Call<LoginPegawaiResponse> loginPegawaiResponseCall = Api.Pegawai(LoginPegawai.this).loginPegawai(modelLoginPegawai);
        loginPegawaiResponseCall.enqueue(new Callback<LoginPegawaiResponse>() {
            @Override
            public void onResponse(Call<LoginPegawaiResponse> call, Response<LoginPegawaiResponse> response) {
                LoadingDialog.close();
                if (response.code() != 200){
                    ErrorDialog.message(LoginPegawai.this, "Akun tidak ditemukan", bind.getRoot());
                } else {
                    sp.setIdPegawai(modelLoginPegawai.getIdpegawai());
                    SuccessDialog.message(LoginPegawai.this, "Login berhasil", bind.getRoot());
                    sp.setRemember(bind.checkBox.isChecked());
                    startActivity(new Intent(LoginPegawai.this, HomePage.class));
                    finish();
                }
            }

            @Override
            public void onFailure(Call<LoginPegawaiResponse> call, Throwable t) {
                LoadingDialog.close();
                ErrorDialog.message(LoginPegawai.this, t.getMessage(), bind.getRoot());
            }
        });
    }

    public void refreshPegawai(){
        Call<PegawaiGetResp> pegawaiGetRespCall = Api.Pegawai(LoginPegawai.this).getPeg("");
        pegawaiGetRespCall.enqueue(new Callback<PegawaiGetResp>() {
            @Override
            public void onResponse(Call<PegawaiGetResp> call, Response<PegawaiGetResp> response) {
                dataPegawai.clear();
                pegawai.clear();
                idpegawai.clear();
                dataPegawai.addAll(response.body().getData());
                pegawaiRepository.insertAll(response.body().getData(),true);
                for(ModelPegawai modelPegawai : response.body().getData()){
                    pegawai.add(modelPegawai.getNama_pegawai());
                    idpegawai.add(String.valueOf(modelPegawai.getIdpegawai()));
                }
                adapterPegawai = new ArrayAdapter(LoginPegawai.this, android.R.layout.simple_spinner_dropdown_item, pegawai);
                bind.namaPegawai.setAdapter(adapterPegawai);
            }

            @Override
            public void onFailure(Call<PegawaiGetResp> call, Throwable t) {
                Toast.makeText(LoginPegawai.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

//    public void refreshData(boolean fetch){
//        //get SQLite
//        pegawaiRepository.getAllPegawai("").observe(this, new Observer<List<ModelPegawai>>() {
//            @Override
//            public void onChanged(List<ModelPegawai> modelPegawais) {
//                dataPegawai.clear();
//                dataPegawai.addAll(modelPegawais);
//            }
//        });
//        if(fetch) {
//            //get Retrofit
//            PegawaiService ps = Api.Pegawai(MasterPegawai.this);
//            Call<PegawaiGetResp> pegawaiGetRespCall = ps.getPeg("");
//            pegawaiGetRespCall.enqueue(new Callback<PegawaiGetResp>() {
//                @Override
//                public void onResponse(Call<PegawaiGetResp> call, Response<PegawaiGetResp> response) {
//                    if (pegawaiRepository.size() != response.body().getData().size() || !pegawaiRepository.equals(response.body().getData())) {
//                        //Memasukkan ke db kalau gada yg sm
//                        pegawaiRepository.insertAll(response.body().getData(), true);
//
//                        //merefresh adapter
//                        pegawaiRepository.clear();
//                        pegawaiRepository.addAll(response.body().getData());
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<PegawaiGetResp> call, Throwable t) {
//                    Toast.makeText(MasterPegawai.this, t.getMessage(), Toast.LENGTH_SHORT).show();
//                }
//            });
//        }
//    }
}
