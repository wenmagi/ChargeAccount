package com.wen.magi.baseframe.fragments.calendar;

import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wen.magi.baseframe.R;
import com.wen.magi.baseframe.adapters.WeekdayArrayAdapter;
import com.wen.magi.baseframe.annotations.From;
import com.wen.magi.baseframe.base.BaseActivity;
import com.wen.magi.baseframe.base.BaseLazyLoadFragment;
import com.wen.magi.baseframe.interfaces.calendar.DayChangeListener;
import com.wen.magi.baseframe.interfaces.calendar.WeekChangedListener;
import com.wen.magi.baseframe.managers.AppManager;
import com.wen.magi.baseframe.models.MonthDatas;
import com.wen.magi.baseframe.utils.Constants;
import com.wen.magi.baseframe.utils.LangUtils;
import com.wen.magi.baseframe.utils.SysUtils;
import com.wen.magi.baseframe.utils.ViewUtils;
import com.wen.magi.baseframe.utils.date.DayStyles;
import com.wen.magi.baseframe.utils.date.Lunar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static com.wen.magi.baseframe.utils.ViewUtils.find;

/**
 * Created by MVEN on 16/7/29.
 * <p/>
 * email: magiwen@126.com.
 */


public class CalendarFragment extends BaseLazyLoadFragment {

    @From(R.id.layout_week_calendar)
    private LinearLayout mWeekLayout;

    @From(R.id.layout_month_calendar)
    private LinearLayout mMonthLayout;

    @From(R.id.icon_back_today)
    private ImageView mIconToday;

    @From(R.id.tv_title)
    private TextView mTitle;

    @From(R.id.tv_sub_title)
    private TextView mSubTitle;

    //选择的日期比今天大
    private static int LARGER_THAN_TODAY = 1;
    //选择的日期是今天
    private static int SAME_WITH_TODAY = 0;
    //选择的日期比今天小
    private static int SMALL_THAN_TODAY = -1;
    private int Cell_Width = 0;
    private int Cell_height = 0;
    private Date mSelectedDate;
    private WeekFragment mWeekPagerFragment;
    private MonthFragment mMonthPagerFragment;
    private Lunar lunar = AppManager.lunar;
    //当前状态：LARGER_THAN_TODAY LARGER_THAN_TODAY SMALL_THAN_TODAY
    private int state = 0;

    WeekChangedListener weekListener = new WeekChangedListener() {
        public void onSelectDate(Date date, View view) {
            if (mSelectedDate.equals(date)) {
                return;
            }
        }

        public void onChangeWeek(int day, int month, int year) {
            Calendar calendar = new GregorianCalendar(year, month - 1, day);
            Date date = calendar.getTime();
            if (mSelectedDate.equals(date))
                return;
        }
    };

    DayChangeListener dayChangeListener = new DayChangeListener() {
        @Override
        public void onDayChangeListener(Date date) {
            if (mSelectedDate.equals(date)) {
                return;
            }
            state = LangUtils.compareDate(mSelectedDate, date);
            updateCalendarUI(date);
            updateTodayIcon();
        }
    };

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_calendar, null);
        return rootView;
    }

    @Override
    protected void lazyLoad() {
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
        ViewUtils.setImageClickStateChangeListener(mIconToday);
        mIconToday.setOnClickListener(this);
        updateCalendarUI(new Date());
    }

    /**
     * month View
     */
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

        FragmentTransaction t = ((BaseActivity) activity).getSupportFragmentManager().beginTransaction();
        t.replace(R.id.layout_month_calendar, mMonthPagerFragment);
        t.commit();
        mMonthPagerFragment.setDayChangeListener(dayChangeListener);
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

        FragmentTransaction t = ((BaseActivity) activity).getSupportFragmentManager().beginTransaction();
        t.add(R.id.layout_week_calendar, mWeekPagerFragment);
        t.commit();

        mWeekPagerFragment.setKiwiWeekAndDayListener(weekListener);
    }

    /**
     * week header 一 二 三 四 五 六 日
     */
    private void generateWeekHeaderView() {
        GridView weekDayGridView = find(activity, R.id.weekday_gridview);

        ArrayList<String> weekDayNameLists = new ArrayList<>();
        for (int iDay = 0; iDay < Constants.MONTH_CALENDAR_COLUMN; iDay++) {
            String WeekDay = DayStyles.getShortWeekDayName(DayStyles
                    .getWeekDay(iDay, Constants.FIRST_DAY_OF_WEEK));
            weekDayNameLists.add(WeekDay);
        }

        WeekdayArrayAdapter weekdaysAdapter = new WeekdayArrayAdapter(activity,
                R.layout.adapter_week_name_list_item, weekDayNameLists);
        weekDayGridView.setAdapter(weekdaysAdapter);
    }


    @Override
    protected void OnClickView(View v) {
        if (v == mIconToday) {
            ViewUtils.showToast(activity, "click iconToday");
        }
    }


    private void updateTodayIcon() {
        switch (state) {
            case 0:
                break;
        }
    }


    private void updateCalendarUI(Date date) {
        lunar.setCalendar(date);
        mTitle.setText(LangUtils.format("yyyy.MM.dd", date));
        mSubTitle.setText(lunar.getLunarMonthString() + lunar.getLunarDayString());
    }
}
