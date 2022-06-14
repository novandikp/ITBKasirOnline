package com.itb.aplikasitoko.Service;

import com.itb.aplikasitoko.Model.ModelToko;
import com.itb.aplikasitoko.Response.IdentitasGetResp;
import com.itb.aplikasitoko.Response.IdentitasResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface TokoInterface {
    //POST
    @POST("toko/identitas")
    Call<IdentitasResponse> postIdentitas(@Body ModelToko modelToko);

    //GET DATA
    @GET("toko/identitas")
    Call<IdentitasGetResp> getIdentitas();
}
