package com.itb.aplikasitoko.Response;

import com.itb.aplikasitoko.Model.ModelLogin;

public class LoginResponse {
    private String token;
    private String page;
    private ModelLogin data;

    public ModelLogin getData() {
        return data;
    }

    public void setData(ModelLogin data) {
        this.data = data;
    }

    public String getToken() {
        return token;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public void setToken(String token) {
        this.token = token;
    }


}
