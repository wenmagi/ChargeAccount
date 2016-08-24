package com.wen.magi.baseframe.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.Set;

import static com.wen.magi.baseframe.base.AppConfig.DEFAULT_WEB_URL;
import static com.wen.magi.baseframe.utils.LangUtils.isEmpty;

/**
 * Created by MVEN on 16/6/19.
 * <p/>
 * email: magiwen@126.com.
 */


public class WebUtils {

    /**
     * 手动拼接url以及参数
     *
     * @param url
     * @param params
     * @return String
     */
    @NonNull
    public static String compositeUrl(@NonNull String url, Map<String, Object> params) {
        StringBuilder b = new StringBuilder(url.length()).append(url);
        if (params != null && !params.isEmpty()) {
            if (!url.contains("?"))
                b.append('?');
            else
                b.append('&');
            LogUtils.d("compositeUrl: url is %s", b.toString());
            int i = 0;
            Set<Map.Entry<String, Object>> set = params.entrySet();
            for (Map.Entry<String, Object> entry : set) {
                i++;
                b.append(entry.getKey()).append('=')
                        .append(entry.getValue() == null ? "" : Uri.encode(entry.getValue().toString()));
                if (i < set.size())
                    b.append('&');
            }
        }

        return b.toString();
    }

    /**
     * 使用String创建url
     *
     * @param relativeString
     * @return
     */
    public static URL createURL(String relativeString) {
        return createURL(relativeString, DEFAULT_WEB_URL);
    }

    /**
     * 在base URL上创建url
     *
     * @param relativeString
     * @param base
     * @return null 表示创建url异常
     */
    public static URL createURL(String relativeString, URL base) {
        try {
            URL url;

            if (base == null) {
                url = new URL(relativeString);
            } else {
                url = new URL(base, relativeString);
            }
            return url;
        } catch (MalformedURLException e) {
            LogUtils.e("Failed to createURI %s, %s", relativeString, base);
            return null;
        }
    }

    /**
     * Get Boolean from a JSON object.
     *
     * @param json
     * @param key
     * @return Boolean
     */

    public static Boolean getJsonBoolean(JSONObject json, String key) {
        return getJsonBoolean(json, key, false);
    }

    /**
     * Get Boolean from a JSON object.
     *
     * @param json
     * @param key
     * @param defaultValue
     * @return Boolean
     */

    public static Boolean getJsonBoolean(JSONObject json, String key,
                                         Boolean defaultValue) {
        if (json == null)
            return defaultValue;
        try {
            if (json.containsKey(key)) {
                Boolean value = json.getBoolean(key);
                return value == null ? defaultValue : value;
            }
        } catch (JSONException e) {
            LogUtils.e(e, "Failed to getJsonBoolean %s", key);
        }

        return defaultValue;
    }


    /**
     * Get String from a JSON object.
     *
     * @param json
     * @param key
     * @return String
     */

    public static String getJsonString(JSONObject json, String key) {
        return getJsonString(json, key, null);
    }

    /**
     * Get String from a JSON object.
     *
     * @param json
     * @param key
     * @param defaultValue
     * @return String
     */

    public static String getJsonString(JSONObject json, String key, String defaultValue) {
        if (json == null)
            return defaultValue;
        try {
            if (json.containsKey(key)) {
                String value = json.getString(key);
                return value == null || value.equals("null") ? defaultValue : value;
            }
        } catch (JSONException e) {
//      LogUtils.e(e, "Failed to getJsonString %s", key);
        }

        return defaultValue;
    }

    /**
     * Get String from a JSONArray.
     *
     * @param array
     * @param index
     * @return String
     */
    public static String getJsonString(JSONArray array, int index) {
        return getJsonString(array, index, null);
    }

    /**
     * Get String from a JSONArray.
     *
     * @param array
     * @param index
     * @param defaultValue
     * @return String
     */
    public static String getJsonString(JSONArray array, int index, String defaultValue) {
        if (array == null)
            return defaultValue;
        try {
            return array.getString(index);
        } catch (JSONException e) {
            LogUtils.e(e, "Failed to getJsonString %d, %d", array.size(), index);
        }

        return defaultValue;
    }

    /**
     *
     */
    public static JSONArray getJsonArray(JSONArray array, int index) {
        if (array == null)
            return null;
        try {
            return array.getJSONArray(index);
        } catch (JSONException e) {
            LogUtils.e(e, "Failed to getJsonString %d, %d", array.size(), index);
        }

        return null;
    }

    /**
     * Get int from a JSON object.
     *
     * @param json
     * @param key
     * @return int
     */
    public static int getJsonInt(JSONObject json, String key) {
        return getJsonInt(json, key, -1);
    }

    /**
     * Get int from a JSON object.
     *
     * @param json
     * @param key
     * @param defaultValue
     * @return int
     */
    public static int getJsonInt(JSONObject json, String key, int defaultValue) {
        if (json == null)
            return defaultValue;
        if (json.containsKey(key)) {
            try {
                int value = json.getIntValue(key);
                return value;
            } catch (JSONException e) {
                LogUtils.e(e, "Failed to getJsonInt %s", key);
            }
        }

        return defaultValue;
    }

