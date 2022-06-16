package com.itb.aplikasitoko;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.itb.aplikasitoko.Component.LoadingDialog;
import com.itb.aplikasitoko.Model.ModelToko;
import com.itb.aplikasitoko.Response.OtpResponse;
import com.itb.aplikasitoko.SharedPref.SpHelper;
import com.itb.aplikasitoko.databinding.ActivityOtpverificationBinding;
import com.itb.aplikasitoko.databinding.ActivityTelpVerificationBinding;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OTPVerification extends AppCompatActivity {

    ActivityOtpverificationBinding bind;
    Context context;
    String NoHp, OTP;
    SpHelper sp ;
    private boolean resend = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bind = ActivityOtpverificationBinding.inflate(getLayoutInflater());
        setContentView(bind.getRoot());
        sp = new SpHelper(this);
        NoHp = sp.getValue(Config.phoneOTP);
        EditText kode = bind.kodeOTP;

        bind.submitKodeOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (kode.getText().toString().length() < 4 || kode.getText().toString().length() > 4){
                    kode.requestFocus();
                    kode.setError("OTP terdiri dari 4 angka");
                } else if (kode.getText().toString().isEmpty()){
                    kode.requestFocus();
                    kode.setError("Harap diisi");
                } else {
                    //Toast.makeText(OTPVerification.this, "...", Toast.LENGTH_SHORT).show();
                    VerifOtp(kode.getText().toString());

                }

            }
        });

        bind.kirimUlang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(resend){
                    ModelToko modelToko = new ModelToko();
                    modelToko.setNomer_toko(NoHp);
                    MintaOtp(modelToko);
                }else{
                    Toast.makeText(OTPVerification.this, "Harap tunggu selama "+ bind.kirimUlang.getText().toString(), Toast.LENGTH_SHORT).show();
                }

            }
        });


    }


    private void setResendTimer(){
        resend =false;
        new CountDownTimer(300000,1000) {
            @Override
            public void onTick(long l) {
                int minute = (int) (l / 60000);
                int second = (int)( l - minute*60000)/1000;

                String detik = String.valueOf(second);
                if(detik.length() ==1){
                    detik = "0"+detik;
                }

                bind.kirimUlang.setText("0"+minute+":"+detik);

            }

            @Override
            public void onFinish() {
                resend = true;
                bind.kirimUlang.setText("Kirim Ulang OTP");
            }
        }.start();
    }


    public void MintaOtp(ModelToko modelToko) {
        LoadingDialog.load(this);
        setResendTimer();
        Call<OtpResponse> OtpResponseCall = Api.getService(OTPVerification.this).mintaOtp(modelToko);
        OtpResponseCall.enqueue(new Callback<OtpResponse>() {
            @Override
            public void onResponse(Call<OtpResponse> call, Response<OtpResponse> response) {
                LoadingDialog.close();
                if(response.isSuccessful()){
                    String message = "Kode OTP terkirim";
                    Toast.makeText(OTPVerification.this, message , Toast.LENGTH_LONG).show();

                } else {
                    String message = "Nomer telepon tidak valid";
                    Toast.makeText(OTPVerification.this, message, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<OtpResponse> call, Throwable t) {
                LoadingDialog.close();
                String message = t.getLocalizedMessage();
                Toast.makeText(OTPVerification.this, message, Toast.LENGTH_LONG).show();
            }
        });
    }

    public boolean VerifOtp(String otp) {
        LoadingDialog.load(this);
        Call<OtpResponse> OtpResponseCall = Api.getService(OTPVerification.this).verifOtp(otp);
        OtpResponseCall.enqueue(new Callback<OtpResponse>() {
            @Override
            public void onResponse(Call<OtpResponse> call, Response<OtpResponse> response) {
                LoadingDialog.close();
                if (response.isSuccessful()){
                    startActivity(new Intent(OTPVerification.this, InformasiBisnis.class));
                    finish();
                } else {
                    Toast.makeText(OTPVerification.this, "Masukkan kode OTP dengan benar", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<OtpResponse> call, Throwable t) {
                LoadingDialog.close();
                Toast.makeText(OTPVerification.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        return true;
    }
}