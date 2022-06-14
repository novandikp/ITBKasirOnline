package com.itb.aplikasitoko.Model;

public class ModelOtp {
    private String nomor_toko;
    private String otp;

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    private String token;

    public String getNomor_toko() {
        return nomor_toko;
    }

    public void setNomor_toko(String nomor_toko) {
        this.nomor_toko = nomor_toko;
    }
}
