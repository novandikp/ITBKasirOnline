package com.itb.aplikasitoko.Response;

import com.itb.aplikasitoko.Model.ModelPegawai;

import java.util.List;

public class PegawaiGetResp {
    private boolean status;
    private List<ModelPegawai> data;

    public PegawaiGetResp(boolean status, List<ModelPegawai> data) {
        this.status = status;
        this.data = data;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public List<ModelPegawai> getData() {
        return data;
    }

    public void setData(List<ModelPegawai> data) {
        this.data = data;
    }
}
