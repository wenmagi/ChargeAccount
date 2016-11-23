package com.wen.magi.baseframe.views.touch3d;

import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import com.wen.magi.baseframe.utils.LogUtils;
import com.wen.magi.baseframe.utils.SysUtils;

/**
 * @author zhangzhaowen @ Zhihu Inc.
 * @since 11-18-2016
 */

public class Touch3DUtils {

    public static int WIDTH;
    public static int HEIGHT;
    public static float DENSITY;
    public static float DENSITYDPI;
    public static float SCALEDDENSITY;

    private void init(Context ctx) {
        try {
            WindowManager wm = (WindowManager) ctx
                    .getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB_MR2) {
                WIDTH = display.getWidth();
                HEIGHT = display.getHeight();
            } else {
                Point size = new Point();
                display.getSize(size);
                WIDTH = size.x;
                HEIGHT = size.y;
            }


            DisplayMetrics metric = new DisplayMetrics();
            display.getMetrics(metric);
            DENSITYDPI = metric.densityDpi;
            DENSITY = metric.density;
            SCALEDDENSITY = metric.scaledDensity;

        } catch (Exception e) {
            LogUtils.e(e, "Failed to get display info");
        }
    }

    public static int dp2pix(float dp) {
        return dp > 0 ? (int) (dp * SysUtils.DENSITY + 0.5f) : (int) dp;
    }
}
