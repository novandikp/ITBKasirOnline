package com.itb.aplikasitoko.Model;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "tbldetailjual")
public class ModelDetailJual
{
    @PrimaryKey(autoGenerate = true)
    private int iddetailjual;
    private int idjual;
    private String idbarang;
    private double jumlahjual;
    private double hargajual;

    @Ignore
    private int idtoko;


    public ModelDetailJual(int idjual, String idbarang, double jumlahjual, double hargajual) {
        this.idjual = idjual;
        this.idbarang = idbarang;
        this.jumlahjual = jumlahjual;
        this.hargajual = hargajual;
    }

    public int getIddetailjual() {
        return iddetailjual;
    }

    public void setIddetailjual(int iddetailjual) {
        this.iddetailjual = iddetailjual;
    }

    public int getIdjual() {
        return idjual;
    }

    public void setIdjual(int idjual) {
        this.idjual = idjual;
    }

    public String getIdbarang() {
        return idbarang;
    }

    public void setIdbarang(String idbarang) {
        this.idbarang = idbarang;
    }

    public double getJumlahjual() {
        return jumlahjual;
    }

    public void setJumlahjual(double jumlahjual) {
        this.jumlahjual = jumlahjual;
    }

    public double getHargajual() {
        return hargajual;
    }

    public void setHargajual(double hargajual) {
        this.hargajual = hargajual;
    }

    public int getIdtoko() {
        return idtoko;
    }

    public void setIdtoko(int idtoko) {
        this.idtoko = idtoko;
    }

    public void addJumlah(){
        this.jumlahjual++;
    }

    public void relieveJumlah() {
        this.jumlahjual--;
    }
}