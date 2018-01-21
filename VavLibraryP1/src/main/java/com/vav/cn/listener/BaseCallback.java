package com.vav.cn.listener;


import com.vav.cn.model.ErrorInfo;
import com.vav.cn.model.GeneralResponse;

/**
 * Created by thunde91 on 8/11/16.
 */

public interface BaseCallback {
    /**
     * This callback method is invoked when the login is successful.
     */
    void onRequestSuccess(GeneralResponse generalResponse);

    /**
     * This callback method is invoked when the login is failed
     */
    void onRequestFailure(ErrorInfo errorInfo);
}
