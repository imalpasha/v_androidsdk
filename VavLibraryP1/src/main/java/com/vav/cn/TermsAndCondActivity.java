package com.vav.cn;

/**
 * Created by Handrata Samsul on 1/11/2016.
 */

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Handrata Samsul on 1/11/2016.
 */
public class TermsAndCondActivity extends AppCompatActivity {
    public static final String URL_RESOURCE = "WEB_URL";
    public static final String TITLE_RESOURCE = "WEB_TITLE";
    private ImageView mBtnBack;
    private TextView mLblTitle;
    private WebView mWebView;
    private String url;
    private String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.terms_and_cond);
        if (getIntent().getExtras() != null) {
            url = getIntent().getExtras().getString(URL_RESOURCE);
            title = getIntent().getExtras().getString(TITLE_RESOURCE);
        }
        mBtnBack = (ImageView) findViewById(R.id.btnHeaderLeft);
        mBtnBack.setVisibility(View.VISIBLE);
        mBtnBack.setFocusable(true);
        mBtnBack.setClickable(true);
        mBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        mLblTitle = (TextView) findViewById(R.id.lblHeaderTitle);
        mLblTitle.setText(title);

        mWebView = (WebView) findViewById(R.id.wvAboutUs);
        mWebView.loadUrl(url);
        mWebView.setBackgroundColor(Color.TRANSPARENT);
        mWebView.setScrollbarFadingEnabled(true);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
