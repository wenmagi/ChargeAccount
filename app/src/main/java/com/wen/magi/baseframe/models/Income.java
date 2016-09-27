package com.wen.magi.baseframe.models;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

import java.util.Date;

/**
 * Created by MVEN on 16/9/20.
 * <p/>
 * email: magiwen@126.com.
 */

@Entity
public class Income {

    @Id(autoincrement = true)
    private Long _id;
    private Long incomeID;
    private String title;
    private String desc;
    private Long incomeNum;
    private Date incomeDate;




}
