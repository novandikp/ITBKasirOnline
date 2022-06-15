package com.itb.aplikasitoko.ui.load;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.itb.aplikasitoko.Api;
import com.itb.aplikasitoko.Database.Repository.BarangRepository;
import com.itb.aplikasitoko.Database.Repository.DetailJualRepository;
import com.itb.aplikasitoko.Database.Repository.JualRepository;
import com.itb.aplikasitoko.Database.Repository.KategoriRepository;
import com.itb.aplikasitoko.Database.Repository.PegawaiRepository;
import com.itb.aplikasitoko.Database.Repository.PelangganRepository;
import com.itb.aplikasitoko.Database.Repository.SatuanRepository;
import com.itb.aplikasitoko.Database.Repository.TokoRepository;
import com.itb.aplikasitoko.HomePage;
import com.itb.aplikasitoko.LoginActivity;
import com.itb.aplikasitoko.Model.ModelToko;
import com.itb.aplikasitoko.Response.BarangGetResp;
import com.itb.aplikasitoko.Response.DetailJualResp;
import com.itb.aplikasitoko.Response.IdentitasGetResp;
import com.itb.aplikasitoko.Response.JualResponse;
import com.itb.aplikasitoko.Response.KategoriGetResp;
import com.itb.aplikasitoko.Response.PegawaiGetResp;
import com.itb.aplikasitoko.Response.PelangganGetResp;
import com.itb.aplikasitoko.Response.SatuanGetResp;
import com.itb.aplikasitoko.databinding.ActivityLoadBinding;
import com.itb.aplikasitoko.ui.pengaturan.pegawai.LoginPegawai;
import com.itb.aplikasitoko.ui.pengaturan.toko.IdentitasToko;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoadActivity extends AppCompatActivity {
    ActivityLoadBinding bind;
    KategoriRepository kategoriRepository;
    BarangRepository barangRepository;
    SatuanRepository satuanRepository;
    PegawaiRepository pegawaiRepository;
    PelangganRepository pelangganRepository;
    TokoRepository tokoRepository;
    JualRepository jualRepository;
    DetailJualRepository detailJualRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bind = ActivityLoadBinding.inflate(getLayoutInflater());
        setContentView(bind.getRoot());
        getSupportActionBar().hide();
        kategoriRepository = new KategoriRepository(getApplication());
        barangRepository = new BarangRepository(getApplication());
        satuanRepository = new SatuanRepository(getApplication());
        pegawaiRepository = new PegawaiRepository(getApplication());
        pelangganRepository = new PelangganRepository(getApplication());
        tokoRepository = new TokoRepository(getApplication());
        jualRepository = new JualRepository(getApplication());
        detailJualRepository = new DetailJualRepository(getApplication());
        load(1);
    }


    public void load(int id) {
        switch (id) {
            case 1:
                bind.progressBar2.setProgress(12);
                bind.tvKet.setText("Sedang mengunduh data kategori");
                getKategori();
                break;
            case 2:
                bind.progressBar2.setProgress(24);
                bind.tvKet.setText("Sedang mengunduh data satuan");
                getSatuan();
                break;
            case 3:
                bind.progressBar2.setProgress(36);
                bind.tvKet.setText("Sedang mengunduh data barang");
                getBarang();
                break;
            case 4:
                bind.progressBar2.setProgress(48);
                bind.tvKet.setText("Sedang mengunduh data pelanggan");
                getPelanggan();
                break;
            case 5:
                bind.progressBar2.setProgress(60);
                bind.tvKet.setText("Sedang mengunduh data pegawai");
                getPegawai();
                break;
            case 6:
                bind.progressBar2.setProgress(72);
                bind.tvKet.setText("Sedang mengunduh data identitas");
                getToko();
                break;
            case 7:
                bind.progressBar2.setProgress(84);
                bind.tvKet.setText("Sedang mengunduh data penjualan");
                getJual();
                break;
            case 8:
                bind.progressBar2.setProgress(100);
                bind.tvKet.setText("Sedang mengunduh data detail penjualan");
                getDetail();
                break;
            default:
                startActivity(new Intent(LoadActivity.this, LoginPegawai.class));
                finish();
                break;
        }
    }

    public void getKategori(){
        Call<KategoriGetResp> call = Api.Kategori(this).getKat("");
        call.enqueue(new Callback<KategoriGetResp>() {
            @Override
            public void onResponse(Call<KategoriGetResp> call, Response<KategoriGetResp> response) {
                if(response.isSuccessful()){
                    KategoriGetResp resp = response.body();
                    if(resp.getStatus()){
                        kategoriRepository.insertAll(resp.getData(),true);
                    }
                }

                load(2);
            }

            @Override
            public void onFailure(Call<KategoriGetResp> call, Throwable t) {
                load(2);
            }
        });
    }

    public void getSatuan(){
        Call<SatuanGetResp> call = Api.Satuan(this).getSat("");
        call.enqueue(new Callback<SatuanGetResp>() {
            @Override
            public void onResponse(Call<SatuanGetResp> call, Response<SatuanGetResp> response) {
                if(response.isSuccessful()){
                    SatuanGetResp resp = response.body();
                    satuanRepository.insertAll(resp.getData(),true);
                }
                load(3);
            }

            @Override
            public void onFailure(Call<SatuanGetResp> call, Throwable t) {
                load(3);
            }
        });
    }

    public void getBarang(){
        Call<BarangGetResp> call = Api.Barang(this).getBarang("");
        call.enqueue(new Callback<BarangGetResp>() {
            @Override
            public void onResponse(Call<BarangGetResp> call, Response<BarangGetResp> response) {
                if(response.isSuccessful()){
                    BarangGetResp resp = response.body();
                    barangRepository.insertAll(resp.getData(),true);
                }
                load(4);
            }

            @Override
            public void onFailure(Call<BarangGetResp> call, Throwable t) {
                load(4);
            }
        });
    }

    public void getPelanggan(){
        Call<PelangganGetResp> call = Api.Pelanggan(this).getPel("");
        call.enqueue(new Callback<PelangganGetResp>() {
            @Override
            public void onResponse(Call<PelangganGetResp> call, Response<PelangganGetResp> response) {
                if(response.isSuccessful()){
                    PelangganGetResp resp = response.body();
                    pelangganRepository.insertAll(resp.getData(),true);
                }
                load(5);
            }

            @Override
            public void onFailure(Call<PelangganGetResp> call, Throwable t) {
                load(5);
            }
        });
    }

    public void getPegawai(){
        Call<PegawaiGetResp> call = Api.Pegawai(this).getPeg("");
        call.enqueue(new Callback<PegawaiGetResp>() {
            @Override
            public void onResponse(Call<PegawaiGetResp> call, Response<PegawaiGetResp> response) {
                if(response.isSuccessful()){
                    PegawaiGetResp resp = response.body();
                    pegawaiRepository.insertAll(resp.getData(),true);
                }
                load(6);
            }

            @Override
            public void onFailure(Call<PegawaiGetResp> call, Throwable t) {
                load(6);
            }
        });
    }

    public void getToko(){
        Call<IdentitasGetResp> identitasGetRespCall = Api.Identitas(LoadActivity.this).getIdentitas();
        identitasGetRespCall.enqueue(new Callback<IdentitasGetResp>() {
            @Override
            public void onResponse(Call<IdentitasGetResp> call, Response<IdentitasGetResp> response) {
                if (response.isSuccessful()){
                    ModelToko modelToko = response.body().getData();
                    if(modelToko!=null){
                        tokoRepository.insert(modelToko);
                    }

                }
                load(7);
            }

            @Override
            public void onFailure(Call<IdentitasGetResp> call, Throwable t) {
                load(7);
            }
        });
    }

    public void getJual(){
       Call<JualResponse> jualResponseCall = Api.Order(LoadActivity.this).getJual();
       jualResponseCall.enqueue(new Callback<JualResponse>() {
           @Override
           public void onResponse(Call<JualResponse> call, Response<JualResponse> response) {
               if (response.isSuccessful()){
                   JualResponse jualResponse = response.body();
                   jualRepository.insertAll(jualResponse.getData(),true);
               }
               load(8);
           }

           @Override
           public void onFailure(Call<JualResponse> call, Throwable t) {
               load(8);
           }
       });
    }


    public void getDetail(){
        Call<DetailJualResp> detailJualRespCall = Api.Order(LoadActivity.this).getDetailJual();
        detailJualRespCall.enqueue(new Callback<DetailJualResp>() {
            @Override
            public void onResponse(Call<DetailJualResp> call, Response<DetailJualResp> response) {
                if (response.isSuccessful()){
                    DetailJualResp detailJualResp = response.body();
                    detailJualRepository.insertAll(detailJualResp.getData(),true);
                }
                load(9);
            }

            @Override
            public void onFailure(Call<DetailJualResp> call, Throwable t) {
                load(9);
            }
        });

    }

}