package com.itb.aplikasitoko.ui.laporan;

import android.Manifest;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.DatePicker;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.itb.aplikasitoko.Adapter.RekapBarangAdapter;
import com.itb.aplikasitoko.Api;
import com.itb.aplikasitoko.Component.LoadingDialog;
import com.itb.aplikasitoko.Response.RekapBarangResp;
import com.itb.aplikasitoko.ViewModel.ViewModelRekapBarang;
import com.itb.aplikasitoko.databinding.ActivityRekapBarangBinding;
import com.itb.aplikasitoko.util.Modul;
import com.itb.aplikasitoko.util.ModulExcel;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

//export excell
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RekapBarang extends AppCompatActivity {
    ActivityRekapBarangBinding bind;
    private RekapBarangAdapter adapter;
    private List<ViewModelRekapBarang> data = new ArrayList<>();

    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat dateFormatter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        bind = ActivityRekapBarangBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Rekap per Barang");
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        setContentView(bind.getRoot());

        //Recyclerview
        bind.item.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RekapBarangAdapter(RekapBarang.this, data);
        bind.item.setAdapter(adapter);

        init();

        bind.dateFrom.setText(Modul.getDate("yyyy-MM-dd"));
        bind.dateTo.setText(Modul.getDate("yyyy-MM-dd"));

        refreshData(true);

        //        Minta izin
        ModulExcel.askForPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE,0,RekapBarang.this,RekapBarang.this);

        bind.btnExcel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    ModulExcel.askForPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE,0,RekapBarang.this,RekapBarang.this);
                    ExportExcel();
                }catch (Exception e){
                    Toast.makeText(RekapBarang.this, "Terjadi kesalahan harap coba lagi", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void init(){
        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

        bind.searchView.setFocusable(false);
        bind.dateFrom.setFocusable(false);
        bind.dateFrom.setClickable(true);
        bind.dateTo.setFocusable(false);
        bind.dateTo.setClickable(true);

        bind.dateFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateFrom();
            }
        });

        bind.dateTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateTo();
            }
        });

//        bind.icSearch.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                bind.layouticsearch.setVisibility(View.GONE);
//                bind.layoutpenjualan.setVisibility(View.GONE);
//                bind.layouttotalpenjualan.setVisibility(View.GONE);
//
//                bind.layoutsearch.setVisibility(View.VISIBLE);
//            }
//        });

        bind.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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

    public void setTotal(){
        double total=0;
        for(ViewModelRekapBarang jual:data){
            total+=Modul.strToDouble(jual.getTotal_pendapatan());
        }
        if (data.size() == 0) {
            bind.item.setVisibility(View.GONE);
            bind.txtKosong.setVisibility(View.VISIBLE);
        }else{
            bind.item.setVisibility(View.VISIBLE);
            bind.txtKosong.setVisibility(View.GONE);
        }
        bind.txtRekapBarang.setText("Rp. "+Modul.removeE(total));
    }

    public void refreshData(boolean fetch){
        LoadingDialog.load(RekapBarang.this);
        String cari = bind.searchView.getQuery().toString();
        String mulai = bind.dateFrom.getText().toString(); 
        String sampai = bind.dateTo.getText().toString();
        if (true){
            Call<RekapBarangResp> rekapBarangRespCall = Api.RekapBarang(RekapBarang.this).getRekapBarang(cari, mulai, sampai);
            rekapBarangRespCall.enqueue(new Callback<RekapBarangResp>() {
                @Override
                public void onResponse(Call<RekapBarangResp> call, Response<RekapBarangResp> response) {
                    LoadingDialog.close();
                    data.clear();
                    if (response.isSuccessful()){

                        data.addAll(response.body().getData());

                    }
                    setTotal();

                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onFailure(Call<RekapBarangResp> call, Throwable t) {
                    LoadingDialog.close();
                    Toast.makeText(RekapBarang.this, t.getMessage(), Toast.LENGTH_SHORT).show();

                }
            });
        }
    }

    private void ExportExcel() throws IOException, WriteException {

        String nama = "LaporanRekapBarang";
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
                "Kode Barang",
                "Barang",
                "Kategori",
                "Satuan",
                "Jumlah Penjualan",
                "Total Pendapatan"
        };
        ModulExcel.setJudul(sheet, judul);
        int row = ModulExcel.row;
        int no = 1;
        for (ViewModelRekapBarang detail : data) {
            int col = 0;
            ModulExcel.addLabel(sheet, col++, row, Modul.intToStr(no));
            ModulExcel.addLabel(sheet, col++, row, detail.getIdbarang());
            ModulExcel.addLabel(sheet, col++, row, detail.getBarang());
            ModulExcel.addLabel(sheet, col++, row, detail.getNama_kategori());
            ModulExcel.addLabel(sheet, col++, row, detail.getNama_satuan());
            ModulExcel.addLabel(sheet, col++, row, detail.getTotal_jual());
            ModulExcel.addLabel(sheet, col++, row, "Rp. "+Modul.removeE(detail.getTotal_pendapatan()));
            row++;
            no++;
        }
        workbook.write();
        workbook.close();
        Toast.makeText(this, "Berhasil disimpan di "+file.getPath(), Toast.LENGTH_SHORT).show();

    }

    public void showDateFrom(){
        Calendar kalender = Calendar.getInstance();

        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, month, dayOfMonth);

                bind.dateFrom.setText(dateFormatter.format(newDate.getTime()));
                refreshData(true);
            }
        }, kalender.get(Calendar.YEAR), kalender.get(Calendar.MONTH), kalender.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.show();
    }

    public void showDateTo(){
        Calendar kalender = Calendar.getInstance();

        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, month, dayOfMonth);

                bind.dateTo.setText(dateFormatter.format(newDate.getTime()));
                refreshData(true);
            }
        }, kalender.get(Calendar.YEAR), kalender.get(Calendar.MONTH), kalender.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.show();
    }

}
