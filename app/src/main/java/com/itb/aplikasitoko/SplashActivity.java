package com.itb.aplikasitoko;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;

import com.itb.aplikasitoko.SharedPref.SpHelper;
import com.itb.aplikasitoko.ui.load.LoadActivity;
import com.itb.aplikasitoko.ui.pengaturan.pegawai.LoginPegawai;
import com.itb.aplikasitoko.util.Modul;


public class SplashActivity extends AppCompatActivity{
    private static final int SPLASH_SCREEN_TIMEOUT = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //pengecekan
        SpHelper sp = new SpHelper(this);
        String cek = sp.getValue(Config.lastPageSign);



        setContentView(R.layout.splash_activity);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                switch (cek) {
                    case Config.PageSigned.DASHBOARD:

                        if(!sp.getValue("isFirstLogin","").equals(Modul.getDate("yyyy-MM-dd"))){
                            sp.setValue("isFirstLogin",Modul.getDate("yyyy-MM-dd"));
                            startActivity(new Intent(SplashActivity.this, LoadActivity.class));
                        }else if(sp.getIdPegawai().isEmpty()){
                            startActivity(new Intent(SplashActivity.this, HomePage.class));
                        }else{
                            startActivity(new Intent(SplashActivity.this, LoginPegawai.class));
                        }
                        break;
                    case Config.PageSigned.OTP:
                        startActivity(new Intent(SplashActivity.this, TelpVerification.class));
                        break;
                    case Config.PageSigned.PROFIL:
                        startActivity(new Intent(SplashActivity.this, InformasiBisnis.class));
                        break;
                    default:
                        startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                        break;
                }
                finish();
            }
        }, SPLASH_SCREEN_TIMEOUT);


    }

}
