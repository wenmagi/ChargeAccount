package com.wen.magi.baseframe.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.wen.magi.baseframe.R;
import com.wen.magi.baseframe.utils.StringUtils;
import com.wen.magi.baseframe.views.viewpager.TabView;

import java.util.ArrayList;

/**
 * Created by MVEN on 16/8/19.
 * <p/>
 * email: magiwen@126.com.
 */


public class MainPagerAdapter extends FragmentPagerAdapter implements TabView.OnItemIconTextSelectListener {
    private static final int TAB_COUNT = 3;
    private ArrayList<Fragment> _fragments;
    private final int[] normalIcons = {R.mipmap.tab_view_main, R.mipmap.tab_view_chart, R.mipmap.tab_view_setting};

    public MainPagerAdapter(ArrayList<Fragment> fragments, FragmentManager fm) {
        super(fm);
        _fragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return _fragments == null ? null : _fragments.get(position);
    }

    @Override
    public int getCount() {
        return TAB_COUNT;
    }

    @Override
    public int[] onIconSelect(int position) {
        int[] icons = new int[2];
        icons[0] = normalIcons[position];
        icons[1] = normalIcons[position];
        return null;
    }

    @Override
    public String onTextSelect(int position) {
        switch (position) {
            case 0:
                return StringUtils.getString(R.string.tab_view_main);
            case 1:
                return StringUtils.getString(R.string.tab_view_chart);
            case 2:
                return StringUtils.getString(R.string.tab_view_setting);
        }
        return "";
    }
}