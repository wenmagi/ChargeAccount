package com.wen.magi.baseframe.managers;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.IntDef;
import android.support.v4.content.SharedPreferencesCompat;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author MVEN @ Zhihu Inc.
 * @since 09-30-2016
 */

public class ThemeManager {

    public static final int LIGHT = 0x01;

    public static final int DARK = 0x02;

    private static final ThemeManager sInstance = new ThemeManager();

    public static ThemeManager getInstance() {
        return ThemeManager.sInstance;
    }

    private ThemeManager() {

    }

    @Theme
    public int getCurrentTheme(final Context pContext) {
        final SharedPreferences sharedPreferences = pContext.getSharedPreferences("theme", Context.MODE_PRIVATE);

        final int theme = sharedPreferences.getInt("theme", ThemeManager.LIGHT);

        switch (theme) {
            case ThemeManager.LIGHT:
                return ThemeManager.LIGHT;
            case ThemeManager.DARK:
                return ThemeManager.DARK;
            default:
                return ThemeManager.LIGHT;
        }
    }

    public void setCurrentTheme(final Context pContext, @Theme final int pTheme) {
        final SharedPreferences sharedPreferences = pContext.getSharedPreferences("theme", Context.MODE_PRIVATE);

        SharedPreferencesCompat.EditorCompat.getInstance().apply(sharedPreferences.edit().putInt("theme", pTheme));
    }

    @IntDef({ ThemeManager.LIGHT, ThemeManager.DARK })
    @Retention(RetentionPolicy.SOURCE)
    public @interface Theme {}
}
