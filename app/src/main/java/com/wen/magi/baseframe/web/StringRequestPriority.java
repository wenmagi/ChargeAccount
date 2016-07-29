package com.wen.magi.baseframe.web;

import com.alibaba.fastjson.JSONObject;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.wen.magi.baseframe.managers.RequestQueueManager;
import com.wen.magi.baseframe.utils.LangUtils;
import com.wen.magi.baseframe.utils.StringUtils;
import com.wen.magi.baseframe.utils.SysUtils;
import com.wen.magi.baseframe.utils.WebUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by MVEN on 16/6/19.
 * <p/>
 * email: magiwen@126.com.
 */


public class StringRequestPriority extends StringRequest {
    private Request.Priority mPriority = Request.Priority.NORMAL;// HIGH;
    private HashMap<String, String> postParams;
    private byte[] postBody;


    /**
     * Creates a new request with the given method.
     *
     * @param method        the request {@link Method} to use
     * @param url           URL to fetch the string at
     * @param listener      Listener to receive the String response
     * @param errorListener Error listener, or null to ignore errors
     */
    public StringRequestPriority(int method, String url, Response.Listener<String> listener,
                                 Response.ErrorListener errorListener) {
        super(method, url, listener, errorListener);
    }

    /**
     * Creates a new GET request.
     *
     * @param url           URL to fetch the string at
     * @param listener      Listener to receive the String response
     * @param errorListener Error listener, or null to ignore errors
     */
    public StringRequestPriority(String url, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.GET, url, listener, errorListener);
    }

    @Override
    public Priority getPriority() {
        return mPriority;
    }

    public void setPriority(Priority priority) {
        mPriority = priority;
    }

    public void setPostParams(HashMap<String, String> postParams) {
        this.postParams = postParams;
    }


    public void setPostBody(byte[] postData) {
        this.postBody = postData;
    }

    public byte[] getBody() throws AuthFailureError {
        if (postBody == null || postBody.length <= 0) {
            return super.getBody();
        } else {
            return postBody;
        }
    }


    /*
     * (non-Javadoc)
     *
     * @see
     * com.android.volley.toolbox.StringRequest#parseNetworkResponse(com.android.volley.NetworkResponse
     * )
     */
    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        // since we don't know which of the two underlying network vehicles
        // will Volley use, we have to handle and store session cookies manually
        if (getUrl().contains("client/login") && response.statusCode == 200) {
            String content = StringUtils.utf8String(response.data, "");
            if (LangUtils.isNotEmpty(content)) {
                JSONObject j = WebUtils.parseJsonObject(content);
                int code = WebUtils.getJsonInt(j, "code", -1);
                if (code == 0) {
                    RequestQueueManager.checkSessionCookie(response.headers);
                }
            }

        }
        return super.parseNetworkResponse(response);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.android.volley.Request#getHeaders()
     */
    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> headers = super.getHeaders();

        if (headers == null || headers.equals(Collections.emptyMap())) {
            headers = new HashMap<>();
        }

        RequestQueueManager.addSessionCookie(headers);

        StringBuilder sbUserAgent = new StringBuilder();
        sbUserAgent.append(System.getProperty("http.agent"));

        if (LangUtils.isNotEmpty(SysUtils.APP_VERSION_NAME)) {
            sbUserAgent.append(" card/");
            sbUserAgent.append(SysUtils.APP_VERSION_NAME);
        }

        if (LangUtils.isNotEmpty(SysUtils.mUniquePsuedoID)) {
            sbUserAgent.append(" DeviceID/");
            sbUserAgent.append(SysUtils.mUniquePsuedoID);
        }
        headers.put("User-Agent", sbUserAgent.toString());

        return headers;
    }

    protected Map<String, String> getParams() throws AuthFailureError {
        return postParams;
    }

}
