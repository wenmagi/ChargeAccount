package com.wen.magi.baseframe.activities;

import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

import com.wen.magi.baseframe.R;
import com.wen.magi.baseframe.adapters.WeekdayArrayAdapter;
import com.wen.magi.baseframe.annotations.From;
import com.wen.magi.baseframe.base.BaseActivity;
import com.wen.magi.baseframe.fragments.MonthFragment;
import com.wen.magi.baseframe.fragments.WeekFragment;
import com.wen.magi.baseframe.interfaces.calendar.MonthChangedListener;
import com.wen.magi.baseframe.interfaces.calendar.WeekChangedListener;
import com.wen.magi.baseframe.models.MonthDatas;
import com.wen.magi.baseframe.utils.Constants;
import com.wen.magi.baseframe.utils.LangUtils;
import com.wen.magi.baseframe.utils.SysUtils;
import com.wen.magi.baseframe.utils.date.CalendarHelper;
import com.wen.magi.baseframe.utils.date.DayStyles;
import com.wen.magi.baseframe.views.calendar.SlideDrawerLayout;

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

    @From(R.id.layout_week_calendar)
    private LinearLayout mWeekLayout;

    @From(R.id.layout_month_calendar)
    private LinearLayout mMonthLayout;


    private int Cell_Width = 0;
    private int Cell_height = 0;
    private Date mSelectedDate;
    private WeekFragment mWeekPagerFragment;
    private MonthFragment mMonthPagerFragment;

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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        generateData();
        generateUI();
    }

    private void generateData() {
        Cell_Width = SysUtils.WIDTH / Constants.MONTH_CALENDAR_COLUMN;
        Cell_height = Cell_Width;
        mSelectedDate = new Date();
    }

    private void generateUI() {
        generateWeekHeaderView();
        generateWeekPagerView();
        generateCalendarUI();
    }

    private void generateCalendarUI() {
        ViewGroup.LayoutParams params = mMonthLayout.getLayoutParams();
        params.height = Constants.ROWS_OF_MONTH_CALENDAR * Cell_height;

        mMonthPagerFragment = new MonthFragment();

        Bundle args = new Bundle();
        MonthDatas datas = new MonthDatas();
        datas.cellWidth = Cell_Width;
        datas.cellHeight = Cell_height;
        datas.startDayOfMonth = Constants.FIRST_DAY_OF_WEEK;
        args.putSerializable(Constants.BUNDLE_MONTH_FRAGMENT, datas);
        mMonthPagerFragment.setArguments(args);

        FragmentTransaction t = getSupportFragmentManager().beginTransaction();
        t.replace(R.id.layout_month_calendar, mMonthPagerFragment);
        t.commit();
    }

    /**
     * Week view 5 6 7 8 9 10 11
     */
    private void generateWeekPagerView() {
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
        t.add(R.id.layout_week_calendar, mWeekPagerFragment);
        t.commit();

        mWeekPagerFragment.setKiwiWeekAndDayListener(weekListener);
    }

    /**
     * week header 一 二 三 四 五 六 日
     */
    private void generateWeekHeaderView() {
        GridView weekDayGridView = (GridView) findViewById(R.id.weekday_gridview);

        ArrayList<String> weekDayNameLists = new ArrayList<>();
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

    protected void refreshSelectedDateDelay(Date date,
                                            int selectTypeFromMonthScroll, int delay) {
        Message msg = new Message();
        msg.what = Constants.CALENDAR_UPDATE_CALENDAR;
        msg.arg1 = selectTypeFromMonthScroll;
        msg.obj = date;
    }
}
