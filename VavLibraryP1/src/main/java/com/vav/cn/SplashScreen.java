package com.vav.cn;

import android.content.Intent;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.vav.cn.util.GeneralUtil;
import com.vav.cn.util.PreferenceManagerCustom;


/**
 * Created by Handrata Samsul on 1/15/2015.
 */
public class SplashScreen extends AppCompatActivity {
    private static final String TAG_LOG = SplashScreen.class.getSimpleName();

    /**
     * Duration of wait
     **/
    private final int SPLASH_DISPLAY_LENGTH = 1000;
    private LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        PreferenceManagerCustom.getInstance().setIsWentThroughSplash(true);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (PreferenceManagerCustom.getInstance().getUserIsLoggedIn()) {
                    GeneralUtil.getInstance().goToHomeMemberPage(SplashScreen.this);
                    /*Intent homeMemberIntent = new Intent(getBaseContext(), HomeMember2Activity.class);
                    SplashScreen.this.startActivity(homeMemberIntent);*/
                    SplashScreen.this.finish();
                } else {
                    Intent mainIntent = new Intent(SplashScreen.this, HomeGuestActivity.class);
                    SplashScreen.this.startActivity(mainIntent);
                    SplashScreen.this.finish();
                }
            }
        }, SPLASH_DISPLAY_LENGTH);
        linearLayout = (LinearLayout) findViewById(R.id.linearLayout);
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        Glide.with(SplashScreen.this).load(R.drawable.login_bg).into(new SimpleTarget<GlideDrawable>(size.x, size.y) {
            @Override
            public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    linearLayout.setBackground(resource);
                } else {
                    linearLayout.setBackgroundDrawable(resource);
                }
            }
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }
}
