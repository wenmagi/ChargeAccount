package com.wen.magi.baseframe.base.net;

import java.io.Serializable;

/**
 * Created by MVEN on 16/6/20.
 * <p/>
 * email: magiwen@126.com.
 */


public class BaseResultParams implements Serializable {
    /**
     * 后端返回的code
     */
    int code;
    /**
     * 后端返回的msg
     */
    String msg;
}
