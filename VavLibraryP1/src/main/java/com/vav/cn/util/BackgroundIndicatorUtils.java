package com.vav.cn.util;

/**
 * Created by thunde91 on 3/8/16.
 */
public class BackgroundIndicatorUtils {
    private static BackgroundIndicatorUtils ourInstance = new BackgroundIndicatorUtils();
    public boolean isFromBackground = true;

    private BackgroundIndicatorUtils() {
    }

    public static BackgroundIndicatorUtils getInstance() {
        return ourInstance;
    }

    public boolean isFromBackground() {
        return isFromBackground;
    }

    public void setIsFromBackground(boolean isFromBackground) {
        this.isFromBackground = isFromBackground;
    }
}
