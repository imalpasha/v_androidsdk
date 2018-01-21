package com.vav.cn.server.model;

/**
 * Created by Handrata Samsul on 2/3/2016.
 */
public class UseVoucherResponse {
    private String status;
    private String message;
    private String serial_no;
    private boolean isRequestVoucher;

    public boolean isRequestVoucher() {
        return isRequestVoucher;
    }

    public void setIsRequestVoucher(boolean isRequestVoucher) {
        this.isRequestVoucher = isRequestVoucher;
    }

    public String getSerial_no() {
        return serial_no;
    }

    public void setSerial_no(String serial_no) {
        this.serial_no = serial_no;
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
