package com.vav.cn.server.model;

/**
 * Created by Handrata Samsul on 1/15/2016.
 */
public class LoginResponse {
    private String guid;
    private String auth_token;
    private String status;
    private String message;

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getAuthToken() {
        return auth_token;
    }

    public void setAuthToken(String auth_token) {
        this.auth_token = auth_token;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
