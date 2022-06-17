package com.itb.aplikasitoko.ui.pengaturan.produk;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import com.itb.aplikasitoko.Api;
import com.itb.aplikasitoko.Component.ErrorDialog;
import com.itb.aplikasitoko.Component.LoadingDialog;
import com.itb.aplikasitoko.Component.SuccessDialog;
import com.itb.aplikasitoko.Database.Repository.BarangRepository;
import com.itb.aplikasitoko.Database.Repository.KategoriRepository;
import com.itb.aplikasitoko.Database.Repository.SatuanRepository;
import com.itb.aplikasitoko.Model.ModelBarang;
import com.itb.aplikasitoko.Model.ModelKategori;
import com.itb.aplikasitoko.Model.ModelSatuan;
import com.itb.aplikasitoko.R;
import com.itb.aplikasitoko.Response.BarangResponse;
import com.itb.aplikasitoko.Response.KategoriGetResp;
import com.itb.aplikasitoko.Response.SatuanGetResp;
import com.itb.aplikasitoko.databinding.EditProdukBinding;
import com.itb.aplikasitoko.util.Modul;
import com.itb.aplikasitoko.util.NumberTextWatcher;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProduk extends AppCompatActivity {
    EditProdukBinding bind;
    //buat update
    BarangRepository barangRepository;
    private EditText inNama, inKode, inHarga, inhargaBeli, inStok;
    private AutoCompleteTextView inKategori, inSatuan;

    //spinner kategori
    List<ModelKategori> dataKategori = new ArrayList<>();
    ArrayAdapter adapterKategori;
    List<String> kategori = new ArrayList<>();
    KategoriRepository kategoriRepository;

    //spinner satuan
    List<ModelSatuan> dataSatuan = new ArrayList<>();
    ArrayAdapter adapterSatuan;
    List<String> satuan = new ArrayList<>();
    SatuanRepository satuanRepository;

    private ModelKategori modelKategoriSelected;
    private ModelSatuan modelSatuanSelected;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        bind = EditProdukBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        bind.hargaAwal.addTextChangedListener(new NumberTextWatcher(bind.hargaAwal, new Locale("id","ID"),0));
        bind.hargaJual.addTextChangedListener(new NumberTextWatcher(bind.hargaJual, new Locale("id","ID"),0));
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Edit Detail Produk");
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        setContentView(bind.getRoot());

        //panggil db/repository
        barangRepository = new BarangRepository(getApplication());

        bind.opsiSatuan.setClickable(true);
        bind.opsiSatuan.setFocusable(false);
        bind.opsiSatuan.setFocusableInTouchMode(false);
        bind.opsiKategori.setClickable(true);
        bind.opsiKategori.setFocusable(false);
        bind.opsiKategori.setFocusableInTouchMode(false);
        bind.opsiKategori.setOnTouchListener((view, motionEvent) -> {
            bind.opsiKategori.showDropDown();
            return true;
        });
        bind.opsiSatuan.setOnTouchListener((view, motionEvent) -> {
            bind.opsiSatuan.showDropDown();
            return true;
        });



        init();

        //Spinner kategori
        kategoriRepository = new KategoriRepository(getApplication());
        kategoriRepository.getAllKategori().observe(this, new Observer<List<ModelKategori>>() {
            @Override
            public void onChanged(List<ModelKategori> modelKategoris) {
                dataKategori.clear();
                dataKategori.addAll(modelKategoris);
                for (ModelKategori modelKategori : modelKategoris){
                    kategori.add(modelKategori.getNama_kategori());
                    if(String.valueOf(modelKategori.getIdkategori()).equals(getIntent().getStringExtra("idkategori"))){
                        bind.opsiKategori.setText(modelKategori.getNama_kategori());
                    }
                }
                adapterKategori = new ArrayAdapter(EditProduk.this, android.R.layout.simple_spinner_dropdown_item, kategori);
                bind.opsiKategori.setAdapter(adapterKategori);

                //memaggil function kategori/satuan biar bisa berjalan saat online

            }
        });
        refreshKategori();
        //Spinner satuan
        satuanRepository = new SatuanRepository(getApplication());
        satuanRepository.getAllSatuan().observe(this, new Observer<List<ModelSatuan>>() {
            @Override
            public void onChanged(List<ModelSatuan> modelSatuans) {
                dataSatuan.clear();
                dataSatuan.addAll(modelSatuans);
                for (ModelSatuan modelSatuan: modelSatuans){
                    satuan.add(modelSatuan.getNama_satuan());
                    if(String.valueOf(modelSatuan.getIdsatuan()).equals(getIntent().getStringExtra("idsatuan"))){
                        bind.opsiSatuan.setText(modelSatuan.getNama_satuan());
                    }
                }
                adapterSatuan = new ArrayAdapter(EditProduk.this, android.R.layout.simple_spinner_dropdown_item, satuan);
                bind.opsiSatuan.setAdapter(adapterSatuan);

                //memaggil function kategori/satuan biar bisa berjalan saat online

            }
        });
        refreshSatuan();
        //inisiasi variabel (update data)
        inNama = bind.namaProduk;
        inKode = bind.kodeProduk;
        inHarga = bind.hargaJual;
        inhargaBeli = bind.hargaAwal;
        inKategori = bind.opsiKategori;
        inSatuan = bind.opsiSatuan;
        inStok = bind.stokAwal;

        bind.btnUpdateProduk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nama = inNama.getText().toString();
                String kode = inKode.getText().toString();
                String harga = Modul.unnumberFormat(inHarga.getText().toString());
                String hargaBeli = Modul.unnumberFormat(inhargaBeli.getText().toString());
                String stok = inStok.getText().toString();

                String idkategori = String.valueOf(KategoriSelected().getIdkategori());
                String idsatuan = String.valueOf(SatuanSelected().getIdsatuan());

                if (nama.isEmpty() || kode.isEmpty() || harga.isEmpty() || hargaBeli.isEmpty() || stok.isEmpty()){
                    inNama.setError("Harap isi dengan benar");
                    inKode.setError("Harap isi dengan benar");
                    inHarga.setError("Harap isi dengan benar");
                    inhargaBeli.setError("Harap isi dengan benar");
                    inStok.setError("Harap isi dengan benar");

                } else {
                    double Harga = Double.parseDouble(harga);
                    double HargaBeli = Double.parseDouble(hargaBeli);
                    double StokAwal = Double.parseDouble(stok);
                    String id = getIntent().getStringExtra("idbarang");
                    ModelBarang modelBarang = new ModelBarang();
                    modelBarang.setIdbarang(id);
                    modelBarang.setBarang(nama);
                    modelBarang.setIdkategori(idkategori);
                    modelBarang.setIdsatuan(idsatuan);
                    modelBarang.setHarga(Harga);
                    modelBarang.setHargabeli(HargaBeli);
                    modelBarang.setStok(StokAwal);
                    UpdateBarang(id, modelBarang);
                }
            }
        });
    }

    public ModelKategori KategoriSelected(){
        return dataKategori.get(kategori.indexOf(bind.opsiKategori.getText().toString()));
    }

    public ModelSatuan SatuanSelected(){
        return dataSatuan.get(satuan.indexOf(bind.opsiSatuan.getText().toString()));
    }
    public void init(){
        //deklarasi vaiabel
        inNama = bind.namaProduk;
        inKode = bind.kodeProduk;
        inHarga = bind.hargaJual;
        inhargaBeli = bind.hargaAwal;
        inKategori = bind.opsiKategori;
        inSatuan = bind.opsiSatuan;
        inStok = bind.stokAwal;

        //mengambil value dr intent
        inNama.setText(getIntent().getStringExtra("barang"));
        inKode.setText(getIntent().getStringExtra("idbarang"));
        String idkategori = getIntent().getStringExtra("idkategori");
        String idsatuan = getIntent().getStringExtra("idsatuan");
        inKategori.setText(idkategori);
        inSatuan.setText(idsatuan);


//        inSatuan;
        inHarga.setText(getIntent().getStringExtra("harga"));
        inhargaBeli.setText(getIntent().getStringExtra("hargaBeli"));
        inStok.setText(getIntent().getStringExtra("stok"));


    }

    public void UpdateBarang(String id, ModelBarang modelBarang){
        LoadingDialog.load(this);
        Call<BarangResponse> barangResponseCall = Api.Barang(EditProduk.this).updateBarang(id, modelBarang);
        barangResponseCall.enqueue(new Callback<BarangResponse>() {
            @Override
            public void onResponse(Call<BarangResponse> call, Response<BarangResponse> response) {
                LoadingDialog.close();
                if (response.isSuccessful()){
                    if (response.body().isStatus()){
                        barangRepository.update(modelBarang);

                        finish();
                        SuccessDialog.message(EditProduk.this, getString(R.string.updated_success), bind.getRoot());

                    }
                } else {
                    String message = Api.getError(response).message;
                    ErrorDialog.message(EditProduk.this, message, bind.getRoot());
                }

            }

            @Override
            public void onFailure(Call<BarangResponse> call, Throwable t) {
                LoadingDialog.close();
                Toast.makeText(EditProduk.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void refreshKategori(){
        Call<KategoriGetResp> kategoriGetRespCall = Api.Kategori(EditProduk.this).getKat("");
        kategoriGetRespCall.enqueue(new Callback<KategoriGetResp>() {
            @Override
            public void onResponse(Call<KategoriGetResp> call, Response<KategoriGetResp> response) {
                if (dataKategori.size() != response.body().getData().size()){
                    dataKategori.clear();
                    kategori.clear();
                    dataKategori.addAll(response.body().getData());
                    kategoriRepository.insertAll(response.body().getData(),true);
                    for (ModelKategori modelKategori : response.body().getData()){
                        kategori.add(modelKategori.getNama_kategori());
                        if(String.valueOf(modelKategori.getIdkategori()).equals(getIntent().getStringExtra("idkategori"))){
                            bind.opsiKategori.setText(modelKategori.getNama_kategori());
                        }
                    }
                    adapterKategori = new ArrayAdapter(EditProduk.this, android.R.layout.simple_spinner_dropdown_item, kategori);
                }   bind.opsiKategori.setAdapter(adapterKategori);
            }

            @Override
            public void onFailure(Call<KategoriGetResp> call, Throwable t) {
                Toast.makeText(EditProduk.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void refreshSatuan(){
        Call<SatuanGetResp> satuanGetRespCall = Api.Satuan(EditProduk.this).getSat("");
        satuanGetRespCall.enqueue(new Callback<SatuanGetResp>() {
            @Override
            public void onResponse(Call<SatuanGetResp> call, Response<SatuanGetResp> response) {
                if (dataSatuan.size() != response.body().getData().size()){
                    dataSatuan.clear();
                    satuan.clear();
                    dataSatuan.addAll(response.body().getData());
                    satuanRepository.insertAll(response.body().getData(),true);
                    String kategori = "";
                    for (ModelSatuan modelSatuan : response.body().getData()){
                        satuan.add(modelSatuan.getNama_satuan());
                        if(String.valueOf(modelSatuan.getIdsatuan()).equals(getIntent().getStringExtra("idsatuan"))){
                            bind.opsiSatuan.setText(modelSatuan.getNama_satuan());
                        }
                    }
                    adapterSatuan = new ArrayAdapter(EditProduk.this, android.R.layout.simple_spinner_dropdown_item, satuan);
                    bind.opsiSatuan.setAdapter(adapterSatuan);
                }
            }

            @Override
            public void onFailure(Call<SatuanGetResp> call, Throwable t) {
                Toast.makeText(EditProduk.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
