package com.itb.aplikasitoko.Service;

import com.itb.aplikasitoko.Model.ModelLogin;
import com.itb.aplikasitoko.Model.ModelRegister;
import com.itb.aplikasitoko.Model.ModelToko;
import com.itb.aplikasitoko.ViewModel.ViewModelBarang;
import com.itb.aplikasitoko.Response.InfoBisnisResponse;
import com.itb.aplikasitoko.Response.LoginResponse;
import com.itb.aplikasitoko.Response.OtpResponse;
import com.itb.aplikasitoko.Response.RegisBarangResponse;
import com.itb.aplikasitoko.Response.RegisterResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface UserService {
    //LOGIN
    @POST("auth/login")
    Call<LoginResponse> loginUser(@Body ModelLogin modelLogin);

    //REGISTER
    @POST("auth/register/v2")
    Call<RegisterResponse> registerUsers(@Body ModelRegister modelRegister);

    //MINTA OTP(ini butuh token yg hrs dimasukkan dulu lewat okhttp)
    @POST("register/minta")
    Call<OtpResponse> mintaOtp(@Body ModelToko modelToko);

    //VERIFIKASI OTP(ini butuh token yg hrs dimasukkan dulu lewat okhttp)
    @FormUrlEncoded
    @POST("register/verifikasi")
    Call<OtpResponse> verifOtp(@Field("otp") String otp);

    //REGISTER INFORMASI BISNIS
    @POST("register/profile")
    Call<InfoBisnisResponse> masukProfil(@Body ModelToko modelToko);

    //TAMBAHKAN PRODUK
    @POST("register/barang")
    Call<RegisBarangResponse> regisBarang(@Body ViewModelBarang modelView);




}
