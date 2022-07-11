package com.itb.aplikasitoko;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.itb.aplikasitoko.Component.LoadingDialog;
import com.itb.aplikasitoko.Model.ModelRegister;
import com.itb.aplikasitoko.Response.RegisterResponse;
import com.itb.aplikasitoko.SharedPref.SpHelper;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class UserRegister extends AppCompatActivity {

    private TextView userRegister;
    private EditText editTextEmail, editTextPassword, editTextConfirm, edtTextNoTelp;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register);
        edtTextNoTelp = (EditText) findViewById(R.id.noWa);

        editTextEmail = (EditText) findViewById(R.id.email);
        editTextPassword = (EditText) findViewById(R.id.password);
        editTextConfirm = (EditText) findViewById(R.id.confirmPassword);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        userRegister = (Button) findViewById(R.id.register);

        //ini buat waktu onclick, isi dr textviewnya keambil trs diproses ke model biar bisa masuk ke database
        userRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Validasi()) {
                    Toast.makeText(UserRegister.this, "Harap isi dengan benar", Toast.LENGTH_SHORT).show();
                } else {
                    String noTelp = edtTextNoTelp.getText().toString();
                    if(noTelp.substring(0,2).equals("08")){
                        noTelp = "62"+noTelp.substring(1,noTelp.length());
                    }
                    ModelRegister modelRegister = new ModelRegister();
                    modelRegister.setNomer_toko(noTelp);
                    modelRegister.setEmail(editTextEmail.getText().toString());
                    modelRegister.setPassword(editTextPassword.getText().toString());
                    modelRegister.setPassword(editTextConfirm.getText().toString());
                    registerUser(modelRegister);
                }
            }
        });
    }

    //function register ini ngisi func registerusers yg diambil dr interface file useRegister, jadi diisi dulu baru dipanggil waktu onclick nnti
    public void registerUser(ModelRegister modelRegister){
        LoadingDialog.load(this);
        SpHelper sp = new SpHelper(this);
        Call<RegisterResponse> registerResponseCall = Api.getService().registerUsers(modelRegister);
        registerResponseCall.enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                LoadingDialog.close();
                if(response.isSuccessful()) {
                    sp.setToken(response.body().getToken());
                    sp.setValue(Config.phoneOTP, modelRegister.getNomer_toko());
                    sp.setValue(Config.lastPageSign,Config.PageSigned.OTP);
                    String message = "Kode OTP terkirim";
                    Toast.makeText(UserRegister.this, message, Toast.LENGTH_LONG).show();
                    startActivity(new Intent(UserRegister.this, OTPVerification.class));
                    finish();
                } else {
                    String message = "Terjadi error, mohon coba lagi";
                    Log.e("Error", response.errorBody().toString());
                    Toast.makeText(com.itb.aplikasitoko.UserRegister.this, response.errorBody().toString(), Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                LoadingDialog.close();
                String message = t.getLocalizedMessage();
                Toast.makeText(com.itb.aplikasitoko.UserRegister.this, message, Toast.LENGTH_LONG).show();
            }
        });
    }

    public boolean Validasi() {

        if(editTextEmail.getText().toString().isEmpty() || editTextPassword.getText().toString().isEmpty() || edtTextNoTelp.getText().toString().isEmpty() || editTextConfirm.getText().toString().isEmpty()) {
            if(editTextEmail.getText().toString().isEmpty()) {
                editTextEmail.setError("Email tidak boleh kosong");
            }
            if(editTextPassword.getText().toString().isEmpty()) {
                editTextPassword.setError("Password tidak boleh kosong");
            }
            if(edtTextNoTelp.getText().toString().isEmpty()) {
                edtTextNoTelp.setError("No Whatsapp tidak boleh kosong");
            }
            if(editTextConfirm.getText().toString().isEmpty()) {
                editTextConfirm.setError("Konfirmasi Password tidak boleh kosong");
            }
            return true;

        } else if (!editTextPassword.getText().toString().matches(editTextConfirm.getText().toString())){
            editTextPassword.requestFocus();
            editTextPassword.setError("Password tidak sama");

            editTextConfirm.requestFocus();
            editTextConfirm.setError("Password tidak sama");
            return true;
        } else if (!editTextEmail.getText().toString().matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")) {
            editTextEmail.requestFocus();
            editTextEmail.setError("Masukkan email yang valid");
            return true;
        }else if(!edtTextNoTelp.getText().toString().substring(0, 2).equals("08") && !edtTextNoTelp.getText().toString().substring(0, 3).equals("628")) {

            edtTextNoTelp.requestFocus();
            edtTextNoTelp.setError("Masukkan nomer whatsapp yang valid");
            return true;
        }
        else {
            Toast.makeText(this, "Harap tunggu...", Toast.LENGTH_SHORT).show();
        }
        return false;
    }


}