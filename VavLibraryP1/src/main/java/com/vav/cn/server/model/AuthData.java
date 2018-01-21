package com.vav.cn.server.model;

/**
 * Created by thunde91 on 8/10/16.
 */
public class AuthData {
    private String authToken;
    private String guid;
    private String referralCode;

    public AuthData() {
    }

    public AuthData(String authToken, String guid) {
        this.authToken = authToken;
        this.guid = guid;
    }

    public AuthData(String authToken, String guid, String referralCode) {
        this.authToken = authToken;
        this.guid = guid;
        this.referralCode = referralCode;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getReferralCode() {
        return referralCode;
    }

    public void setReferralCode(String referralCode) {
        this.referralCode = referralCode;
    }
}
