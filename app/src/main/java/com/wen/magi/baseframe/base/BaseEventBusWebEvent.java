package com.wen.magi.baseframe.base;

/**
 * Created by MVEN on 16/7/21.
 * <p/>
 * email: magiwen@126.com.
 * <p/>
 * 针对一次网络访问事件，结束后发起的通知事件
 */


public class BaseEventBusWebEvent {
    public boolean bResult;

    public int nFailedType;

    /*断网状态*/
    public static final int FAILED_TYPE_NO_NETWORK = 1;

    /*拉取失败原因未知*/
    public static final int FAILED_TYPE_UNKOWN = 2;

    /*返回code值不匹配*/
    public static final int FAILED_TYPE_RESPONSE_CODE_WRONG = 3;// code != 0: wrong

    /*解析失败*/
    public static final int FAILED_TYPE_RESPONSE_DATA_FORMAT_WRONG = 4;

    public BaseEventBusWebEvent(boolean bResult) {
        this.bResult = bResult;
    }
}
