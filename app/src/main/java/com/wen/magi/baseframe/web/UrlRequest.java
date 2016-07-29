package com.wen.magi.baseframe.web;

import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NoConnectionError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.wen.magi.baseframe.R;
import com.wen.magi.baseframe.managers.AppManager;
import com.wen.magi.baseframe.managers.AppSessionManager;
import com.wen.magi.baseframe.managers.RequestQueueManager;
import com.wen.magi.baseframe.utils.LangUtils;
import com.wen.magi.baseframe.utils.LogUtils;

import java.net.URL;
import java.util.HashMap;

import static com.wen.magi.baseframe.utils.WebUtils.compositeUrl;
import static com.wen.magi.baseframe.utils.WebUtils.createURL;
import static com.android.volley.Request.Method.*;
import static com.android.volley.Request.Priority.*;

/**
 * Created by MVEN on 16/6/19.
 * <p/>
 * email: magiwen@126.com.
 */

public class UrlRequest {
    public interface RequestDelegate {
        void requestFailed(UrlRequest request, int statusCode, String errorString);

        void requestFinished(UrlRequest request);
    }


    private static final int MY_SOCKET_TIMEOUT_MS = 45 * 1000;

    public static final int REQUEST_SUCCESS_CODE = 0;

    private static String mURL;
    protected RequestDelegate mDelegate;
    private HashMap<String, String> mPostParams;
    private int mMethod = GET;
    private String mStringData;


    public UrlRequest(String url) {
        autoCompileUrl(url, null);
    }

    public UrlRequest(String url, HashMap<String, Object> params) {
        autoCompileUrl(url, params);
    }

    private void autoCompileUrl(String url, HashMap<String, Object> params) {
        URL u = createURL(params == null ? url : compositeUrl(url, params));
        if (u != null)
            mURL = u.toString();
    }

    public void setDelegate(RequestDelegate delegate) {
        mDelegate = delegate;
    }


    private void fireDelegate(boolean result, int code, String errorString) {
        RequestDelegate d = mDelegate;
        if (!result) {
            LogUtils.w("requst failed, url = %s code = %s", mURL, code);
        }
        if (d != null) {
            if (result) {
                d.requestFinished(this);
            } else {
                d.requestFailed(this, code, errorString);
            }
        }
    }

    public void addPostParams(String key, String value) {
        if (mPostParams == null) {
            mPostParams = new HashMap<>();
        }
        mPostParams.put(key, String.valueOf(value));
    }

    public void start() {
        start(true);
    }

    public void start(boolean immediate) {
        if (mPostParams != null || mPostParams.size() > 0) {
            mMethod = POST;
        }

        StringRequestPriority requestPriority = new StringRequestPriority(mMethod, mURL, succListener, errorListener);
        if (mMethod == POST) {
            requestPriority.setPostParams(mPostParams);
        }

        requestPriority.setShouldCache(false);
        requestPriority.setPriority(immediate ? IMMEDIATE : NORMAL);
        requestPriority.setRetryPolicy(new DefaultRetryPolicy(MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueueManager.addRequest(requestPriority);
    }

    private Response.ErrorListener errorListener = new Response.ErrorListener() {

        @Override
        public void onErrorResponse(VolleyError error) {
            int code = error.networkResponse != null ? error.networkResponse.statusCode : -1;
            LogUtils.w("onErrorResponse %s", error);
            if (error instanceof NoConnectionError) {
                AppSessionManager.getSessionManager().setSessionMode(AppSessionManager.SessionMode.OffLine);
            }
            fireDelegate(false, code, null);
        }
    };

    private Response.Listener<String> succListener = new Response.Listener<String>() {

        @Override
        public void onResponse(String response) {
            String errorString = AppManager.getApplicationContext().getString(R.string.no_network_warn);
            JSONObject json = null;
            try {
                json = JSON.parseObject(response);
            } catch (Exception e) {

            }
            if (json != null) {
                int code = json.getIntValue("code");
                String errorMsg = json.getString("msg");
                mStringData = json.getString("data");
                if (LangUtils.isNotEmpty(errorMsg)) {
                    errorString = errorMsg;
                }
                if (code == REQUEST_SUCCESS_CODE) {
                    fireDelegate(true, REQUEST_SUCCESS_CODE, null);
                } else {
                    if (code == 102) {//LOGIN_REQUIRED = (102, '需要登录')
                        Toast.makeText(AppManager.getApplicationContext(), errorMsg, Toast.LENGTH_LONG).show();
                        AppManager.logout();
                    } else {
                        fireDelegate(false, code, errorString);
                    }
                }
            } else {
                if (LangUtils.isNotEmpty(response)) {
                    mStringData = response;
                    fireDelegate(true, REQUEST_SUCCESS_CODE, null);
                } else {
                    fireDelegate(false, -1, errorString);
                }
            }
        }
    };

    public String getStringData() {
        return mStringData;
    }
}
