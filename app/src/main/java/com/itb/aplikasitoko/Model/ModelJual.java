package com.itb.aplikasitoko.Model;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "tbljual")
public class ModelJual  {
    @PrimaryKey(autoGenerate = true)
    protected int idjual;

    protected String fakturjual;

    protected double bayar;

    protected double total;

    protected double kembali;

    protected double potongan;

    protected int idpelanggan;

    protected int idpegawai;

    protected String tanggal_jual;


    @Ignore
    private int idtoko;

    public ModelJual(String fakturjual, double bayar, double total, double kembali, double potongan, int idpelanggan, int idpegawai, String tanggal_jual) {
        this.fakturjual = fakturjual;
        this.bayar = bayar;
        this.total = total;
        this.kembali = kembali;
        this.potongan = potongan;
        this.idpelanggan = idpelanggan;
        this.idpegawai = idpegawai;
        this.tanggal_jual = tanggal_jual;

    }

    public int getIdjual() {
        return idjual;
    }

    public void setIdjual(int idjual) {
        this.idjual = idjual;
    }

    public String getFakturjual() {
        return fakturjual;
    }

    public void setFakturjual(String fakturjual) {
        this.fakturjual = fakturjual;
    }

    public double getBayar() {
        return bayar;
    }

    public void setBayar(double bayar) {
        this.bayar = bayar;
        this.kembali = bayar - this.total;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public double getKembali() {
        return kembali;
    }

    public void setKembali(double kembali) {
        this.kembali = kembali;
    }

    public double getPotongan() {
        return potongan;
    }

    public void setPotongan(double potongan) {
        this.potongan = potongan;
    }

    public int getIdpelanggan() {
        return idpelanggan;
    }

    public void setIdpelanggan(int idpelanggan) {
        this.idpelanggan = idpelanggan;
    }

    public int getIdpegawai() {
        return idpegawai;
    }

    public void setIdpegawai(int idpegawai) {
        this.idpegawai = idpegawai;
    }

    public String getTanggal_jual() {
        return tanggal_jual;
    }

    public void setTanggal_jual(String tanggal_jual) {
        this.tanggal_jual = tanggal_jual;
    }

    public int getIdtoko() {
        return idtoko;
    }

    public void setIdtoko(int idtoko) {
        this.idtoko = idtoko;
    }
}