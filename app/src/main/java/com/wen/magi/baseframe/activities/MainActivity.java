package com.wen.magi.baseframe.activities;

import android.app.ActivityManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Toast;

import com.wen.magi.baseframe.R;
import com.wen.magi.baseframe.adapters.MainPagerAdapter;
import com.wen.magi.baseframe.annotations.From;
import com.wen.magi.baseframe.base.BaseActivity;
import com.wen.magi.baseframe.fragments.calendar.CalendarFragment;
import com.wen.magi.baseframe.utils.ViewUtils;
import com.wen.magi.baseframe.views.viewpager.TabView;

import java.util.ArrayList;

public class MainActivity extends BaseActivity {

    private static final int TAB_NUM = 3;
    @From(R.id.main_viewpager)
    private ViewPager viewPager;

    @From(R.id.id_tab)
    private TabView tabView;

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
        Fragment calendarFragment1 = getFragmentCache(R.id.main_viewpager, 1);
        if (calendarFragment1 == null)
            calendarFragment1 = new CalendarFragment();
        Fragment calendarFragment2 = getFragmentCache(R.id.main_viewpager, 2);
        if (calendarFragment2 == null)
            calendarFragment2 = new CalendarFragment();
        ArrayList<Fragment> fragments = new ArrayList<>(TAB_NUM);
        fragments.add(calendarFragment);
        fragments.add(calendarFragment1);
        fragments.add(calendarFragment2);

        pagerAdapter = new MainPagerAdapter(fragments, getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        viewPager.setOffscreenPageLimit(TAB_NUM);
        tabView.setViewPager(viewPager);
    }

    @Override
    protected void OnClickView(View v) {
    }

    @Override
    protected boolean isTitleBarAvailable() {
        return false;
    }

    private static final long DELAY_EXIT_TIME = 1000;
    private boolean canExit = false;

    @Override
    public void onBackPressed() {
        if (canExit) {
            ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
            am.killBackgroundProcesses(getPackageName());
            finish();
        } else {
            ViewUtils.showToast(this, getString(R.string.main_activity_back_warn), Toast.LENGTH_SHORT);
            canExit = true;
            ViewUtils.postDelayed(new Runnable() {
                @Override
                public void run() {
                    canExit = false;
                }
            }, DELAY_EXIT_TIME);
        }

    }
}
