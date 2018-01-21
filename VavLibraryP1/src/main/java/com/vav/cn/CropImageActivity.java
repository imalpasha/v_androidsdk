package com.vav.cn;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.isseiaoki.simplecropview.CropImageView;
import com.vav.cn.config.Config;
import com.vav.cn.util.GeneralUtil;
import com.vav.cn.util.PreferenceManagerCustom;

import java.io.File;

/**
 * Created by Handrata Samsul on 1/12/2016.
 */
public class CropImageActivity extends AppCompatActivity {
    public static final String IMAGE_PATH_KEY = "IMAGE_PATH_KEY";

    private TextView mLblTitle;
    private CropImageView mCropImageView;
    private TextView mBtnCrop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crop_image);

        mLblTitle = (TextView) findViewById(R.id.lblHeaderTitle);
        mLblTitle.setText(R.string.crop_image_title);

        mCropImageView = (CropImageView) findViewById(R.id.cropImageView);
        mCropImageView.setCropMode(CropImageView.CropMode.CIRCLE);
        mCropImageView.setFrameColor(getResources().getColor(R.color.primary_color));
        mCropImageView.setHandleColor(getResources().getColor(R.color.primary_color));
        mCropImageView.setTouchPaddingInDp(16);
        mCropImageView.setHandleSizeInDp(8);
        mCropImageView.setHandleShowMode(CropImageView.ShowMode.SHOW_ALWAYS);
        mCropImageView.setGuideShowMode(CropImageView.ShowMode.SHOW_ON_TOUCH);

        Intent intent = getIntent();
        String mImgPathKey = intent.getStringExtra(IMAGE_PATH_KEY);
        File image = new File(mImgPathKey);
        Bitmap bitmap = BitmapFactory.decodeFile(image.getAbsolutePath());
        mCropImageView.setImageBitmap(bitmap);

        mBtnCrop = (TextView) findViewById(R.id.btnCrop);
        mBtnCrop.setFocusable(true);
        mBtnCrop.setClickable(true);
        mBtnCrop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String filePath = GeneralUtil.getInstance().saveImageInExternalCacheDir(CropImageActivity.this, mCropImageView.getCroppedBitmap(), Config.USER_PROFILE_PICT_FILE_NAME);
                PreferenceManagerCustom.getInstance().setUserProfilePictPath(filePath);
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
            }
        });

    }
}
