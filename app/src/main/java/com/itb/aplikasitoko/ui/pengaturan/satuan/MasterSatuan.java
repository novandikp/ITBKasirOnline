package com.itb.aplikasitoko.ui.pengaturan.satuan;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.itb.aplikasitoko.Adapter.SatuanAdapter;
import com.itb.aplikasitoko.Api;
import com.itb.aplikasitoko.Component.ErrorDialog;
import com.itb.aplikasitoko.Component.LoadingDialog;
import com.itb.aplikasitoko.Component.SuccessDialog;
import com.itb.aplikasitoko.Database.Repository.SatuanRepository;
import com.itb.aplikasitoko.Model.ModelSatuan;
import com.itb.aplikasitoko.R;
import com.itb.aplikasitoko.Response.SatuanGetResp;
import com.itb.aplikasitoko.Response.SatuanResponse;
import com.itb.aplikasitoko.Service.SatuanService;
import com.itb.aplikasitoko.databinding.ActivityMasterSatuanBinding;
import com.itb.aplikasitoko.databinding.DialogAddSatuanBinding;
import com.itb.aplikasitoko.databinding.DialogDetailSatuanBinding;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MasterSatuan extends AppCompatActivity {
    ActivityMasterSatuanBinding bind;
    List<ModelSatuan> data = new ArrayList<>();
    SatuanAdapter adapter;
    SatuanRepository satuanRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        bind = ActivityMasterSatuanBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Satuan");
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        setContentView(bind.getRoot());

        //memanggil database
        satuanRepository = new SatuanRepository(getApplication());

        //mendefinisikan recyclerview
        bind.item.setLayoutManager(new LinearLayoutManager(this));
        adapter = new SatuanAdapter(MasterSatuan.this, data);
        bind.item.setAdapter(adapter);

        refreshData(true);

        bind.searchSatuan.setFocusable(false);
        bind.searchSatuan.setClickable(true);

//        bind.btnSimpan.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String nama_satuan = inSatuan.getText().toString();
//                if(nama_satuan.isEmpty()){
//                    inSatuan.setError("Harap isi dengan benar");
//
//                } else {
//                    inSatuan.setError(null);
//                    ModelSatuan data = new ModelSatuan(nama_satuan);
//                    PostSat(data);
//                }
//            }
//        });

        bind.plusBtnSatuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ModelSatuan modelSatuan = new ModelSatuan();
                dialogAddSatuan(modelSatuan);
            }
        });

        bind.searchSatuan.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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
    }

    public void dialogEditSatuan(ModelSatuan modelSatuan){
        DialogDetailSatuanBinding binder = DialogDetailSatuanBinding.inflate(LayoutInflater.from(this));
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setView(binder.getRoot());
        alertBuilder.setTitle("EDIT SATUAN");
        AlertDialog dialog = alertBuilder.create();
        binder.txtSatuan.setText(modelSatuan.getNama_satuan());
        binder.btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nama_satuan = binder.txtSatuan.getText().toString();
                if (nama_satuan.isEmpty()){
                    binder.txtSatuan.setError("Harap isi dengan benar");

                } else {
                    binder.txtSatuan.setError(null);
                    modelSatuan.setNama_satuan(nama_satuan);
                    UpdateSat(modelSatuan.getIdsatuan(), modelSatuan);
                    dialog.cancel();
                }
            }
        });
        dialog.show();
    }

    public void dialogAddSatuan(ModelSatuan modelSatuan){
        DialogAddSatuanBinding binder = DialogAddSatuanBinding.inflate(LayoutInflater.from(this));
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setView(binder.getRoot());
        alertBuilder.setTitle("ADD SATUAN");
        AlertDialog dialog = alertBuilder.create();
        binder.txtAddSatuan.setText(modelSatuan.getNama_satuan());
        binder.btnAddSatuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nama_satuan = binder.txtAddSatuan.getText().toString();
                if (nama_satuan.isEmpty()){
                    binder.txtAddSatuan.setError("Harap isi dengan benar");

                } else {
                    binder.txtAddSatuan.setError(null);
                    modelSatuan.setNama_satuan(nama_satuan);
                    PostSat(modelSatuan);
                    dialog.cancel();
                }
            }
        });
        dialog.show();
    }

    public void refreshData(boolean fetch){
        LoadingDialog.load(MasterSatuan.this);
        String cari = bind.searchSatuan.getQuery().toString();

        //get sqlite
        satuanRepository.getAllSatuan(cari).observe(this, new Observer<List<ModelSatuan>>() {
            @Override
            public void onChanged(List<ModelSatuan> modelSatuans) {
                LoadingDialog.close();
                data.clear();
                data.addAll(modelSatuans);
                adapter.notifyDataSetChanged();
            }
        });

        //get retrofit
        if (true){
            SatuanService ss = Api.Satuan(MasterSatuan.this);
            Call<SatuanGetResp> call = ss.getSat(cari);
            call.enqueue(new Callback<SatuanGetResp>() {
                @Override
                public void onResponse(Call<SatuanGetResp> call, Response<SatuanGetResp> response) {
                    LoadingDialog.close();
                    if (data.size() != response.body().getData().size() || !data.equals(response.body().getData())) {
                        // memasukkan inputan ke sqlite jika tak ada yg sama
                        satuanRepository.insertAll(response.body().getData(), true);
                        //merefresh adapter
                        data.clear();
                        data.addAll(response.body().getData());
                        adapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onFailure(Call<SatuanGetResp> call, Throwable t) {
                    LoadingDialog.close();
                    Toast.makeText(MasterSatuan.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }


    public void PostSat(ModelSatuan modelSatuan){
        LoadingDialog.load(this);
        Call<SatuanResponse> satuanResponseCall = Api.Satuan(MasterSatuan.this).postSat(modelSatuan);
        satuanResponseCall.enqueue(new Callback<SatuanResponse>() {
            @Override
            public void onResponse(Call<SatuanResponse> call, Response<SatuanResponse> response) {
                LoadingDialog.close();
                if (response.isSuccessful() && response.body().isStatus()) {
                    SuccessDialog.message(MasterSatuan.this, getString(R.string.success_added), bind.getRoot());

                    satuanRepository.insert(modelSatuan);
                    data.add(response.body().getData());
                    adapter.notifyItemInserted(data.size());
                } else {
                    ErrorDialog.message(MasterSatuan.this, getString(R.string.add_kategori_error), bind.getRoot());
                }
            }

            @Override
            public void onFailure(Call<SatuanResponse> call, Throwable t) {
                LoadingDialog.close();
                Toast.makeText(MasterSatuan.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void DeleteSat(int id){
        AlertDialog.Builder alert = new AlertDialog.Builder(MasterSatuan.this);
        alert.setTitle("Konfirmasi");
        alert.setMessage("Apakah anda yakin untuk menghapus data ini ?");
        alert.setPositiveButton("Iya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                LoadingDialog.load(MasterSatuan.this);
                Call<SatuanResponse> satuanResponseCall = Api.Satuan(MasterSatuan.this).deleteSat(id);
                satuanResponseCall.enqueue(new Callback<SatuanResponse>() {
                    @Override
                    public void onResponse(Call<SatuanResponse> call, Response<SatuanResponse> response) {
                        LoadingDialog.close();
                        if(response.isSuccessful()) {
                            if(response.body().isStatus()){
                                satuanRepository.delete(response.body().getData()); //dia menghapus dr data dlm body
                                SuccessDialog.message(MasterSatuan.this, getString(R.string.deleted_success), bind.getRoot());
                            }
                        } else {
                            ErrorDialog.message(MasterSatuan.this, getString(R.string.error_satuan_message), bind.getRoot());
                        }

                        refreshData(true);
                    }

                    @Override
                    public void onFailure(Call<SatuanResponse> call, Throwable t) {
                        LoadingDialog.close();
                        Toast.makeText(MasterSatuan.this, t.getMessage(), Toast.LENGTH_SHORT).show();
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

    public void UpdateSat(int id, ModelSatuan modelSatuan){
        LoadingDialog.load(MasterSatuan.this);
        Call<SatuanResponse> satuanResponseCall = Api.Satuan(MasterSatuan.this).updateSat(id, modelSatuan);
        satuanResponseCall.enqueue(new Callback<SatuanResponse>() {
            @Override
            public void onResponse(Call<SatuanResponse> call, Response<SatuanResponse> response) {
                LoadingDialog.close();
                if(response.isSuccessful()){
                    if (response.body().isStatus()){
                        satuanRepository.update(modelSatuan);
                        refreshData(true);

                        SuccessDialog.message(MasterSatuan.this,getString(R.string.updated_success) ,bind.getRoot());
                    }
                } else {
                    ErrorDialog.message(MasterSatuan.this, getString(R.string.updated_error), bind.getRoot());
                }
            }

            @Override
            public void onFailure(Call<SatuanResponse> call, Throwable t) {
                LoadingDialog.close();
                Toast.makeText(MasterSatuan.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
