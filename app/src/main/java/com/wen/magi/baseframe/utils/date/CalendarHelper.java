package com.wen.magi.baseframe.utils.date;

import android.graphics.Paint;
import android.graphics.Paint.Style;

import com.wen.magi.baseframe.R;
import com.wen.magi.baseframe.managers.AppManager;
import com.wen.magi.baseframe.utils.LangUtils;
import com.wen.magi.baseframe.utils.LogUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Convenient helper to work with date, JODA DateTime and String
 *
 * @author thomasdao
 */
public class CalendarHelper {

    public static SimpleDateFormat yyyyMMddFormat = new SimpleDateFormat(
            "yyyy-MM-dd", Locale.ENGLISH);

    public static SimpleDateFormat MMMFormat = new SimpleDateFormat("MMM",
            Locale.getDefault());

    private static DateTime selectedDay = CalendarHelper
            .convertDateToDateTime(new Date());

    public static DateTime getSelectedDay() {
        return selectedDay;
    }

    public static void setSelectedDay(Date selectedDay) {
        CalendarHelper.selectedDay = convertDateToDateTime(selectedDay);
    }

    public static void setSelectedDay(DateTime selectedDay) {
        CalendarHelper.selectedDay = selectedDay;
    }

    /**
     * Retrieve all the dates for a given calendar month Include previous month,
     * current month and next month.
     *
     * @param startDayOfWeek : calendar can start from customized date instead of Sunday
     */

    public static ArrayList<DateTime> getFullWeeks(int month, int year,
                                                   int startDayOfWeek, boolean sixWeeksInCalendar) {
        ArrayList<DateTime> datetimeList = new ArrayList<DateTime>();

        DateTime firstDateOfMonth = new DateTime(year, month, 1, 0, 0, 0, 0);
        DateTime lastDateOfMonth = firstDateOfMonth.plusDays(firstDateOfMonth
                .getNumDaysInMonth() - 1);

        // Add dates of first week from previous month
        int weekdayOfFirstDate = firstDateOfMonth.getWeekDay();

        // If weekdayOfFirstDate smaller than startDayOfWeek
        // For e.g: weekdayFirstDate is Monday, startDayOfWeek is Tuesday
        // increase the weekday of FirstDate because it's in the future
        if (weekdayOfFirstDate < startDayOfWeek) {
            weekdayOfFirstDate += 7;
        }

        while (weekdayOfFirstDate > 0) {
            DateTime dateTime = firstDateOfMonth.minusDays(weekdayOfFirstDate
                    - startDayOfWeek);
            if (!dateTime.lt(firstDateOfMonth)) {
                break;
            }

            datetimeList.add(dateTime);
            weekdayOfFirstDate--;
        }

        // Add dates of current month
        for (int i = 0; i < lastDateOfMonth.getDay(); i++) {
            datetimeList.add(firstDateOfMonth.plusDays(i));
        }

        // Add dates of last week from next month
        int endDayOfWeek = startDayOfWeek - 1;

        if (endDayOfWeek == 0) {
            endDayOfWeek = 7;
        }

        if (lastDateOfMonth.getWeekDay() != endDayOfWeek) {
            int i = 1;
            while (true) {
                DateTime nextDay = lastDateOfMonth.plusDays(i);
                datetimeList.add(nextDay);
                i++;
                if (nextDay.getWeekDay() == endDayOfWeek) {
                    break;
                }
            }
        }

        // Add more weeks to fill remaining rows
        if (sixWeeksInCalendar) {
            int size = datetimeList.size();
            int row = size / 7;
            int numOfDays = (6 - row) * 7;
            DateTime lastDateTime = datetimeList.get(size - 1);
            for (int i = 1; i <= numOfDays; i++) {
                DateTime nextDateTime = lastDateTime.plusDays(i);
                datetimeList.add(nextDateTime);
            }
        }

        return datetimeList;
    }

