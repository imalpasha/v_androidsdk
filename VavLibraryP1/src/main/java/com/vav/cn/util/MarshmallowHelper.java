package com.vav.cn.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

import com.vav.cn.R;
import com.vav.cn.config.Config;

/**
 * Created by thunde91 on 7/28/16.
 */

public class MarshmallowHelper {
    private static MarshmallowHelper instance = new MarshmallowHelper();

    public static MarshmallowHelper getInstance() {
        return instance;
    }

    public void handleAudioPermission(Context context) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context,
                    Manifest.permission.RECORD_AUDIO)) {
                Toast.makeText(context, R.string.audio_permission_needed, Toast.LENGTH_SHORT).show();
            } else
                ActivityCompat.requestPermissions((Activity) context,
                        new String[]{Manifest.permission.RECORD_AUDIO},
                        Config.REQUEST_PERMISSION_RECORD_AUDIO);
        }
    }

    public void handleCameraPermission(Context context) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context,
                    Manifest.permission.CAMERA) || ActivityCompat.shouldShowRequestPermissionRationale((Activity) context,
                    Manifest.permission.READ_EXTERNAL_STORAGE) || ActivityCompat.shouldShowRequestPermissionRationale((Activity) context,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                Toast.makeText(context, R.string.open_camera_failed, Toast.LENGTH_SHORT).show();
            } else
                ActivityCompat.requestPermissions((Activity) context,
                        new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, Config.REQUEST_PERMISSION_CAMERA);
        } else {
            MediaOpenHelper.getInstance().openCamera(((Activity) context));
        }
    }

    public void handleStoragePermissionGallery(Context context) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context,
                    Manifest.permission.READ_EXTERNAL_STORAGE) || ActivityCompat.shouldShowRequestPermissionRationale((Activity) context,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                Toast.makeText(context, R.string.read_gallery_file_fail, Toast.LENGTH_SHORT).show();
            } else
                ActivityCompat.requestPermissions((Activity) context,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, Config.REQUEST_PERMISSION_GALLERY);
        } else {
            MediaOpenHelper.getInstance().openGallery(((Activity) context));
        }
    }
}
