package com.itb.aplikasitoko.Model;

public class ModelLoginPegawai {
    private String idpegawai;
    private String pin;

    public ModelLoginPegawai(String pegawai, String pin) {
        this.idpegawai = pegawai;
        this.pin = pin;
    }

    public String getIdpegawai() {
        return idpegawai;
    }

    public void setIdpegawai(String idpegawai) {
        this.idpegawai = idpegawai;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    @Override
    public String toString() {
        return "ModelLoginPegawai{" +
                "idpegawai='" + idpegawai + '\'' +
                ", pin='" + pin + '\'' +
                '}';
    }
}
