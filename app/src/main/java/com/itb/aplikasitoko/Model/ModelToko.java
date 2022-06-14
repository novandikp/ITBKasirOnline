package com.itb.aplikasitoko.Model;


public class ModelToko {
    private String idtoko;
    private String nama_toko;
    private String alamat_toko;
    private String nomer_toko;
    private String nama_pemilik;
    private String email_toko;
    private String password_toko;
    private String jenis_toko;

    public String getIdtoko() {
        return idtoko;
    }


    public ModelToko() {
        this.idtoko = idtoko;
        this.nama_toko = nama_toko;
        this.alamat_toko = alamat_toko;
        this.nomer_toko = nomer_toko;
        this.nama_pemilik = nama_pemilik;
        this.email_toko = email_toko;
        this.password_toko = password_toko;
        this.jenis_toko = jenis_toko;
    }

    public void setIdtoko(String idtoko) {
        this.idtoko = idtoko;
    }

    public String getNama_toko() {
        return nama_toko;
    }

    public void setNama_toko(String nama_toko) {
        this.nama_toko = nama_toko;
    }

    public String getAlamat_toko() {
        return alamat_toko;
    }

    public void setAlamat_toko(String alamat_toko) {
        this.alamat_toko = alamat_toko;
    }

    public String getNomer_toko() {
        return nomer_toko;
    }

    public void setNomer_toko(String nomer_toko) {
        this.nomer_toko = nomer_toko;
    }

    public String getNama_pemilik() {
        return nama_pemilik;
    }

    public void setNama_pemilik(String nama_pemilik) {
        this.nama_pemilik = nama_pemilik;
    }

    public String getEmail_toko() {
        return email_toko;
    }

    public void setEmail_toko(String email_toko) {
        this.email_toko = email_toko;
    }

    public String getPassword_toko() {
        return password_toko;
    }

    public void setPassword_toko(String password_toko) {
        this.password_toko = password_toko;
    }

    public String getJenis_toko() {
        return jenis_toko;
    }

    public void setJenis_toko(String jenis_toko) {
        this.jenis_toko = jenis_toko;
    }
}
