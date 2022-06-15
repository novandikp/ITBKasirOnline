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

import com.itb.aplikasitoko.Adapter.RekapKategoriAdapter;
import com.itb.aplikasitoko.Api;
import com.itb.aplikasitoko.Database.Repository.KategoriRepository;
import com.itb.aplikasitoko.Response.RekapKategoriResp;
import com.itb.aplikasitoko.ViewModel.ViewModelRekapKategori;
import com.itb.aplikasitoko.databinding.ActivityRekapKategoriBinding;
import com.itb.aplikasitoko.util.Modul;
import com.itb.aplikasitoko.util.ModulExcel;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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

public class RekapKategori extends AppCompatActivity {
    ActivityRekapKategoriBinding bind;
    private List<ViewModelRekapKategori> data = new ArrayList<>();
    private RekapKategoriAdapter adapter;
    private KategoriRepository kategoriRepository;

    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat dateFormatter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        bind = ActivityRekapKategoriBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Rekap per Kategori");
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        setContentView(bind.getRoot());

        //manggil database (sqlite)
        kategoriRepository = new KategoriRepository(getApplication());

        bind.item.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RekapKategoriAdapter(RekapKategori.this, data);
        bind.item.setAdapter(adapter);

        init();

        bind.dateFrom.setText(Modul.getDate("yyyy-MM-dd"));
        bind.dateTo.setText(Modul.getDate("yyyy-MM-dd"));

        refreshData(true);

        //        Minta izin
        ModulExcel.askForPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE,0,RekapKategori.this,RekapKategori.this);

        bind.btnExcel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    ModulExcel.askForPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE,0,RekapKategori.this,RekapKategori.this);
                    ExportExcel();
                }catch (Exception e){
                    Toast.makeText(RekapKategori.this, "Terjadi kesalahan harap coba lagi", Toast.LENGTH_SHORT).show();
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
        for(ViewModelRekapKategori jual:data){
            total+=Modul.strToDouble(jual.getTotal_pendapatan());
        }
        bind.txtRekapKategori.setText("Rp. "+Modul.removeE(total));
    }

    public void refreshData(boolean fetch){
        String cari = bind.searchView.getQuery().toString();

//        kategoriRepository.getRekapKategori(137).observe(this, new Observer<List<ViewModelRekapKategori>>() {
//            @Override
//            public void onChanged(List<ViewModelRekapKategori> viewModelRekapKategoris) {
//                data.clear();
//                data.addAll(viewModelRekapKategoris);
//                adapter.notifyDataSetChanged();
//            }
//        });

        if (true){
            Call<RekapKategoriResp> rekapKategoriRespCall = Api.RekapKategori(RekapKategori.this).getRekapKategori(cari);
            rekapKategoriRespCall.enqueue(new Callback<RekapKategoriResp>() {
                @Override
                public void onResponse(Call<RekapKategoriResp> call, Response<RekapKategoriResp> response) {
                    if (response.isSuccessful()){
                        data.clear();
                        data.addAll(response.body().getData());

                        setTotal();
                        adapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onFailure(Call<RekapKategoriResp> call, Throwable t) {

                }
            });
        }
    }

    private void ExportExcel() throws IOException, WriteException {

        String nama = "LaporanRekapKategori";
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
                "Kategori",
                "Jumlah Penjualan",
                "Total Pendapatan"
        };
        ModulExcel.setJudul(sheet, judul);
        int row = ModulExcel.row;
        int no = 1;
        for (ViewModelRekapKategori detail : data) {
            int col = 0;
            ModulExcel.addLabel(sheet, col++, row, Modul.intToStr(no));
            ModulExcel.addLabel(sheet, col++, row, detail.getNama_kategori());
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
