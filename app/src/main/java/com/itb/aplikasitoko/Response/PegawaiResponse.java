package com.itb.aplikasitoko.Response;

import com.itb.aplikasitoko.Model.ModelPegawai;

public class PegawaiResponse {
    private boolean status;
    private ModelPegawai data;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public ModelPegawai getData() {
        return data;
    }

    public void setData(ModelPegawai data) {
        this.data = data;
    }
}
