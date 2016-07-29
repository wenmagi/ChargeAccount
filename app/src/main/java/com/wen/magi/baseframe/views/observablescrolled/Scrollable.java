package com.wen.magi.baseframe.views.observablescrolled;

/**
 * Created by MVEN on 16/6/23.
 * <p/>
 * email: magiwen@126.com.
 */


public interface Scrollable {

    int getCurrentScrollY();

    void setScrollCallback(ObservableScrollCallback callback);

}
