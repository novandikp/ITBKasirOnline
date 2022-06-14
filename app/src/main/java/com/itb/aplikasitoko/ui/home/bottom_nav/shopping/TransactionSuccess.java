package com.itb.aplikasitoko.ui.home.bottom_nav.shopping;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.itb.aplikasitoko.HomePage;
import com.itb.aplikasitoko.R;
import com.itb.aplikasitoko.Service.OrderService;
import com.itb.aplikasitoko.databinding.ActivityTransactionSuccessNotifBinding;
import com.itb.aplikasitoko.ui.home.PrintStruk;
import com.itb.aplikasitoko.util.Modul;

import java.text.NumberFormat;
import java.util.Locale;

public class TransactionSuccess extends AppCompatActivity {
    ActivityTransactionSuccessNotifBinding bind;
    private OrderService service = OrderService.getInstance();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        bind = ActivityTransactionSuccessNotifBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(bind.getRoot());

         init();

    }

    public void init(){
        NumberFormat kurensi = NumberFormat.getCurrencyInstance(new Locale("in", "ID"));
        bind.txtTotalTransaksi.setText("Rp "+ Modul.removeE(getIntent().getStringExtra("total")));
        bind.txtChange.setText("Rp "+Modul.removeE(getIntent().getStringExtra("kembali")));


        bind.txtCetakStruk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String idjual = getIntent().getStringExtra("idjual");
                Intent intent = new Intent(TransactionSuccess.this, PrintStruk.class);
                intent.putExtra("idjual", idjual);
                startActivity(intent);
            }
        });

        bind.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, HomePage.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}
