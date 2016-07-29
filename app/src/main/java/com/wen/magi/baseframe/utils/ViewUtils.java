package com.wen.magi.baseframe.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.ResultReceiver;
import android.support.annotation.NonNull;
import android.support.v4.text.TextUtilsCompat;
import android.text.TextUtils;
import android.view.Display;
import android.view.TouchDelegate;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.wen.magi.baseframe.managers.AppManager;

import java.lang.ref.WeakReference;

/**
 * Created by MVEN on 16/4/28.
 */
public class ViewUtils {
    public static final int ID_NONE = -1;
    private static boolean isInited = false;
    private static EnhancedHandler handler;
    private static WeakReference<Thread> _thread_ref;

    public ViewUtils() {
    }

    public static void initialize() {
        if (isInited)
            return;
        handler = new EnhancedHandler();
        _thread_ref = new WeakReference<>(Thread.currentThread());
        isInited = true;
    }

    /**
     * let runnable run in mainThread
     *
     * @param runnable runnable
     */
    public static void runInHandlerThread(Runnable runnable) {
        if (handler != null)
            handler.runInMainThread(runnable);
    }

    public static void removeRunnable(Runnable runnable) {
        if (handler != null)
            handler.removeCallbacks(runnable);
    }

    public static void post(Runnable runnable) {
        if (handler != null)
            handler.post(runnable);
    }

    /**
     * check if is in MainThread
     *
     * @return true:in false:not in
     */
    public static boolean isInMainThread() {
        if (!isInited)
            return true;
        else if (_thread_ref != null)
            return _thread_ref.get() == Thread.currentThread();
        else
            return false;
    }

    public static float pix2sp(int pix) {
        return pix > 0 ? pix / SysUtils.SCALEDDENSITY + 0.5f : pix;
    }

    public static float pix2dp(int pix) {
        return pix > 0 ? pix / SysUtils.DENSITYDPI + 0.5f : pix;
    }

    public static int sp2pix(float sp) {
        return sp > 0 ? (int) (sp * SysUtils.SCALEDDENSITY + 0.5f) : (int) sp;
    }

    public static int dp2pix(float dp) {
        return dp > 0 ? (int) (dp * SysUtils.DENSITYDPI + 0.5f) : (int) dp;
    }

    /**
     * every Parent can set childView only one time
     *
     * @param view childView
     */
    public static void extendTouchArea(final View view) {
        if (view == null)
            return;
        View parent = (View) view.getParent();
        parent.post(new Runnable() {
            @Override
            public void run() {
                Rect rect = new Rect();
                view.getHitRect(rect);
                rect.bottom += 50;
                rect.top -= 50;
                rect.left -= 100;
                rect.right += 100;
                TouchDelegate delegete = new TouchDelegate(rect, view);
                if (View.class.isInstance(view.getParent()))
                    ((View) view.getParent()).setTouchDelegate(delegete);
            }
        });
    }

    /**
     * get picture by string
     *
     * @param drawableStr name of drawable in "drawable" package
     * @return drawable
     */
    public static Drawable getDrawableByString(final String drawableStr) {
        if (LangUtils.isEmpty(drawableStr))
            return null;
        Context context = AppManager.getApplicationContext();
        Resources res = context.getResources();
        int resID = res.getIdentifier(drawableStr, "drawable", context.getPackageName());
        try {
            return context.getResources().getDrawable(resID);
        } catch (Resources.NotFoundException e) {
            return null;
        }
    }

    public static <T extends View> T find(Activity activity, int id) {
        return (T) activity.findViewById(id);
    }

    public static <T extends View> T find(View view, int id) {
        return (T) view.findViewById(id);
    }

    /**
     * darken target color
     *
     * @param color target color
     * @return darker Color
     */
    private static int darkenColor(int color) {
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        hsv[2] *= 0.9f;
        return Color.HSVToColor(hsv);
    }

    /**
     * light target color
     *
     * @param color target color
     * @return light color
     */
    private static int lightenColor(int color) {
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        hsv[2] *= 1.1f;
        return Color.HSVToColor(hsv);
    }

    /**
     * 隐藏软键盘
     *
     * @param view
     * @return void
     */
    public static void hideSoftKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /**
     * 显示软键盘
     *
     * @param view
     * @return void
     */
    public static void showSoftkeyboard(View view) {
        showSoftkeyboard(view, null);
    }

    /**
     * 显示软键盘
     *
     * @param view
     * @param resultReceiver
     * @return void
     */
    public static void showSoftkeyboard(View view, ResultReceiver resultReceiver) {
        Configuration config = view.getContext().getResources().getConfiguration();
        if (config.hardKeyboardHidden == Configuration.HARDKEYBOARDHIDDEN_YES) {
            InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);

            if (resultReceiver != null) {
                imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT, resultReceiver);
            } else {
                imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
            }
        }
    }

    /**
     * 截屏
     *
     * @param activity
     * @return Bitmap
     * @author gdpancheng@gmail.com 2013-10-26 下午2:39:01
     */
    public static Bitmap shot(Activity activity) {
        View view = activity.getWindow().getDecorView();
        Display display = activity.getWindowManager().getDefaultDisplay();
        view.layout(0, 0, display.getWidth(), display.getHeight());
        // 允许当前窗口保存缓存信息，这样getDrawingCache()方法才会返回一个Bitmap
        view.setDrawingCacheEnabled(true);
        Bitmap bmp = Bitmap.createBitmap(view.getDrawingCache());
        view.setDrawingCacheEnabled(false);
        return bmp;
    }

    /**
     * 获取文字的宽高
     *
     * @param paint
     * @param str
     * @return 数组形式返回
     */
    public static float[] getTextWH(Paint paint, String str) {
        float a[] = new float[2];
        a[0] = getTextWidth(paint, str);
        a[1] = getTextHeight(paint, str);
        return a;
    }

    /**
     * 获取文字的高度
     *
     * @param paint
     * @param str
     * @return
     */
    public static float getTextHeight(Paint paint, String str) {
        if (paint == null || TextUtils.isEmpty(str))
            return 0;
        Paint.FontMetrics metrics = paint.getFontMetrics();
        return (int) Math.ceil(metrics.descent - metrics.top) + 2;
    }

    /**
     * 获取文字的宽度
     *
     * @param paint
     * @param str
     * @return
     */
    public static float getTextWidth(Paint paint, String str) {
        if (paint == null || TextUtils.isEmpty(str))
            return 0;
        return paint.measureText(str);
    }

    private static Toast toast;

    /**
     * 展示文字Toast
     *
     * @param context
     * @param toastStr
     */
    public static void showToast(Context context, String toastStr) {
        showToast(context, toastStr, Toast.LENGTH_LONG);
    }

    /**
     * 展示文字Toast
     *
     * @param context
     * @param toastStr
     * @param duration
     */
    public static void showToast(final Context context, final String toastStr, final int duration) {
        if (isInMainThread()) {
            showToastImpl(context, toastStr, duration);
        } else {
            runInHandlerThread(new Runnable() {
                @Override
                public void run() {
                    showToastImpl(context, toastStr, duration);
                }
            });
        }
    }

    /**
     * 展示文字Toast的实现,避免多次点击生成多个Toast对象
     *
     * @param context
     * @param toastStr
     */
    private static void showToastImpl(Context context, String toastStr, int duration) {
        if (toast == null) {
            toast = Toast.makeText(context, toastStr, duration);
        } else {
            toast.setText(toastStr);
        }
        toast.show();
    }
}
