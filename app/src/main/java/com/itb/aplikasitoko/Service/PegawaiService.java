package com.itb.aplikasitoko.Service;

import com.itb.aplikasitoko.Model.ModelLoginPegawai;
import com.itb.aplikasitoko.Model.ModelPegawai;
import com.itb.aplikasitoko.Response.LoginPegawaiResponse;
import com.itb.aplikasitoko.Response.PegawaiGetResp;
import com.itb.aplikasitoko.Response.PegawaiResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface PegawaiService {
    //LOGIN
    @POST("pegawai/login")
    Call<LoginPegawaiResponse> loginPegawai(@Body ModelLoginPegawai modelLoginPegawai);

    //GET DATA
    @GET("pegawai")
    Call<PegawaiGetResp> getPeg(@Query("cari") String cari);

    //DELETE DATA
    @DELETE("pegawai/{id}")
    Call<PegawaiResponse> deletePeg(@Path("id") int id);

    //POST DATA
    @POST("pegawai")
    Call<PegawaiResponse> postPeg(@Body ModelPegawai modelPegawai);

    //UPDATE DATA
    @POST("pegawai/{id}")
    Call<PegawaiResponse> updatePeg(@Path("id") int id,@Body ModelPegawai modelPegawai); //kalau gk pakai @body bakal eror
}
