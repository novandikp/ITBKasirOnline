package com.itb.aplikasitoko.Response;

import com.itb.aplikasitoko.Model.ModelKategori;

public class KategoriResponse {
    private boolean status;
    private ModelKategori data; //ini perhatikan bentuknya, kalau object btkny kaya gini. klau btknya gk tepat nnti masuk ke localizederror

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public ModelKategori getData() {
        return data;
    }

    public void setData(ModelKategori data) {
        this.data = data;
    }
}
