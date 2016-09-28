package com.wen.magi.baseframe.models;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

import java.util.Date;
import org.greenrobot.greendao.annotation.Generated;

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
    @Generated(hash = 1386151032)
    public Income(Long _id, Long incomeID, String title, String desc,
            Long incomeNum, Date incomeDate) {
        this._id = _id;
        this.incomeID = incomeID;
        this.title = title;
        this.desc = desc;
        this.incomeNum = incomeNum;
        this.incomeDate = incomeDate;
    }
    @Generated(hash = 1009272208)
    public Income() {
    }




}
