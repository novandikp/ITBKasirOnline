package com.itb.aplikasitoko.Model;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import androidx.annotation.NonNull;

@Entity(tableName = "tblbarang")
public class ModelBarang {
    @PrimaryKey@NonNull
    private String idbarang;

    private String idkategori;

    private String idsatuan;

    private String barang;

    private double harga;

    private double hargabeli;

    private double stok;

    private int flag_stok;

    @Ignore
    private String idtoko;

    //buat update / post barang scr manual
    @Ignore
    public ModelBarang() {
    }





    public ModelBarang(@NonNull String idbarang, String idkategori, String idsatuan, String barang, double harga, double hargabeli, double stok, int flag_stok) {
        this.idbarang = idbarang;
        this.idkategori = idkategori;
        this.idsatuan = idsatuan;
        this.barang = barang;
        this.harga = harga;
        this.hargabeli = hargabeli;
        this.stok = stok;
        this.flag_stok = flag_stok;
    }

    public String getIdbarang() {
        return idbarang;
    }

    public void setIdbarang(String idbarang) {
        this.idbarang = idbarang;
    }

    public String getIdkategori() {
        return idkategori;
    }

    public void setIdkategori(String idkategori) {
        this.idkategori = idkategori;
    }

    public String getIdsatuan() {
        return idsatuan;
    }

    public void setIdsatuan(String idsatuan) {
        this.idsatuan = idsatuan;
    }

    public String getBarang() {
        return barang;
    }

    public void setBarang(String barang) {
        this.barang = barang;
    }


    public String getIdtoko() {
        return idtoko;
    }

    public void setIdtoko(String idtoko) {
        this.idtoko = idtoko;
    }

    public double getHarga() {
        return harga;
    }

    public void setHarga(double harga) {
        this.harga = harga;
    }

    public double getHargabeli() {
        return hargabeli;
    }

    public void setHargabeli(double hargabeli) {
        this.hargabeli = hargabeli;
    }

    public double getStok() {
        return stok;
    }

    public void setStok(double stok) {
        this.stok = stok;
    }

    public int getFlag_stok() {
        return flag_stok;
    }

    public void setFlag_stok(int flag_stok) {
        this.flag_stok = flag_stok;
    }
}
