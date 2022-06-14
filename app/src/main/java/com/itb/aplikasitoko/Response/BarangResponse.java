package com.itb.aplikasitoko.Response;


import com.itb.aplikasitoko.Model.ModelBarang;

public class BarangResponse {
    private boolean status;
    private ModelBarang data;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public ModelBarang getData() {
        return data;
    }

    public void setData(ModelBarang data) {
        this.data = data;
    }
}
