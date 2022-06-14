package com.itb.aplikasitoko.Response;

import com.itb.aplikasitoko.Model.ModelSatuan;

import java.util.List;

public class SatuanGetResp {
    private boolean status;
    private List<ModelSatuan> data;

    public SatuanGetResp(boolean status, List<ModelSatuan> data) {
        this.status = status;
        this.data = data;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public List<ModelSatuan> getData() {
        return data;
    }

    public void setData(List<ModelSatuan> data) {
        this.data = data;
    }
}
