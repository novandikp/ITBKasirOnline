package com.itb.aplikasitoko.Response;

import com.itb.aplikasitoko.Model.ModelBarang;

import java.util.List;

public class BarangGetResp {
    private boolean status;
    private List<ModelBarang> data;

    public BarangGetResp(boolean status, List<ModelBarang> data) {
        this.status = status;
        this.data = data;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public List<ModelBarang> getData() {
        return data;
    }

    public void setData(List<ModelBarang> data) {
        this.data = data;
    }
}
