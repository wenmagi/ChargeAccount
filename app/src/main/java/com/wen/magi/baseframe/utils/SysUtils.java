package com.wen.magi.baseframe.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import com.wen.magi.baseframe.managers.AppManager;
import com.wen.magi.baseframe.managers.AppSessionManager;

import java.io.File;
import java.lang.reflect.Field;
import java.util.UUID;

/**
 * Created by MVEN on 16/4/28.
 */
public class SysUtils {

    public static String TELE_DEVICE_ID;
    public static String ANDROID_ID;
    public static String DEVICE_NAME;
    public static String VERSION_NAME;
    public static String WIFI_MAC;
    public static String PHONE_NUMBER;
    public static int WIDTH;
    public static int HEIGHT;
    public static float DENSITY;
    public static float DENSITYDPI;
    public static float SCALEDDENSITY;
    public static boolean EXTERNAL_STORAGE_WRITABLE = false;
    public static String mUniquePsuedoID;
    public static String APP_VERSION_NAME;

    private SysUtils() {
    }


    @SuppressWarnings("deprecation")
    @SuppressLint("NewApi")
    public static void initialize(Context ctx) {
        try {
            TelephonyManager tm = (TelephonyManager) ctx
                    .getSystemService(Context.TELEPHONY_SERVICE);
            TELE_DEVICE_ID = tm.getDeviceId();
            DEVICE_NAME = tm.getSubscriberId();
        } catch (Exception e) {
            LogUtils.d("Failed to get tele info: " + e);
        }

        try {
            ANDROID_ID = Settings.Secure.getString(ctx.getContentResolver(),
                    Settings.Secure.ANDROID_ID);
        } catch (Exception e) {
            LogUtils.w(e, "Failed to get ANDROID_ID");
        }

        if (LangUtils.isEmpty(ANDROID_ID))
            ANDROID_ID = "emulator";

        if (DEVICE_NAME == null)
            DEVICE_NAME = "";

        VERSION_NAME = Build.VERSION.RELEASE;

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

        try {
            String state = Environment.getExternalStorageState();
            if (Environment.MEDIA_MOUNTED.equals(state)) {
                File tmp = new File(Environment.getExternalStorageDirectory(),
                        Constants.EXTERNAL_STORAGE_WRITABLE);
                if (tmp.exists())
                    tmp.delete();
                if (tmp.createNewFile()) {
                    EXTERNAL_STORAGE_WRITABLE = true;
                    tmp.delete();
                }
            }
        } catch (Exception e) {
            LogUtils.d("Failed to test external storage writable: " + e);
        }
        try {
            WifiManager wifi = (WifiManager) ctx
                    .getSystemService(Context.WIFI_SERVICE);
            WifiInfo info = wifi.getConnectionInfo();
            if (info.getMacAddress() != null) {
                WIFI_MAC = info.getMacAddress();
            } else {
                WIFI_MAC = "";
            }
        } catch (Exception e) {
            LogUtils.d("Failed to get WIFI MAC: " + e);
        }

        try {
            TelephonyManager tm = (TelephonyManager) ctx
                    .getSystemService(Context.TELEPHONY_SERVICE);
            if (tm.getLine1Number() != null) {
                PHONE_NUMBER = tm.getLine1Number();
            } else {
                PHONE_NUMBER = "";
            }
        } catch (Exception e) {
            LogUtils.d("Failed to get phone number: " + e);
        }

        mUniquePsuedoID = getUniquePsuedoID();
        APP_VERSION_NAME = getVersionName(ctx);

    }

