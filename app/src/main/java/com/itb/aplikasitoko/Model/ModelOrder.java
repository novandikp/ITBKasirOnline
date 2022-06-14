package com.itb.aplikasitoko.Model;

import java.util.List;

public class ModelOrder {
    private int idjual;
    private String fakturjual;
    private double bayar;
    private double total;
    private double kembali;
    private double potongan;
    private int idpelanggan;
    private int idpegawai;
    private String tanggal_jual;
    private List<ModelDetailJual> detail;

    public ModelOrder(ModelJual model, List<ModelDetailJual> modelDetailJualList) {
        this.fakturjual = model.fakturjual;
        this.bayar = model.bayar;
        this.total = model.total;
        this.kembali = model.kembali;
        this.potongan = model.potongan;
        this.idpelanggan = model.idpelanggan;
        this.idpegawai = model.idpegawai;
        this.tanggal_jual = model.tanggal_jual;
        this.detail = modelDetailJualList;
    }
    public ModelJual getJual(){
        ModelJual model = new ModelJual(fakturjual, bayar, total, kembali, potongan, idpelanggan, idpegawai, tanggal_jual);
        model.setIdjual(idjual);
        return  model;
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

    public List<ModelDetailJual> getDetail() {
        return detail;
    }

    public void setDetail(List<ModelDetailJual> detail) {
        this.detail = detail;
    }
}
