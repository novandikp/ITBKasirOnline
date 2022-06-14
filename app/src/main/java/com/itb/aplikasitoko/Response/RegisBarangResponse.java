package com.itb.aplikasitoko.Response;

import com.itb.aplikasitoko.ViewModel.ViewModelBarang;

public class RegisBarangResponse {
    private Boolean status;
    private ViewModelBarang data; //ini krn objectnya itu berii sm ky modelview barang

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public ViewModelBarang getData() {
        return data;
    }

    public void setData(ViewModelBarang data) {
        this.data = data;
    }
}
