package com.wen.magi.baseframe.views.calendar.week;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.view.MotionEvent;
import android.view.View;

import com.wen.magi.baseframe.utils.Constants;
import com.wen.magi.baseframe.utils.LangUtils;
import com.wen.magi.baseframe.utils.LogUtils;
import com.wen.magi.baseframe.utils.date.CalendarHelper;
import com.wen.magi.baseframe.utils.date.DateTime;
import com.wen.magi.baseframe.views.calendar.unit.CalendarCellView;


public class WeekView extends View {

    public interface OnCellClickListener {
        void onCellClick(DateTime day);
    }

    private OnCellClickListener mOnCellClickListener = null;

    private double density;
    protected Timer mRefreshTipTimer = null;
    protected ArrayList<DateTime> mDatetimeList;
    protected Context mContext;
    protected DateTime mCurrentDateTime;
    protected DateTime mToday;
    private DateTime mSelectedTime;
    protected int mStartDayOfWeek;

    private int mCellWidth = 0, mCellHeight = 0;
    private CalendarCellView[][] mCells = null;
    private int mPosition = Constants.MAX_WEEK_SCROLL_COUNT / 2;

    public WeekView(Context context) {
        super(context);
    }

    public WeekView(Context context, int position, int width, int height,
                    int startDayOfWeek) {
        super(context);
        this.mPosition = position;
        this.mCellWidth = width;
        this.mCellHeight = height;
        this.mStartDayOfWeek = startDayOfWeek;
        this.mContext = context;
        this.density = getResources().getDisplayMetrics().density;
        initData();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(mCellHeight,
                MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void doDestory() {
        clearTimer();
        WeekViewCache.getInstance().clearCache();
    }

    public void clearTimer() {
        if (mRefreshTipTimer != null) {
            mRefreshTipTimer.cancel();
            mRefreshTipTimer = null;
        }
    }

    public ArrayList<DateTime> getDatetimeList() {
        return mDatetimeList;
    }

    public void initData() {

        DateTime firstDayOfTodayWeek = CalendarHelper.getFirstDateOfThisWeek(
                getToday(), mStartDayOfWeek);

        Date beginOf = LangUtils.cc_dateByMovingToBeginningOfDay(CalendarHelper
                .convertDateTimeToDate(firstDayOfTodayWeek));

        firstDayOfTodayWeek = CalendarHelper.convertDateToDateTime(beginOf);

        int offSet = mPosition - Constants.MAX_WEEK_SCROLL_COUNT / 2;

        this.mCurrentDateTime = firstDayOfTodayWeek.plusDays(7 * offSet);

        this.mDatetimeList = WeekViewCache.getInstance().getDateTimeList(
                mCurrentDateTime);
        if (mDatetimeList == null || mDatetimeList.size() < 7) {
            mDatetimeList = CalendarHelper.getThisWeek(this.mCurrentDateTime,
                    mStartDayOfWeek);
            WeekViewCache.getInstance().putDateTimeList(mCurrentDateTime,
                    mDatetimeList);
        }

        initCells();
        updateCells(CalendarHelper.getSelectedDay(), true);
    }

    public void initCells() {
        if (this.mCells == null) {
            this.mCells = new CalendarCellView[mDatetimeList.size() / 7][7];
            RectF bound = new RectF(0, 0, mCellWidth, mCellHeight);
            for (int week = 0; week < mCells.length; week++) {
                for (int day = 0; day < mCells[week].length; day++) {
                    mCells[week][day] = new CalendarCellView(mDatetimeList.get(week
                            * 7 + day), new RectF(bound),
                            CalendarHelper.getContentPaintBlack(),
                            CalendarHelper.getTipPaintGray());
                    bound.offset(mCellWidth, 0);
                }
                bound.offset(0, mCellHeight);
                bound.left = 0;
                bound.right = mCellWidth;
            }
        }
        for (int week = 0; week < mCells.length; week++) {
            for (int day = 0; day < mCells[week].length; day++) {
                CalendarCellView kdc = mCells[week][day];
                kdc.setDate(mDatetimeList.get(week * 7 + day));
                kdc.setContentPaint(CalendarHelper.getContentPaintBlack());
                kdc.setTipPaint(CalendarHelper.getTipPaintGray());
            }
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

                DateTime dateTime = mCells[week][day].getDate();
                if (dateTime.getWeekDay() == Calendar.SATURDAY
                        || dateTime.getWeekDay() == Calendar.SUNDAY)
                    mCells[week][day].setContentPaint(CalendarHelper
                            .getContentPaintGreen());
                else
                    mCells[week][day].setContentPaint(CalendarHelper
                            .getContentPaintBlack());

                mCells[week][day].setActiveMonth(true);

                if (dateTime.equals(getToday())) {
                    mCells[week][day].setToday(true);
                    mCells[week][day].setTodayCirclePaint(CalendarHelper
                            .getTodayCirclePaint());
                } else {
                    mCells[week][day].setToday(false);
                    mCells[week][day].setTodayCirclePaint(null);
                }

                if ((CalendarHelper.isInThisWeek(mSelectedTime,
                        mCurrentDateTime, mStartDayOfWeek) && dateTime
                        .equals(mSelectedTime))
                        || (!CalendarHelper.isInThisWeek(mSelectedTime,
                        mCurrentDateTime, mStartDayOfWeek) && dateTime
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

                //TODO
                int eventsCount = 1;
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
        invalidate();
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
        LogUtils.d("WeekNew onDraw");
        if (mCells != null) {
            for (CalendarCellView[] week : mCells) {
                for (CalendarCellView day : week) {
                    day.draw(canvas);
                }
            }
        }
    }

    private float lastX, lastY, nowX, nowY;

    @SuppressLint("ClickableViewAccessibility")
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
                                if (day.hitCell((int) event.getX(),
                                        (int) event.getY())) {
                                    mOnCellClickListener.onCellClick(day.getDate());
                                    CalendarHelper.setSelectedDay(day.getDate());
                                    updateCells(CalendarHelper.getSelectedDay(),
                                            true);
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

    public int getPosition() {
        return mPosition;
    }

    public void setPosition(int position) {
        this.mPosition = position;
    }
}