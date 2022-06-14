package com.itb.aplikasitoko.Response;

import com.itb.aplikasitoko.ViewModel.ViewModelDetailJual;

import java.util.List;

public class PenjualanGetResp {
    private boolean status;
    private List<ViewModelDetailJual> data;

    public PenjualanGetResp(boolean status, List<ViewModelDetailJual> data) {
        this.status = status;
        this.data = data;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public List<ViewModelDetailJual> getData() {
        return data;
    }

    public void setData(List<ViewModelDetailJual> data) {
        this.data = data;
    }
}
