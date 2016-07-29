package com.wen.magi.baseframe.utils;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.Random;
import java.util.TimeZone;

/**
 * Created by MVEN on 16/4/28.
 */
public class LangUtils {

    private static BytePool BYTES_POOL = new BytePool();

    private LangUtils() {
    }

    /**
     * check params if is empty or not
     *
     * @param s
     * @return
     */
    public static boolean isEmpty(CharSequence s) {
        return s == null || s.length() == 0;
    }

    public static boolean isNotEmpty(CharSequence s) {
        return s != null && s.length() > 0;
    }

    public static boolean isNotEmpty(byte[] s) {
        return s != null && s.length > 0;
    }

    public static boolean isEmpty(byte[] s) {
        return s == null || s.length == 0;
    }

    public static <E> boolean isEmpty(Collection<E> list) {
        return list == null || list.size() == 0;
    }

    public static <E> boolean isNotEmpty(Collection<E> list) {
        return list != null && list.size() > 0;
    }

    /**
     * get earlier date
     *
     * @param d1
     * @param d2
     * @return
     */
    public static Date getEarlierDate(Date d1, Date d2) {
        if (d1 == null || d2 == null) {
            if (d1 != null)
                return d1;
            if (d2 != null)
                return d2;
            return null;
        }
        return d1.before(d2) ? d1 : d2;
    }

    /**
     * get later date
     *
     * @param d1 date1
     * @param d2 date2
     * @return
     */
    public static Date getLaterDate(Date d1, Date d2) {
        if (d1 == null || d2 == null) {
            if (d1 != null)
                return d1;
            if (d2 != null)
                return d2;
            return null;
        }
        return d1.after(d2) ? d1 : d2;
    }

    /**
     * get day of month 1-|28|29|30|31
     *
     * @param date
     * @return int of date's day
     */
    public static int getMonthDay(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * get day of week 1-|28|29|30|31
     *
     * @param date
     * @return
     */
    public static int getWeekDay(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.DAY_OF_WEEK);
    }

    /**
     * get a new date by older adding day
     *
     * @param date
     * @param day
     * @return
     */
    public static Date getDateAddingDay(Date date, int day) {
        return new Date(date.getTime() + day * 86400000l);
    }

    /**
     * get years between d1 and d2
     *
     * @param d1
     * @param d2
     * @return
     */
    public static int getYearsBetweenDates(Date d1, Date d2) {
        if (d1 == null || d2 == null)
            return Integer.MAX_VALUE;
        Calendar c = Calendar.getInstance();
        c.setTime(d1);
        int y1 = c.get(Calendar.YEAR);
        c.setTime(d2);
        int y2 = c.get(Calendar.YEAR);
        return y1 - y2;
    }

    public static int getDays2Today(Date d1) {
        return getYearsBetweenDates(d1, new Date());

    }

    /**
     * @param date
     * @param day
     * @return
     */
    public static Date dateByAddingTimeDay(Date date, long day) {
        return new Date(date.getTime() + day * 86400000l);
    }

    public static Date cc_dateByMovingToBeginningOfDay(Date d1) {
        return cc_dateByMovingToBeginningOfDay(d1, TimeZone.getDefault());
    }

    public static Date cc_dateByMovingToBeginningOfDay(Date date, TimeZone tz) {
        if (date == null) {
            return null;
        }
        Calendar calendar = Calendar.getInstance(tz);
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    /**
     * @param date
     * @return
     */
    public static Date cc_dateByMovingToEndOfDay(Date date) {
        if (date == null) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTime();
    }

    /**
     * @param date
     * @return
     */
    public static Date cc_dateByMovingToFirstDayOfTheFollowingWeek(Date date) {
        if (date == null) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.WEEK_OF_YEAR, 1);
        calendar.set(Calendar.DAY_OF_WEEK, 1);
        return calendar.getTime();
    }

