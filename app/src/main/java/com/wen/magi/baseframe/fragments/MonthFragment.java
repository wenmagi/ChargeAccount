package com.wen.magi.baseframe.fragments;

import java.util.Calendar;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wen.magi.baseframe.base.BaseFragment;
import com.wen.magi.baseframe.utils.Constants;
import com.wen.magi.baseframe.views.calendar.month.MonthView;

/**
 * Created by MVEN on 16/7/29.
 * <p/>
 * email: magiwen@126.com.
 */

public class MonthFragment extends BaseFragment {
    private MonthView monthView;

    public MonthView getGridView() {
        return monthView;
    }

    public void setGridView(MonthView gridView) {
        this.monthView = gridView;
    }

    private MonthView.OnCellClickListener mOnCellClickListener;
    private int mCellWidth = 0;
    private int mCellHeight = 0;
    private int mStartDayOfWeek = Calendar.MONDAY;

    private int mPosition = Constants.MAX_MONTH_SCROLL_COUNT / 2;

    public void setData(int cellWidth, int cellHeight, int startDayOfMonth) {
        this.mCellHeight = cellHeight;
        this.mCellWidth = cellWidth;
        this.mStartDayOfWeek = startDayOfMonth;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        monthView = new MonthView(getActivity(), mPosition, mCellWidth,
                mCellHeight, mStartDayOfWeek);
        Bundle b = getArguments();
        if (b == null)
            return;
        mPosition = b.getInt(Constants.BUNDLE_MONTH_FRAGMENT_POSITION);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (mOnCellClickListener != null)
            monthView.setOnCellClickListener(mOnCellClickListener);

        ViewGroup parent = (ViewGroup) monthView.getParent();
        if (parent != null) {
            parent.removeAllViewsInLayout();
        }
        return monthView;
    }

    public MonthView.OnCellClickListener getOnCellClickListener() {
        return mOnCellClickListener;
    }

    public void setOnCellClickListener(MonthView.OnCellClickListener mOnCellClickListener) {
        this.mOnCellClickListener = mOnCellClickListener;
    }

    public int getCellWidth() {
        return mCellWidth;
    }

    public void setCellWidth(int mCellWidth) {
        this.mCellWidth = mCellWidth;
    }

    public int getCellHeight() {
        return mCellHeight;
    }

    public void setCellHeight(int mCellHeight) {
        this.mCellHeight = mCellHeight;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (monthView != null) {
            monthView.doDestory();
        }
    }

    @Override
    protected void OnClickView(View v) {

    }
}
