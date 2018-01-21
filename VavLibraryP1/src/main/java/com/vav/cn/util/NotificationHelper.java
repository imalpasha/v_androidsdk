package com.vav.cn.util;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.vav.cn.HomeMemberActivity;
import com.vav.cn.R;
import com.vav.cn.config.Config;

import java.util.Date;

/**
 * Created by Handrata Samsul on 2/9/2016.
 */
public class NotificationHelper {
    private static final String TAG_LOG = NotificationHelper.class.getSimpleName();

    private static NotificationHelper instance = new NotificationHelper();

    public static NotificationHelper getInstance() {
        return instance;
    }

    public void cancelAllNotif(Context context) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.cancelAll();
    }

    public void showLocalNotification(Context context, String title, String message) {
        Bitmap largeIcon = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.stamp_active);

        Intent launchIntent = new Intent(context, HomeMemberActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(
                context,
                0,
                launchIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Notification notification = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.notification_icon)
                .setLargeIcon(largeIcon)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)
                .build();

        notification.flags |= Notification.FLAG_AUTO_CANCEL;

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        long time = new Date().getTime();
        String tmpStr = String.valueOf(time);
        String last4Str = tmpStr.substring(tmpStr.length() - 5);
        int notificationId = Integer.valueOf(last4Str);

        notificationManager.notify(Config.COMPANY_ID, notification);
    }

    public void showGcmNotificationOpenSplashScreen(Context context, String title, String message, String type) {
        Intent intent = new Intent(context, HomeMemberActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("type", type);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,
                0 /* Request code */,
                intent,
                PendingIntent.FLAG_ONE_SHOT);

        Bitmap largeIcon = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.stamp_active);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.notification_icon)
                .setLargeIcon(largeIcon)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

}
