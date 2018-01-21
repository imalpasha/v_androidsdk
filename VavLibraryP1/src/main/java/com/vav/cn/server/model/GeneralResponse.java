package com.vav.cn.server.model;

/**
 * Created by Handrata Samsul on 1/15/2016.
 */
public class GeneralResponse {
    private String status;
    private String message;
    private String error;

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

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

}