    public static ArrayList<DateTime> getThisWeek(int day, int month, int year,
                                                  int startDayOfWeek) {
        ArrayList<DateTime> datetimeList = new ArrayList<DateTime>();

        DateTime currentDate = new DateTime(year, month, day, 0, 0, 0, 0);
        int weekdayOfCurrentDate = currentDate.getWeekDay();

        if (weekdayOfCurrentDate < startDayOfWeek)
            weekdayOfCurrentDate += 7;

        int afterDaysNumber = 6 - (weekdayOfCurrentDate - startDayOfWeek);
        while (weekdayOfCurrentDate > 0) {
            DateTime dt = currentDate.minusDays(weekdayOfCurrentDate
                    - startDayOfWeek);
            if (!dt.lt(currentDate))
                break;
            datetimeList.add(dt);
            weekdayOfCurrentDate--;
        }

        for (int i = 0; i <= afterDaysNumber; i++) {
            datetimeList.add(currentDate.plusDays(i));
        }

        return datetimeList;
    }

    public static ArrayList<DateTime> getThisWeek(DateTime currentDate,
                                                  int startDayOfWeek) {
        ArrayList<DateTime> datetimeList = new ArrayList<DateTime>();

        int weekdayOfCurrentDate = currentDate.getWeekDay();

        if (weekdayOfCurrentDate < startDayOfWeek)
            weekdayOfCurrentDate += 7;

        int afterDaysNumber = 6 - (weekdayOfCurrentDate - startDayOfWeek);
        while (weekdayOfCurrentDate > 0) {
            DateTime dt = currentDate.minusDays(weekdayOfCurrentDate
                    - startDayOfWeek);
            if (!dt.lt(currentDate))
                break;
            datetimeList.add(dt);
            weekdayOfCurrentDate--;
        }

        for (int i = 0; i <= afterDaysNumber; i++) {
            datetimeList.add(currentDate.plusDays(i));
        }

        return datetimeList;
    }

    public static DateTime getThanksgivingDay(int year, int month) {
        if (month != 11) {
            return null;
        }
        DateTime thanksgivingDay = null;
        DateTime currentDate = new DateTime(year, month, 1, 0, 0, 0, 0);
        int weekdayOfCurrentDate = currentDate.getWeekDay();

        if (weekdayOfCurrentDate <= Calendar.THURSDAY)
            weekdayOfCurrentDate += 7;
        DateTime firstThursdayOfThisMonth = currentDate
                .minusDays(weekdayOfCurrentDate - Calendar.THURSDAY);
        thanksgivingDay = firstThursdayOfThisMonth.plusDays(28);
        return thanksgivingDay;
    }

    public static DateTime getFatherDay(int year, int month) {
        if (month != 6) {
            return null;
        }
        DateTime motherDay = null;
        DateTime currentDate = new DateTime(year, month, 1, 0, 0, 0, 0);
        int weekdayOfCurrentDate = currentDate.getWeekDay();

        if (weekdayOfCurrentDate <= Calendar.SUNDAY)
            weekdayOfCurrentDate += 7;
        DateTime firstSundayOfThisMonth = currentDate
                .minusDays(weekdayOfCurrentDate - Calendar.SUNDAY);
        motherDay = firstSundayOfThisMonth.plusDays(21);
        return motherDay;
    }

    public static DateTime getMotherDay(int year, int month) {
        if (month != 5) {
            return null;
        }
        DateTime motherDay = null;
        DateTime currentDate = new DateTime(year, month, 1, 0, 0, 0, 0);
        int weekdayOfCurrentDate = currentDate.getWeekDay();

        if (weekdayOfCurrentDate <= Calendar.SUNDAY)
            weekdayOfCurrentDate += 7;
        DateTime firstSundayOfThisMonth = currentDate
                .minusDays(weekdayOfCurrentDate - Calendar.SUNDAY);
        motherDay = firstSundayOfThisMonth.plusDays(14);
        return motherDay;
    }

