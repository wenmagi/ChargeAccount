package com.wen.magi.baseframe.base;

import android.util.Log;

import com.wen.magi.baseframe.utils.LogUtils;

import java.net.MalformedURLException;
import java.net.URL;

public class AppConfig {
    public static final boolean DEV_BUILD = true;
    public static final int MIN_LOG_LEVEL = DEV_BUILD ? Log.DEBUG : Log.INFO;
    public static final boolean LOG_LINE_NUMBER = false;
    public static final String DEFAULT_URL;
    public static URL DEFAULT_WEB_URL;

    static {
        //区分测试服，正式服URL
        if (DEV_BUILD) {
            DEFAULT_URL = "";
        } else {
            DEFAULT_URL = "";
        }
        try {
            DEFAULT_WEB_URL = new URL(DEFAULT_URL);
        } catch (MalformedURLException e) {
            DEFAULT_WEB_URL = null;
            LogUtils.e("parse Default URL error %s", e);
        }
    }

}
