package com.wen.magi.baseframe.base.net;

import java.io.Serializable;

import static com.android.volley.Request.Method.GET;
import static com.android.volley.Request.Method.POST;
/**
 * Created by MVEN on 16/6/20.
 * <p/>
 * email: magiwen@126.com.
 */

/**
 * 后端
 */
public enum EService implements Serializable {

    GymList("client/gym/list", BaseResultParams.class);

    static final int METHOD_OF_GET = GET;
    static final int METHOD_OF_POST = POST;

    private String mUrl;
    private int mUrlType;
    private Class<? extends BaseResultParams> mClazz;
    private int mMethod;


    EService(String url, Class<? extends BaseResultParams> clazz) {
        this(url, clazz, GET);
    }

    EService(String url, Class<? extends BaseResultParams> clazz, int method) {
        mUrl = url;
        mClazz = clazz;
        mMethod = method;
    }

    String getUrl() {
        return mUrl;
    }

    int getUrlType() {
        return mUrlType;
    }

    Class<? extends BaseResultParams> getClazz() {
        return mClazz;
    }
}