    /**
     * get the ID of android device
     *
     * @param c
     * @return
     */
    public static String getDeviceID(Context c) {
        String duid = Settings.Secure.getString(c.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        return LangUtils.isEmpty(duid) ? "emulator" : duid;
    }

    /**
     * get version name
     *
     * @param c
     * @return
     */
    public static String getVersionName(Context c) {
        if (c == null)
            c = AppManager.getApplicationContext();
        if (c == null)
            Log.e(Constants.LOG_TAG, "made mistakes when getVersion name");
        PackageManager pm = c.getPackageManager();
        String versionName = "";
        try {
            PackageInfo info = pm.getPackageInfo(c.getPackageName(), 0);
            versionName = info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionName;
    }

    /**
     * Pseudo-Unique ID - Software (for all Android devices)
     * Return pseudo unique ID
     *
     * @return ID
     * <p/>
     * http://stackoverflow.com/questions/2785485/is-there-a-unique-android-device-id/
     */
    public static String getUniquePsuedoID() {
        // If all else fails, if the user does have lower than API 9 (lower
        // than Gingerbread), has reset their device or 'Secure.ANDROID_ID'
        // returns 'null', then simply the ID returned will be solely based
        // off their Android device information. This is where the collisions
        // can happen.
        // Thanks http://www.pocketmagic.net/?p=1662!
        // Try not to use DISPLAY, HOST or ID - these items could change.
        // If there are collisions, there will be overlapping data
        String m_szDevIDShort = "35" + (Build.BOARD.length() % 10) + (Build.BRAND.length() % 10) +
                (Build.CPU_ABI.length() % 10) + (Build.DEVICE.length() % 10) + (Build.MANUFACTURER.length() % 10)
                + (Build.MODEL.length() % 10) + (Build.PRODUCT.length() % 10);

        // Thanks to @Roman SL!
        // http://stackoverflow.com/a/4789483/950427
        // Only devices with API >= 9 have android.os.Build.SERIAL
        // http://developer.android.com/reference/android/os/Build.html#SERIAL
        // If a user upgrades software or roots their device, there will be a duplicate entry
        String serial = null;
        try {
            serial = android.os.Build.class.getField("SERIAL").get(null).toString();

            // Go ahead and return the serial for api => 9
            return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
        } catch (Exception exception) {
            // String needs to be initialized
            serial = "serial"; // some value
        }

        // Thanks @Joe!
        // http://stackoverflow.com/a/2853253/950427
        // Finally, combine the values we have found by using the UUID class to create a unique identifier
        return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
    }


    /**
     * 获取应用程序的版本号
     *
     * @return String
     * @author gdpancheng@gmail.com 2013-10-12 下午2:30:12
     */
    public static String getAppVersionCode() {
        return getAppVersionCode(null);
    }

    /**
     * 获取指定应用程序的版本号
     *
     * @param packageName
     * @return String
     */
    public static String getAppVersionCode(String packageName) {
        String versionCode;
        Context context = AppManager.getApplicationContext();
        if (packageName == null) {
            packageName = context.getPackageName();
        }

        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(packageName, 0);
            versionCode = Integer.toString(packageInfo.versionCode);
        } catch (Exception e) {
            Log.e(Constants.LOG_TAG, "made mistakes when getVersion code");
            versionCode = "";
        }

        return versionCode;
    }

    /*
     * 判断是否是该签名打包
	 */
    public static boolean isRelease(String signatureString) {
        final String releaseSignatureString = signatureString;
        Context context = AppManager.getApplicationContext();
        if (releaseSignatureString == null || releaseSignatureString.length() == 0) {
            throw new RuntimeException("Release signature string is null or missing.");
        }

        final Signature releaseSignature = new Signature(releaseSignatureString);
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature sig : pi.signatures) {
                if (sig.equals(releaseSignature)) {
                    return true;
                }
            }
        } catch (Exception e) {
            LogUtils.e("judge isRelease error: %s", e);
            return true;
        }
        return false;
    }

    /**
     * 判断是否是模拟器
     *
     * @return boolean
     */
    public static boolean isEmulator() {
        return Build.MODEL.equals("sdk") || Build.MODEL.equals("google_sdk");
    }

    /**
     * @param @return 设定文件
     * @return String 返回类型
     * @Description: 获取手机的硬件信息
     */
    public static String getMobileInfo() {
        StringBuffer sb = new StringBuffer();
        /**
         * 通过反射获取系统的硬件信息 获取私有的信息
         */
        try {
            Field[] fields = Build.class.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                String name = field.getName();
                String value = field.get(null).toString();
                sb.append(name + "=" + value);
                sb.append("\n");
            }
        } catch (Exception e) {
            LogUtils.e("getMobileInfo error: %s", e);
        }
        return sb.toString();
    }

    /**
     * get the type of network
     *
     * @return
     */
    public static AppSessionManager.NetWorkType getNetWorkType() {
        Context con = AppManager.getApplicationContext();
        ConnectivityManager conn = (ConnectivityManager) con
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (conn == null)
            return AppSessionManager.NetWorkType.NETWORK_TYPE_NULL;
        NetworkInfo info = conn.getActiveNetworkInfo();
        if (info == null)
            return AppSessionManager.NetWorkType.NETWORK_TYPE_NULL;
        String type = info.getTypeName();
        if ("wifi".equals(type) || "WIFI".equals(type))
            return AppSessionManager.NetWorkType.NETWORK_TYPE_WIFI;
        else if ("mobile".equals(type) || "MOBILE".equals(type)) {
            String apn = info.getExtraInfo();
            if (apn.contains("wap") && apn != null)
                return AppSessionManager.NetWorkType.NETWORK_TYPE_WAP;
            else
                return AppSessionManager.NetWorkType.NETWORK_TYPE_NET;
        }
        return AppSessionManager.NetWorkType.NETWORK_TYPE_NULL;
    }


    /**
     * 判断当前SDK版本是否大于目标版本
     *
     * @param targetSDK 目标版本
     * @return boolean
     */
    public static boolean nowSDKINTBigger(int targetSDK) {
        if (Build.VERSION.SDK_INT >= targetSDK)
            return true;
        else
            return false;
    }
}
