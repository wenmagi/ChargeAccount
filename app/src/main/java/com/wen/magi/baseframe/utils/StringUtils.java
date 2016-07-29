package com.wen.magi.baseframe.utils;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Formatter;
import java.util.Random;

/**
 * Created by MVEN on 16/6/16.
 * <p/>
 * email: magiwen@126.com.
 */


public class StringUtils {

    public static final int TYPE_NUMBER = 0;
    public static final int TYPE_ENGLISH = 1;
    public static final int TYPE_FUHAO = 2;
    public static final int TYPE_CHINA = 3;

    /**
     * 替换string中的指定字符串
     *
     * @param strSearch
     * @param replacement
     * @param body
     * @return String
     */
    public static String replaceAllByStringBuffer(String strSearch, String replacement, String body) {
        StringBuffer sbf = new StringBuffer(body);
        int index = 0;
        int offset = 0;
        do {
            index = sbf.indexOf(strSearch, offset);
            if (index > -1) {
                sbf.replace(index, index + strSearch.length(), replacement);
                /**
                 * 下一次开始的点是index加上置换后的字符串的长度
                 */
                offset = index + replacement.length();
            }
        } while (index > -1);

        return sbf.toString();
    }

    /**
     * 判断字节数 汉字2个字节英文1个字节
     *
     * @param content
     * @return int
     */
    public static int getLengths(String content) {
        int count = 0;
        for (int i = 0; i < content.length(); i++) {
            if (sepMarkNot(content.charAt(i)) == TYPE_CHINA) {
                count = count + 2;
            } else {
                count = count + 1;
            }
        }
        return count;
    }

    /**
     * 判断 char c 是汉字还是数字 还是字母
     *
     * @param c
     * @return int
     */
    public static int sepMarkNot(char c) {
        // 数字 48-57
        if (c > 47 && c < 58) {
            return TYPE_NUMBER;
        }
        // 大写字母 65-90
        if (c > 64 && c < 91) {
            return TYPE_ENGLISH;
        }
        // 小写字母 97-122
        if (c > 96 && c < 122) {
            return TYPE_ENGLISH;
        }
        // 汉字（简体）
        if (c >= 0x4e00 && c <= 0x9fbb) {
            return TYPE_CHINA;
        }
        return TYPE_FUHAO;
    }

    /**
     * String与Date之间的转换
     *
     * @param formater
     * @param date
     * @return
     */
    @SuppressLint("SimpleDateFormat")
    public static String date2String(String formater, Date date) {
        if (LangUtils.isEmpty(formater))
            return null;
        if (date == null)
            return null;
        return (new SimpleDateFormat(formater)).format(date);
    }

    @SuppressLint("SimpleDateFormat")
    public static Date string2Date(String formater, String dateStr) {
        if (LangUtils.isEmpty(formater) || LangUtils.isEmpty(dateStr))
            return null;
        try {
            return (new SimpleDateFormat(formater)).parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static final String[] s_uid2445_char_table = {"a", "b", "c", "d",
            "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q",
            "r", "s", "t", "u", "v", "w", "x", "y", "z", "0", "1", "2", "3",
            "4", "5", "6", "7", "8", "9"};
    public static final int PPY_UID2445_LENGTH = 26;

    /**
     * generate uid 2445 of calendar
     *
     * @return uid2445
     */
    public static String generateUID2445() {
        StringBuilder result = new StringBuilder(PPY_UID2445_LENGTH);
        int table_length = s_uid2445_char_table.length;// remove last two char
        Random random = new Random();
        for (int i = 0; i < PPY_UID2445_LENGTH; i++) {
            result.append(String.format(
                    "%s",
                    s_uid2445_char_table[Math.abs(random.nextInt()
                            % table_length)]));
        }
        return result.toString();
    }

    /**
     * 获取随机长度的字符串
     *
     * @param len length of the String
     * @return randomString
     */
    public static String getRandomString(int len) {
        if (len <= 0)
            return "";
        Random random = new Random();
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < len; i++) {
            int randomIndex = random.nextInt(s_uid2445_char_table.length);
            stringBuffer.append(s_uid2445_char_table[randomIndex]);
        }
        return stringBuffer.toString();
    }


    /**
     * Get UTF8 format String from a byte[] data based on default value.
     *
     * @param data
     * @param defaultValue
     * @return String
     */
    public static String utf8String(byte[] data, String defaultValue) {
        if (data == null)
            return defaultValue;

        try {
            int index = 0;
            if (data.length >= 3 && data[0] == (byte) 0xEF && data[1] == (byte) 0xBB
                    && data[2] == (byte) 0xBF)
                index = 3;

            return new String(data, index, data.length - index, "UTF-8");
        } catch (Exception e) {
            LogUtils.e(e, "Failed to get utf8String");
        }

        return defaultValue;
    }

    /**
     * Calculate the MD5 code of specific content.
     *
     * @param content
     * @return String
     */

    public static String md5(String content) {
        return md5(LangUtils.getBytes(content));
    }

    @NonNull
    public static String md5(byte[] defaultBytes) {
        try {
            MessageDigest algorithm = MessageDigest.getInstance("MD5"); //$NON-NLS-1$
            algorithm.reset();
            algorithm.update(defaultBytes);
            byte messageDigest[] = algorithm.digest();

            StringBuilder hexString = new StringBuilder();
            for (int i = 0; i < messageDigest.length; i++) {
                String hex = Integer.toHexString(0xFF & messageDigest[i]);
                if (hex.length() > 1)
                    hexString.append(hex);
                else
                    hexString.append('0').append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            LogUtils.w(e, "Failed to compute md5"); //$NON-NLS-1$
        }

        return null;
    }

    /**
     * String format多个参数
     *
     * @param format
     * @param args
     * @return String
     */
    @NonNull
    public static String format(String format, Object... args) {
        try {
            StringBuilder buffer = new StringBuilder(256);
            Formatter f = new Formatter(buffer);
            f.format(format, args);
            return f.toString();
        } catch (Exception e) {
            return format;
        }
    }
}
