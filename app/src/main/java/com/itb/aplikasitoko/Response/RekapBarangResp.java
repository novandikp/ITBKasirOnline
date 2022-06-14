package com.itb.aplikasitoko.Response;

import com.itb.aplikasitoko.ViewModel.ViewModelRekapBarang;

import java.util.List;

public class RekapBarangResp {
    private boolean status;
    private List<ViewModelRekapBarang> data;

    public RekapBarangResp(boolean status, List<ViewModelRekapBarang> data) {
        this.status = status;
        this.data = data;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public List<ViewModelRekapBarang> getData() {
        return data;
    }

    public void setData(List<ViewModelRekapBarang> data) {
        this.data = data;
    }
}
