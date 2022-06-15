package com.itb.aplikasitoko.Service;

import com.itb.aplikasitoko.Model.ModelOrder;
import com.itb.aplikasitoko.Response.DetailJualResp;
import com.itb.aplikasitoko.Response.DetailOrderResponse;
import com.itb.aplikasitoko.Response.JualResponse;
import com.itb.aplikasitoko.Response.OrderResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface OrderServiceInterface {
    @POST("order")
    Call<OrderResponse> postOrder(@Body ModelOrder modelOrder);

    @GET("order")
    Call<JualResponse> getJual();

    @GET("detailjual")
    Call<DetailJualResp> getDetailJual();

    @GET("order/detail/{id}")
    Call<DetailOrderResponse> getOrderDetail(@Path("id") String id);
}
