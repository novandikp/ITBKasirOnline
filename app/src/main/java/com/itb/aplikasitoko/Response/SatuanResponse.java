package com.itb.aplikasitoko.Response;

import com.itb.aplikasitoko.Model.ModelSatuan;

public class SatuanResponse {
    private boolean status;
    private ModelSatuan data;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public ModelSatuan getData() {
        return data;
    }

    public void setData(ModelSatuan data) {
        this.data = data;
    }
}
