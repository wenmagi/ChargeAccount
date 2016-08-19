package com.wen.magi.baseframe.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by MVEN on 16/8/19.
 * <p/>
 * email: magiwen@126.com.
 */


public class MainPagerAdapter extends FragmentPagerAdapter {
    private static final int TAB_COUNT = 1;
    private ArrayList<Fragment> _fragments;

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
}