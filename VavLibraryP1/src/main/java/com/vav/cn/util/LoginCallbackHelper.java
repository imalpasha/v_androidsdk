package com.vav.cn.util;

import com.vav.cn.listener.OnLoginCallbackSuccess;

/**
 * Created by calvin on 4/14/16.
 */
public class LoginCallbackHelper {
    private static LoginCallbackHelper instance = new LoginCallbackHelper();
    private OnLoginCallbackSuccess onLoginCallbackSuccess;

    private LoginCallbackHelper() {
    }

    public static LoginCallbackHelper getInstance() {
        return instance;
    }

    public OnLoginCallbackSuccess getOnLoginCallbackSuccess() {
        return onLoginCallbackSuccess;
    }

    public void setOnLoginCallbackSuccess(OnLoginCallbackSuccess onLoginCallbackSuccess) {
        this.onLoginCallbackSuccess = onLoginCallbackSuccess;
    }
}
