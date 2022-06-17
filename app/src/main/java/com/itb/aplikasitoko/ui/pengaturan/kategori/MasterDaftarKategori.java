package com.itb.aplikasitoko.ui.pengaturan.kategori;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import com.itb.aplikasitoko.Adapter.KategoriAdapter;
import com.itb.aplikasitoko.Api;
import com.itb.aplikasitoko.Component.ErrorDialog;
import com.itb.aplikasitoko.Component.LoadingDialog;
import com.itb.aplikasitoko.Component.SuccessDialog;
import com.itb.aplikasitoko.Database.Repository.KategoriRepository;
import com.itb.aplikasitoko.Model.ModelKategori;
import com.itb.aplikasitoko.R;
import com.itb.aplikasitoko.Response.KategoriGetResp;
import com.itb.aplikasitoko.Response.KategoriResponse;
import com.itb.aplikasitoko.Service.KategoriService;
import com.itb.aplikasitoko.databinding.ActivityMasterDaftarKategoriBinding;
import com.itb.aplikasitoko.databinding.DialogAddKategoriBinding;
import com.itb.aplikasitoko.databinding.DialogDetailKategoriBinding;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MasterDaftarKategori extends AppCompatActivity {
    ActivityMasterDaftarKategoriBinding bind;
    List<ModelKategori> data= new ArrayList<>();
    KategoriAdapter adapter;
    KategoriRepository kategoriRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        bind = ActivityMasterDaftarKategoriBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Kategori");
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        setContentView(bind.getRoot());
//       Repo sqlite
        //manggil database (sqlite)
        kategoriRepository = new KategoriRepository(getApplication());

//      Definisi Reclerview
        bind.item.setLayoutManager(new LinearLayoutManager(this));
        adapter = new KategoriAdapter(MasterDaftarKategori.this, data);
        bind.item.setAdapter(adapter);

        refreshData(true);

        bind.plusBtnKategori.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ModelKategori modelKategori = new ModelKategori();
                dialogAddKategori(modelKategori);
            }
        });

        bind.searchKategori.setFocusable(false);
        bind.searchKategori.setClickable(true);

        bind.searchKategori.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                refreshData(false);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty()){
                    refreshData(false);
                }
                return false;
            }
        });


