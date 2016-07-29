package com.wen.magi.baseframe.base.net;

import com.wen.magi.baseframe.utils.LangUtils;
import com.wen.magi.baseframe.web.UrlRequest;

/**
 * Created by MVEN on 16/6/20.
 * <p/>
 * email: magiwen@126.com.
 */


public class AResponse {

    public BaseResultParams parseResultParams(UrlRequest request, int statusCode) {
        BaseResultParams result = null;
        if (request == null || LangUtils.isEmpty(request.getStringData()))
            return null;
        return null;
    }
}
