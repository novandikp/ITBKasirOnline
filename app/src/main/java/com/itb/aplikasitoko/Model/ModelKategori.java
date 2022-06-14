package com.itb.aplikasitoko.Model;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "tblkategori")
public class ModelKategori {
    @PrimaryKey(autoGenerate = true)
    private int idkategori;

    private String nama_kategori;

    @Ignore
    private String idtoko;

    @Ignore
    public ModelKategori(String nama_kategori, String idtoko, int idkategori) {
        this.nama_kategori = nama_kategori;
        this.idtoko = idtoko;
        this.idkategori = idkategori;
    }

    public ModelKategori(String nama_kategori) {
        this.nama_kategori = nama_kategori;
    }

    public ModelKategori() {

    }

    public int getIdkategori() {
        return idkategori;
    }

    public void setIdkategori(int idkategori) {
        this.idkategori = idkategori;
    }

    public String getNama_kategori() {
        return nama_kategori;
    }

    public void setNama_kategori(String nama_kategori) {
        this.nama_kategori = nama_kategori;
    }

    public String getIdtoko() {
        return idtoko;
    }

    public void setIdtoko(String idtoko) {
        this.idtoko = idtoko;
    }
}
