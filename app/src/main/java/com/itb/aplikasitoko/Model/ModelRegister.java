package com.itb.aplikasitoko.Model;

public class ModelRegister {
    private String email;
    private String password;
    private String NamaUsaha;
    private String NamaPemilik;
    private String Lokasi;
    private String JenisUsaha;

    public String getJenisUsaha() {
        return JenisUsaha;
    }

    public void setJenisUsaha(String jenisUsaha) {
        JenisUsaha = jenisUsaha;
    }

    public String getNamaUsaha() {
        return NamaUsaha;
    }

    public void setNamaUsaha(String namaUsaha) {
        NamaUsaha = namaUsaha;
    }

    public String getNamaPemilik() {
        return NamaPemilik;
    }

    public void setNamaPemilik(String pemilikUsaha) {
        NamaPemilik = NamaPemilik;
    }

    public String getLokasi() {
        return Lokasi;
    }

    public void setLokasi(String lokasi) {
        Lokasi = lokasi;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
