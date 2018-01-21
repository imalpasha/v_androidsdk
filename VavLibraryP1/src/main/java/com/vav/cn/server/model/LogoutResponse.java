package com.vav.cn.server.model;

/**
 * Created by Handrata Samsul on 1/26/2016.
 */
public class LogoutResponse {
    private String status;
    private String message;

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
