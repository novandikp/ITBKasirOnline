package com.itb.aplikasitoko.Response;

import com.itb.aplikasitoko.Model.ModelDetailJual;

import java.util.List;

public class DetailJualResp {
        private String message;
        private boolean status;
        private List<ModelDetailJual> data;

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

        public List<ModelDetailJual> getData() {
                return data;
        }

        public void setData(List<ModelDetailJual> data) {
                this.data = data;
        }
}