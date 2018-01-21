package com.vav.cn.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.vav.cn.R;

import java.util.HashMap;

/**
 * Created by Handrata Samsul on 1/25/2016.
 */
public class VoucherWebviewFragment extends Fragment {
    public static final String TAG_LOG = VoucherFragment.class.getSimpleName();
    public static final String TAG_PARAM_URL_ITEM = "TAG_PARAM_URL_ITEM";
    public static final String TAG_PARAM_HTML_TEXT = "TAG_PARAM_HTML_TEXT";
    public static final String TAG_PARAM_TITLE = "TAG_PARAM_TITLE";

    private WebView webView;
    private TextView mLblHeaderTitle;
    private ImageView mBtnHeaderLeft;
    private String htmlText;
    private String urlWebview;
    private String title;
    private HashMap<String, String> headers = new HashMap<>();

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
        urlWebview = args.getString(TAG_PARAM_URL_ITEM);
        title = args.getString(TAG_PARAM_TITLE);
        htmlText = args.getString(TAG_PARAM_HTML_TEXT);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (container == null) {
            return null;
        }
        View view = inflater.inflate(R.layout.details_voucher_webview, container, false);

        mLblHeaderTitle = (TextView) view.findViewById(R.id.lblHeaderTitle);
        mLblHeaderTitle.setText(title);
        mBtnHeaderLeft = (ImageView) view.findViewById(R.id.btnHeaderLeft);
        mBtnHeaderLeft.setVisibility(View.VISIBLE);
        mBtnHeaderLeft.setFocusable(true);
        mBtnHeaderLeft.setClickable(true);
        mBtnHeaderLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                Fragment fragment = fm.findFragmentByTag(TAG_LOG);
                if (fragment != null) {
                    ft.remove(fragment);
                    ft.commit();
                    fm.popBackStack();
                }
            }
        });
        webView = (WebView) view.findViewById(R.id.voucherWebview);
        webView.setWebChromeClient(new WebChromeClient());
        webView.setWebViewClient(new WebViewClient());
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        try {
            if (urlWebview != null)
                webView.loadUrl(urlWebview);
            else
                webView.loadData(htmlText.replace("\r\n", "<br/>"), "text/html", "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }
}
