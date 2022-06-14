package com.itb.aplikasitoko;

import android.content.Context;

import androidx.annotation.NonNull;

import com.itb.aplikasitoko.Service.BarangService;
import com.itb.aplikasitoko.Service.KategoriService;
import com.itb.aplikasitoko.Service.LaporanService;
import com.itb.aplikasitoko.Service.OrderServiceInterface;
import com.itb.aplikasitoko.Service.PegawaiService;
import com.itb.aplikasitoko.Service.PelangganService;
import com.itb.aplikasitoko.Service.SatuanService;
import com.itb.aplikasitoko.Service.TokoInterface;
import com.itb.aplikasitoko.Service.UserService;
import com.itb.aplikasitoko.SharedPref.SpHelper;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Api {

    public static String BASE_URL = "http://45.77.245.19/";
    public static Retrofit getRetrofit(Context context) {
        SpHelper sp = new SpHelper(context);//inisiasi sp helper

        OkHttpClient okHttpClient = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
            @NonNull
            @Override
            public Response intercept(@NonNull Chain chain) throws IOException {
                String token = sp.getToken(); //sp.getValue("token2");//ini ambil token dr response di postman
                Request newRequest = chain.request().newBuilder()
                        .addHeader("Authorization",token)
                        .build();
                return chain.proceed(newRequest);
            }
        }).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();
        return retrofit;
    }

    public static Retrofit getRetrofit() {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient okHttpClient = new OkHttpClient.Builder().addInterceptor(httpLoggingInterceptor).build();

//        OkHttpClient okHttpClient = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
//            @NonNull
//            @Override
//            public Response intercept(@NonNull Chain chain) throws IOException {
//                String token = sp.getToken();//ini ambil token dr response di postman
//                Request newRequest = chain.request().newBuilder()
//                        .addHeader("Authorization",token)
//                        .build();
//                return chain.proceed(newRequest);
//            }
//        }).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();
        return retrofit;
    }

    //LOGIN REGISTER
    // tanpa hedaer (register login)
    public static UserService getService() {
        UserService userService = getRetrofit().create(UserService.class);

        return userService;
    }

    //dg header
    public static UserService getService(Context context) {
        UserService userService = getRetrofit(context).create(UserService.class);

        return userService;
    }

    //KATEGORI
    public static KategoriService Kategori() {
        KategoriService kategoriService = getRetrofit().create(KategoriService.class);

        return kategoriService;
    }

    public static KategoriService Kategori(Context context) {
        KategoriService kategoriService = getRetrofit(context).create(KategoriService.class);

        return kategoriService;
    }

    //SATUAN
    public static SatuanService Satuan(Context context) {
        SatuanService satuanService = getRetrofit(context).create(SatuanService.class);

        return satuanService;
    }

    //PELANGGAN
    public static PelangganService Pelanggan(Context context){
        PelangganService pelangganService = getRetrofit(context).create(PelangganService.class);

        return pelangganService;
    }

    //PEGAWAI
    public static PegawaiService Pegawai(Context context) {
        PegawaiService pegawaiService = getRetrofit(context).create(PegawaiService.class);

        return pegawaiService;
    }

    //PRODUK
    public static BarangService Barang(Context context) {
        BarangService barangService = getRetrofit(context).create(BarangService.class);

        return barangService;
    }

    //LAPORAN PENDAPATAN
    public static LaporanService Pendapatan(Context context) {
        LaporanService laporanService = getRetrofit(context).create(LaporanService.class);

        return laporanService;
    }

    //ORDER
    public static OrderServiceInterface Order(Context context){
        OrderServiceInterface orderServiceInterface = getRetrofit(context).create(OrderServiceInterface.class);

        return orderServiceInterface;
    }

    //IDENTITAS
    public static TokoInterface Identitas(Context context){
        TokoInterface orderServiceInterface = getRetrofit(context).create(TokoInterface.class);

        return orderServiceInterface;
    }

    //LAPORAN PENJUALAN
    public static LaporanService Penjualan(Context context) {
        LaporanService laporanService = getRetrofit(context).create(LaporanService.class);

        return laporanService;
    }

    //REKAP KATEGORI
    public static LaporanService RekapKategori(Context context) {
        LaporanService laporanService = getRetrofit(context).create(LaporanService.class);

        return laporanService;
    }

    //REKAP BARANG
    public static LaporanService RekapBarang(Context context) {
        LaporanService laporanService = getRetrofit(context).create(LaporanService.class);

        return laporanService;
    }

    //REKAP PELANGGAN
    public static LaporanService RekapPelanggan(Context context) {
        LaporanService laporanService = getRetrofit(context).create(LaporanService.class);

        return laporanService;
    }

    //REKAP PEGAWAI
    public static LaporanService RekapPegawai(Context context) {
        LaporanService laporanService = getRetrofit(context).create(LaporanService.class);

        return laporanService;
    }
}
