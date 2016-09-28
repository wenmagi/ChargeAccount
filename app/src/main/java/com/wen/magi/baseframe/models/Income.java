package com.wen.magi.baseframe.models;

import com.wen.magi.baseframe.utils.LangUtils;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;

import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by MVEN on 16/9/20.
 * <p/>
 * email: magiwen@126.com.
 */

@Entity
public class Income implements Comparable<Income> {

    @Id(autoincrement = true)
    private Long _id;
    private Long incomeID;
    private String title;
    private String desc;
    private Long incomeNum;
    private Date incomeDate;
    private Date dtStart, dtEnd;
    private String location;
    private String picture;

    private String repeatType;

    @Generated(hash = 66873794)
    public Income(Long _id, Long incomeID, String title, String desc,
            Long incomeNum, Date incomeDate, Date dtStart, Date dtEnd,
            String location, String picture, String repeatType) {
        this._id = _id;
        this.incomeID = incomeID;
        this.title = title;
        this.desc = desc;
        this.incomeNum = incomeNum;
        this.incomeDate = incomeDate;
        this.dtStart = dtStart;
        this.dtEnd = dtEnd;
        this.location = location;
        this.picture = picture;
        this.repeatType = repeatType;
    }

    @Generated(hash = 1009272208)
    public Income() {
    }

    @Override
    public int compareTo(Income another) {
        if (another == null)
            return 1;

        return incomeDate.compareTo(another.incomeDate);
    }

    public String getRepeatType() {
        return repeatType;
    }

    public void setRepeatType(String repeatType) {
        this.repeatType = repeatType;
    }

    public boolean isRepeat() {
        return LangUtils.isNotEmpty(repeatType);
    }

    public Date getDtStart() {
        return dtStart;
    }

    public void setDtStart(Date dtStart) {
        this.dtStart = dtStart;
    }

    public Date getDtEnd() {
        return dtEnd;
    }

    public void setDtEnd(Date dtEnd) {
        this.dtEnd = dtEnd;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public Date getIncomeDate() {
        return this.incomeDate;
    }

    public void setIncomeDate(Date incomeDate) {
        this.incomeDate = incomeDate;
    }

    public Long getIncomeNum() {
        return this.incomeNum;
    }

    public void setIncomeNum(Long incomeNum) {
        this.incomeNum = incomeNum;
    }

    public String getDesc() {
        return this.desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getIncomeID() {
        return this.incomeID;
    }

    public void setIncomeID(Long incomeID) {
        this.incomeID = incomeID;
    }

    public Long get_id() {
        return this._id;
    }

    public void set_id(Long _id) {
        this._id = _id;
    }

}
