package com.example.thunde91.samplesdk;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.vav.cn.application.ApplicationHelper;
import com.vav.cn.directhelper.VavDirectGenerator;
import com.vav.cn.listener.OnCloseFragmentListener;
import com.vav.cn.listener.OnLoginCallbackSuccess;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by thunde91 on 8/9/16.
 */
public class SecondFragmentActivity extends AppCompatActivity implements OnCloseFragmentListener {
    private List<ImageView> mBottomMenuImageList;
    private int mCurrentActiveMenu = -1;
    private ViewGroup mViewGroupMenuVav;
    private ViewGroup mViewGroupMenuVoucher;
    private ImageView mImgMenuVav;
    private ImageView mImgMenuVoucher;
    private RequestType requestType;
    private Fragment currentFragment;

    @Override
    public void onCloseFragment() {
        finish();
    }

    private enum RequestType {
        VOUCHER_BOOK, VAVING
    }

    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        return super.onCreateView(parent, name, context, attrs);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_sample_fragment);
        mViewGroupMenuVav = (ViewGroup) findViewById(com.vav.cn.R.id.viewGroupMenuVav);
        mViewGroupMenuVoucher = (ViewGroup) findViewById(com.vav.cn.R.id.viewGroupMenuVoucher);

        mViewGroupMenuVav.setFocusable(true);
        mViewGroupMenuVav.setClickable(true);
        mViewGroupMenuVav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                directToVaving();
            }
        });
        mViewGroupMenuVoucher.setFocusable(true);
        mViewGroupMenuVoucher.setClickable(true);
        mViewGroupMenuVoucher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                directToVoucher();
            }
        });

        mImgMenuVav = (ImageView) findViewById(com.vav.cn.R.id.imgMenuVav);
        mImgMenuVoucher = (ImageView) findViewById(com.vav.cn.R.id.imgMenuVoucher);

        mBottomMenuImageList = new ArrayList<>();
        mBottomMenuImageList.add(mImgMenuVav);
        mBottomMenuImageList.add(mImgMenuVoucher);
        ApplicationHelper.getInstance().setCurrentApplication(getApplication());
        directToVoucher();
    }

    private void directToVaving() {
        final FragmentTransaction ft = getFragmentManager().beginTransaction();
        requestType = RequestType.VAVING;
        try {
            currentFragment = VavDirectGenerator.getInstance().goToVavingFragment(ft, R.id.frame_home_member_content, this);
        } catch (Exception e) {
            showText("Exception Occur " + e.getMessage());
        }
    }

    private void directToVoucher() {
        final FragmentTransaction ft = getFragmentManager().beginTransaction();
        requestType = RequestType.VOUCHER_BOOK;
        try {
            currentFragment = VavDirectGenerator.getInstance().goToVoucherBook(ft, R.id.frame_home_member_content, this);
        } catch (Exception e) {
            showText("Exception Occur " + e.getMessage());
        }
    }

    public void showText(final String message) {
        if (!isFinishing()) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(SecondFragmentActivity.this, message, Toast.LENGTH_SHORT).show();

                }
            });
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (currentFragment != null) {
            currentFragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}
