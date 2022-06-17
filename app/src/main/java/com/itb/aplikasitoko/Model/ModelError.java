package com.itb.aplikasitoko.Model;

public class ModelError {
    public String message;
    public boolean status;

    public ModelError(String message, boolean status) {
        this.message = message;
        this.status = status;
    }

    public ModelError() {
    }
}