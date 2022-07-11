package com.itb.aplikasitoko.ui.laporan;

import android.Manifest;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.DatePicker;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.itb.aplikasitoko.Adapter.LapPendapatanAdapter;
import com.itb.aplikasitoko.Api;
import com.itb.aplikasitoko.Component.LoadingDialog;
import com.itb.aplikasitoko.Database.Repository.JualRepository;
import com.itb.aplikasitoko.Response.PendapatanGetResp;
import com.itb.aplikasitoko.ViewModel.ViewModelJual;
import com.itb.aplikasitoko.databinding.ActivityLaporanPendapatanBinding;
import com.itb.aplikasitoko.util.Modul;
import com.itb.aplikasitoko.util.ModulExcel;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
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

//retrofit
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LaporanPendapatan extends AppCompatActivity {
    ActivityLaporanPendapatanBinding bind;
    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat dateFormatter;
    private LapPendapatanAdapter adapter;
    private List<ViewModelJual> data = new ArrayList<>();

    JualRepository jualRepository;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        bind = ActivityLaporanPendapatanBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        jualRepository = new JualRepository(getApplication());
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Laporan Pendapatan");
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        setContentView(bind.getRoot());

        init();
        bind.dateFrom.setText(Modul.getDate("yyyy-MM-dd"));
        bind.dateTo.setText(Modul.getDate("yyyy-MM-dd"));

        //inisiasi recyclerview
        bind.itemPendapatan.setLayoutManager(new LinearLayoutManager(this));
        adapter = new LapPendapatanAdapter(LaporanPendapatan.this, data);
        bind.itemPendapatan.setAdapter(adapter);

        refreshData(true);
//        Minta izin
        ModulExcel.askForPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE,0,LaporanPendapatan.this,LaporanPendapatan.this);

        bind.btnExcel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    ModulExcel.askForPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE,0,LaporanPendapatan.this,LaporanPendapatan.this);
                    ExportExcel();
                }catch (Exception e){
                    Toast.makeText(LaporanPendapatan.this, "Terjadi kesalahan harap coba lagi", Toast.LENGTH_SHORT).show();
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
        for(ViewModelJual jual:data){
            total+=jual.getTotal();
        }
        if (data.size() == 0) {
            bind.itemPendapatan.setVisibility(View.GONE);
            bind.txtKosong.setVisibility(View.VISIBLE);
        }else{
            bind.itemPendapatan.setVisibility(View.VISIBLE);
            bind.txtKosong.setVisibility(View.GONE);
        }
        bind.txtTotalPendapatan.setText("Rp. "+Modul.removeE(total));
    }

    public void refreshData(boolean fetch){
        LoadingDialog.load(LaporanPendapatan.this);
        String cari = bind.searchView.getQuery().toString();
        String mulai = bind.dateFrom.getText().toString();
        String sampai = bind.dateTo.getText().toString();
        jualRepository.getPendapatan(cari,mulai,sampai).observe(this, new Observer<List<ViewModelJual>>() {
            @Override
            public void onChanged(List<ViewModelJual> viewModelJuals) {
                if(viewModelJuals.size()>0){
                    LoadingDialog.close();
                    data.clear();
                    data.addAll(viewModelJuals);
                    setTotal();
                    adapter.notifyDataSetChanged();
                }
            }
        });

        if (true){

            Call<PendapatanGetResp> pendapatanGetRespCall = Api.Pendapatan(LaporanPendapatan.this).getPendapatan(mulai, sampai, cari);
            pendapatanGetRespCall.enqueue(new Callback<PendapatanGetResp>() {
                @Override
                public void onResponse(Call<PendapatanGetResp> call, Response<PendapatanGetResp> response) {
                    LoadingDialog.close();
                    data.clear();
                    if (response.isSuccessful()){
                        data.addAll(response.body().getData());
                    }
                    setTotal();
                    adapter.notifyDataSetChanged();

                }

                @Override
                public void onFailure(Call<PendapatanGetResp> call, Throwable t) {
                    LoadingDialog.close();
                    Toast.makeText(LaporanPendapatan.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

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


    private void ExportExcel() throws IOException, WriteException {

        String nama = "LaporanPendapatan";
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
                "Tanggal",
                "Faktur",
                "Pelanggan",
                "Total",
                "Bayar",
                "Kembali"
        };
        ModulExcel.setJudul(sheet, judul);
        int row = ModulExcel.row;
        int no = 1;
        for (ViewModelJual jual : data) {
            int col = 0;
            ModulExcel.addLabel(sheet, col++, row, Modul.intToStr(no));
            try {
                ModulExcel.addLabel(sheet, col++, row, Modul.tanggalku(jual.getTanggal_jual()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            ModulExcel.addLabel(sheet, col++, row, jual.getFakturjual());
            ModulExcel.addLabel(sheet, col++, row, jual.getNama_pelanggan());
            ModulExcel.addLabel(sheet, col++, row, "Rp. "+Modul.removeE(jual.getTotal()));
            ModulExcel.addLabel(sheet, col++, row, "Rp. "+Modul.removeE(jual.getBayar()));
            ModulExcel.addLabel(sheet, col++, row, "Rp. "+Modul.removeE(jual.getKembali()));
            row++;
            no++;
        }
        workbook.write();
        workbook.close();
        Toast.makeText(this, "Berhasil disimpan di "+file.getPath(), Toast.LENGTH_SHORT).show();

    }

}