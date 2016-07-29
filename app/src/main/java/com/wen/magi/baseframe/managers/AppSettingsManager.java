package com.wen.magi.baseframe.managers;

import com.wen.magi.baseframe.utils.Constants;
import com.wen.magi.baseframe.utils.IOUtils;
import com.wen.magi.baseframe.utils.LangUtils;

/**
 * Created by MVEN on 16/7/20.
 * <p/>
 * email: magiwen@126.com.
 */


public class AppSettingsManager {

    /**
     * 是否设置《接受推送通知》
     */
    private static boolean isPushMessageOn = true;

    /**
     * 非wifi流量，查看图片的质量，默认小图
     *
     * 0:不看图  1:原图  2:小图
     */
    private static int picQuality = 2;

    private static AppSettingsManager instance = null;

    public static AppSettingsManager getInstance() {
        if (instance == null) {
            synchronized (AppSettingsManager.class) {
                if (instance == null)
                    instance = new AppSettingsManager();
            }
        }
        return instance;
    }

    private AppSettingsManager() {
        String strPicQuality = IOUtils.getPreferenceValue(Constants.SETTINGS_PIC_QUALITY);
        if (LangUtils.isEmpty(strPicQuality)) {
            IOUtils.savePreference(Constants.SETTINGS_PIC_QUALITY, "2");
        } else {
            picQuality = LangUtils.parseInt(strPicQuality, 2);
        }

        String strPushOn = IOUtils.getPreferenceValue(Constants.SETTINGS_IS_PUSHMESSAGE_ON);
        if (LangUtils.isEmpty(strPushOn)) {
            IOUtils.savePreference(Constants.SETTINGS_IS_PUSHMESSAGE_ON, "true");
        } else {
            isPushMessageOn = strPushOn.equals("true") ? true : false;
        }
    }

    public static void setPicQuality(int quality) {
        picQuality = quality;
        IOUtils.savePreference(Constants.SETTINGS_PIC_QUALITY, String.valueOf(quality));
    }

    public static int getPicQuality() {
        return picQuality;
    }

    public static void setPushMessageOn(boolean pushMessageOn) {
        isPushMessageOn = pushMessageOn;
        IOUtils.savePreference(Constants.SETTINGS_IS_PUSHMESSAGE_ON, pushMessageOn ? "true" : "false");
    }

    public static boolean isPushMessageOn() {
        return isPushMessageOn;
    }
}
