package com.vav.cn.gcm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;

import com.google.android.gms.gcm.GcmPubSub;
import com.vav.cn.application.ApplicationHelper;
import com.vav.cn.util.PreferenceManagerCustom;

import java.io.IOException;

/**
 * Created by Handrata Samsul on 2/10/2016.
 */
public class GcmHelper {
    public static final String[] TOPICS = {"Global", "Marketing", "Android", "Special"};
    private static final String TAG_LOG = GcmHelper.class.getSimpleName();
    private static GcmHelper instance = new GcmHelper();
    private BroadcastReceiver mRegistrationBroadcastReceiver;

    public static GcmHelper getInstance() {
        return instance;
    }

    public void initializeBroadcastReceiver(final GcmListener listener) {
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                boolean sentToken = PreferenceManagerCustom.getInstance().getGcmToken() != null;
                if (sentToken) {
                    listener.onRegistrationSucceed(intent);
                } else {
                    listener.onRegistrationError();
                }
            }
        };
    }

    public void registerRegistrationReceiver(Context context) {
        LocalBroadcastManager.getInstance(context).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(GcmConfig.REGISTRATION_COMPLETE));
    }

    public void unregisterRegistrationReceiver(Context context) {
        LocalBroadcastManager.getInstance(context).unregisterReceiver(mRegistrationBroadcastReceiver);
    }

    public void unregisterTopics() throws IOException {
        GcmPubSub pubSub = GcmPubSub.getInstance(ApplicationHelper.getInstance().getApplication());
        for (String topic : TOPICS) {
            pubSub.unsubscribe(PreferenceManagerCustom.getInstance().getGcmToken(), "/topics/" + topic);
        }
    }
}
