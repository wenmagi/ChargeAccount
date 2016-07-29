package com.wen.magi.baseframe.models;

import java.util.Date;

/**
 * Created by MVEN on 16/6/18.
 * <p/>
 * email: magiwen@126.com.
 */


public class AppUser {

    /*用户ID*/
    public long userID;

    /*用户真实姓名*/
    public String userName;

    /*用户昵称*/
    public String nickName;

    /*个性签名*/
    public String userSign;

    /*用户手机号*/
    public String phoneNum;

    /*用户邮箱*/
    public String userEmail;

    /*用户头像url*/
    public String logoUrl;

    /*注册日期*/
    public Date registerDate;

    /*设备ID*/
    public String[] deviceIDs;

    /**************
     * 用户录入信息 *
     **************/

    /*
    * 用户性别 0:unkown 1:man 2:women
    */
    public int userGender;

    /**
     * 用户年龄段 90:90后  80:80后
     */
    public int userAge;

    /**
     * 用户性格标签
     */
    public int[] userNature;

    /**
     * 用户星座（用来分析用户性格以及兴趣）
     */
    public int userStarSign;

    /**************
     * 兴趣分析信息 *
     **************/

    /*
    * 通过分析用户兴趣点后，得出的性别 0:unkown 1:man 2:women
    */
    public int gender;

    /**
     * 通过分析用户兴趣点后，得出的年龄段 90:90后  80:80后
     */
    public int age;

    /**
     * 通过分析用户兴趣点后，得出的性格标签
     */
    public int[] nature;

    /**
     * 通过分析用户兴趣点后，得出的星座（用来分析用户性格以及兴趣）
     */
    public int starSign;

}
