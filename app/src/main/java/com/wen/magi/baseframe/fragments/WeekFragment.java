package com.wen.magi.baseframe.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wen.magi.baseframe.base.BaseFragment;
import com.wen.magi.baseframe.interfaces.calendar.WeekChangedListener;
import com.wen.magi.baseframe.utils.Constants;
import com.wen.magi.baseframe.utils.LogUtils;
import com.wen.magi.baseframe.utils.ViewUtils;
import com.wen.magi.baseframe.utils.date.CalendarHelper;
import com.wen.magi.baseframe.utils.date.DateTime;
import com.wen.magi.baseframe.views.calendar.unit.CalendarDrawerLayout;
import com.wen.magi.baseframe.views.calendar.week.WeekView;
import com.wen.magi.baseframe.views.calendar.week.WeekViewPager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class WeekFragment extends BaseFragment implements WeekViewPager.OnWeekChangedListener {
    private int mStartDayOfWeek = Calendar.MONDAY;

    private static final String KEY_POSTION = "WeekFragment_position";
    private static final String KEY_CURRENT_DAY = "WeekFragment_current_day";

    public static final String START_DAY_OF_WEEK = "startDayOfWeek";
    public static final String CELL_HEIGHT_WIDTH = "width_height";

    private WeekViewPager mWeekViewPager;
    private List<WeekView> mWeekViewsList = new ArrayList<WeekView>();
    private int mPosition = Constants.MAX_WEEK_SCROLL_COUNT / 2;
    private static final int WEEK_VIEW_COUNT = 3;

    private WeekChangedListener mKiwiWeekAndDayListener;

    private int mCellWidth = 0;
    private int mCellHeight = 0;

    private WeekView.OnCellClickListener mOnCellClickListener;
    private DateTime mToday;
    private DateTime mCurrentDay;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            mPosition = savedInstanceState.getInt(KEY_POSTION);
            mCurrentDay = (DateTime) savedInstanceState
                    .getSerializable(KEY_CURRENT_DAY);
        } else {
            mCurrentDay = getToday();
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(KEY_POSTION, mPosition);
        outState.putSerializable(KEY_CURRENT_DAY, mCurrentDay);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onPause() {
        if (mWeekViewsList != null) {
            for (WeekView view : mWeekViewsList) {
                view.clearTimer();
            }
        }
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mWeekViewsList != null) {
            for (WeekView view : mWeekViewsList) {
                view.doDestory();
            }
        }
        if (mWeekViewPager != null)
            mWeekViewPager.clearTimer();
    }

    @Override
    protected void OnClickView(View v) {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        retrieveInitialArgs();
        mWeekViewPager = new WeekViewPager(activity);
        mWeekViewPager.setOnPageChangedListener(this);
        mWeekViewPager.setSlidingLayout(mSlidingLayout);

        for (int i = 0; i < WEEK_VIEW_COUNT; i++) {
            WeekView weekView = new WeekView(getActivity(), mPosition - 1 + i,
                    mCellWidth, mCellHeight, mStartDayOfWeek);
            weekView.setOnCellClickListener(getDateCellClickListener());
            mWeekViewsList.add(weekView);
            mWeekViewPager.addView(weekView);
        }

        return mWeekViewPager;
    }

    /**
     * Below code fixed the issue viewpager disappears in dialog mode on
     * orientation change Code taken from Andy Dennie and Zsombor Erdody-Nagy
     * http://stackoverflow.com/questions/8235080/fragments-dialogfragment
     * -and-screen-rotation
     */
    private void retrieveInitialArgs() {
        Bundle args = getArguments();
        if (args != null) {
            // Get week, year
            mCellWidth = mCellHeight = args.getInt(CELL_HEIGHT_WIDTH, -1);
            // Get start day of Week. Default calendar first column is SUNDAY
            mStartDayOfWeek = args.getInt(START_DAY_OF_WEEK, Calendar.MONDAY);
            if (mStartDayOfWeek > 7) {
                mStartDayOfWeek = mStartDayOfWeek % 7;
            }
        }
    }

    public void moveToDate(Date date) {
        if (!CalendarHelper.isInThisWeek(date, mCurrentDay, mStartDayOfWeek)) {
            mCurrentDay = CalendarHelper.convertDateToDateTime(date);
            int weeks = CalendarHelper.weeksBetweenDate(date,
                    CalendarHelper.convertDateTimeToDate(mToday),
                    mStartDayOfWeek);

            mPosition = Constants.MAX_WEEK_SCROLL_COUNT / 2 + weeks;

            mWeekViewsList.get(0).setPosition(mPosition - 1);
            mWeekViewsList.get(0).initData();

            mWeekViewsList.get(1).setPosition(mPosition);
            mWeekViewsList.get(1).initData();

            mWeekViewsList.get(2).setPosition(mPosition + 1);
            mWeekViewsList.get(2).initData();
        }
    }

    private void refreshCurrentView(boolean bForceUpdate) {

        if (mWeekViewsList != null && mWeekViewsList.size() > 2) {
            WeekView weekView = mWeekViewsList.get(1);
            if (weekView != null) {
                weekView.updateCells(CalendarHelper.getSelectedDay(),
                        bForceUpdate);
            }
        }
    }

    public void refreshView(Date date, boolean bForceUpdate) {
        if (CalendarHelper.isInThisWeek(date, mCurrentDay, mStartDayOfWeek)) {
            refreshCurrentView(bForceUpdate);
        } else {
            moveToDate(date);
        }
    }

    private WeekView.OnCellClickListener getDateCellClickListener() {
        if (mOnCellClickListener == null) {
            mOnCellClickListener = new WeekView.OnCellClickListener() {
                @Override
                public void onCellClick(DateTime day) {
                    if (mKiwiWeekAndDayListener != null) {
                        Date date = CalendarHelper.convertDateTimeToDate(day);
                        mCurrentDay = day;
                        mKiwiWeekAndDayListener.onSelectDate(date, null);

                    }
                }
            };
        }
        return mOnCellClickListener;
    }

    public int getCellWidth() {
        return mCellWidth;
    }

    public void setCellWidth(int iCellWidth) {
        this.mCellWidth = iCellWidth;
    }

    public int getCellHeight() {
        return mCellHeight;
    }

    public void setCellHeight(int iCellHeight) {
        this.mCellHeight = iCellHeight;
    }

    public WeekChangedListener getKiwiWeekAndDayListener() {
        return mKiwiWeekAndDayListener;
    }

    public void setKiwiWeekAndDayListener(
            WeekChangedListener kiwiWeekAndDayListener) {
        mKiwiWeekAndDayListener = kiwiWeekAndDayListener;
    }

    protected DateTime getToday() {
        if (mToday == null) {
            mToday = CalendarHelper.convertDateToDateTime(new Date());
        }
        return mToday;
    }

    @Override
    public void onNextWeek() {
        mKiwiWeekAndDayListener.onChangeWeek(mCurrentDay.getDay(),
                mCurrentDay.getMonth(), mCurrentDay.getYear());
    }

    @Override
    public void onPrevWeek() {
        mKiwiWeekAndDayListener.onChangeWeek(mCurrentDay.getDay(),
                mCurrentDay.getMonth(), mCurrentDay.getYear());
    }

    private void changeWeek() {
        mCurrentDay = CalendarHelper.getFirstDateOfThisWeek(
                getToday().plusDays(
                        7 * (mPosition - Constants.MAX_WEEK_SCROLL_COUNT / 2)),
                mStartDayOfWeek);
        LogUtils.d(
                "NewKiwiWeekPagerFragment onChangeWeek setCalendarDateTime day=%s;month=%s;year=%s",
                mCurrentDay.getDay(), mCurrentDay.getMonth(),
                mCurrentDay.getYear());

        ViewUtils.post(new Runnable() {
            @Override
            public void run() {
                mWeekViewsList.get(0).setPosition(mPosition - 1);
                mWeekViewsList.get(0).initData();

                mWeekViewsList.get(1).setPosition(mPosition);
                mWeekViewsList.get(1).initData();

                mWeekViewsList.get(2).setPosition(mPosition + 1);
                mWeekViewsList.get(2).initData();
            }
        });
    }

    @Override
    public void onPrepareNextWeek() {
        mPosition++;
        changeWeek();
    }

    @Override
    public void onPreparePrevWeek() {
        mPosition--;
        changeWeek();
    }

    public boolean isUpOrDown() {
        if (mWeekViewPager != null)
            return mWeekViewPager.isUpOrDown();
        return false;
    }

    public void setUpOrDown(boolean isUpOrDown) {
        if (mWeekViewPager != null)
            mWeekViewPager.setUpOrDown(isUpOrDown);
    }

    CalendarDrawerLayout mSlidingLayout;

    public void setSlidingLayout(CalendarDrawerLayout slidingLayout) {
        mSlidingLayout = slidingLayout;
        if (mWeekViewPager != null)
            mWeekViewPager.setSlidingLayout(slidingLayout);
    }
}
