package com.itb.aplikasitoko.Service;

import com.itb.aplikasitoko.Model.ModelPelanggan;
import com.itb.aplikasitoko.Response.PelangganGetResp;
import com.itb.aplikasitoko.Response.PelangganResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface PelangganService {
    //GET DATA
    @GET("pelanggan")
    Call<PelangganGetResp> getPel(@Query("cari") String cari);

    @GET("pelanggan")
    Call<PelangganGetResp> getPel2();

//    //SELECT DATA
//    @GET("pelanggan/{id}")
//    Call<InfoBisnisResponse> barang(@Path("id") String id);

    //POST DATA
    @POST("pelanggan")
    Call<PelangganResponse> postPel(@Body ModelPelanggan modelPelanggan);

    //UPDATE DATA
    @POST("pelanggan/{id}")
    Call<PelangganResponse> updatePel(@Path("id") int id,@Body ModelPelanggan modelPelanggan);

    //DELETE DATA
    @DELETE("pelanggan/{id}")
    Call<PelangganResponse> deletePel(@Path("id") int id);

    //SELECT DATA
    @GET("pelanggan/{id}")
    Call<PelangganResponse> cariData(@Path("id") int id,@Body ModelPelanggan modelPelanggan);
}
