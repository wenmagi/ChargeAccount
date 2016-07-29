package com.wen.magi.baseframe.views.calendar.week;


import com.wen.magi.baseframe.utils.date.DateTime;

import java.util.ArrayList;
import java.util.HashMap;


public class WeekViewCache {

    private static WeekViewCache mWeekViewCache = null;

    private HashMap<DateTime, ArrayList<DateTime>> mWeekDaysListCache = null;
    private static final int DAYS_LIST_COUNT_MAX = 20 * 7;

    private WeekViewCache() {

    }

    public static WeekViewCache getInstance() {
        if (mWeekViewCache == null)
            mWeekViewCache = new WeekViewCache();
        return mWeekViewCache;
    }


    public ArrayList<DateTime> getDateTimeList(DateTime beginOfWeek) {
        if (mWeekDaysListCache == null)
            return null;
        return mWeekDaysListCache.get(beginOfWeek);
    }

    public void putDateTimeList(DateTime beginOfWeek, ArrayList<DateTime> list) {
        if (mWeekDaysListCache == null)
            mWeekDaysListCache = new HashMap<>();
        if (mWeekDaysListCache.size() > DAYS_LIST_COUNT_MAX) {
            mWeekDaysListCache.clear();
        }
        mWeekDaysListCache.put(beginOfWeek, list);
    }

    public void clearCache() {
        if (mWeekDaysListCache != null) {
            mWeekDaysListCache.clear();
            mWeekDaysListCache = null;
        }

    }
}
