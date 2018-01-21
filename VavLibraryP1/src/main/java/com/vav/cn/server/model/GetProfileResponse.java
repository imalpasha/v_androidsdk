package com.vav.cn.server.model;

/**
 * Created by Handrata Samsul on 1/15/2016.
 */
public class GetProfileResponse {
    private String name;
    private String email;
    private String phone;
    private int vav_counter;
    private String photo_url;
    private String status;
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPhoto_url() {

        return photo_url;
    }

    public void setPhoto_url(String photo_url) {
        this.photo_url = photo_url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getVav_counter() {
        return vav_counter;
    }

    public void setVav_counter(int vav_counter) {
        this.vav_counter = vav_counter;
    }
}
