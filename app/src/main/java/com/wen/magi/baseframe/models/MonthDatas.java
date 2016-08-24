package com.wen.magi.baseframe.models;

import java.io.Serializable;

/**
 * Created by MVEN on 16/8/4.
 * <p/>
 * email: magiwen@126.com.
 */


public class MonthDatas implements Serializable{
    private static final long serialVersionUID = -1315982192469819046L;
    /**
     * MonthView每个cell的宽度
     */
    public int cellWidth;

    /**
     * MonthView每个cell的高度
     */
    public int cellHeight;

    /**
     * MonthView开始日期
     */
    public int startDayOfMonth;

    public int position;
}