    /**
     * @param date
     * @return
     */
    public static Date cc_dateByMovingToFirstDayOfTheFollowingMonth(Date date) {
        if (date == null) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, 1);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        return calendar.getTime();
    }

    /**
     * @param date
     * @return
     */
    public static Date cc_dateByMovingToSameDayOfTheFollowingNumMonth(
            Date date, int num) {
        if (date == null) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, num);
        return calendar.getTime();
    }

    /**
     * @param date
     * @return
     */
    public static Date cc_dateByMovingToFirstDayOfTheFollowingYear(Date date) {
        if (date == null) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.YEAR, 1);
        calendar.set(Calendar.DAY_OF_YEAR, 1);
        return calendar.getTime();
    }

    /**
     * @param date
     * @return
     */
    public static Date cc_dateByMovingToFirstDayOfWeek(Date date) {
        if (date == null) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_WEEK, 1);
        return calendar.getTime();
    }

    public static Date cc_dateByMovingToLastDay(Date date) {
        if (date == null) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_YEAR, -1);
        return calendar.getTime();
    }

    /**
     * @param date
     * @return
     */
    public static Date cc_dateByMovingToFirstDayOfTheCurrentWeek(Date date) {
        if (date == null) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_WEEK, 1);
        return calendar.getTime();
    }

    /**
     * @param date
     * @return
     */
    public static Date cc_dateByMovingToFirstDayOfTheCurrentMonth(Date date) {
        if (date == null) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        return calendar.getTime();
    }

    /**
     * @param date
     * @return
     */
    public static Date cc_dateByMovingToFirstDayOfTheCurrentYear(Date date) {
        if (date == null) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        // calendar.add(Calendar.YEAR, -1);
        calendar.set(Calendar.DAY_OF_YEAR, 1);
        return calendar.getTime();
    }

    /**
     * get days between d1 and d2
     *
     * @param d1
     * @param d2
     * @return
     */
    public static int getDaysBetweenDates(Date d1, Date d2) {
        if (d1 == null || d2 == null)
            return Integer.MAX_VALUE;
        if (Math.abs(d1.getTime() - d2.getTime()) > 150l * 3600 * 24 * 365
                * 1000) {
            return d1.before(d2) ? -150 * 365 : 150 * 365;
        }
        int off = (int) ((cc_dateByMovingToBeginningOfDay(d1).getTime() - cc_dateByMovingToBeginningOfDay(
                d2).getTime()) / (3600 * 24 * 1000l));
        return off;
    }

    public static boolean isSameDay(Date date1, Date date2) {
        if (date1 == null || date2 == null) {
            return false;
        }
        Calendar calendar1 = Calendar.getInstance(), calendar2 = Calendar
                .getInstance();
        calendar1.setTime(date1);
        calendar2.setTime(date2);
        if (calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR)
                && calendar1.get(Calendar.MONTH) == calendar2
                .get(Calendar.MONTH)
                && calendar1.get(Calendar.DAY_OF_MONTH) == calendar2
                .get(Calendar.DAY_OF_MONTH))
            return true;
        return false;
    }

    public static boolean isSameMonth(Date date1, Date date2) {
        if (date1 == null || date2 == null) {
            return false;
        }
        Calendar calendar1 = Calendar.getInstance(), calendar2 = Calendar
                .getInstance();
        calendar1.setTime(date1);
        calendar2.setTime(date2);
        if (calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR)
                && calendar1.get(Calendar.MONTH) == calendar2
                .get(Calendar.MONTH))
            return true;
        return false;
    }

    public static boolean isSameYear(Date date1, Date date2) {
        if (date1 == null || date2 == null) {
            return false;
        }
        Calendar calendar1 = Calendar.getInstance(), calendar2 = Calendar
                .getInstance();
        calendar1.setTime(date1);
        calendar2.setTime(date2);
        if (calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR))
            return true;
        return false;
    }

    /**
     * @param date1
     * @param date2
     * @return dates between date1 and date2
     */
    public static int daysBetweenDate2(Date date1, Date date2) {
        Date d1 = cc_dateByMovingToBeginningOfDay(date1);
        Date d2 = cc_dateByMovingToBeginningOfDay(date2);
        int o = (int) ((d1.getTime() - d2.getTime()) / (86400000));
        return o;
    }

    public static long monthsBetweenDate(Date date1, Date date2) {
        Calendar calDate1 = Calendar.getInstance(), calDate2 = Calendar
                .getInstance();
        calDate1.setTime(date1);
        calDate2.setTime(date2);
        int year1 = calDate1.get(Calendar.YEAR);
        int month1 = calDate1.get(Calendar.MONTH);
        int year2 = calDate2.get(Calendar.YEAR);
        int month2 = calDate2.get(Calendar.MONTH);
        return (year1 - year2) * 12l + month1 - month2;
    }

    /**
     * Parse int from a String object.
     *
     * @param text
     * @return int
     */
    public static int parseInt(String text) {
        return parseInt(text, -1);
    }

    /**
     * Parse int from a String object based on a default value.
     *
     * @param text
     * @param defaultValue
     * @return
     */
    public static int parseInt(String text, int defaultValue) {
        if (text == null)
            return defaultValue;
        try {
            return Integer.parseInt(text);
        } catch (Exception e) {
            LogUtils.w(e, "failed to parse int [%s], using default value [%s]",
                    text, defaultValue);
            return defaultValue;
        }
    }

    public static long parseLong(Object text, long defaultValue) {
        if (text == null)
            return defaultValue;
        try {
            return Long.parseLong(String.valueOf(text));
        } catch (Exception e) {
            LogUtils.w("failed to parse long [%s], using default value [%s]",
                    text, defaultValue);
            return defaultValue;
        }
    }

    public static String parseString(Object o) {
        return parseString(o, "");
    }

    public static String parseString(Object o, String defaultValue) {
        String ret = String.valueOf(o);
        if (ret == null || "null".equalsIgnoreCase(ret)) {
            ret = defaultValue;
            // LogUtils.d("parseString ret = null o = %s",o);
        }
        return ret;
    }

    /**
     * @param list
     * @return the first obj or null
     */
    public static <E> E getFirstObj(ArrayList<E> list) {
        return isNotEmpty(list) ? list.get(0) : null;
    }

    /**
     * @param list
     * @return the last obj or null
     */
    public static <E> E getLastObj(ArrayList<E> list) {
        return isNotEmpty(list) ? list.get(list.size() - 1) : null;
    }

    /**
     * Parse byte[] from a String object.
     *
     * @param s
     * @return byte[]
     */
    @NonNull
    public static byte[] getBytes(String s) {
        try {
            if (s != null)
                return s.getBytes("UTF-8");
        } catch (Exception e) {
            LogUtils.w(e, "Failed to getBytes: " + s);
        }

        return new byte[0];
    }


    /**
     * Acquire byte[] data from a linked list.
     *
     * @param minCapacity
     * @return
     */
    @NonNull
    public static byte[] acquireBytes(int minCapacity) {
        return BYTES_POOL.acquire(minCapacity);
    }

    /**
     * Release byte[] data at the last of a Linked list.
     *
     * @param buf
     */
    public static void releaseBytes(@NonNull byte[] buf) {
        BYTES_POOL.release(buf);
    }

    public static int max(int value1, int value2) {
        return value1 > value2 ? value1 : value2;
    }
}
