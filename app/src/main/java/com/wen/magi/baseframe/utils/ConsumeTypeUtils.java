package com.wen.magi.baseframe.utils;

import android.content.res.Resources;

import com.alibaba.fastjson.JSONObject;

import org.apache.http.util.EncodingUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zhangzhaowen @ Zhihu Inc.
 * @since 10-14-2016
 */

public class ConsumeTypeUtils {

    public static final String ENCODING = "UTF-8";

    public enum ConsumeType {
        TRAFFIC,//交通相关
        DINING,//餐饮
        CLOTH, //衣服美妆
        FAMILY,//家庭物业
        DAILY_CHARGE,//日常缴费
        CARS_ABOUT,//汽车相关
        ENTERTAINMENT,//休闲娱乐
        ELECTRONIC,//电子器具
        TRIP_VACATION,//旅游度假
        PETS,//宠物宝贝
        GIFTS,//礼品购买
        MEDICAL_TREATMENT//医疗保健
    }

    public List<String> getItemsByConsumeType(ConsumeType type) {
        return new ArrayList<>();
    }

    public static JSONObject initFromAssets(Resources resources) {
        String result = "";
        try {
            InputStream in = resources.getAssets().open("base_consume_types.txt");
            int length = in.available();
            byte[] buffer = new byte[length];
            in.read(buffer);
            result = EncodingUtils.getString(buffer, ENCODING);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (LangUtils.isEmpty(result))
            return null;

        JSONObject jsonObject = WebUtils.parseJsonObject(result);

        return jsonObject;
    }

}
