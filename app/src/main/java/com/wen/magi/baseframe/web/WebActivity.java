package com.wen.magi.baseframe.web;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import static com.wen.magi.baseframe.utils.UrlConstants.*;
import static com.wen.magi.baseframe.utils.Constants.*;

import com.wen.magi.baseframe.base.BaseActivity;
import com.wen.magi.baseframe.utils.LangUtils;

/**
 * Created by MVEN on 16/7/5.
 * <p/>
 * email: magiwen@126.com.
 */


public class WebActivity extends BaseActivity {
    private WebView mWebView;
    private String mUrl = defaultURL;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        if (intent != null) {
            mUrl = intent.getStringExtra(ACTIVITY_WEB_KEY_INTENT_URL);
        }
        mWebView = new WebView(this);
        setContentView(mWebView);
        initWebViews();
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void initWebViews() {
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setBuiltInZoomControls(false);
        mWebView.setWebViewClient(new MyWebViewClient());
        mWebView.setWebChromeClient(new MyWebChromeClient());
        if (LangUtils.isNotEmpty(mUrl))
            mWebView.loadUrl(mUrl);
    }

    private class MyWebChromeClient extends WebChromeClient {
        public void onProgressChanged(WebView view, int newProgress) {
        }
    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

    @Override
    public void onBackPressed() {
        if (mWebView.canGoBack()) {
            mWebView.goBack();
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void OnClickView(View v) {

    }
}
