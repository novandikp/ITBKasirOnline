package com.itb.aplikasitoko.Response;

import com.itb.aplikasitoko.ViewModel.ViewModelRekapPegawai;

import java.util.List;

public class RekapPegawaiResp {
    private boolean status;
    private List<ViewModelRekapPegawai> data;

    public RekapPegawaiResp(boolean status, List<ViewModelRekapPegawai> data) {
        this.status = status;
        this.data = data;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public List<ViewModelRekapPegawai> getData() {
        return data;
    }

    public void setData(List<ViewModelRekapPegawai> data) {
        this.data = data;
    }
}
