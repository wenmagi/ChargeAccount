package com.wen.magi.baseframe.views.calendar.unit;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;

import com.wen.magi.baseframe.utils.LangUtils;
import com.wen.magi.baseframe.utils.LogUtils;
import com.wen.magi.baseframe.utils.SysUtils;
import com.wen.magi.baseframe.utils.ViewUtils;
import com.wen.magi.baseframe.utils.date.DateTime;

/**
 * Created by MVEN on 16/7/29.
 * <p/>
 * email: magiwen@126.com.
 */


public class CalendarCellView {

    private RectF mBound = null;
    private DateTime mDate = null;

    private boolean isCurrentMonth = false;
    private boolean isSelected = false;
    private String strTip = "";
    private boolean isDuty = false;
    private boolean isRest = false;
    private boolean isToday = false;
    private boolean isLunarHoliday = false;
    private boolean isSolarHoliday = false;

    private double mScreenDensity = 0;

    private int mEventCount = -1;

    private Paint mContentPaint = null;
    private Paint mTipPaint = null;
    private Paint mSelectedCirclePaint = null;
    private Paint mTodayCirclePaint = null;
    private Paint mHasEventsCirclePaint = null;

    private Bitmap mBitmapRest = null;
    private Bitmap mBitmapWork = null;

    public CalendarCellView(DateTime date, RectF rect, Paint contentPaint,
                            Paint tipPaint) {
        mBound = rect;
        setContentPaint(contentPaint);
        setTipPaint(tipPaint);
        setDate(date);
    }

