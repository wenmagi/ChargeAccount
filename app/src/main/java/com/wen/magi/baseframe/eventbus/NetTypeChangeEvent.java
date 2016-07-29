package com.wen.magi.baseframe.eventbus;

import com.wen.magi.baseframe.managers.AppSessionManager;

/**
 * Created by MVEN on 16/7/25.
 * <p/>
 * email: magiwen@126.com.
 */


public class NetTypeChangeEvent {

    public AppSessionManager.NetWorkType netWorkType;

    public void setNetWorkType(AppSessionManager.NetWorkType netWorkType) {
        this.netWorkType = netWorkType;
    }

}
