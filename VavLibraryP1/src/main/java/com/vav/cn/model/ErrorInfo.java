package com.vav.cn.model;

/**
 * Created by thunde91 on 8/10/16.
 */
public class ErrorInfo {
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

    public ErrorInfo(String status, String message) {
        this.status = status;
        this.message = message;
    }
}
