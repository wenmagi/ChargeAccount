package com.wen.magi.baseframe.eventbus;

/**
 * Created by MVEN on 16/7/25.
 * <p/>
 * email: magiwen@126.com.
 */


public class NetConnChangeEvent {

    public boolean isOnline = false;

    public void setOnline(boolean isOnline) {
        this.isOnline = isOnline;
    }

}
