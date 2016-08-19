package com.wen.magi.baseframe.activities;

import android.app.ActivityManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.wen.magi.baseframe.R;
import com.wen.magi.baseframe.adapters.MainPagerAdapter;
import com.wen.magi.baseframe.annotations.From;
import com.wen.magi.baseframe.base.BaseActivity;
import com.wen.magi.baseframe.fragments.calendar.CalendarFragment;

import java.util.ArrayList;

public class MainActivity extends BaseActivity {

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
        Fragment calendarFragment = getFragmentCache(R.id.main_viewpager, 0);
        if (calendarFragment == null)
            calendarFragment = new CalendarFragment();
        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(calendarFragment);
        pagerAdapter = new MainPagerAdapter(fragments, getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
    }

    @Override
    protected void OnClickView(View v) {
    }

    @Override
    protected boolean isTitleBarAvailable() {
        return false;
    }
}
