package com.itb.aplikasitoko.ui.pengaturan.produk;

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

import com.itb.aplikasitoko.Adapter.ProdukAdapter;
import com.itb.aplikasitoko.Api;
import com.itb.aplikasitoko.Component.LoadingDialog;
import com.itb.aplikasitoko.Component.SuccessDialog;
import com.itb.aplikasitoko.Database.Repository.BarangRepository;
import com.itb.aplikasitoko.HomePage;
import com.itb.aplikasitoko.Model.ModelBarang;
import com.itb.aplikasitoko.R;
import com.itb.aplikasitoko.Response.BarangGetResp;
import com.itb.aplikasitoko.Response.BarangResponse;
import com.itb.aplikasitoko.databinding.ActivityMasterProdukBinding;
import com.itb.aplikasitoko.ui.pengaturan.PengaturanFragment;
import com.itb.aplikasitoko.ui.pengaturan.kategori.MasterDaftarKategori;
import com.itb.aplikasitoko.ui.pengaturan.pegawai.MasterPegawai;
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

public class MasterProduk extends AppCompatActivity {
    ActivityMasterProdukBinding bind;
    List<ModelBarang> data = new ArrayList<>();
    BarangRepository br;
    ProdukAdapter pa;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        bind = ActivityMasterProdukBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Produk");
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        setContentView(bind.getRoot());

        //memanggil db
        br = new BarangRepository(getApplication());

        //memanggil dan mengisi recyclerview
        bind.item.setLayoutManager(new LinearLayoutManager(this));
        pa = new ProdukAdapter(MasterProduk.this, data);
        bind.item.setAdapter(pa);

        refreshData(true);

        bind.searchProduk.setFocusable(false);
        bind.searchProduk.setClickable(true);

        bind.plusBtnProduk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MasterProduk.this, TambahProduk.class));
            }
        });

        //buat search
        bind.searchProduk.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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
        //inisiasi search
        String cari = bind.searchProduk.getQuery().toString();

        //ini dibuat getnya ada 2 yaitu rerofit dan sqlite, biar kalau salah satu ada error, appnya msh bisa erjalan
        //get sqlite
        br.getAllBarang(cari).observe(this, new Observer<List<ModelBarang>>() {
            @Override
            public void onChanged(List<ModelBarang> modelBarangs) {
                data.clear();
                data.addAll(modelBarangs);
                pa.notifyDataSetChanged();
            }
        });

        //get Retrofit
        if (fetch){
            Call<BarangGetResp> barangGetRespCall = Api.Barang(MasterProduk.this).getBarang(cari);
            barangGetRespCall.enqueue(new Callback<BarangGetResp>() {
                @Override
                public void onResponse(Call<BarangGetResp> call, Response<BarangGetResp> response) {
                    if (data.size() != response.body().getData().size() || !data.equals(response.body().getData())){
                        //memasukkan data ke / dr db
                        br.insertAll(response.body().getData(), true);

                        //merefresh adapter
                        data.clear();
                        data.addAll(response.body().getData());
                        pa.notifyDataSetChanged();
                    }
                }

                @Override
                public void onFailure(Call<BarangGetResp> call, Throwable t) {
                    Toast.makeText(MasterProduk.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public void DeleteProduk(String id) {
        AlertDialog.Builder alert = new AlertDialog.Builder(MasterProduk.this);
        alert.setTitle("Konfirmasi");
        alert.setMessage("Apakah anda yakin untuk menghapus data ini ?");
        alert.setPositiveButton("Iya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                LoadingDialog.load(MasterProduk.this);
                Call<BarangResponse> barangResponseCall = Api.Barang(MasterProduk.this).delBarang(id);
                barangResponseCall.enqueue(new Callback<BarangResponse>() {
                    @Override
                    public void onResponse(Call<BarangResponse> call, Response<BarangResponse> response) {
                        LoadingDialog.close();
                        if(response.isSuccessful()){
                            if (response.body().isStatus()){
                                br.delete(response.body().getData());
                                SuccessDialog.message(MasterProduk.this, getString(R.string.deleted_success), bind.getRoot());
                            }
                        } else {
                            //ErrorDialog.message(MasterProduk.this, getString(R.string.error_produk_message), bind.getRoot());
                            Toast.makeText(MasterProduk.this, response.toString(), Toast.LENGTH_SHORT).show();
                        }
                        refreshData(true);
                    }

                    @Override
                    public void onFailure(Call<BarangResponse> call, Throwable t) {
                        LoadingDialog.close();
                        Toast.makeText(MasterProduk.this, t.getMessage(), Toast.LENGTH_SHORT).show();
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

        String nama = "LaporanBarang";
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
                "Barang",
                "Harga Beli",
                "Harga Jual",

        };
        ModulExcel.setJudul(sheet, judul);
        int row = ModulExcel.row;
        int no = 1;
        for (ModelBarang detail : data) {
            int col = 0;
            ModulExcel.addLabel(sheet, col++, row, Modul.intToStr(no));
            ModulExcel.addLabel(sheet, col++, row, detail.getBarang());
            ModulExcel.addLabel(sheet, col++, row, Modul.removeE(detail.getHargabeli()));
            ModulExcel.addLabel(sheet, col++, row, Modul.removeE(detail.getHarga()));
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
                ModulExcel.askForPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE,0, MasterProduk.this,MasterProduk.this);
                ExportExcel();
            }catch (Exception e){
                Toast.makeText(MasterProduk.this, "Terjadi kesalahan harap coba lagi", Toast.LENGTH_SHORT).show();
            }
        } return true;
    }

}
