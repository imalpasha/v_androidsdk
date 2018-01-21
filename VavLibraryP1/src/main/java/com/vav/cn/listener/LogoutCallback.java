package com.vav.cn.listener;

import com.vav.cn.model.ErrorInfo;

/**
 * Created by thunde91 on 8/10/16.
 */
public interface LogoutCallback {
    void onLogoutSuccess();

    void onLogoutFailure(ErrorInfo errorInfo);
}
