package com.wen.magi.baseframe.base.net;

import com.wen.magi.baseframe.utils.LangUtils;
import com.wen.magi.baseframe.utils.StringUtils;
import com.wen.magi.baseframe.web.UrlRequest;

import static com.wen.magi.baseframe.base.net.BaseRequestParams.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by MVEN on 16/6/20.
 * <p/>
 * email: magiwen@126.com.
 */


public class ARequest {

    public static void start(UrlRequest.RequestDelegate delegate, EService service, BaseRequestParams params) {

        if (delegate == null || service == null)
            return;

        String url = service.getUrl();
        if (LangUtils.isEmpty(url))
            return;

        UrlRequest r = initUrlRequest(service, params, url);
        r.setDelegate(delegate);
        r.start();
    }

    private static UrlRequest initUrlRequest(EService service, BaseRequestParams params, String url) {
        if (params == null)
            return new UrlRequest(url);

        HashMap<String, Object> param = params.getRequestParams();

        if (param != null && param.containsKey(URL_PARAMS)) {
            Object[] args = (Object[]) param.get(URL_PARAMS);
            if (args != null)
                url = StringUtils.format(url, args);
            param.remove(URL_PARAMS);
        }
        UrlRequest r;
        if (service.getUrlType() == EService.METHOD_OF_GET) {
            r = new UrlRequest(url, param);
        } else {
            r = new UrlRequest(url);
            for (Map.Entry<String, Object> entry : param.entrySet()) {
                r.addPostParams(entry.getKey(), (String) entry.getValue());
            }
        }
        return r;
    }
}
