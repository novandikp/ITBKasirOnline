package com.itb.aplikasitoko.ui.pengaturan.pegawai;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.itb.aplikasitoko.Adapter.PegawaiAdapter;
import com.itb.aplikasitoko.Api;
import com.itb.aplikasitoko.Component.ErrorDialog;
import com.itb.aplikasitoko.Component.LoadingDialog;
import com.itb.aplikasitoko.Component.SuccessDialog;
import com.itb.aplikasitoko.Database.Repository.PegawaiRepository;
import com.itb.aplikasitoko.HomePage;
import com.itb.aplikasitoko.Model.ModelPegawai;
import com.itb.aplikasitoko.R;
import com.itb.aplikasitoko.Response.PegawaiGetResp;
import com.itb.aplikasitoko.Response.PegawaiResponse;
import com.itb.aplikasitoko.Service.PegawaiService;
import com.itb.aplikasitoko.databinding.ActivityMasterPegawaiBinding;
import com.itb.aplikasitoko.ui.pengaturan.pelanggan.MasterPelanggan;
import com.itb.aplikasitoko.util.Modul;
import com.itb.aplikasitoko.util.ModulExcel;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MasterPegawai extends AppCompatActivity {
    ActivityMasterPegawaiBinding bind;
    List<ModelPegawai> data = new ArrayList<>();
    PegawaiAdapter pa;
    PegawaiRepository pr;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        bind = ActivityMasterPegawaiBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Pegawai");
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        setContentView(bind.getRoot());

        //memanggil db
        pr = new PegawaiRepository(getApplication());

        //menginisiasi adapter dan recycler view
        bind.item.setLayoutManager(new LinearLayoutManager(this));
        pa = new PegawaiAdapter(MasterPegawai.this, data);
        bind.item.setAdapter(pa);

        refreshData(true);

        bind.plusBtnPegawai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MasterPegawai.this, TambahPegawai.class));

            }
        });

        bind.searchPegawai.setFocusable(false);
        bind.searchPegawai.setClickable(true);

        //buat search
        bind.searchPegawai.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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



    public void refreshData(boolean fetch){
        LoadingDialog.load(MasterPegawai.this);
        //inisiasi cari dr file layout
        String cari = bind.searchPegawai.getQuery().toString();
        //get SQLite
        pr.getAllPegawai(cari).observe(this, new Observer<List<ModelPegawai>>() {
            @Override
            public void onChanged(List<ModelPegawai> modelPegawais) {
                LoadingDialog.close();
                data.clear();
                data.addAll(modelPegawais);
                pa.notifyDataSetChanged();
            }
        });
        if(fetch) {
            //get Retrofit
            PegawaiService ps = Api.Pegawai(MasterPegawai.this);
            Call<PegawaiGetResp> pegawaiGetRespCall = ps.getPeg(cari);
            pegawaiGetRespCall.enqueue(new Callback<PegawaiGetResp>() {
                @Override
                public void onResponse(Call<PegawaiGetResp> call, Response<PegawaiGetResp> response) {
                    LoadingDialog.close();
                    if (data.size() != response.body().getData().size() || !data.equals(response.body().getData())) {
                        //Memasukkan ke db kalau gada yg sm
                        pr.insertAll(response.body().getData(), true);

                        //merefresh adapter
                        data.clear();
                        data.addAll(response.body().getData());
                        pa.notifyDataSetChanged();
                    }
                }

                @Override
                public void onFailure(Call<PegawaiGetResp> call, Throwable t) {
                    LoadingDialog.close();
                    Toast.makeText(MasterPegawai.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public void DeletePeg(int id){
        AlertDialog.Builder alert = new AlertDialog.Builder(MasterPegawai.this);
        alert.setTitle("Konfirmasi");
        alert.setMessage("Anda yakin ingin menghapus data ini?");
        alert.setPositiveButton("iya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                LoadingDialog.load(MasterPegawai.this);
                Call<PegawaiResponse> modelPegawaiCall = Api.Pegawai(MasterPegawai.this).deletePeg(id);
                modelPegawaiCall.enqueue(new Callback<PegawaiResponse>() {
                    @Override
                    public void onResponse(Call<PegawaiResponse> call, Response<PegawaiResponse> response) {
                        LoadingDialog.close();
                        if (response.isSuccessful()){
                            if (response.body().isStatus()){
                                pr.delete(response.body().getData());
                                SuccessDialog.message(MasterPegawai.this, getString(R.string.deleted_success), bind.getRoot());
                            }
                        } else {
                            ErrorDialog.message(MasterPegawai.this, getString(R.string.error_pegawai_message), bind.getRoot());
                        }
                        refreshData(true);
                    }

                    @Override
                    public void onFailure(Call<PegawaiResponse> call, Throwable t) {
                        LoadingDialog.close();
                        Toast.makeText(MasterPegawai.this, t.getMessage(), Toast.LENGTH_SHORT).show();
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


    private void ExportExcel() throws IOException, WriteException {

        String nama = "LaporanPegawai";
        String path = Environment.getExternalStorageDirectory().toString() + "/Download/";
        File file = new File(path + nama + " " + Modul.getDate("dd-MM-yyyy_HHmmss") + ".xls");
        WorkbookSettings wbSettings = new WorkbookSettings();

        wbSettings.setLocale(new Locale("en", "EN"));

        WritableWorkbook workbook = Workbook.createWorkbook(file, wbSettings);
        workbook.createSheet("Report", 0);
        WritableSheet sheet = workbook.getSheet(0);

        ModulExcel.createLabel(sheet);
//        setHeader(db,sheet,5);
        ModulExcel.excelNextLine(sheet, 2);

        String[] judul = {"No.",
                "Pelanggan",
                "Alamat",
                "No Telepon"
        };
        ModulExcel.setJudul(sheet, judul);
        int row = ModulExcel.row;
        int no = 1;
        for (ModelPegawai detail : data) {
            int col = 0;
            ModulExcel.addLabel(sheet, col++, row, Modul.intToStr(no));
            ModulExcel.addLabel(sheet, col++, row, detail.getNama_pegawai());
            ModulExcel.addLabel(sheet, col++, row, detail.getAlamat_pegawai());
            ModulExcel.addLabel(sheet, col++, row, detail.getNo_pegawai());
            row++;
            no++;
        }
        workbook.write();
        workbook.close();
        Toast.makeText(this, "Berhasil disimpan di "+file.getPath(), Toast.LENGTH_SHORT).show();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_export,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        } else if (id == R.id.export) {
            try {
                ModulExcel.askForPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE,0, MasterPegawai.this,MasterPegawai.this);
                ExportExcel();
            }catch (Exception e){
                Toast.makeText(MasterPegawai.this, "Terjadi kesalahan harap coba lagi", Toast.LENGTH_SHORT).show();
            }
        } return true;
    }


}
