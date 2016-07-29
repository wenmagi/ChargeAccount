package com.wen.magi.baseframe.views.calendar.month;

import com.wen.magi.baseframe.utils.date.DateTime;

import java.util.ArrayList;
import java.util.HashMap;


public class MonthViewCache {

    private static MonthViewCache mMonthViewCache = null;

    private HashMap<DateTime, ArrayList<DateTime>> mMonthDaysListCache = null;
    private static final int DAYS_LIST_COUNT_MAX = 20;

    private MonthViewCache() {

    }

    public static MonthViewCache getInstance() {
        if (mMonthViewCache == null)
            mMonthViewCache = new MonthViewCache();
        return mMonthViewCache;
    }

    public ArrayList<DateTime> getDateTimeList(DateTime beginOfMonth) {
        if (mMonthDaysListCache == null)
            return null;
        return mMonthDaysListCache.get(beginOfMonth);
    }

    public void putDateTimeList(DateTime beginOfMonth, ArrayList<DateTime> list) {
        if (mMonthDaysListCache == null)
            mMonthDaysListCache = new HashMap<>();
        if (mMonthDaysListCache.size() > DAYS_LIST_COUNT_MAX) {
            mMonthDaysListCache.clear();
        }
        mMonthDaysListCache.put(beginOfMonth, list);
    }

    public void clearCache() {
        if (mMonthDaysListCache != null) {
            mMonthDaysListCache.clear();
            mMonthDaysListCache = null;
        }

    }
}
