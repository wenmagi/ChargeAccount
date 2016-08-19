package com.wen.magi.baseframe.fragments.calendar;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.wen.magi.baseframe.R;
import com.wen.magi.baseframe.adapters.MonthPagerAdapter;
import com.wen.magi.baseframe.base.BaseFragment;
import com.wen.magi.baseframe.interfaces.calendar.DayChangeListener;
import com.wen.magi.baseframe.interfaces.calendar.MonthChangedListener;
import com.wen.magi.baseframe.models.MonthDatas;
import com.wen.magi.baseframe.utils.Constants;
import com.wen.magi.baseframe.utils.LangUtils;
import com.wen.magi.baseframe.utils.LogUtils;
import com.wen.magi.baseframe.utils.ViewUtils;
import com.wen.magi.baseframe.utils.date.CalendarHelper;
import com.wen.magi.baseframe.utils.date.DateTime;
import com.wen.magi.baseframe.views.calendar.month.MonthView;
import com.wen.magi.baseframe.views.calendar.month.MonthView.OnCellClickListener;
import com.wen.magi.baseframe.views.calendar.month.MonthViewPager;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by MVEN on 16/7/29.
 * <p/>
 * email: magiwen@126.com.
 */

public class MonthFragment extends BaseFragment {

    private DayChangeListener dayChangeListener;
    private OnCellClickListener mOnCellClickListener;
    private MonthDatas datas;
    private volatile boolean bMoveToDate = false;


    private MonthViewPager monthPager;
    private MonthPagerAdapter monthAdapter;
    private DateTime mToday;
    //选中的日期
    private DateTime mCurrentDay;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle b = getArguments();
        if (b == null)
            return;
        datas = (MonthDatas) b.getSerializable(Constants.BUNDLE_MONTH_FRAGMENT);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        monthPager = new MonthViewPager(activity);

        initData();
        initUI();

        return monthPager;
    }

    protected DateTime getToday() {
        if (mToday == null) {
            mToday = CalendarHelper.convertDateToDateTime(new Date());
        }
        return mToday;
    }

    private void initData() {
        mCurrentDay = getToday();
        mOnCellClickListener = getDateCellClickListener();
    }

    private void initUI() {
        monthAdapter = new MonthPagerAdapter(activity, datas, mOnCellClickListener);
        monthPager.setAdapter(monthAdapter);
        monthPager.setCurrentItem(Constants.MAX_MONTH_SCROLL_COUNT / 2);
        monthPager.setOnPageChangeListener(new MonthPageChangeListener());
        monthPager.setOffscreenPageLimit(0);
    }

    private class MonthPageChangeListener implements ViewPager.OnPageChangeListener {
        volatile boolean isPageChanged = false;

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            bMoveToDate = false;
        }

        @Override
        public void onPageSelected(int position) {
            isPageChanged = true;
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            switch (state) {
                case ViewPager.SCROLL_STATE_IDLE:
                    if (isPageChanged) {
                        if (!bMoveToDate) {

                            Calendar firstDayOfTodayMonth = Calendar
                                    .getInstance();
                            firstDayOfTodayMonth.set(Calendar.DAY_OF_MONTH,
                                    1);
                            Date beginOf = LangUtils
                                    .cc_dateByMovingToBeginningOfDay(firstDayOfTodayMonth
                                            .getTime());

                            int offSet = monthPager
                                    .getCurrentItem()
                                    - Constants.MAX_MONTH_SCROLL_COUNT / 2;

                            if (offSet > 0)
                                mCurrentDay = CalendarHelper
                                        .convertDateToDateTime(beginOf)
                                        .plus(0, offSet, 0, 0, 0, 0, 0, DateTime.DayOverflow.LastDay);


                            else if (offSet < 0)
                                mCurrentDay = CalendarHelper
                                        .convertDateToDateTime(beginOf)
                                        .minus(0, -offSet, 0, 0, 0, 0, 0, DateTime.DayOverflow.LastDay);

                            else
                                mCurrentDay = mToday;


                            CalendarHelper.setSelectedDay(mCurrentDay);
                            MonthView view = (MonthView) monthPager.findViewWithTag(monthPager.getCurrentItem());
                            view.updateCells(CalendarHelper.getSelectedDay(), true);

                            if (dayChangeListener != null)
                                dayChangeListener.onDayChangeListener(CalendarHelper.convertDateTimeToDate(mCurrentDay));
                        } else {
                            bMoveToDate = false;
                        }
                    }
                    break;
                case ViewPager.SCROLL_STATE_DRAGGING:
                    break;
                case ViewPager.SCROLL_STATE_SETTLING:
                    break;
            }
        }
    }


    private OnCellClickListener getDateCellClickListener() {
        if (mOnCellClickListener == null) {
            mOnCellClickListener = new OnCellClickListener() {
                @Override
                public void onCellClick(DateTime day) {
                    if (day.getMonth().equals(mCurrentDay.getMonth())) {
                        mCurrentDay = day;
                    } else {
                        changeCurrentDateAndMonth(day);
                    }
                    if (dayChangeListener != null)
                        dayChangeListener.onDayChangeListener(CalendarHelper.convertDateTimeToDate(day));

                }
            };
        }
        return mOnCellClickListener;
    }

    public void setDayChangeListener(DayChangeListener dayChangeListener) {
        this.dayChangeListener = dayChangeListener;
    }

    private void changeCurrentDateAndMonth(DateTime day) {
        Date date = CalendarHelper.convertDateTimeToDate(day);
        Date curr = CalendarHelper.convertDateTimeToDate(mCurrentDay);
        int item = monthPager.getCurrentItem();

        if (date.after(curr)) {
            monthPager.setCurrentItem(item + 1);
        } else {
            monthPager.setCurrentItem(item - 1);
        }
        mCurrentDay = day;
        CalendarHelper.setSelectedDay(mCurrentDay);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        CalendarHelper.setSelectedDay(mToday);
    }

    @Override
    protected void OnClickView(View v) {

    }
}
