package com.wen.magi.baseframe.utils;

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
    public static final int MAX_EVENTS_SCROLL_COUNT = 84000;// must include
    public static final int MAX_WEEK_SCROLL_COUNT = MAX_EVENTS_SCROLL_COUNT / 7;
    public static final int MAX_MONTH_SCROLL_COUNT = MAX_EVENTS_SCROLL_COUNT / 30;


}
