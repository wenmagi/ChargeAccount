package com.wen.magi.baseframe.fragments.home.calendar;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wen.magi.baseframe.R;
import com.wen.magi.baseframe.activities.MainActivity;
import com.wen.magi.baseframe.adapters.WeekdayArrayAdapter;
import com.wen.magi.baseframe.annotations.From;
import com.wen.magi.baseframe.base.BaseActivity;
import com.wen.magi.baseframe.base.BaseLazyLoadFragment;
import com.wen.magi.baseframe.eventbus.BackTodayEvent;
import com.wen.magi.baseframe.interfaces.calendar.DayChangeListener;
import com.wen.magi.baseframe.interfaces.calendar.WeekChangedListener;
import com.wen.magi.baseframe.models.MonthDatas;
import com.wen.magi.baseframe.utils.Constants;
import com.wen.magi.baseframe.utils.LangUtils;
import com.wen.magi.baseframe.utils.SysUtils;
import com.wen.magi.baseframe.utils.ViewUtils;
import com.wen.magi.baseframe.utils.date.DayStyles;
import com.wen.magi.baseframe.utils.date.Lunar;

import org.greenrobot.eventbus.EventBus;

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


public class CalendarFragment extends BaseLazyLoadFragment implements TabLayout.OnTabSelectedListener{

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

    @From(R.id.textview)
    private TextView textView;


    private int Cell_Width = 0;
    private int Cell_height = 0;
    private Date mSelectedDate;
    private WeekFragment mWeekPagerFragment;
    private MonthFragment mMonthPagerFragment;
    private Lunar lunar = Lunar.getInstance();


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
            updateCalendarUI(date);
        }
    };

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_calendar, null);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onResume() {
        super.onResume();
        MainActivity.from(getContext()).registerTabObserver(this);
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
        ViewUtils.setViewClickStateChangeListener(textView);
        mIconToday.setOnClickListener(this);
        textView.setOnClickListener(this);
        updateCalendarUI(LangUtils.cc_dateByMovingToBeginningOfDay(mSelectedDate));
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
            EventBus.getDefault().post(new BackTodayEvent());
            mIconToday.setVisibility(View.GONE);
        }
    }

    private void updateCalendarUI(Date date) {
        lunar.setCalendar(date);
        mTitle.setText(LangUtils.format("yyyy.MM.dd", date));
        mSubTitle.setText(lunar.getLunarMonthString() + lunar.getLunarDayString());

        if (date != null && !date.equals(LangUtils.cc_dateByMovingToBeginningOfDay(new Date()))) {
            mIconToday.setVisibility(View.VISIBLE);
        } else
            mIconToday.setVisibility(View.GONE);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
}
