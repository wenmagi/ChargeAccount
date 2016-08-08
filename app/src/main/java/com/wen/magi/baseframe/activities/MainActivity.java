package com.wen.magi.baseframe.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.wen.magi.baseframe.R;
import com.wen.magi.baseframe.annotations.From;
import com.wen.magi.baseframe.base.BaseActivity;
import com.wen.magi.baseframe.fragments.calendar.CalendarFragment;

import java.util.ArrayList;

public class MainActivity extends BaseActivity {

    private static final int TAB_COUNT = 1;

    @From(R.id.main_viewpager)
    private ViewPager viewPager;

    private MainPagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initPager();
    }

    private void initPager() {
        Fragment calendarFragment;
        calendarFragment = new CalendarFragment();
        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(calendarFragment);
        pagerAdapter = new MainPagerAdapter(fragments, getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
    }

    @Override
    protected void OnClickView(View v) {
    }

    class MainPagerAdapter extends FragmentPagerAdapter {
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
}
