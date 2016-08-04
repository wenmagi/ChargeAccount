package com.wen.magi.baseframe.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.wen.magi.baseframe.R;
import com.wen.magi.baseframe.adapters.WeekdayArrayAdapter;
import com.wen.magi.baseframe.base.BaseActivity;
import com.wen.magi.baseframe.fragments.WeekFragment;
import com.wen.magi.baseframe.interfaces.calendar.WeekChangedListener;
import com.wen.magi.baseframe.utils.Constants;
import com.wen.magi.baseframe.utils.date.DayStyles;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by MVEN on 16/7/29.
 * <p/>
 * email: magiwen@126.com.
 */


public class CalendarActivity extends BaseActivity {
    private LinearLayout mLayoutContent;
    private RelativeLayout mCalendarContent;
    private FrameLayout mWeekLayout;
    private int Calendar_Width = 0;
    private int Cell_Width = 0;
    private int Cell_height = 0;
    private Date mSelectedDate;
    private WeekFragment mWeekPagerFragment;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case Constants.CALENDAR_UPDATE_CALENDAR:
                    if (null != msg.obj) {
                        updateSelectedDate((Date) msg.obj, msg.arg1);
                    }
                    break;
                case Constants.CALENDAR_UPDATE_MONTH:
                    if (null != msg.obj) {
                        int nForceUpdate = msg.arg1;
                        refreshMonthPager((Date) msg.obj, nForceUpdate == 1 ? true
                                : false);
                    }
                    break;
                case Constants.CALENDAR_UPDATE_WEEK:
                    if (null != msg.obj) {
                        int nForceUpdate1 = msg.arg1;
                        refreshWeekPager((Date) msg.obj, nForceUpdate1 == 1 ? true
                                : false);
                    }
                    break;
            }
        }

    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        initUI();
    }

    private void initUI() {
        initWeekHeaderView();
        initWeekPagerView();

    }

    /**
     * Week view 5 6 7 8 9 10 11
     */
    private void initWeekPagerView() {
        mWeekLayout = (FrameLayout) findViewById(R.id.layout_week_calendar);
        ViewGroup.LayoutParams weekParams = mWeekLayout.getLayoutParams();
        weekParams.height = Cell_height;
        mWeekLayout.setLayoutParams(weekParams);

        mWeekLayout.bringToFront();

        mWeekPagerFragment = new WeekFragment();

        Bundle args = new Bundle();
        args.putInt(WeekFragment.CELL_HEIGHT_WIDTH, Cell_Width);
        args.putInt(WeekFragment.START_DAY_OF_WEEK, Constants.FIRST_DAY_OF_WEEK);
        mWeekPagerFragment.setArguments(args);

        FragmentTransaction t = getSupportFragmentManager().beginTransaction();
        t.replace(R.id.layout_week_calendar, mWeekPagerFragment);
        t.commit();

        mWeekPagerFragment.setKiwiWeekAndDayListener(weekListener);

    }

    /**
     * week header 一 二 三 四 五 六 日
     */
    private void initWeekHeaderView() {
        GridView weekDayGridView = (GridView) findViewById(R.id.weekday_gridview);

        ArrayList<String> weekDayNameLists = new ArrayList<String>();
        for (int iDay = 0; iDay < Constants.MONTH_CALENDAR_COLUMN; iDay++) {
            String WeekDay = DayStyles.getShortWeekDayName(DayStyles
                    .getWeekDay(iDay, Constants.FIRST_DAY_OF_WEEK));
            weekDayNameLists.add(WeekDay);
        }

        WeekdayArrayAdapter weekdaysAdapter = new WeekdayArrayAdapter(this,
                R.layout.adapter_week_name_list_item, weekDayNameLists);
        weekDayGridView.setAdapter(weekdaysAdapter);
    }


    @Override
    protected void OnClickView(View v) {

    }


    WeekChangedListener weekListener = new WeekChangedListener() {
        public void onSelectDate(Date date, View view) {
            if (mSelectedDate.equals(date)) {
                return;
            }
            refreshSelectedDateDelay(date,
                    Constants.SELECT_TYPE_FROM_WEEK_CLICK, 100);
        }

        public void onChangeWeek(int day, int month, int year) {
            Calendar calendar = new GregorianCalendar(year, month - 1, day);
            Date date = calendar.getTime();
            if (mSelectedDate.equals(date))
                return;
            refreshSelectedDateDelay(date,
                    Constants.SELECT_TYPE_FROM_WEEK_SCROLL, 100);
        }
    };


    protected void refreshSelectedDateDelay(Date date,
                                            int selectTypeFromMonthScroll, int delay) {
        changeSelectedDateNow(date);
        Message msg = new Message();
        msg.what = Constants.CALENDAR_UPDATE_CALENDAR;
        msg.arg1 = selectTypeFromMonthScroll;
        msg.obj = date;
        mHandler.sendMessageAtTime(msg, delay);
    }
}
