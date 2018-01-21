package com.vav.cn.util;

import android.app.Activity;
import android.content.Intent;

import com.vav.cn.config.Config;

/**
 * Created by thunde91 on 7/28/16.
 */
public class MediaOpenHelper {
    private static MediaOpenHelper ourInstance = new MediaOpenHelper();

    public static MediaOpenHelper getInstance() {
        return ourInstance;
    }

    private MediaOpenHelper() {
    }

    public void openCamera(Activity activity) {
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        activity.startActivityForResult(intent, Config.SELECT_PHOTO_CAMERA_CODE);
    }

    public void openGallery(Activity activity) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        activity.startActivityForResult(Intent.createChooser(intent, "Complete action using"), Config.SELECT_PHOTO_GALLERY_CODE);
    }
}
