package com.itb.aplikasitoko.Model;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "tblsatuan")
public class ModelSatuan {
    @PrimaryKey(autoGenerate = true)
    private int idsatuan;

    public ModelSatuan() {

    }

    public String getNama_satuan() {
        return nama_satuan;
    }

    private String nama_satuan;

    @Ignore
    private String idtoko;


    public ModelSatuan(String nama_satuan) {
        this.nama_satuan = nama_satuan;
    }

    public int getIdsatuan() {
        return idsatuan;
    }

    public void setIdsatuan(int idsatuan) {
        this.idsatuan = idsatuan;
    }



    public void setNama_satuan(String nama_satuan) {
        this.nama_satuan = nama_satuan;
    }

    public String getIdtoko() {
        return idtoko;
    }

    public void setIdtoko(String idtoko) {
        this.idtoko = idtoko;
    }
}
