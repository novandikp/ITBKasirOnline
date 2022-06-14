package com.itb.aplikasitoko.Response;

import com.itb.aplikasitoko.Model.ModelOrder;

public class OrderResponse {
    private String message;
    private boolean status;
    private ModelOrder data;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public ModelOrder getData() {
        return data;
    }

    public void setData(ModelOrder data) {
        this.data = data;
    }
}
