package com.itb.aplikasitoko;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.TextView;

import com.itb.aplikasitoko.Component.LoadingDialog;
import com.itb.aplikasitoko.SharedPref.SpHelper;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.itb.aplikasitoko.databinding.ActivityHomePageBinding;
import com.itb.aplikasitoko.ui.pengaturan.pegawai.LoginPegawai;

public class HomePage extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityHomePageBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityHomePageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        setSupportActionBar(binding.appBarHomePage.toolbar);

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        View headerView = navigationView.getHeaderView(0);

        //mengatur email/username di sidebar
        SpHelper sp = new SpHelper(this);
        sp.setValue(Config.lastPageSign,Config.PageSigned.DASHBOARD);
        TextView tvnama = headerView.findViewById(R.id.user);
        tvnama.setText(sp.getUsername());
        TextView tvEmail = headerView.findViewById(R.id.email);
        tvEmail.setText(sp.getEmail());
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_penjualan, R.id.nav_laporan, R.id.nav_pengaturan, R.id.nav_logout_toko, R.id.nav_logout_pegawai)
                .setOpenableLayout(drawer)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_home_page);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if(id == R.id.nav_logout_toko) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(HomePage.this);
                    alert.setTitle("Konfirmasi");
                    alert.setMessage("Apakah anda ingin keluar dari akun ini dan kembali ke Login Page?");
                    alert.setPositiveButton("Iya", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            LoadingDialog.load(HomePage.this);
                            SpHelper sp = new SpHelper(HomePage.this);
                            sp.clearAll();
                            finish();
                            startActivity(new Intent(HomePage.this, LoginActivity.class));
                        }
                    }).setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).show();

                    return true;
                }else  if(id == R.id.nav_logout_pegawai) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(HomePage.this);
                    alert.setTitle("Konfirmasi");
                    alert.setMessage("Apakah anda yakin ingin keluar?");
                    alert.setPositiveButton("Iya", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            LoadingDialog.load(HomePage.this);
                            SpHelper sp = new SpHelper(HomePage.this);
                            sp.clearPegawai();
                            finish();
                            startActivity(new Intent(HomePage.this, LoginPegawai.class));
                        }
                    }).setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).show();

                    return true;
                } else {
                    boolean handled = NavigationUI.onNavDestinationSelected(item, navController);
                    if (handled)
                        drawer.closeDrawer(navigationView);
                    return handled;
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_page, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_home_page);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }


}