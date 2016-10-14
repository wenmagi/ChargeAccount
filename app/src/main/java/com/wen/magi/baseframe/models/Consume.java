package com.wen.magi.baseframe.models;

import com.wen.magi.baseframe.utils.LangUtils;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

import java.util.Date;

/**
 * @author zhangzhaowen @ Zhihu Inc.
 * @since 10-13-2016
 */
@Entity
public class Consume implements Comparable<Consume> {
    @Id
    private Long _id;
    private Long consumeID;
    private String title;
    private String desc;
    private Long consumeNum;
    private Date consumeDate;
    private Date dtStart, dtEnd;
    private String location;
    private String picture;
    //是否重复
    private String repeatType;

    @Generated(hash = 1812487999)
    public Consume(Long _id, Long consumeID, String title, String desc,
            Long consumeNum, Date consumeDate, Date dtStart, Date dtEnd,
            String location, String picture, String repeatType) {
        this._id = _id;
        this.consumeID = consumeID;
        this.title = title;
        this.desc = desc;
        this.consumeNum = consumeNum;
        this.consumeDate = consumeDate;
        this.dtStart = dtStart;
        this.dtEnd = dtEnd;
        this.location = location;
        this.picture = picture;
        this.repeatType = repeatType;
    }

    @Generated(hash = 836215753)
    public Consume() {
    }

    @Override
    public int compareTo(Consume another) {
        if (another == null)
            return 1;

        return consumeDate.compareTo(another.consumeDate);
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

    public Date getConsumeDate() {
        return this.consumeDate;
    }

    public void setConsumeDate(Date consumeDate) {
        this.consumeDate = consumeDate;
    }

    public Long getConsumeNum() {
        return this.consumeNum;
    }

    public void setConsumeNum(Long consumeNum) {
        this.consumeNum = consumeNum;
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

    public Long getConsumeID() {
        return this.consumeID;
    }

    public void setConsumeID(Long consumeID) {
        this.consumeID = consumeID;
    }

    public Long get_id() {
        return this._id;
    }

    public void set_id(Long _id) {
        this._id = _id;
    }
}
