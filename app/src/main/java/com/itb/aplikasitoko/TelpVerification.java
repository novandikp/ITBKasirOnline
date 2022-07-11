package com.itb.aplikasitoko;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.itb.aplikasitoko.Component.LoadingDialog;
import com.itb.aplikasitoko.Model.ModelToko;
import com.itb.aplikasitoko.Response.OtpResponse;
import com.itb.aplikasitoko.SharedPref.SpHelper;
import com.itb.aplikasitoko.databinding.ActivityTelpVerificationBinding;
import com.itb.aplikasitoko.util.Modul;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TelpVerification extends AppCompatActivity {
    private EditText NoTelp;
    private TextView btnOtp;
    ActivityTelpVerificationBinding bind;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        bind = ActivityTelpVerificationBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(bind.getRoot());
        SpHelper sp = new SpHelper(this); //disimpan di minta otp
        sp.setValue(Config.lastPageSign,Config.PageSigned.OTP);

        bind.sendKodeOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ModelToko modelToko = new ModelToko();
                String NoTelp = bind.noTelp.getText().toString();
                if(NoTelp.substring(0,2).equals("08")){
                    NoTelp = "62"+NoTelp.substring(1,NoTelp.length());
                }
                modelToko.setNomer_toko(Modul.PhoneFormat(NoTelp));
                sp.setValue(Config.phoneOTP, NoTelp); //ini menyimpan notelpon ke dlm shared pref
                MintaOtp(modelToko);
            }
        });

        bind.backToAdvance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sp.clearAll();

                startActivity(new Intent(TelpVerification.this, UserRegister.class));
                finish();
            }
        });
    }


    public void MintaOtp(ModelToko modelToko) {
        LoadingDialog.load(this);
        Call<OtpResponse> OtpResponseCall = Api.getService(this).mintaOtp(modelToko);
        OtpResponseCall.enqueue(new Callback<OtpResponse>() {
            @Override
            public void onResponse(Call<OtpResponse> call, Response<OtpResponse> response) {
                LoadingDialog.close();
                if(response.isSuccessful()){
                    String message = "Kode OTP terkirim";
                    Toast.makeText(TelpVerification.this, message, Toast.LENGTH_LONG).show();
                    startActivity(new Intent(TelpVerification.this, OTPVerification.class));

                } else {

                    String message = Api.getError(response).message;
                    Toast.makeText(TelpVerification.this, message, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<OtpResponse> call, Throwable t) {
                LoadingDialog.close();
                String message = t.getLocalizedMessage();
                Toast.makeText(TelpVerification.this, message, Toast.LENGTH_LONG).show();
            }
        });
    }
}