//        inKategori = bind.eKategori;
//        refreshData();
//        bind.btnSimpan.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String nama_kategori = inKategori.getText().toString();
//                if(nama_kategori.isEmpty()){
//                    inKategori.setError("Harap isi dengan benar");
//                }else{
//                    inKategori.setError(null);
//                    ModelKategori data = new ModelKategori(nama_kategori);
//                    PostKat(data);
//                }
//
//            }
//        });

    }

    public void refreshData(boolean fetch) {
        LoadingDialog.load(MasterDaftarKategori.this);
        String cari = bind.searchKategori.getQuery().toString();

        // Get sqlite
        kategoriRepository.getAllKategori(cari).observe(this, new Observer<List<ModelKategori>>() {
            @Override
            public void onChanged(List<ModelKategori> kategoris) {
                LoadingDialog.close();
                data.clear();
                data.addAll(kategoris);
                adapter.notifyDataSetChanged();
            }
        });
        // Get Retrofit
        if (true){
            KategoriService ks = Api.Kategori(MasterDaftarKategori.this);
            Call<KategoriGetResp> call = ks.getKat(cari);
            call.enqueue(new Callback<KategoriGetResp>() {
                @Override
                public void onResponse(Call<KategoriGetResp> call, Response<KategoriGetResp> response) {
                    LoadingDialog.close();
                    if(response.body().getStatus()) {
                        if (data.size() != response.body().getData().size() || !data.equals(response.body().getData())) {
                            //  Masukan data pada sqlite jika data tidak ada
                            kategoriRepository.insertAll(response.body().getData(), true);
                            // Refresh adapter
                            data.clear();
                            data.addAll(response.body().getData());
                            adapter.notifyDataSetChanged();
                        }
                    }
                }

                @Override
                public void onFailure(Call<KategoriGetResp> call, Throwable t) {
                    LoadingDialog.close();
                    Toast.makeText(MasterDaftarKategori.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }


    public void dialogEditKategori(ModelKategori modelKategori){
        DialogDetailKategoriBinding binder = DialogDetailKategoriBinding.inflate(LayoutInflater.from(this));
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setView(binder.getRoot());
        alertBuilder.setTitle("EDIT KATEGORI");
        AlertDialog dialog = alertBuilder.create();
        binder.txtKategori.setText(modelKategori.getNama_kategori());
        binder.btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nama_kategori = binder.txtKategori.getText().toString();
                if(nama_kategori.isEmpty()){
                    binder.txtKategori.setError("Harap isi dengan benar");
                }else {
                    binder.txtKategori.setError(null);
                    modelKategori.setNama_kategori(nama_kategori);
                    UpdateKat(modelKategori.getIdkategori(), modelKategori);
                    dialog.cancel();

                }
            }
        });
        dialog.show();
    }

    public void dialogAddKategori(ModelKategori modelKategori){
        DialogAddKategoriBinding binder = DialogAddKategoriBinding.inflate(LayoutInflater.from(this));
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setView(binder.getRoot());
        alertBuilder.setTitle("ADD KATEGORI");
        AlertDialog dialog = alertBuilder.create();
        binder.txtAddKategori.setText(modelKategori.getNama_kategori());
        binder.btnAddKategori.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nama_kategori = binder.txtAddKategori.getText().toString();
                if(nama_kategori.isEmpty()){
                    binder.txtAddKategori.setError("Harap isi dengan benar");
                }else {
                    binder.txtAddKategori.setError(null);
                    modelKategori.setNama_kategori(nama_kategori);
                    PostKat(modelKategori);
                    dialog.cancel();

                }
            }
        });
        dialog.show();
    }

    public void PostKat(ModelKategori modelKategori){
//        bind.btnSimpan.setEnabled(false);
        LoadingDialog.load(this);
        Call<KategoriResponse> kategoriResponseCall = Api.Kategori(MasterDaftarKategori.this).postKat(modelKategori);
        kategoriResponseCall.enqueue(new Callback<KategoriResponse>() {
            @Override
            public void onResponse(Call<KategoriResponse> call, Response<KategoriResponse> response) {
                LoadingDialog.close();
//                EditText inKategori = bind.eKategori;
                if (response.isSuccessful()) {
                    SuccessDialog.message(MasterDaftarKategori.this,getString(R.string.success_added),bind.getRoot());

                    kategoriRepository.insert(modelKategori);
                    data.add(response.body().getData());
                    adapter.notifyItemInserted(data.size());
                } else {
                    ErrorDialog.message(MasterDaftarKategori.this,getString(R.string.add_kategori_error),bind.getRoot());

                }
//                bind.btnSimpan.setEnabled(true);
            }
            @Override
            public void onFailure(Call<KategoriResponse> call, Throwable t) {
                LoadingDialog.close();
                ErrorDialog.message(MasterDaftarKategori.this,getString(R.string.error_fetch), bind.getRoot());
//                bind.btnSimpan.setEnabled(true);
            }
        });
    }

    public void DeleteKat(int id){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Konfirmasi");
        alert.setMessage("Apakah anda yakin untuk menghapus data ini ?");
        alert.setPositiveButton("Iya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                LoadingDialog.load(MasterDaftarKategori.this);
                Call<KategoriResponse> kategoriResponseCall = Api.Kategori(MasterDaftarKategori.this).deleteKat(id);
                kategoriResponseCall.enqueue(new Callback<KategoriResponse>() {
                    @Override
                    public void onResponse(Call<KategoriResponse> call, Response<KategoriResponse> response) {
                        LoadingDialog.close();
                        if(response.isSuccessful()){
                            if(response.body().isStatus()) {
                                kategoriRepository.delete(response.body().getData()); //get data krn hasilnya get kategori
                                SuccessDialog.message(
                                        MasterDaftarKategori.this,
                                        getString(R.string.deleted_success),
                                        bind.getRoot());
                            }
                        }else{
                            ErrorDialog.message(
                                    MasterDaftarKategori.this,
                                    getString(R.string.error_kategori_message),
                                    bind.getRoot());
                        }
                        refreshData(true);

                    }

                    @Override
                    public void onFailure(Call<KategoriResponse> call, Throwable t) {

                        LoadingDialog.close();
                        ErrorDialog.message(MasterDaftarKategori.this,getString(R.string.error_fetch), bind.getRoot());
                    }
                });
            }
        }).setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        alert.show();
    }

    public void UpdateKat(int id, ModelKategori kategori){
        LoadingDialog.load(this);
        Call<KategoriResponse> kategoriResponseCall = Api.Kategori(this).updateKat(id, kategori);
        kategoriResponseCall.enqueue(new Callback<KategoriResponse>() {
            @Override
            public void onResponse(Call<KategoriResponse> call, Response<KategoriResponse> response) {
                LoadingDialog.close();
                if(response.isSuccessful()){
                    if(response.body().isStatus()){
                        kategoriRepository.update(kategori);
                        refreshData(true);

                        SuccessDialog.message(
                                MasterDaftarKategori.this,
                                getString(R.string.updated_success),
                                bind.getRoot());
                    }
                }else{
                    ErrorDialog.message(MasterDaftarKategori.this,
                            getString(R.string.updated_error),bind.getRoot());
                }

            }

            @Override
            public void onFailure(Call<KategoriResponse> call, Throwable t) {

                LoadingDialog.close();
                ErrorDialog.message(MasterDaftarKategori.this,getString(R.string.error_fetch), bind.getRoot());
            }
        });
    }



}