    public void draw(Canvas canvas) {
        drawCellBg(canvas);

        float dayBottom = drawSolarNum(canvas);

        drawBottomDesc(canvas, dayBottom);

        drawEventCircle(canvas, dayBottom);

        try {
            //假期图标的绘制
            if (isDuty && mBitmapWork != null && !mBitmapWork.isRecycled()) {
                Rect sourceRect = new Rect(0, 0, mBitmapWork.getWidth(),
                        mBitmapWork.getHeight());
                RectF dstRect = new RectF(mBound.right - mBitmapWork.getWidth()
                        , mBound.top, mBound.right, mBound.top + mBitmapWork.getHeight());

                if (mBitmapWork != null && !mBitmapWork.isRecycled())
                    canvas.drawBitmap(mBitmapWork, sourceRect, dstRect, null);

            }
            //休息日图标的绘制
            if (isRest && mBitmapRest != null && !mBitmapRest.isRecycled()) {
                Rect sourceRect = new Rect(0, 0, mBitmapRest.getWidth(),
                        mBitmapRest.getHeight());
                RectF dstRect = new RectF(
                        mBound.right - mBitmapRest.getWidth(), mBound.top,
                        mBound.right, mBound.top + mBitmapRest.getHeight());
                if (mBitmapRest != null && !mBitmapRest.isRecycled())
                    canvas.drawBitmap(mBitmapRest, sourceRect, dstRect, null);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 绘制每个Cell的背景，当前定位圆圈
     *
     * @param canvas 画布
     */
    private void drawCellBg(Canvas canvas) {
        //没选中的今天cell背景
        float centerX = mBound.centerX();
        float padding = ViewUtils.dp2pix(1);
        float edge = mBound.width() < mBound.height() ? mBound.width() : mBound.height();
        float radius = edge / 2 - padding;
        if (isToday && isCurrentMonth && !isSelected) {
            canvas.drawCircle(centerX, mBound.centerY(),
                    radius,
                    mTodayCirclePaint);
        }
        //已选中的cell背景
        if (isSelected) {
            canvas.drawCircle(centerX, mBound.centerY(),
                    radius,
                    mSelectedCirclePaint);
        }
    }

    /**
     * 绘制事件存在时的小圆点
     *
     * @param canvas    画布
     * @param dayBottom 文字底部的y坐标
     */
    private void drawEventCircle(Canvas canvas, float dayBottom) {
        if (!isCurrentMonth || mEventCount <= 0)
            return;

        int eventRadius = ViewUtils.dp2pix(2);
        int marginY = ViewUtils.dp2pix(2);
        if (SysUtils.DENSITY >= 1.5 && SysUtils.DENSITY < 2.0)// hdpi
            marginY = 0;
        canvas.drawCircle(mBound.centerX(), dayBottom
                        - ViewUtils.getTextHeight(mContentPaint, "0") - eventRadius - marginY, eventRadius,
                mHasEventsCirclePaint);
    }

    /**
     * 绘制阳历日期底部的文字，没有节日或事件，默认阴历
     *
     * @param canvas    画布
     * @param dayBottom 文字底部的y坐标
     */
    private void drawBottomDesc(Canvas canvas, float dayBottom) {
        if (!isCurrentMonth)
            return;

        //绘制节日
        String strTip = getStrTip();
        if (LangUtils.isNotEmpty(strTip) && strTip.length() > 3) {
            strTip = strTip.substring(0, 3);
        }

        float tipLen = mTipPaint.measureText(strTip, 0, strTip.length());
        float tipLeft = mBound.left + (mBound.width() - tipLen) / 2.0f;
        float tipBottom = dayBottom + ViewUtils.getTextHeight(mTipPaint, strTip);
        canvas.drawText(strTip,
                tipLeft,
                tipBottom, mTipPaint);
    }

    /**
     * 绘制阳历数字
     *
     * @param canvas 画布
     * @return 文字底部的y坐标
     */
    private float drawSolarNum(Canvas canvas) {
        String strDay = String.valueOf(mDate.getDay());
        //绘制今天日期
        RectF bounds = new RectF(mBound);
        bounds.right = mContentPaint.measureText(strDay, 0, strDay.length());
        bounds.bottom = mContentPaint.descent() - mContentPaint.ascent();
        bounds.left += (mBound.width() - bounds.right) / 2.0f;
        bounds.top += (mBound.height() - bounds.bottom) / 2.0f;

        float dayBottom = bounds.top - mContentPaint.ascent()
                + ViewUtils.dp2pix(-15);
        if (SysUtils.DENSITY >= 1.5 && SysUtils.DENSITY < 2.0)// hdpi
            dayBottom = bounds.top - mContentPaint.ascent()
                    + ViewUtils.dp2pix(-10);
        canvas.drawText(strDay,
                bounds.left,
                dayBottom, mContentPaint);
        return dayBottom;
    }

    public boolean hitCell(int x, int y) {
        return mBound.contains(x, y);
    }

    public boolean isCurrentMonth() {
        return isCurrentMonth;
    }

    public void setActiveMonth(boolean isCurrentMonth) {
        this.isCurrentMonth = isCurrentMonth;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public String getStrTip() {
        return strTip;
    }

    public void setStrTip(String strTip) {
        this.strTip = strTip;
    }

    public boolean isToday() {
        return isToday;
    }

    public void setToday(boolean isToday) {
        this.isToday = isToday;
    }

    public DateTime getDate() {
        return mDate;
    }

    public void setDate(DateTime mDate) {
        this.mDate = mDate;
    }

    public boolean isDuty() {
        return isDuty;
    }

    public void setDuty(boolean isDuty) {
        this.isDuty = isDuty;
    }

    public boolean isRest() {
        return isRest;
    }

    public void setRest(boolean isRest) {
        this.isRest = isRest;
    }

    public Paint getTipPaint() {
        return mTipPaint;
    }

    public void setTipPaint(Paint mTipPaint) {
        this.mTipPaint = mTipPaint;
    }

    public Paint getContentPaint() {
        return mContentPaint;
    }

    public void setContentPaint(Paint mContentPaint) {
        this.mContentPaint = mContentPaint;
    }

    public Paint getSelectedCirclePaint() {
        return mSelectedCirclePaint;
    }

    public void setSelectedCirclePaint(Paint mSelectedCirclePaint) {
        this.mSelectedCirclePaint = mSelectedCirclePaint;
    }

    public Paint getTodayCirclePaint() {
        return mTodayCirclePaint;
    }

    public void setTodayCirclePaint(Paint mTodayCirclePaint) {
        this.mTodayCirclePaint = mTodayCirclePaint;
    }

    public int getEventCount() {
        return mEventCount;
    }

    public void setEventCount(int mEventCount) {
        this.mEventCount = mEventCount;
    }

    public Paint getHasEventsCirclePaint() {
        return mHasEventsCirclePaint;
    }

    public void setHasEventsCirclePaint(Paint mHasEventsCirclePaint) {
        this.mHasEventsCirclePaint = mHasEventsCirclePaint;
    }

    public Bitmap getBitmapRest() {
        return mBitmapRest;
    }

    public void setBitmapRest(Bitmap mBitmapRest) {
        this.mBitmapRest = mBitmapRest;
    }

    public Bitmap getBitmapWork() {
        return mBitmapWork;
    }

    public void setBitmapWork(Bitmap mBitmapWork) {
        this.mBitmapWork = mBitmapWork;
    }

    public void setLunarHoliday(boolean isLunarHoliday) {
        this.isLunarHoliday = isLunarHoliday;
    }

    public void setSolarHoliday(boolean isSolarHoliday) {
        this.isSolarHoliday = isSolarHoliday;
    }

    public boolean isLunarHoliday() {
        return isLunarHoliday;
    }

    public boolean isSolarHoliday() {
        return isSolarHoliday;
    }
}
