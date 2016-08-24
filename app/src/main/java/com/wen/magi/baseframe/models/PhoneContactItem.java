package com.wen.magi.baseframe.models;

import android.support.annotation.NonNull;

import com.alibaba.fastjson.JSONObject;
import com.wen.magi.baseframe.utils.WebUtils;

import java.io.Serializable;

/**
 * Created by MVEN on 16/8/23.
 * <p/>
 * email: magiwen@126.com.
 */


public class PhoneContactItem implements Comparable<PhoneContactItem>, Serializable {
    private static final long serialVersionUID = 5746359308193057338L;

    @Override
    public int compareTo(@NonNull PhoneContactItem another) {
        if (this.isUserOfXX) {
            if ((another).isUserOfXX) {
                if (this.isFollowed) {
                    if (!(another).isFollowed) {
                        return -1;
                    }
                } else {
                    if ((another).isFollowed) {
                        return 1;
                    }
                }
            } else {
                return -1;
            }
        } else {
            if ((another).isUserOfXX) {
                return 1;
            }
        }

        return 0;
    }

    public int uid = -1;
    public String strUserName;
    public String strNickerName;
    public boolean isFollowed = false;
    public String phoneNumber;
    public boolean isUserOfXX = false;

    public static PhoneContactItem fromJson(JSONObject jsonObject) {
        int uid = WebUtils.getJsonInt(jsonObject, "id", -1);
        String phoneNumber = WebUtils.getJsonString(jsonObject, "phone");
        String strNickerName = WebUtils.getJsonString(jsonObject, "nickname");

        PhoneContactItem gymItem = new PhoneContactItem();
        gymItem.uid = uid;
        gymItem.strNickerName = strNickerName;
        gymItem.phoneNumber = phoneNumber;
        gymItem.isUserOfXX = true;

        return gymItem;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o instanceof PhoneContactItem) {
            if (this.uid == ((PhoneContactItem) o).uid || this.strUserName.equals(((PhoneContactItem) o).strUserName)) {
                return true;
            }
        }
        return false;
    }
}
