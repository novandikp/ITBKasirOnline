package com.itb.aplikasitoko.Response;

import com.itb.aplikasitoko.Model.ModelPelanggan;

import java.util.List;

public class PelangganGetResp {
    private boolean status;
    private List<ModelPelanggan> data;

    public PelangganGetResp(boolean status, List<ModelPelanggan> data) {
        this.status = status;
        this.data = data;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public List<ModelPelanggan> getData() {
        return data;
    }

    public void setData(List<ModelPelanggan> data) {
        this.data = data;
    }
}
