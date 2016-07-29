package com.wen.magi.baseframe.views.observablescrolled;

/**
 * Created by MVEN on 16/6/23.
 * <p/>
 * email: magiwen@126.com.
 */


public interface ObservableScrollCallback {

    void onScrollChanged(int scrollY);

    void onScrollStateChanged(ScrollState scrollState);
}
