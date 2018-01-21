package com.vav.cn;

import android.app.Activity;
import android.app.Application;
import android.support.multidex.MultiDex;

import com.vav.cn.application.ApplicationHelper;
import com.vav.cn.util.PreferenceManagerCustom;


/**
 * Created by Handrata Samsul on 1/11/2015.
 */
public class MainApplication extends Application {
    public static boolean isAppRunning = false;
    private static Activity activity;

    public static Activity getActivity() {
        return activity;
    }

    public static void setActivity(Activity activity) {
        MainApplication.activity = activity;
    }

    @Override
    public void onCreate() {
        super.onCreate();
//		Fabric.with(this, new Crashlytics());
        isAppRunning = true;
        ApplicationHelper.getInstance().setCurrentApplication(this);
        PreferenceManagerCustom.getInstance().setIsWentThroughSplash(false);

        MultiDex.install(this);
    }

    @Override
    public void onTerminate() {
        isAppRunning = false;
        super.onTerminate();
    }
}
