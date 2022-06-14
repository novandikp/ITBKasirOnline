package com.itb.aplikasitoko.ViewModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class ViewModelDetailJual {
    private String fakturjual;
    private String nama_pegawai;
    private String nama_pelanggan;
    private String tanggal_jual;
    private String barang;
    private int jumlahjual;
    private String nama_satuan;
    private double hargajual;
    private double hargabeli;
    private int idtoko;
    private double laba;

    public String getFakturjual() {
        return fakturjual;
    }

    public void setFakturjual(String fakturjual) {
        this.fakturjual = fakturjual;
    }

    public String getNama_pegawai() {
        return nama_pegawai;
    }

    public void setNama_pegawai(String nama_pegawai) {
        this.nama_pegawai = nama_pegawai;
    }

    public String getNama_pelanggan() {
        return nama_pelanggan;
    }

    public void setNama_pelanggan(String nama_pelanggan) {
        this.nama_pelanggan = nama_pelanggan;
    }

    public String getTanggal_jual() {
        return tanggal_jual;
    }

    public void setTanggal_jual(String tanggal_jual) {
        this.tanggal_jual = tanggal_jual;
    }

    public String getBarang() {
        return barang;
    }

    public void setBarang(String barang) {
        this.barang = barang;
    }

    public int getJumlahjual() {
        return jumlahjual;
    }

    public void setJumlahjual(int jumlahjual) {
        this.jumlahjual = jumlahjual;
    }

    public String getNama_satuan() {
        return nama_satuan;
    }

    public void setNama_satuan(String nama_satuan) {
        this.nama_satuan = nama_satuan;
    }

    public double getHargajual() {
        return hargajual;
    }

    public void setHargajual(double hargajual) {
        this.hargajual = hargajual;
    }

    public double getHargabeli() {
        return hargabeli;
    }

    public void setHargabeli(double hargabeli) {
        this.hargabeli = hargabeli;
    }

    public int getIdtoko() {
        return idtoko;
    }

    public void setIdtoko(int idtoko) {
        this.idtoko = idtoko;
    }

    public double getLaba() {
        return laba;
    }

    public void setLaba(double laba) {
        this.laba = laba;
    }

    public String tanggalku() throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date tanggal = format.parse(getTanggal_jual());
        SimpleDateFormat formatBaru = new SimpleDateFormat("yyyy-MM-dd", new Locale("id", "ID"));
        return formatBaru.format(tanggal);
    }
}
