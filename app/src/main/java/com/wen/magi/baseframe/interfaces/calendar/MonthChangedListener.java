package com.wen.magi.baseframe.interfaces.calendar;

import android.view.View;

import java.util.Date;

/**
 * Created by MVEN on 16/8/4.
 * <p/>
 * email: magiwen@126.com.
 */


public abstract class MonthChangedListener {

    /**
     * 选择日期的回调
     *
     * @param date
     * @param view
     */
    public abstract void onSelectDate(Date date, View view);


    /**
     * 长按日期的回调
     *
     * @param date
     * @param view
     */
    public void onLongClickDate(Date date, View view) {
        // Do nothing
    }


    /**
     * 月份改变的回调
     *
     * @param month
     * @param year
     */
    public void onChangeMonth(int day, int month, int year) {
        // Do nothing
    }


    /**
     * View创建完毕的回调
     */
    public void onCaldroidViewCreated() {
        // Do nothing
    }
}
