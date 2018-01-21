package com.vav.cn.pincast;

import com.senstation.android.pincast.Pincast;
import com.vav.cn.application.ApplicationHelper;

import java.util.Observer;

/**
 * Created by thunde91 on 4/29/16.
 */
public class PincastHelper {
    private static final PincastHelper instance = new PincastHelper();
    private Pincast pincast;

    private PincastHelper() {
        try {
            pincast = new Pincast(ApplicationHelper.getInstance().getApplication(), "zFLPKQzkkZWpPCpOrN");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static PincastHelper getInstance() {
        return instance;
    }

    public void startPincast(Observer observer) {
        if (pincast != null) {
            pincast.start();
            pincast.addObserver(observer);
        }
    }


    public void stopPincast() {
        if (instance != null) {
            pincast.stop();
            pincast.deleteObservers();
        }
    }
}
