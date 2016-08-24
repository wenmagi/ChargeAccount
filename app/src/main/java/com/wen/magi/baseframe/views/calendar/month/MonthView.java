package com.wen.magi.baseframe.views.calendar.month;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.view.View;

import com.wen.magi.baseframe.R;
import com.wen.magi.baseframe.managers.AppManager;
import com.wen.magi.baseframe.utils.Constants;
import com.wen.magi.baseframe.utils.LangUtils;
import com.wen.magi.baseframe.utils.StringUtils;
import com.wen.magi.baseframe.utils.date.CalendarHelper;
import com.wen.magi.baseframe.utils.date.DateTime;
import com.wen.magi.baseframe.utils.date.Lunar;
import com.wen.magi.baseframe.views.calendar.unit.CalendarCellView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MonthView extends View {

    public interface OnCellClickListener {
        void onCellClick(DateTime day);
    }

    private OnCellClickListener mOnCellClickListener = null;

    protected ArrayList<DateTime> mDatetimeList;
    protected Context mContext;
    //选中的日期
    protected DateTime mCurrentDateTime;
    //今天
    protected DateTime mToday;
    private DateTime mSelectedTime;
    protected int mStartDayOfWeek;

    private int mCellWidth = 0, mCellHeight = 0;
    private CalendarCellView[][] mCells = null;
    private int mPosition = Constants.MAX_MONTH_SCROLL_COUNT / 2;
    //判断日历是否分6行
    private boolean hasSixLine = false;

    public MonthView(Context context) {
        super(context);
    }

    public MonthView(Context context, int position, int width, int height,
                     int startDayOfWeek) {
        super(context);
        this.mPosition = position;
        this.mCellWidth = width;
        this.mCellHeight = height;
        this.mStartDayOfWeek = startDayOfWeek;
        this.mContext = context;

        initData();
    }

    public void setBackground(Drawable d) {
        super.setBackground(d);
    }

    public void doDestory() {
    }

    public ArrayList<DateTime> getDatetimeList() {
        return mDatetimeList;
    }

    private void initData() {
        Calendar firstDayOfTodayMonth = Calendar.getInstance();
        firstDayOfTodayMonth.set(Calendar.DAY_OF_MONTH, 1);

        Date beginOf = LangUtils
                .cc_dateByMovingToBeginningOfDay(firstDayOfTodayMonth.getTime());

        int offSet = mPosition - Constants.MAX_MONTH_SCROLL_COUNT / 2;

        if (offSet > 0)
            this.mCurrentDateTime = CalendarHelper.convertDateToDateTime(
                    beginOf).plus(0, offSet, 0, 0, 0, 0, 0,
                    DateTime.DayOverflow.LastDay);
        else if (offSet < 0)
            this.mCurrentDateTime = CalendarHelper.convertDateToDateTime(
                    beginOf).minus(0, -offSet, 0, 0, 0, 0, 0,
                    DateTime.DayOverflow.LastDay);
        else
            this.mCurrentDateTime = CalendarHelper
                    .convertDateToDateTime(beginOf);

        this.mDatetimeList = MonthViewCache.getInstance().getDateTimeList(
                mCurrentDateTime);
        if (mDatetimeList == null || mDatetimeList.size() < 28) {
            mDatetimeList = CalendarHelper.getFullWeeks(
                    mCurrentDateTime.getMonth(), mCurrentDateTime.getYear(),
                    mStartDayOfWeek, true);
            MonthViewCache.getInstance().putDateTimeList(mCurrentDateTime,
                    mDatetimeList);
        }

        if (mDatetimeList.get(5 * 7).getMonth() == mCurrentDateTime.getMonth()) {
            hasSixLine = true;
        }
        initCells();
        updateCells(CalendarHelper.getSelectedDay(), true);
    }

    public void initCells() {
        if (this.mCells != null) {
            for (CalendarCellView[] week : mCells) {
                for (CalendarCellView kdc : week) {
                    kdc.setBitmapRest(null);
                    kdc.setBitmapWork(null);
                    kdc.setContentPaint(null);
                    kdc.setHasEventsCirclePaint(null);
                    kdc.setSelectedCirclePaint(null);
                    kdc.setTipPaint(null);
                    kdc.setTodayCirclePaint(null);
                    kdc = null;
                }
            }
            mCells = null;
        }

        mCells = new CalendarCellView[mDatetimeList.size() / 7][7];

        RectF bound = new RectF(0, 0, mCellWidth, hasSixLine ? mCellHeight : mCellHeight * 6 / 5);

        for (int week = 0; week < mCells.length; week++) {
            for (int day = 0; day < mCells[week].length; day++) {

                if (!hasSixLine && week == 5)
                    break;

                mCells[week][day] = new CalendarCellView(mDatetimeList.get(week * 7
                        + day), new RectF(bound),
                        CalendarHelper.getContentPaintBlack(),
                        CalendarHelper.getTipPaintLunarDate());
                mCells[week][day].setNeedLargeForFiveLine(!hasSixLine);

                bound.offset(mCellWidth, 0);

            }

            bound.offset(0, hasSixLine ? mCellHeight : mCellHeight * 6 / 5);
            bound.left = 0;
            bound.right = mCellWidth;
        }
    }

    public void updateCells(DateTime selectedTime, boolean bForceUpdate) {
        if (this.mCells == null
                || (!bForceUpdate && selectedTime.equals(mSelectedTime))) {
            return;
        }
        mSelectedTime = selectedTime;

        for (int week = 0; week < mCells.length; week++) {
            for (int day = 0; day < mCells[week].length; day++) {

                if (mCells[week][day] == null)
                    continue;

                DateTime dateTime = mCells[week][day].getDate();

                updateTips(mCells[week][day], dateTime);

                updatePaintColor(mCells[week][day], dateTime);

                updateCellBg(mCells[week][day], dateTime);

                updateChooseState(mCells[week][day], dateTime);

                updateEventCount(mCells[week][day], dateTime);
            }
        }
        invalidate();
    }

    /**
     * 设置Cell是否有事件
     *
     * @param mCell
     * @param dateTime
     */
    private void updateEventCount(CalendarCellView mCell, DateTime dateTime) {
        if (dateTime.getMonth() != mCurrentDateTime.getMonth()) {
            mCell.setEventCount(0);
        } else {
            // TODO 获取事件个数
            int eventsCount = 0;
            mCell.setEventCount(eventsCount);
            if (eventsCount > 0) {
                if (mCell.isSelected()) {
                    mCell.setHasEventsCirclePaint(CalendarHelper
                            .getHasEventsCirclePaintWhite());
                } else {
                    mCell.setHasEventsCirclePaint(CalendarHelper
                            .getHasEventsCirclePaintGreen());
                }
            } else {
                mCell.setEventCount(0);
            }
        }
    }

    /**
     * 设置选选中状态
     *
     * @param mCell
     * @param dateTime
     */
    private void updateChooseState(CalendarCellView mCell, DateTime dateTime) {
        //设置选中和未选中的状态
        if ((mCurrentDateTime.getMonth() == mSelectedTime.getMonth() && dateTime
                .equals(mSelectedTime))
                || (mCurrentDateTime.getMonth() != mSelectedTime
                .getMonth() && dateTime
                .equals(mCurrentDateTime))) {
            mCell.setSelected(true);
            mCell.setContentPaint(CalendarHelper
                    .getContentPaintWhite());
            mCell.setTipPaint(mCell.isLunarHoliday() ? CalendarHelper.getTipPaintLunarHolidayWhite() : CalendarHelper.getTipPaintWhite());
            mCell.setSelectedCirclePaint(CalendarHelper
                    .getSelectedCirclePaint());
        } else {
            if (dateTime.getMonth() == mCurrentDateTime.getMonth()) {
                if (mCell.isLunarHoliday())
                    mCell.setTipPaint(CalendarHelper.getTipPaintLunarHolidayRed());
                else if (mCell.isSolarHoliday())
                    mCell.setTipPaint(CalendarHelper.getTipPaintSolarHoliday());
                else
                    mCell.setTipPaint(CalendarHelper.getTipPaintLunarDate());
            } else {
                mCell.setTipPaint(CalendarHelper
                        .getTipPaintGray());
            }
            mCell.setSelected(false);
            mCell.setSelectedCirclePaint(null);
        }
    }

    /**
     * 设置背景
     *
     * @param mCell
     * @param dateTime
     */
    private void updateCellBg(CalendarCellView mCell, DateTime dateTime) {
        //设置当天或他天背景圆圈
        if (dateTime.equals(getToday())) {
            mCell.setToday(true);
            mCell.setTodayCirclePaint(CalendarHelper
                    .getTodayCirclePaint());
        } else {
            mCell.setToday(false);
            mCell.setTodayCirclePaint(null);
        }

        mCell.setDuty(false);
        mCell.setBitmapWork(null);
    }

    /**
     * 设置文本颜色
     *
     * @param mCell
     * @param dateTime
     */
    private void updatePaintColor(CalendarCellView mCell, DateTime dateTime) {
        //设置双休和工作日文本颜色
        if (dateTime.getWeekDay() == Calendar.SATURDAY
                || dateTime.getWeekDay() == Calendar.SUNDAY)
            mCell.setContentPaint(CalendarHelper
                    .getContentPaintGreen());
        else
            mCell.setContentPaint(CalendarHelper
                    .getContentPaintBlack());
        //设置当月和他月文本颜色
        if (dateTime.getMonth() == mCurrentDateTime.getMonth()) {
            mCell.setActiveMonth(true);
        } else {
            mCell.setActiveMonth(false);
            mCell.setContentPaint(CalendarHelper
                    .getContentPaintGray());
        }
    }

    /**
     * 设置tips，包括节假日、农历
     *
     * @param mCell
     * @param dateTime
     */
    private void updateTips(CalendarCellView mCell, DateTime dateTime) {
        Lunar lunar = AppManager.lunar;
        lunar.setCalendar(dateTime.getDate());
        String lunarStr = lunar.getLunarDayString();
        String holiday = lunar.getChineseHoliday();
        String solarHoliday = lunar.getDateHoliday();
        String tips = null;
        if (LangUtils.isNotEmpty(holiday)) {
            mCell.setLunarHoliday(true);
            tips = holiday;
        } else if (LangUtils.isNotEmpty(solarHoliday)) {
            mCell.setSolarHoliday(true);
            tips = solarHoliday;
        } else {
            if (lunarStr != null && lunarStr.contains(StringUtils.getString(R.string.first_day_of_lunar_month))) {
                tips = lunar.getLunarMonthString();
            } else {
                tips = lunarStr;
            }
            mCell.setLunarHoliday(false);
            mCell.setSolarHoliday(false);
        }
        if (tips != null)
            mCell.setStrTip(tips);
    }

    public void updateToday() {
        mToday = CalendarHelper.convertDateToDateTime(new Date());
    }

    protected DateTime getToday() {
        if (mToday == null) {
            mToday = CalendarHelper.convertDateToDateTime(new Date());
        }
        return mToday;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mCells != null) {
            for (CalendarCellView[] week : mCells) {
                for (CalendarCellView day : week) {

                    if (day == null)
                        continue;

                    day.draw(canvas);
                }
            }
        }
    }

    @Override
    public void onLayout(boolean changed, int left, int top, int right,
                         int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(mCellHeight * 6,
                MeasureSpec.EXACTLY);

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private float lastX, lastY, nowX, nowY;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean isHandled = false;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastX = nowX = event.getX();
                lastY = nowY = event.getY();
                isHandled = true;
                break;
            case MotionEvent.ACTION_CANCEL:
                isHandled = false;
                break;
            case MotionEvent.ACTION_MOVE:
                nowX = event.getX();
                nowY = event.getY();

                isHandled = false;
                break;
            case MotionEvent.ACTION_UP:
                nowX = event.getX();
                nowY = event.getY();
                if (Math.abs(nowX - lastX) < 100 && Math.abs(nowY - lastY) < 100) {
                    if (mOnCellClickListener != null) {
                        for (CalendarCellView[] week : mCells) {
                            for (CalendarCellView day : week) {
                                if (day == null)
                                    continue;
                                if (day.hitCell((int) event.getX(),
                                        (int) event.getY())) {

                                    if (day.getDate().getMonth() == CalendarHelper
                                            .getSelectedDay().getMonth()) {
                                        CalendarHelper.setSelectedDay(day.getDate());
                                        updateCells(CalendarHelper.getSelectedDay(), true);
                                    }

                                    mOnCellClickListener.onCellClick(day.getDate());
                                }
                            }
                        }
                    }
                    return true;
                }
                break;
            default:
                break;
        }
        return isHandled;
    }

    public OnCellClickListener getOnCellClickListener() {
        return mOnCellClickListener;
    }

    public void setOnCellClickListener(OnCellClickListener mOnCellClickListener) {
        this.mOnCellClickListener = mOnCellClickListener;
    }
}
