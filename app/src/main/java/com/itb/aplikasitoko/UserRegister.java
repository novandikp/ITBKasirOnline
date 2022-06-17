package com.itb.aplikasitoko;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.itb.aplikasitoko.Model.ModelRegister;
import com.itb.aplikasitoko.Response.RegisterResponse;
import com.itb.aplikasitoko.SharedPref.SpHelper;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class UserRegister extends AppCompatActivity {

    private TextView userRegister;
    private EditText editTextEmail, editTextPassword, editTextConfirm;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register);

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
                    Toast.makeText(UserRegister.this, "Email atau Password tidak valid", Toast.LENGTH_SHORT).show();
                } else {
                    ModelRegister modelRegister = new ModelRegister();
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
        SpHelper sp = new SpHelper(this);
        Call<RegisterResponse> registerResponseCall = Api.getService().registerUsers(modelRegister);
        registerResponseCall.enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                if(response.isSuccessful()) {

                    
                    SpHelper spHelper = new SpHelper(UserRegister.this);
                    spHelper.setToken(response.body().getToken());
                    sp.setEmail(response.body().getData().getEmail_toko());

                    String message = "Registrasi berhasil";
                    
                    Toast.makeText(com.itb.aplikasitoko.UserRegister.this, message, Toast.LENGTH_LONG).show();

                    
                    startActivity(new Intent(UserRegister.this, TelpVerification.class));

                } else {

                    String message = Api.getError(response).message;
                    Toast.makeText(com.itb.aplikasitoko.UserRegister.this, message, Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                String message = t.getLocalizedMessage();
                Toast.makeText(com.itb.aplikasitoko.UserRegister.this, message, Toast.LENGTH_LONG).show();
            }
        });
    }

    public boolean Validasi() {
        if(editTextEmail.getText().toString().isEmpty() || editTextPassword.getText().toString().isEmpty() || editTextConfirm.getText().toString().isEmpty()) {
            editTextEmail.requestFocus();
            editTextEmail.setError("Harap diisi");

            editTextPassword.requestFocus();
            editTextPassword.setError("Harap diisi");

            editTextConfirm.requestFocus();
            editTextConfirm.setError("Harap diisi");
            return true;
        }else if(editTextPassword.getText().length() <8){
            editTextPassword.requestFocus();
            editTextPassword.setError("Password minimal 8 karakter");

            editTextConfirm.requestFocus();
            editTextConfirm.setError("Password minimal 8 karakter");
            return  true;
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
        } else {
            Toast.makeText(this, "Harap tunggu...", Toast.LENGTH_SHORT).show();
        }
        return false;
    }


}