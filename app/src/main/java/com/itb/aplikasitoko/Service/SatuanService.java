package com.itb.aplikasitoko.Service;

import com.itb.aplikasitoko.Model.ModelSatuan;
import com.itb.aplikasitoko.Response.SatuanGetResp;
import com.itb.aplikasitoko.Response.SatuanResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface SatuanService {
    //GET DATA
    @GET("satuan")
    Call<SatuanGetResp> getSat(@Query("cari") String cari);
//
//    //SELECT DATA
//    @GET("satuan/{id}")
//    Call<InfoBisnisResponse> barang(@Path("id") String id);

    //POST DATA
    @POST("satuan")
    Call<SatuanResponse> postSat(@Body ModelSatuan modelSatuan);

    //UPDATE DATA
    @POST("satuan/{id}")
    Call<SatuanResponse> updateSat(@Path("id") int id,@Body ModelSatuan modelSatuan);

    //DELETE DATA
    @DELETE("satuan/{id}")
    Call<SatuanResponse> deleteSat(@Path("id") int id);
}
