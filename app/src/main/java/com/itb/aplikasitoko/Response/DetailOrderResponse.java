package com.itb.aplikasitoko.Response;

import com.itb.aplikasitoko.ViewModel.ModelViewStruk;

import java.util.List;

public class DetailOrderResponse {
    private boolean status;
    private List<ModelViewStruk> data;

    public DetailOrderResponse(boolean status, List<ModelViewStruk> data) {
        this.status = status;
        this.data = data;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public List<ModelViewStruk> getData() {
        return data;
    }

    public void setData(List<ModelViewStruk> data) {
        this.data = data;
    }
}
