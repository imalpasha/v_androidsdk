package com.vav.cn.gcm;

import android.content.Intent;

/**
 * Created by Handrata Samsul on 2/10/2016.
 */
public interface GcmListener {
    public abstract void onRegistrationSucceed(Intent intent);

    public abstract void onRegistrationError();
}
