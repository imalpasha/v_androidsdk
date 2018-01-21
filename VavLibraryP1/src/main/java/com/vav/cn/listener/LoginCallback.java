package com.vav.cn.listener;


import com.vav.cn.model.ErrorInfo;

/**
 * Created by thunde91 on 8/8/16.
 *
 * This callback interface represents the success or failure of login process.
 *
 */
public interface LoginCallback{
    /**
     * This callback method is invoked when the login is successful.
     */
    void onLoginSuccess();

    /**
     * This callback method is invoked when the login is failed
     */
    void onLoginFailure(ErrorInfo errorInfo);
}
