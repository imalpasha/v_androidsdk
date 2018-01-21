package com.example.thunde91.samplesdk;

import android.app.Activity;
import android.app.Application;
import android.support.multidex.MultiDex;

import com.vav.cn.application.ApplicationHelper;
import com.vav.cn.directhelper.VavDirectGenerator;
import com.vav.cn.util.PreferenceManagerCustom;


/**
 * Created by Handrata Samsul on 1/11/2015.
 */
public class MainApplication extends Application {


    @Override
    public void onCreate() {
        super.onCreate();


        MultiDex.install(this);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }
}
