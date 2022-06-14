package com.itb.aplikasitoko.Response;

import com.itb.aplikasitoko.Model.ModelKategori;

import java.util.List;

public class KategoriGetResp {
    private boolean status;
    private List<ModelKategori> data;

    public KategoriGetResp(boolean status, List<ModelKategori> data) {
        this.status = status;
        this.data = data;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public List<ModelKategori> getData() {
        return data;
    }

    public void setData(List<ModelKategori> data) {
        this.data = data;
    }


}
