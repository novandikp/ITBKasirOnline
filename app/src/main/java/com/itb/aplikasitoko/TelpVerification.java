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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TelpVerification extends AppCompatActivity {
    private EditText NoTelp;
    private TextView btnOtp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_telp_verification);
        SpHelper sp = new SpHelper(this); //disimpan di minta otp

        NoTelp = (EditText) findViewById(R.id.noTelp);
        btnOtp = (Button) findViewById(R.id.sendKodeOTP);
        btnOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ModelToko modelToko = new ModelToko();
                //ini perubahannya
                modelToko.setNomer_toko(NoTelp.getText().toString().substring(0, 1).replace("08", "62")+NoTelp.getText().toString().substring(1));
                sp.setValue(Config.phoneOTP, NoTelp.getText().toString()); //ini menyimpan notelpon ke dlm shared pref
                MintaOtp(modelToko);
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