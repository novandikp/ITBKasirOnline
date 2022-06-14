package com.itb.aplikasitoko.Service;

import com.itb.aplikasitoko.Model.ModelBarang;
import com.itb.aplikasitoko.Response.BarangGetResp;
import com.itb.aplikasitoko.Response.BarangResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface BarangService {
    //GET DATA
    @GET("barang")
    Call<BarangGetResp> getBarang(@Query("cari") String cari);

    //GET DATA
    @GET("barang")
    Call<BarangGetResp> getBarangz();

//    //SELECT DATA
//    @GET("barang/{id}")
//    Call<InfoBisnisResponse> barang(@Path("id") String id);

    //POST DATA
    @POST("barang")
    Call<BarangResponse> postBarang(@Body ModelBarang modelBarang);

    //UPDATE DATA
    @POST("barang/{id}")
    Call<BarangResponse> updateBarang(@Path("id") String id, @Body ModelBarang modelBarang);

    //DELETE DATA
    @DELETE("barang/{id}")
    Call<BarangResponse> delBarang(@Path("id") String id);
}
