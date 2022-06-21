package com.itb.aplikasitoko.ui.home.bottom_nav.shopping;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.itb.aplikasitoko.Adapter.CartAdapter;
import com.itb.aplikasitoko.Database.Repository.DetailJualRepository;
import com.itb.aplikasitoko.Model.ModelBarang;
import com.itb.aplikasitoko.Model.ModelDetailJual;
import com.itb.aplikasitoko.R;
import com.itb.aplikasitoko.Service.OrderService;
import com.itb.aplikasitoko.databinding.ActivityCartBinding;
import com.itb.aplikasitoko.databinding.DialogKeteranganOrderBinding;
import com.itb.aplikasitoko.util.Modul;
import com.itb.aplikasitoko.util.NumberTextWatcher;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ShoppingCart extends AppCompatActivity {
    private ActivityCartBinding bind;
    private DetailJualRepository detailJualRepository;
    private CartAdapter cartAdapter;
    private List<ModelDetailJual> modelDetailJualList = new ArrayList<>();
    private List<ModelBarang> modelBarangList = new ArrayList<>();
    private OrderService service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        bind = ActivityCartBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Keranjang");
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        service = OrderService.getInstance();
        setContentView(bind.getRoot());

        // insiiasi db/repo
        detailJualRepository = new DetailJualRepository(getApplication());

        // inisiasi recyclerview
        bind.item.setLayoutManager(new LinearLayoutManager(this));
        cartAdapter = new CartAdapter(ShoppingCart.this, modelDetailJualList, modelBarangList);
        bind.item.setAdapter(cartAdapter);

        refreshData();
        BayarButton();

    }

    public void DialogTotal(ModelDetailJual modelDetailJual, ModelBarang modelBarang) {

        DialogKeteranganOrderBinding binder = DialogKeteranganOrderBinding.inflate(LayoutInflater.from(this));
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setView(binder.getRoot());
        alertBuilder.setTitle("Jumlah Order");
        alertBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                double jumlah = Modul.strToDouble(binder.tvJumlah.getText().toString());
                double hargaBaru = Modul.strToDouble(Modul.unnumberFormat(binder.eHarga.getText().toString()));
                if (binder.tvJumlah.getText().toString().isEmpty()) {
                    binder.tvJumlah.setError("Harap isi dengan benar");
                    return;
                }

                service.setJumlahBeli(modelBarang, modelDetailJual.getJumlahjual(), jumlah, hargaBaru);
                refreshData();
            }
        }).setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        binder.eHarga.setText(Modul.toString(modelDetailJual.getHargajual()));
        binder.eHarga.addTextChangedListener(new NumberTextWatcher(binder.eHarga, new Locale("id", "ID"), 0));
        binder.cbHarga.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    binder.eHarga.setVisibility(View.VISIBLE);
                } else {
                    binder.eHarga.setVisibility(View.GONE);
                }
                binder.eHarga.setText(Modul.toString(modelDetailJual.getHargajual()));
            }
        });
        binder.tvJumlah.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                double jumlah = Modul.strToDouble(binder.tvJumlah.getText().toString());
                if (Math.floor(jumlah) == 0) {
                    binder.kurang.setEnabled(false);
                    binder.kurang.setTextColor(getColor(R.color.darkgrey));
                } else {
                    binder.kurang.setEnabled(true);
                    binder.kurang.setTextColor(getColor(R.color.default1));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        binder.tvJumlah.setText(Modul.toString(modelDetailJual.getJumlahjual()));
        binder.tambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                double jumlah = Modul.strToDouble(binder.tvJumlah.getText().toString());
                jumlah++;
                binder.tvJumlah.setText(Modul.toString(jumlah));
            }
        });
        binder.kurang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                double jumlah = Modul.strToDouble(binder.tvJumlah.getText().toString());
                jumlah--;
                binder.tvJumlah.setText(Modul.toString(jumlah));
                if (Math.floor(jumlah) == 0) {
                    binder.kurang.setEnabled(false);
                    binder.kurang.setTextColor(getColor(R.color.darkgrey));
                } else {
                    binder.kurang.setEnabled(true);
                    binder.kurang.setTextColor(getColor(R.color.default1));
                }
            }
        });
        AlertDialog dialog = alertBuilder.create();
        dialog.show();
    }

    public void refreshData() {
        OrderService orderService = OrderService.getInstance();

        if (orderService.getBarang().size() == 0 || orderService.getDetail().size() == 0) {
            bind.txtKeranjang.setVisibility(View.VISIBLE);
            bind.item.setVisibility(View.GONE);
            bind.btnBayar.setBackgroundColor(getColor(R.color.darkgrey));
            bind.btnBayar.setEnabled(false);

        } else {
            bind.tvTotal.setText(Modul.removeE(orderService.getTotal()));// menampilkan total harga
            modelDetailJualList.clear();
            modelBarangList.clear();
            bind.btnBayar.setBackgroundColor(getColor(R.color.default1));
            modelDetailJualList.addAll(orderService.getDetail());
            modelBarangList.addAll(orderService.getBarang());
            cartAdapter.notifyDataSetChanged();
        }
    }

    public void BayarButton() {
        bind.btnBayar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ShoppingCart.this, Payment.class));
                finish();
            }
        });
    }
}
