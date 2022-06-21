package com.itb.aplikasitoko.ui.laporan;

import android.Manifest;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.os.Environment;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.itb.aplikasitoko.Adapter.RekapPelangganAdapter;
import com.itb.aplikasitoko.Api;
import com.itb.aplikasitoko.Component.LoadingDialog;
import com.itb.aplikasitoko.Response.RekapPelangganResp;
import com.itb.aplikasitoko.ViewModel.ViewModelRekapPelanggan;
import com.itb.aplikasitoko.databinding.ActivityRekapPelangganBinding;
import com.itb.aplikasitoko.util.Modul;
import com.itb.aplikasitoko.util.ModulExcel;

import java.text.SimpleDateFormat;
import java.io.File;
import java.io.IOException;
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

public class RekapPelanggan extends AppCompatActivity {
    ActivityRekapPelangganBinding bind;
    private List<ViewModelRekapPelanggan> data = new ArrayList<>();
    private RekapPelangganAdapter adapter;

    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat dateFormatter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        bind = ActivityRekapPelangganBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Rekap per Pelanggan");
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        setContentView(bind.getRoot());

        bind.item.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RekapPelangganAdapter(RekapPelanggan.this, data);
        bind.item.setAdapter(adapter);

        init();

        bind.dateFrom.setText(Modul.getDate("yyyy-MM-dd"));
        bind.dateTo.setText(Modul.getDate("yyyy-MM-dd"));

        refreshData(true);

        //        Minta izin
        ModulExcel.askForPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE,0,RekapPelanggan.this,RekapPelanggan.this);

        bind.btnExcel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    ModulExcel.askForPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE,0,RekapPelanggan.this,RekapPelanggan.this);
                    ExportExcel();
                }catch (Exception e){
                    Toast.makeText(RekapPelanggan.this, "Terjadi kesalahan harap coba lagi", Toast.LENGTH_SHORT).show();
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
        for(ViewModelRekapPelanggan jual:data){
            total+=Modul.strToDouble(jual.getTotal_pendapatan());
        }
        bind.txtRekapPelanggan.setText("Rp. "+Modul.removeE(total));
    }

    public void refreshData(boolean fetch){
        LoadingDialog.load(RekapPelanggan.this);
        String cari = bind.searchView.getQuery().toString();
        String mulai = bind.dateFrom.getText().toString(); 
        String sampai = bind.dateTo.getText().toString();
        if (true){
            Call<RekapPelangganResp> rekapPelangganRespCall = Api.RekapPelanggan(RekapPelanggan.this).getRekapPelanggan(cari, mulai, sampai);
            rekapPelangganRespCall.enqueue(new Callback<RekapPelangganResp>() {
                @Override
                public void onResponse(Call<RekapPelangganResp> call, Response<RekapPelangganResp> response) {
                    LoadingDialog.close();
                    data.clear();
                    if (response.isSuccessful()){

                        data.addAll(response.body().getData());

                        setTotal();
                        adapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onFailure(Call<RekapPelangganResp> call, Throwable t) {
                    LoadingDialog.close();
                    Toast.makeText(RekapPelanggan.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void ExportExcel() throws IOException, WriteException {

        String nama = "LaporanRekapPelanggan";
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
                "Pegawai",
                "Alamat",
                "No Telepon",
                "Jumlah Penjualan",
                "Total Pendapatan"
        };
        ModulExcel.setJudul(sheet, judul);
        int row = ModulExcel.row;
        int no = 1;
        for (ViewModelRekapPelanggan detail : data) {
            int col = 0;
            ModulExcel.addLabel(sheet, col++, row, Modul.intToStr(no));
            ModulExcel.addLabel(sheet, col++, row, detail.getNama_pelanggan());
            ModulExcel.addLabel(sheet, col++, row, detail.getAlamat_pelanggan());
            ModulExcel.addLabel(sheet, col++, row, detail.getNo_telepon());
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
