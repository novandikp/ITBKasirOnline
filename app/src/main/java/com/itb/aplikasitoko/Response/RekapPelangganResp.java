package com.itb.aplikasitoko.Response;

import com.itb.aplikasitoko.ViewModel.ViewModelRekapPelanggan;

import java.util.List;

public class RekapPelangganResp {
    private boolean status;
    private List<ViewModelRekapPelanggan> data;

    public RekapPelangganResp(boolean status, List<ViewModelRekapPelanggan> data) {
        this.status = status;
        this.data = data;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public List<ViewModelRekapPelanggan> getData() {
        return data;
    }

    public void setData(List<ViewModelRekapPelanggan> data) {
        this.data = data;
    }
}