    /**
     * get float from json
     *
     * @param json
     * @param key
     * @param defaultValue
     * @return
     */
    public static double getJsonDouble(JSONObject json, String key, double defaultValue) {
        if (json == null)
            return defaultValue;
        if (json.containsKey(key)) {
            try {
                double value = json.getDouble(key);
                return value;
            } catch (JSONException e) {
                LogUtils.e(e, "Failed to getJsonInt %s", key);
            }
        }

        return defaultValue;
    }

    /**
     * @param json
     * @param key
     * @param defaultValue
     * @return
     */
    public static double getJsonFloat(JSONObject json, String key, float defaultValue) {
        if (json == null)
            return defaultValue;
        if (json.containsKey(key)) {
            try {
                double value = json.getDouble(key);
                return value;
            } catch (JSONException e) {
                LogUtils.e(e, "Failed to getJsonInt %s", key);
            }
        }

        return defaultValue;
    }

    /**
     * Get int from a JSON object.
     *
     * @param json
     * @param key
     * @param defaultValue
     * @return int
     */
    public static long getJsonLong(JSONObject json, String key, Long defaultValue) {
        if (json == null)
            return defaultValue;
        if (json.containsKey(key)) {
            try {
                long value = json.getLong(key);
                return value;
            } catch (JSONException e) {
                LogUtils.e(e, "Failed to getJsonInt %s", key);
            }
        }

        return defaultValue;
    }

    /**
     * Get int from a JSONArray.
     *
     * @param array
     * @param index
     * @return int
     */
    public static int getJsonInt(JSONArray array, int index) {
        return getJsonInt(array, index, -1);
    }

    /**
     * Get int from a JSONArray.
     *
     * @param array
     * @param index
     * @param defaultValue
     * @return int
     */
    public static int getJsonInt(JSONArray array, int index, int defaultValue) {
        if (array == null)
            return defaultValue;
        try {
            return array.getIntValue(index);
        } catch (JSONException e) {
            LogUtils.e(e, "Failed to get JsonInt %d, %d", array.size(), index);
        }

        return defaultValue;
    }

    /**
     * Get long from a JSONArray.
     *
     * @param array
     * @param index
     * @param defaultValue
     * @return long
     */
    public static long getJsonLong(JSONArray array, int index, long defaultValue) {
        if (array == null)
            return defaultValue;
        try {
            return array.getLong(index);
        } catch (JSONException e) {
            LogUtils.e(e, "Failed to get JsonInt %d, %d", array.size(), index);
        }

        return defaultValue;
    }

    /**
     * Get a JSONObject from a JSONObject.
     *
     * @param json
     * @param key
     * @return JSONObject
     */

    public static JSONObject getJsonObject(JSONObject json, String key) {
        if (json == null)
            return null;
        try {
            if (json.containsKey(key))
                return json.getJSONObject(key);
        } catch (JSONException e) {
            LogUtils.e("Failed to getJsonObject key = %s", key);
        }

        return null;
    }

    /**
     * Get a JSONArray from a JSONObject.
     *
     * @param json
     * @param key
     * @return JSONArray
     */

    public static JSONArray getJsonArray(JSONObject json, String key) {
        if (json == null)
            return null;
        try {
            if (json.containsKey(key))
                return json.getJSONArray(key);
        } catch (JSONException e) {
            LogUtils.e(e, "Failed to getJsonArray %s", key);
        }

        return null;
    }


    public static JSONArray getJsonArray(String content) {
        if (content == null)
            return null;
        try {
            JSONArray array = JSON.parseArray(content);
            return array;
        } catch (JSONException e) {
            LogUtils.e(e, "Failed to getJsonArray from %s", content);
        }

        return null;
    }

    /**
     * Get a JSONObject from a JSONArray.
     *
     * @param array
     * @param index
     * @return JSONObject
     */

    public static JSONObject getJsonObject(JSONArray array, int index) {
        if (array == null)
            return null;
        try {
            return array.getJSONObject(index);
        } catch (JSONException e) {
            LogUtils.e("Failed to getJsonObject %d", index);
        }
        return null;
    }

    public static Object getArrayObject(JSONArray array, int index) {
        if (array == null)
            return null;
        try {
            return array.get(index);
        } catch (JSONException e) {
            LogUtils.e(e, "Failed to getJsonObject %d", index);
        }
        return null;
    }


    /**
     * This method never return null, use .length() == 0 to check if the ctx is empty
     *
     * @param json
     * @return
     */
    @NonNull
    public static JSONObject parseJsonObject(String json) {
        if (isEmpty(json)) {
            return null;
        }
        try {
            return JSON.parseObject(json);
        } catch (JSONException e) {
            LogUtils.w(e, "Failed to parseJsonObject, %s", json);
            return null;
        }
    }

    public static JSONArray parseJsonArray(String json) {
        if (isEmpty(json)) {
            return null;
        }
        try {
            return JSON.parseArray(json);
        } catch (JSONException e) {
            LogUtils.w(e, "Failed to parseJsonArray, %s", json);
            return null;
        }
    }

    /**
     * Check whether network connected
     *
     * @param context
     * @return
     */
    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager =
                    (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }
}
