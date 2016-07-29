package com.wen.magi.baseframe.utils;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.ParcelFileDescriptor;
import android.support.annotation.NonNull;

import com.wen.magi.baseframe.base.AppApplication;
import com.wen.magi.baseframe.managers.AppManager;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.HashMap;
import static com.wen.magi.baseframe.utils.LangUtils.*;
/**
 * Created by MVEN on 16/6/17.
 * <p/>
 * email: magiwen@126.com.
 */


public class IOUtils {
    private static final String KEY_SHARED_PREFERENCE = "base_frame";
    public static final byte[] EMPTY_BYTES = new byte[0];
    /**
     * 保存key－value preference
     *
     * @param key
     * @param value
     */
    public static void savePreference(String key, String value) {
        SharedPreferences preferences = AppManager.getApplicationContext().getSharedPreferences(KEY_SHARED_PREFERENCE, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    /**
     * 获取本地存储key值对应的value
     *
     * @param key
     * @return value
     */
    public static String getPreferenceValue(String key) {
        SharedPreferences preferences = AppManager.getApplicationContext().getSharedPreferences(KEY_SHARED_PREFERENCE, Activity.MODE_PRIVATE);
        return preferences.getString(key, null);
    }

    /**
     * 移除指定key值对应的preference
     *
     * @param key
     */
    public static void removePreference(String key) {
        SharedPreferences preferences = AppManager.getApplicationContext().getSharedPreferences(KEY_SHARED_PREFERENCE, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove(key);
        editor.apply();
    }

    /**
     * 清除所有preference值
     */
    public static void clearPreferences() {
        SharedPreferences preferences = AppManager.getApplicationContext().getSharedPreferences(KEY_SHARED_PREFERENCE, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
    }

    /**
     * 清除所有preference，keepKeys对应的值保留
     *
     * @param keepKeys 需要保留的key值
     */
    public static void clearPreferencesKeep(ArrayList<String> keepKeys) {
        SharedPreferences preferences = AppManager.getApplicationContext().getSharedPreferences(KEY_SHARED_PREFERENCE, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        if (LangUtils.isEmpty(keepKeys)) {
            editor.clear();
            editor.apply();
            return;
        }
        HashMap<String, String> map = new HashMap<>();
        for (String key : keepKeys) {
            String value = preferences.getString(key, null);
            if (LangUtils.isNotEmpty(value)) {
                map.put(key, value);
            }
        }
        editor.clear();
        for (HashMap.Entry entry : map.entrySet()) {
            editor.putString((String) entry.getKey(), (String) entry.getValue());
        }
        editor.apply();
    }

    public static boolean mkdirs(File file) {
        if (file == null)
            return false;
        File parent = file.getParentFile();
        if (parent != null && !parent.exists()) {
            if (!parent.mkdirs())
                return false;
        }
        return true;
    }

    /**
     * Write byte[] data to a file.
     *
     * @param file
     * @param data
     * @return boolean
     */
    public static boolean writeBytesToFile(File file, byte[] data) {
        if (file == null || data == null)
            return false;
        mkdirs(file);
        OutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(file);
            outputStream.write(data);
            return true;
        } catch (IOException e) {
            LogUtils.e(e, "Failed to write bytes %s", file);
            return false;
        } finally {
            close(outputStream);
        }
    }


    /**
     * Clear a specific directory.
     *
     * @param dir
     */
    public static void clearDir(@NonNull File dir) {
        if (dir.exists() && dir.isDirectory()) {
            File[] files = dir.listFiles();
            if (LogUtils.D)
                LogUtils.d("clear %d files in %s", files.length, dir);
            for (File sub : files)
                deleteFileOrDir(sub);
        }
    }

    /**
     * Close an IO channel.
     *
     * @param io
     * @return boolean
     */
    public static boolean close(Closeable io) {
        if (io != null)
            try {
                io.close();
                return true;
            } catch (IOException e) {
                LogUtils.e(e, "Failed to close IO %s", io);
                return false;
            }

        return true;
    }

    /**
     * Close a ParcelFileDescriptor.
     *
     * @param descriptor
     * @return boolean
     */
    public static boolean close(ParcelFileDescriptor descriptor) {
        if (descriptor != null)
            try {
                descriptor.close();
                return true;
            } catch (IOException e) {
                LogUtils.w(e, "Failed to close descriptor");
                return false;
            }
        else
            return true;
    }

    /**
     * Delete a specific file or directory.
     *
     * @param file
     * @return boolean
     */
    public static boolean deleteFileOrDir(@NonNull File file) {
        if (!exist(file))
            return true;
        if (file.isFile())
            return file.delete();
        else if (file.isDirectory()) {
            File[] subs = file.listFiles();
            if (subs.length == 0)
                return file.delete();
            else {
                boolean result = true;
                for (File sub : subs)
                    result = result && deleteFileOrDir(sub);
                return result && file.delete();
            }
        } else {
            LogUtils.w("unknown file type: %s", file);
            return false;
        }
    }


    /**
     * Check if a file is already existed.
     *
     * @param file
     * @return boolean
     */
    public static boolean exist(File file) {
        return file != null && file.exists();
    }
    /**
     * Create byte[] data from a file.
     *
     * @param file
     * @return byte[]
     */

    public static byte[] dataFromFile( File file) {
        if (file == null || !file.exists())
            return null;
        try {
            return dataFromStream(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            LogUtils.w(e, "Failed to load dataFromFile %s", file);
        }

        return null;
    }


    /**
     * Convert input stream data to byte[] data.
     *
     * @param input
     * @return byte[]
     */
    public static byte[] dataFromStream( InputStream input) {
        if (input == null)
            return EMPTY_BYTES;
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        byte[] buf = acquireBytes(1024);
        int len = -1;
        try {
            while ((len = input.read(buf)) != -1) {
                output.write(buf, 0, len);
            }
            return output.toByteArray();
        } catch (IOException e) {
            LogUtils.w(e, "Failed to load dataFromStream");
        } finally {
            close(input);
            releaseBytes(buf);
        }
        return EMPTY_BYTES;
    }

    /**
     * Write data from ByteBuffer to file.
     *
     * @param file
     * @param buffer
     * @return boolean
     */
    public static boolean writeBufferToFile(@NonNull File file, @NonNull ByteBuffer buffer) {
        if (file == null)
            return false;
        mkdirs(file);
        FileChannel channel = null;
        try {
            channel = new FileOutputStream(file).getChannel();
            channel.write(buffer);
            return true;
        } catch (IOException e) {
            LogUtils.e(e, "Failed to write buffer %s", file);
            return false;
        } finally {
            close(channel);
        }
    }

    /**
     * Write streams data to a file.
     *
     * @param file
     * @param input
     * @return
     */
    public static boolean writeStreamToFile(File file, @NonNull InputStream input) {
        if (file == null || input == null)
            return false;
        mkdirs(file);

        OutputStream outputStream = null;
        byte[] buf = acquireBytes(1024);
        try {

            int len = -1;
            outputStream = new FileOutputStream(file);
            while ((len = input.read(buf)) != -1)
                outputStream.write(buf, 0, len);
            return true;
        } catch (IOException e) {
            LogUtils.e(e, "Failed to write stream %s", file);
            return false;
        } finally {
            close(outputStream);
            close(input);
            releaseBytes(buf);
        }
    }
}
