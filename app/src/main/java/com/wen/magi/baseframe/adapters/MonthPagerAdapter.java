package com.wen.magi.baseframe.adapters;

import android.content.Context;
import android.support.v4.util.LruCache;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.wen.magi.baseframe.R;
import com.wen.magi.baseframe.models.MonthDatas;
import com.wen.magi.baseframe.utils.Constants;
import com.wen.magi.baseframe.views.calendar.month.MonthView;

/**
 * Created by MVEN on 16/8/4.
 * <p/>
 * email: magiwen@126.com.
 */


public class MonthPagerAdapter extends PagerAdapter {

    private LruCache<Integer, MonthView> views;
    private MonthView.OnCellClickListener listener;
    private Context context;
    private MonthDatas datas;

    public MonthPagerAdapter(Context context, MonthDatas datas, MonthView.OnCellClickListener listener) {
        views = new LruCache<>(Constants.MAX_MONTH_SCROLL_COUNT);
        this.listener = listener;
        this.context = context;
        this.datas = datas;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        MonthView v;
        if (views.get(position) == null) {
            if (datas == null)
                return null;
            datas.position = position;
            v = new MonthView(context, datas.position, datas.cellWidth, datas.cellHeight, datas.startDayOfMonth);
            v.setOnCellClickListener(listener);
            v.setBackgroundResource(R.color.white);
            views.put(position, v);
        } else
            v = views.get(position);
        container.addView(v);
        return v;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        if (views.get(position) != null)
            container.removeView(views.get(position));
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public int getCount() {
        return Constants.MAX_MONTH_SCROLL_COUNT;
    }

}
