package com.itb.aplikasitoko.Response;

import com.itb.aplikasitoko.ViewModel.ViewModelJual;

import java.util.List;

public class PendapatanGetResp {
    private boolean status;
    private List<ViewModelJual> data;

    public PendapatanGetResp(boolean status, List<ViewModelJual> data) {
        this.status = status;
        this.data = data;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public List<ViewModelJual> getData() {
        return data;
    }

    public void setData(List<ViewModelJual> data) {
        this.data = data;
    }
}
