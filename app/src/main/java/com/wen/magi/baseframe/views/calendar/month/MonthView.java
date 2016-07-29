package com.wen.magi.baseframe.views.calendar.month;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.view.View;

import com.wen.magi.baseframe.utils.Constants;
import com.wen.magi.baseframe.utils.LangUtils;
import com.wen.magi.baseframe.utils.LogUtils;
import com.wen.magi.baseframe.utils.date.CalendarHelper;
import com.wen.magi.baseframe.utils.date.DateTime;
import com.wen.magi.baseframe.views.calendar.CalendarCellView;

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
    protected DateTime mCurrentDateTime;
    protected DateTime mToday;
    private DateTime mSelectedTime;
    protected int mStartDayOfWeek;

    private int mCellWidth = 0, mCellHeight = 0;
    private CalendarCellView[][] mCells = null;
    private int mPosition = Constants.MAX_MONTH_SCROLL_COUNT / 2;

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
        this.setBackground(d);
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

        initCells();
        updateCells(CalendarHelper.getSelectedDay(), true);
    }

    public void initCells() {
        double density = getResources().getDisplayMetrics().density;
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
        this.mCells = new CalendarCellView[mDatetimeList.size() / 7][7];
        RectF bound = new RectF(0, 0, mCellWidth, mCellHeight);
        for (int week = 0; week < mCells.length; week++) {
            for (int day = 0; day < mCells[week].length; day++) {
                mCells[week][day] = new CalendarCellView(mDatetimeList.get(week * 7
                        + day), new RectF(bound),
                        CalendarHelper.getContentPaintBlack(),
                        CalendarHelper.getTipPaintGray(), density);
                bound.offset(mCellWidth, 0);
            }
            bound.offset(0, mCellHeight);
            bound.left = 0;
            bound.right = mCellWidth;
        }
    }

    public void updateCells(DateTime selectedTime, boolean bForceUpdate) {
        if (this.mCells == null
                || (!bForceUpdate && selectedTime.equals(mSelectedTime))) {
            LogUtils.d("updateCells dddddddd");
            return;
        }

        mSelectedTime = selectedTime;
        for (int week = 0; week < mCells.length; week++) {
            for (int day = 0; day < mCells[week].length; day++) {

                DateTime dateTime = mCells[week][day].getDate();
                if (dateTime.getWeekDay() == Calendar.SATURDAY
                        || dateTime.getWeekDay() == Calendar.SUNDAY)
                    mCells[week][day].setContentPaint(CalendarHelper
                            .getContentPaintGreen());
                else
                    mCells[week][day].setContentPaint(CalendarHelper
                            .getContentPaintBlack());

                if (dateTime.getMonth() == mCurrentDateTime.getMonth())
                    mCells[week][day].setActiveMonth(true);
                else {
                    mCells[week][day].setActiveMonth(false);
                    mCells[week][day].setContentPaint(CalendarHelper
                            .getContentPaintGray());
                }

                if (dateTime.equals(getToday())) {
                    mCells[week][day].setToday(true);
                    mCells[week][day].setTodayCirclePaint(CalendarHelper
                            .getTodayCirclePaint());
                } else {
                    mCells[week][day].setToday(false);
                    mCells[week][day].setTodayCirclePaint(null);
                }

                mCells[week][day].setDuty(false);
                mCells[week][day].setBitmapWork(null);

                if ((mCurrentDateTime.getMonth() == mSelectedTime.getMonth() && dateTime
                        .equals(mSelectedTime))
                        || (mCurrentDateTime.getMonth() != mSelectedTime
                        .getMonth() && dateTime
                        .equals(mCurrentDateTime))) {
                    mCells[week][day].setSelected(true);
                    mCells[week][day].setContentPaint(CalendarHelper
                            .getContentPaintWhite());
                    mCells[week][day].setTipPaint(CalendarHelper
                            .getTipPaintWhite());
                    mCells[week][day].setSelectedCirclePaint(CalendarHelper
                            .getSelectedCirclePaint());
                } else {
                    mCells[week][day].setSelected(false);
                    mCells[week][day].setSelectedCirclePaint(null);
                }

                if (dateTime.getMonth() != mCurrentDateTime.getMonth()) {
                    mCells[week][day].setEventCount(0);
                } else {
                    // TODO 获取事件个数
                    int eventsCount = 0;
                    mCells[week][day].setEventCount(eventsCount);
                    if (eventsCount > 0) {
                        if (mCells[week][day].isSelected()) {
                            mCells[week][day]
                                    .setHasEventsCirclePaint(CalendarHelper
                                            .getHasEventsCirclePaintWhite());
                        } else {
                            mCells[week][day]
                                    .setHasEventsCirclePaint(CalendarHelper
                                            .getHasEventsCirclePaintGreen());
                        }
                    } else {
                        mCells[week][day].setEventCount(0);
                    }
                }
            }
        }
        invalidate();
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
        // TODO Auto-generated method stub

        heightMeasureSpec = MeasureSpec.makeMeasureSpec(mCellHeight * 6,
                MeasureSpec.EXACTLY);

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private float lastX, lastY, nowX, nowY;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        @SuppressWarnings("unused")
        boolean isMove = false;
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
                                if (day.hitCell((int) event.getX(),
                                        (int) event.getY())) {
                                    if (day.getDate().getMonth() == CalendarHelper
                                            .getSelectedDay().getMonth()) {
                                        CalendarHelper
                                                .setSelectedDay(day.getDate());
                                        updateCells(
                                                CalendarHelper.getSelectedDay(),
                                                true);
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
