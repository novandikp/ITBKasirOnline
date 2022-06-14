package com.itb.aplikasitoko.Response;

import com.itb.aplikasitoko.Model.ModelPelanggan;

public class PelangganResponse {
    private boolean status;
    private ModelPelanggan data;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public ModelPelanggan getData() {
        return data;
    }

    public void setData(ModelPelanggan data) {
        this.data = data;
    }
}
