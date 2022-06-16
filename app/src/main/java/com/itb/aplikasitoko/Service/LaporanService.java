package com.itb.aplikasitoko.Service;

import com.itb.aplikasitoko.Response.PendapatanGetResp;
import com.itb.aplikasitoko.Response.PenjualanGetResp;
import com.itb.aplikasitoko.Response.RekapBarangResp;
import com.itb.aplikasitoko.Response.RekapKategoriResp;
import com.itb.aplikasitoko.Response.RekapPegawaiResp;
import com.itb.aplikasitoko.Response.RekapPelangganResp;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface LaporanService {

    //PENDAPATAN
    @GET("report/pendapatan")
    Call<PendapatanGetResp> getPendapatan(@Query("mulai") String mulai, @Query("sampai") String sampai, @Query("cari") String cari);

    //PENJUALAN/LABA
    @GET("report/laba")
    Call<PenjualanGetResp> getPenjualan(@Query("mulai") String mulai, @Query("sampai") String sampai, @Query("cari") String cari);

    //REKAP KATEGORI
    @GET("report/kategori")
    Call<RekapKategoriResp> getRekapKategori(@Query("cari") String cari, @Query("mulai") String mulai, @Query("sampai") String sampai);

    //REKAP BARANG
    @GET("report/barang")
    Call<RekapBarangResp> getRekapBarang(@Query("cari") String cari, @Query("mulai") String mulai, @Query("sampai") String sampai);

    //REKAP PELANGAGN
    @GET("report/pelanggan")
    Call<RekapPelangganResp> getRekapPelanggan(@Query("cari") String cari, @Query("mulai") String mulai, @Query("sampai") String sampai);

    //REKAP PEGAWAU
    @GET("report/pegawai")
    Call<RekapPegawaiResp> getRekapPegawai(@Query("cari") String cari, @Query("mulai") String mulai, @Query("sampai") String sampai);
}
