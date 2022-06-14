package com.itb.aplikasitoko.Response;

import com.itb.aplikasitoko.ViewModel.ViewModelRekapKategori;

import java.util.List;

public class RekapKategoriResp {
    private boolean status;
    private List<ViewModelRekapKategori> data;

    public RekapKategoriResp(boolean status, List<ViewModelRekapKategori> data) {
        this.status = status;
        this.data = data;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public List<ViewModelRekapKategori> getData() {
        return data;
    }

    public void setData(List<ViewModelRekapKategori> data) {
        this.data = data;
    }
}