    public static DateTime getFirstDateOfThisWeek(DateTime currentDateTime,
                                                  int startDayOfWeek) {
        int weekdayOfCurrentDate = currentDateTime.getWeekDay();

        if (weekdayOfCurrentDate < startDayOfWeek)
            weekdayOfCurrentDate += 7;
        DateTime dt = currentDateTime.minusDays(weekdayOfCurrentDate
                - startDayOfWeek);
        return dt;
    }

    // date is in thisWeek, or not?
    public static boolean isInThisWeek(Date date, DateTime thisWeek,
                                       int startDayOfWeek) {
        boolean result = false;
        try {
            DateTime firstDayOfThisWeek = CalendarHelper
                    .getFirstDateOfThisWeek(thisWeek, startDayOfWeek);
            Calendar firstCalendarOfThisWeek = Calendar.getInstance();
            firstCalendarOfThisWeek.set(firstDayOfThisWeek.getYear(),
                    firstDayOfThisWeek.getMonth() - 1,
                    firstDayOfThisWeek.getDay(), 0, 0, 0);
            firstCalendarOfThisWeek.set(Calendar.MILLISECOND, 0);

            DateTime firstDayOfNextWeek = firstDayOfThisWeek.plusDays(7);
            Calendar firstCalendarOfNextWeek = Calendar.getInstance();
            firstCalendarOfNextWeek.set(firstDayOfNextWeek.getYear(),
                    firstDayOfNextWeek.getMonth() - 1,
                    firstDayOfNextWeek.getDay(), 0, 0, 0);
            firstCalendarOfNextWeek.set(Calendar.MILLISECOND, 0);

            if (date.before(firstCalendarOfNextWeek.getTime())
                    && !date.before(firstCalendarOfThisWeek.getTime())) {
                result = true;
            }
        } catch (Exception e) {
            return result;
        }
        return result;
    }

    public static boolean isInThisWeek(DateTime date, DateTime thisWeek,
                                       int startDayOfWeek) {
        return isInThisWeek(convertDateTimeToDate(date), thisWeek,
                startDayOfWeek);
    }

    public static int weeksBetweenDate(Date date1, Date date2,
                                       int startDayOfWeek) {
        if (date1 == null || date2 == null) {
            LogUtils.w(" date is invalid date1 = %s date2 = %s", date1, date2);
            return Integer.MAX_VALUE;
        }

        date1 = convertDateTimeToDate(getFirstDateOfThisWeek(
                convertDateToDateTime(date1), startDayOfWeek));
        date2 = convertDateTimeToDate(getFirstDateOfThisWeek(
                convertDateToDateTime(date2), startDayOfWeek));

        int off = (int) ((LangUtils.cc_dateByMovingToBeginningOfDay(date1)
                .getTime() - LangUtils.cc_dateByMovingToBeginningOfDay(date2)
                .getTime()) / (7 * 3600 * 24 * 1000l));
        return off;
    }

    public static int getRowIndexOfThisWeek(DateTime currentDateTime,
                                            int startDayOfWeek) {
        int year = currentDateTime.getYear();
        int month = currentDateTime.getMonth();
        DateTime firstDateOfMonth = new DateTime(year, month, 1, 0, 0, 0, 0);
        DateTime firstDateOfTheFirstWeekInThisMonth = getFirstDateOfThisWeek(
                firstDateOfMonth, startDayOfWeek);

        int index = 0;
        if (currentDateTime.lt(firstDateOfTheFirstWeekInThisMonth.plusDays(7)))
            index = 0;
        else if (currentDateTime.lt(firstDateOfTheFirstWeekInThisMonth
                .plusDays(14)))
            index = 1;
        else if (currentDateTime.lt(firstDateOfTheFirstWeekInThisMonth
                .plusDays(21)))
            index = 2;
        else if (currentDateTime.lt(firstDateOfTheFirstWeekInThisMonth
                .plusDays(28)))
            index = 3;
        else if (currentDateTime.lt(firstDateOfTheFirstWeekInThisMonth
                .plusDays(35)))
            index = 4;
        else
            index = 5;

        return index;
    }

