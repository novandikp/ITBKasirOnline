package com.itb.aplikasitoko.Response;

import com.itb.aplikasitoko.Model.ModelJual;
import com.itb.aplikasitoko.Model.ModelOrder;

import java.util.List;

public class JualResponse{
        private String message;
        private boolean status;
        private List<ModelJual> data;

    public List<ModelJual> getData() {
        return data;
    }

    public void setData(List<ModelJual> data) {
        this.data = data;
    }

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
}