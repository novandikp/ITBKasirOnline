package com.itb.aplikasitoko.Service;

import com.itb.aplikasitoko.Model.ModelKategori;
import com.itb.aplikasitoko.Response.KategoriGetResp;
import com.itb.aplikasitoko.Response.KategoriResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface KategoriService {
    //GET DATA
    @GET("kategori")
    Call<KategoriGetResp> getKat(@Query("cari") String cari);

    //POST DATA
    @POST("kategori")
    Call<KategoriResponse> postKat(@Body ModelKategori modelKategori);

    //UPDATE DATA
    @POST("kategori/{id}")
    Call<KategoriResponse> updateKat(@Path("id") int id,@Body ModelKategori modelKategori);

    //DELETE DATA
    @DELETE("kategori/{id}")
    Call<KategoriResponse> deleteKat(@Path("id") int id);

}
