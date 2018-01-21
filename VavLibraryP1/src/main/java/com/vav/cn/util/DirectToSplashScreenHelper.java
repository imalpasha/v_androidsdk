package com.vav.cn.util;

import android.app.Activity;
import android.content.Intent;

import com.vav.cn.SplashScreen;
import com.vav.cn.directhelper.VavDirectGenerator;

/**
 * Created by thunde91 on 3/4/16.
 */
public class DirectToSplashScreenHelper {
    private static DirectToSplashScreenHelper instance = new DirectToSplashScreenHelper();

    private DirectToSplashScreenHelper() {
    }

    public static DirectToSplashScreenHelper getInstance() {
        return instance;
    }

    public void redirectToSplashScreen(Activity activity) {
        if (PreferenceManagerCustom.getInstance().getThirdPartyApps()) {
            PreferenceManagerCustom.getInstance().clearPreferences();
            VavDirectGenerator.getInstance().goToLoginPage();
        } else {
            PreferenceManagerCustom.getInstance().clearPreferences();
            Intent mainIntent = new Intent(activity, SplashScreen.class);
            activity.startActivity(mainIntent);
            activity.finish();
        }
    }
}
