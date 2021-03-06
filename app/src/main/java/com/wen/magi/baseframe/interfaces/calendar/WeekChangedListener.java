package com.wen.magi.baseframe.interfaces.calendar;

import android.view.View;

import java.util.Date;

/**
 * Created by MVEN on 16/7/29.
 * <p/>
 * email: magiwen@126.com.
 */

public abstract class WeekChangedListener {
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
     * 周改变的回调
     *
     * @param month
     * @param year
     */
    public void onChangeWeek(int day, int month, int year) {
        // Do nothing

    }

    /**
     * 周视图创建完毕的回调
     */
    public void onKiwiWeekAndDayViewCreated() {
        // Do nothing
    }
}