    public static DateTime convertDateToDateTime(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.setTime(date);

        int year = calendar.get(Calendar.YEAR);
        int javaMonth = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DATE);

        return new DateTime(year, javaMonth + 1, day, 0, 0, 0, 0);
    }

    public static Date convertDateTimeToDate(DateTime dateTime) {
        int year = dateTime.getYear();
        int datetimeMonth = dateTime.getMonth();
        int day = dateTime.getDay();

        Calendar calendar = Calendar.getInstance();
        calendar.clear();

        calendar.set(year, datetimeMonth - 1, day);

        return calendar.getTime();
    }

    public static Date getDateFromString(String dateString, String dateFormat)
            throws ParseException {
        SimpleDateFormat formatter;
        if (dateFormat == null) {
            formatter = yyyyMMddFormat;
        } else {
            formatter = new SimpleDateFormat(dateFormat, Locale.ENGLISH);
        }

        return formatter.parse(dateString);
    }

    public static DateTime getDateTimeFromString(String dateString,
                                                 String dateFormat) {
        Date date;
        try {
            date = getDateFromString(dateString, dateFormat);
            return convertDateToDateTime(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ArrayList<String> convertToStringList(
            ArrayList<DateTime> dateTimes) {
        ArrayList<String> list = new ArrayList<String>();
        for (DateTime dateTime : dateTimes) {
            list.add(dateTime.format("YYYY-MM-DD"));
        }
        return list;
    }

    // for Calendar Cell View
    private static Paint contentPaintBlack = null;
    private static Paint contentPaintGreen = null;
    private static Paint contentPaintWhite = null;
    private static Paint contentPaintGray = null;
    private static Paint tipPaintGray = null;
    private static Paint tipPaintGreen = null;
    private static Paint tipPaintWhite = null;
    private static Paint tipLunarHolidayRed = null;
    private static Paint tipLunarHolidayWhite = null;
    private static Paint tipPaintLunarNormal = null;
    private static Paint todayCirclePaint = null;
    private static Paint selectedCirclePaint = null;
    private static Paint hasEventsCirclePaintWhite = null;
    private static Paint hasEventsCirclePaintGreen = null;

    public static Paint getContentPaintBlack() {
        if (contentPaintBlack == null) {
            contentPaintBlack = new Paint();
            contentPaintBlack.setAntiAlias(true);
            contentPaintBlack.setShader(null);
            contentPaintBlack.setFakeBoldText(false);
            contentPaintBlack.setTextSize(AppManager.getApplicationContext()
                    .getResources().getDimension(R.dimen.calendar_text_size));
            contentPaintBlack.setColor(AppManager.getApplicationContext()
                    .getResources().getColor(R.color.black));
            // contentPaintBlack.setTypeface(ViewUtils.getLightTypeface());
        }
        return contentPaintBlack;
    }

    /**
     * tips paint
     *
     * @return
     */
    public static Paint getTipPaintGray() {
        if (tipPaintGray == null) {
            tipPaintGray = new Paint();
            tipPaintGray.setAntiAlias(true);
            tipPaintGray.setShader(null);
            tipPaintGray.setFakeBoldText(false);
            tipPaintGray.setTextSize(AppManager.getApplicationContext()
                    .getResources().getDimension(R.dimen.text_size_minimal));
            tipPaintGray.setColor(AppManager.getApplicationContext()
                    .getResources().getColor(R.color.text_color_gray_99));
        }
        return tipPaintGray;
    }

    public static Paint getTipPaintSolarHoliday() {
        if (tipPaintGreen == null) {
            tipPaintGreen = new Paint();
            tipPaintGreen.setAntiAlias(true);
            tipPaintGreen.setShader(null);
            tipPaintGreen.setFakeBoldText(false);
            tipPaintGreen.setTextSize(AppManager.getApplicationContext()
                    .getResources().getDimension(R.dimen.text_size_minimal));
            tipPaintGreen.setColor(AppManager.getApplicationContext()
                    .getResources().getColor(R.color.forest_green));
        }
        return tipPaintGreen;
    }

    public static Paint getTipPaintLunarDate() {
        if (tipPaintLunarNormal == null) {
            tipPaintLunarNormal = new Paint();
            tipPaintLunarNormal.setAntiAlias(true);
            tipPaintLunarNormal.setShader(null);
            tipPaintLunarNormal.setFakeBoldText(false);
            tipPaintLunarNormal.setTextSize(AppManager.getApplicationContext()
                    .getResources().getDimension(R.dimen.text_size_minimal));
            tipPaintLunarNormal.setColor(AppManager.getApplicationContext()
                    .getResources().getColor(R.color.date_tips_normal_gray));
        }
        return tipPaintLunarNormal;
    }

    public static Paint getTipPaintWhite() {
        if (tipPaintWhite == null) {
            tipPaintWhite = new Paint();
            tipPaintWhite.setAntiAlias(true);
            tipPaintWhite.setShader(null);
            tipPaintWhite.setFakeBoldText(false);
            tipPaintWhite.setTextSize(AppManager.getApplicationContext()
                    .getResources().getDimension(R.dimen.text_size_minimal));
            tipPaintWhite.setColor(AppManager.getApplicationContext()
                    .getResources().getColor(R.color.white));
        }
        return tipPaintWhite;
    }

    public static Paint getTipPaintLunarHolidayRed() {
        if (tipLunarHolidayRed == null) {
            tipLunarHolidayRed = new Paint();
            tipLunarHolidayRed.setAntiAlias(true);
            tipLunarHolidayRed.setShader(null);
            tipLunarHolidayRed.setFakeBoldText(false);
            tipLunarHolidayRed.setTextSize(AppManager.getApplicationContext()
                    .getResources().getDimension(R.dimen.text_size_describe));
            tipLunarHolidayRed.setColor(AppManager.getApplicationContext()
                    .getResources().getColor(R.color.china_red));
        }
        return tipLunarHolidayRed;
    }

    public static Paint getTipPaintLunarHolidayWhite() {
        if (tipLunarHolidayWhite == null) {
            tipLunarHolidayWhite = new Paint();
            tipLunarHolidayWhite.setAntiAlias(true);
            tipLunarHolidayWhite.setShader(null);
            tipLunarHolidayWhite.setFakeBoldText(false);
            tipLunarHolidayWhite.setTextSize(AppManager.getApplicationContext()
                    .getResources().getDimension(R.dimen.text_size_describe));
            tipLunarHolidayWhite.setColor(AppManager.getApplicationContext()
                    .getResources().getColor(R.color.white));
        }
        return tipLunarHolidayWhite;
    }

    public static Paint getSelectedCirclePaint() {
        if (selectedCirclePaint == null) {
            selectedCirclePaint = new Paint();
            selectedCirclePaint.setAntiAlias(true);
            selectedCirclePaint.setShader(null);
            selectedCirclePaint.setStyle(Style.FILL);
            selectedCirclePaint.setColor(AppManager.getApplicationContext()
                    .getResources().getColor(R.color.main_theme_color/*date_choose_color_bg*/));
            selectedCirclePaint.setAlpha(200);
        }
        return selectedCirclePaint;
    }

    public static Paint getHasEventsCirclePaintWhite() {
        if (hasEventsCirclePaintWhite == null) {
            hasEventsCirclePaintWhite = new Paint();
            hasEventsCirclePaintWhite.setAntiAlias(true);
            hasEventsCirclePaintWhite.setShader(null);
            hasEventsCirclePaintWhite.setStyle(Style.FILL);
            hasEventsCirclePaintWhite.setColor(AppManager.getApplicationContext()
                    .getResources().getColor(R.color.white));
        }
        return hasEventsCirclePaintWhite;
    }

    public static void setHasEventsCirclePaintWhite(Paint p) {
        hasEventsCirclePaintWhite = p;
    }

    public static Paint getHasEventsCirclePaintGreen() {
        if (hasEventsCirclePaintGreen == null) {
            hasEventsCirclePaintGreen = new Paint();
            hasEventsCirclePaintGreen.setAntiAlias(true);
            hasEventsCirclePaintGreen.setShader(null);
            hasEventsCirclePaintGreen.setStyle(Style.FILL);
            hasEventsCirclePaintGreen.setColor(AppManager.getApplicationContext()
                    .getResources().getColor(R.color.green));
        }
        return hasEventsCirclePaintGreen;
    }

    public static void setHasEventsCirclePaintGreen(Paint p) {
        hasEventsCirclePaintGreen = p;
    }

    public static Paint getContentPaintGreen() {
        if (contentPaintGreen == null) {
            contentPaintGreen = new Paint();
            contentPaintGreen.setAntiAlias(true);
            contentPaintGreen.setShader(null);
            contentPaintGreen.setFakeBoldText(false);
            contentPaintGreen.setTextSize(AppManager.getApplicationContext()
                    .getResources().getDimension(R.dimen.calendar_text_size));
            contentPaintGreen.setColor(AppManager.getApplicationContext()
                    .getResources().getColor(R.color.green));
        }
        return contentPaintGreen;
    }

    public static Paint getContentPaintGray() {
        if (contentPaintGray == null) {
            contentPaintGray = new Paint();
            contentPaintGray.setAntiAlias(true);
            contentPaintGray.setShader(null);
            contentPaintGray.setFakeBoldText(false);
            contentPaintGray.setTextSize(AppManager.getApplicationContext()
                    .getResources().getDimension(R.dimen.calendar_text_size));
            contentPaintGray.setColor(AppManager.getApplicationContext()
                    .getResources().getColor(R.color.text_color_gray_99));
        }
        return contentPaintGray;
    }

    public static Paint getTodayCirclePaint() {
        if (todayCirclePaint == null) {
            todayCirclePaint = new Paint();
            todayCirclePaint.setAntiAlias(true);
            todayCirclePaint.setShader(null);
            todayCirclePaint.setStyle(Style.STROKE);
            todayCirclePaint.setStrokeWidth(1.0f);
            todayCirclePaint.setColor(AppManager.getApplicationContext()
                    .getResources().getColor(R.color.text_color_gray_33));
        }
        return todayCirclePaint;
    }

    public static Paint getContentPaintWhite() {
        if (contentPaintWhite == null) {
            contentPaintWhite = new Paint();
            contentPaintWhite.setAntiAlias(true);
            contentPaintWhite.setShader(null);
            contentPaintWhite.setFakeBoldText(false);
            contentPaintWhite.setTextSize(AppManager.getApplicationContext()
                    .getResources().getDimension(R.dimen.calendar_text_size));
            contentPaintWhite.setColor(AppManager.getApplicationContext()
                    .getResources().getColor(R.color.white));
        }
        return contentPaintWhite;
    }

    public static void setContentPaintWhite(Paint p) {
        contentPaintWhite = p;
    }

    /**
     * 消费数决定颜色
     *
     * @param value
     * @return
     */
    public static int getColorForPrice(int value) {
        if (value < 0) {
            return R.color.white;
        } else if (value <= 10) {
            return R.color.consume_color_degree_10;
        } else if (value <= 50) {
            return R.color.consume_color_degree_50;
        } else if (value <= 100) {
            return R.color.consume_color_degree_100;
        } else if (value <= 150) {
            return R.color.consume_color_degree_150;
        } else if (value <= 200) {
            return R.color.consume_color_degree_200;
        } else if (value <= 300) {
            return R.color.consume_color_degree_300;
        } else if (value <= 400) {
            return R.color.consume_color_degree_400;
        } else if (value <= 600) {
            return R.color.consume_color_degree_600;
        } else if (value <= 800) {
            return R.color.consume_color_degree_800;
        } else {
            return R.color.consume_color_degree_other;
        }
    }
}