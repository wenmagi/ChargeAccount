package com.wen.magi.baseframe.utils;

import android.content.res.Resources;
import android.support.annotation.IntDef;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wen.magi.baseframe.models.Consume;
import com.wen.magi.baseframe.web.WebActivity;

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

//    public enum ConsumeType {
//        TRAFFIC,//交通相关
//        DINING,//餐饮
//        CLOTH, //衣服美妆
//        FAMILY,//家庭物业
//        DAILY_CHARGE,//日常缴费
//        CARS_ABOUT,//汽车相关
//        ENTERTAINMENT,//休闲娱乐
//        ELECTRONIC,//电子器具
//        TRIP_VACATION,//旅游度假
//        PETS,//宠物宝贝
//        GIFTS,//礼品购买
//        MEDICAL_TREATMENT//医疗保健
//    }

    private static final int TRAFFIC = 0x001;
    private static final int DINING = 0x002;
    private static final int CLOTH = 0x003;
    private static final int FAMILY = 0x004;
    private static final int DAILY_CHARGE = 0x005;
    private static final int CARS_ABOUT = 0x006;
    private static final int ENTERTAINMENT = 0x007;
    private static final int ELECTRONIC = 0x008;
    private static final int TRIP_VACATION = 0x009;
    private static final int PETS = 0x010;
    private static final int GIFTS = 0x11;
    private static final int MEDICAL_TREATMENT = 0x012;

    @IntDef({TRAFFIC, DINING, CLOTH, FAMILY, DAILY_CHARGE, CARS_ABOUT, ENTERTAINMENT, ELECTRONIC, TRIP_VACATION, PETS, GIFTS, MEDICAL_TREATMENT})
    public @interface ConsumeType {

    }

    public List<Consume> getItemsByConsumeType(@ConsumeType int consumeType, JSONObject jsonObject) {

        if (LangUtils.isEmpty(jsonObject))
            return null;

        ArrayList<Consume> consumes = new ArrayList<>();
        String key = null;
        switch (consumeType) {
            case TRAFFIC:
                key = "行车交通";
                break;
            case DINING:
                key = "个人用餐";
                break;
            case CLOTH:
                key = "衣服美妆";
                break;
            case FAMILY:
                key = "家庭物业";
                break;
            case DAILY_CHARGE:
                key = "日常缴费";
                break;
            case CARS_ABOUT:
                key = "汽车相关";
                break;
            case ENTERTAINMENT:
                key = "休闲娱乐";
                break;
            case ELECTRONIC:
                key = "电子器具";
                break;
            case TRIP_VACATION:
                key = "旅游度假";
                break;
            case PETS:
                key = "宠物宝贝";
                break;
            case GIFTS:
                key = "礼品购买";
                break;
            case MEDICAL_TREATMENT:
                key = "医疗保健";
                break;
            default:
                key = null;
                break;
        }

        JSONArray array = WebUtils.getJsonArray(jsonObject, key);
        if (array == null)
            return null;
        int len = array.size();
        for (int i = 0; i < len; i++) {
            String itemName = WebUtils.getJsonString(array, i);
            Consume item = new Consume();
            item.setConsumeTitle(itemName);
            item.setConsumeType(consumeType);
            consumes.add(item);
        }
        return consumes;
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


        return WebUtils.parseJsonObject(result);
    }

}
