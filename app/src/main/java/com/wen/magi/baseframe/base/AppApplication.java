package com.wen.magi.baseframe.base;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;

import com.wen.magi.baseframe.managers.AppManager;

import java.util.List;

/**
 * Created by MVEN on 16/4/28.
 */
public class AppApplication extends Application {
    private static AppApplication appApplication;

    @Override
    public void onCreate() {
        super.onCreate();
        appApplication = this;
        String packageName = getApplicationContext().getPackageName();
        //防止多进程造成的多次初始化
        if (packageName != null && packageName.equals(getCurrProcessName()))
            AppManager.initInMainThread(appApplication);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    private String getCurrProcessName() {
        int processID = android.os.Process.myPid();
        ActivityManager activityManager = (ActivityManager) this.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> processes = activityManager.getRunningAppProcesses();
        if (processes == null)
            return null;
        for (ActivityManager.RunningAppProcessInfo appProcessInfo : processes) {
            if (processID == appProcessInfo.pid) {
                return appProcessInfo.processName;
            }
        }
        return null;
    }
}
