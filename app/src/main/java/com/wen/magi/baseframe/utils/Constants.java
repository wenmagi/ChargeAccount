package com.wen.magi.baseframe.utils;

import java.util.Calendar;

/**
 * Created by MVEN on 16/4/28.
 */
public class Constants {

    //      WebActivity
    public static final String ACTIVITY_WEB_KEY_INTENT_URL = "activity_intent_url";
    //    SysUtils
    public static final String LOG_TAG = "App";
    public static final String EXTERNAL_STORAGE_WRITABLE = "app_external_writable";
    /**
     * AppSettingManager
     */
    public static final String SETTINGS_IS_PUSHMESSAGE_ON = "settings_is_pushmessage_on";
    public static final String SETTINGS_PIC_QUALITY = "settings_pic_quality";


    public static final int MAX_EVENTS_COUNT = 15;
    public static final int MAX_EVENTS_SCROLL_COUNT = 84000;
    public static final int MAX_WEEK_SCROLL_COUNT = MAX_EVENTS_SCROLL_COUNT / 7;
    public static final int MAX_MONTH_SCROLL_COUNT = MAX_EVENTS_SCROLL_COUNT / 30;
    public static final int FIRST_DAY_OF_WEEK = Calendar.MONDAY;
    public static final int MAX_EVENT_VIEWPAGER_COUNT = 3;
    public static final int MONTH_CALENDAR_COLUMN = 7;
    public static final int ROWS_OF_MONTH_CALENDAR = 6;


    public final static int CALENDAR_UPDATE_CALENDAR = 0x7;
    public final static int CALENDAR_UPDATE_WEEK = 0x9;
    public final static int CALENDAR_UPDATE_MONTH = 0x10;
    public final static int UPDATE_DATA = 0x11;
    public final static int CALENDAR_UPDATE_EVENT_LIST = 0x12;

    public final static int SELECT_TYPE_FROM_EVENTS_SCROLL = 0x0;
    public final static int SELECT_TYPE_FROM_WEEK_CLICK = 0x1;
    public final static int SELECT_TYPE_FROM_WEEK_SCROLL = 0x2;
    public final static int SELECT_TYPE_FROM_MONTH_CLICK = 0x3;
    public final static int SELECT_TYPE_FROM_MONTH_SCROLL = 0x4;
    public final static int SELECT_TYPE_FROM_JUMP_TO = 0x5;

    /*MonthFragment*/
    public static final String BUNDLE_MONTH_FRAGMENT = "bundle_month_fragment";


}
