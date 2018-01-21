package com.vav.cn.util;

import android.util.Log;

import com.vav.cn.application.ApplicationHelper;
import com.vav.cn.config.Config;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;

/**
 * Created by Handrata Samsul on 1/13/2016.
 */
public class Logger {
    private static Logger instance = new Logger();
    private final String CLASS_TAG = Logger.this.getClass().getSimpleName();
    private String mFolderPath = "";

    public Logger() {
        mFolderPath = GeneralUtil.getInstance().getAppFolderPath(ApplicationHelper.getInstance().getApplication());
    }

    public static Logger getInstance() {
        return instance;
    }

    private static String format(String tag, String msg) {
        if (tag != null && tag.length() > 0) {
            return Calendar.getInstance().getTime() + " " + tag + ">" + msg + "| ";
        } else {
            return msg;
        }
    }

    private void printToFile(String newMsg) {
        PrintWriter pw;
        String filePath = mFolderPath + File.separator + Config.LOG_FILE + ".txt";

        String logTxt = GeneralUtil.getInstance().readFromFile(filePath);
        int logTxtLength = logTxt.length();
        if (logTxtLength >= Config.MAX_LOG_FILE_CHAR) {
            int startChar = logTxtLength - (Config.MAX_LOG_FILE_CHAR - newMsg.length());
            logTxt = logTxt.substring((startChar > logTxtLength) ? logTxtLength : startChar, logTxtLength);
        }

        try {
            pw = new PrintWriter(new File(filePath));
            pw.print(logTxt + newMsg);
            pw.flush();
            pw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void v(String tag, String msg) {
        Log.v(tag, msg);
        printToFile(format(tag, msg));
    }

    public void d(String tag, String msg) {
        Log.d(tag, msg);
        printToFile(format(tag, msg));
    }

    public void i(String tag, String msg) {
        Log.i(tag, msg);
        printToFile(format(tag, msg));
    }

    public void w(String tag, String msg) {
        Log.w(tag, msg);
        printToFile(format(tag, msg));
    }

    public void e(String tag, String msg) {
        Log.e(tag, msg);
        printToFile(format(tag, msg));
    }
}