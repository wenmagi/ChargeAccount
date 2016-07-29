package com.wen.magi.baseframe.base;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MVEN on 16/6/17.
 * <p/>
 * email: magiwen@126.com.
 */
public class BasePagerAdapter<T> extends PagerAdapter {
    protected Context context;
    protected List<T> datas;

    protected BasePagerAdapter(Context context, List<T> datas) {
        this.context = context;
        this.datas = new ArrayList<>();
        if (datas != null)
            this.datas.addAll(datas);
    }

    public void setLists(List<T> datas) {
        if (this.datas == null)
            this.datas = new ArrayList<>();
        else
            this.datas.clear();
        if (datas != null)
            this.datas.addAll(datas);
    }

    @Override
    public int getCount() {
        if (datas == null)
            return 0;
        else {
            return datas.size();
        }
    }


    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public Parcelable saveState() {
        return null;
    }
}
