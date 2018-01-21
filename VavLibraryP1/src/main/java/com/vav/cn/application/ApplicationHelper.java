package com.vav.cn.application;

import android.app.Application;

/**
 * Created by thunde91 on 4/30/16.
 */
public class ApplicationHelper {
    private static ApplicationHelper instance = new ApplicationHelper();
    private Application application;

    private ApplicationHelper() {
    }

    public static ApplicationHelper getInstance() {
        return instance;
    }

    public void setCurrentApplication(Application application) {
        this.application = application;
    }

    public Application getApplication() {
        return application;
    }
}
