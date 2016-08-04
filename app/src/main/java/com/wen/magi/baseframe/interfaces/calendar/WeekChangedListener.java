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
     * Inform client user has clicked on a date
     *
     * @param date
     * @param view
     */
    public abstract void onSelectDate(Date date, View view);


    /**
     * Inform client user has long clicked on a date
     *
     * @param date
     * @param view
     */
    public void onLongClickDate(Date date, View view) {
        // Do nothing
    }


    /**
     * Inform client that calendar has changed month
     *
     * @param month
     * @param year
     */
    public void onChangeWeek(int day, int month, int year) {
        // Do nothing

    }



    /**
     * Inform client that KiwiWeekAndDayFragment view has been created and views are
     * no longer null. Useful for customization of button and text views
     */
    public void onKiwiWeekAndDayViewCreated() {
        // Do nothing
    }
}
