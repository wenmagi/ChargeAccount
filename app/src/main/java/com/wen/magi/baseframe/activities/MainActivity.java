package com.wen.magi.baseframe.activities;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.wen.magi.baseframe.R;
import com.wen.magi.baseframe.adapters.MainPagerAdapter;
import com.wen.magi.baseframe.base.BaseActivity;
import com.wen.magi.baseframe.base.BaseFragment;
import com.wen.magi.baseframe.base.graphics.TintDrawable;
import com.wen.magi.baseframe.databinding.ActivityMainBinding;
import com.wen.magi.baseframe.fragments.home.ConsumeOfMonthFragment;
import com.wen.magi.baseframe.fragments.ParentFragment;
import com.wen.magi.baseframe.fragments.home.SettingFragment;
import com.wen.magi.baseframe.fragments.home.calendar.CalendarFragment;
import com.wen.magi.baseframe.managers.ThemeManager;
import com.wen.magi.baseframe.utils.ViewUtils;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity implements TabLayout.OnTabSelectedListener {

    private static final int TAB_NUM = 3;

    private MainPagerAdapter pagerAdapter;
    private ActivityMainBinding mBinding;
    // 进入后台前，假设当前屏幕上显示的 Fragment A 是全屏的，且存在一个不全屏的 Fragment B ；
    // 进入后台后，再恢复，由于 Activity 恢复 Fragment 的顺序难以确定，
    // 可能出现 A 先恢复，B 后恢复的现象；
    // 虽然显示的是 A ，但是由于 B 不是全屏的，所以导致 A 也不是全屏的；
    // 同理, 恢复状态栏颜色时也会因为顺序不同而出错;
    // 因此添加一个变量, 用于记录进入后台前的屏幕上显示的 Fragment,
    // 只允许这个 Fragment 对状态栏进行操作
    private String mLatestFragmentName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        initPager();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (getCurrentDisplayFragment() instanceof BaseFragment) {
            mLatestFragmentName = getCurrentDisplayFragment().getClass().getName();
        }
    }

    @Nullable
    protected Fragment getCurrentDisplayFragment() {
        final int overlayItemCount = this.getSupportFragmentManager().getBackStackEntryCount();
        if (overlayItemCount > 0) {
            final String currentName = this.getSupportFragmentManager().getBackStackEntryAt(overlayItemCount - 1).getName();
            return this.getSupportFragmentManager().findFragmentByTag(currentName);
        }

        if (getCurrentTabItemContainer() == null) {
            return null;
        }
        return this.getCurrentTabItemContainer().getCurrentChild();
    }

    @Nullable
    public ParentFragment getCurrentTabItemContainer() {
        return (ParentFragment) ((MainPagerAdapter) this.mBinding.mainViewpager.getAdapter()).getCurrentPrimaryItem();
    }

    private void initPager() {
        Fragment calendarFragment = getFragmentCache(R.id.main_viewpager, 0);
        if (calendarFragment == null)
            calendarFragment = new CalendarFragment();
        Fragment consumeFragment = getFragmentCache(R.id.main_viewpager, 1);
        if (consumeFragment == null)
            consumeFragment = new ConsumeOfMonthFragment();
        Fragment calendarFragment2 = getFragmentCache(R.id.main_viewpager, 2);
        if (calendarFragment2 == null)
            calendarFragment2 = new CalendarFragment();
        ArrayList<Fragment> fragments = new ArrayList<>(TAB_NUM);
        fragments.add(calendarFragment);
        fragments.add(consumeFragment);
        fragments.add(calendarFragment2);

        pagerAdapter = new MainPagerAdapter(this);
        mBinding.mainViewpager.setAdapter(pagerAdapter);
        mBinding.mainViewpager.setOffscreenPageLimit(TAB_NUM);
        mBinding.mainTab.setupWithViewPager(mBinding.mainViewpager);
        mBinding.mainTab.addOnTabSelectedListener(this);

        initTabForPager();
    }

    private void initTabForPager() {
        final MainPagerAdapter adapter = (MainPagerAdapter) mBinding.mainViewpager.getAdapter();

        final int currentTheme = ThemeManager.getInstance().getCurrentTheme(this);
        final int tabColorRes = currentTheme == ThemeManager.DARK ? R.color.icon_tab_dark : R.color.icon_tab_light;

        final List<MainPagerAdapter.PagerItem> items = new ArrayList<>();

        final Bundle calendarItemArgs = new Bundle();
        calendarItemArgs.putString("host", CalendarFragment.class.getName());
        final TintDrawable iconCalendar = new TintDrawable(ResourcesCompat.getDrawable(getResources(), R.mipmap.ic_bottomtabbar_calendar, getTheme()));
        iconCalendar.setTintColor(ResourcesCompat.getColorStateList(getResources(), tabColorRes, getTheme()));
        items.add(new MainPagerAdapter.PagerItem(ParentFragment.class, iconCalendar, calendarItemArgs));


        final Bundle consumeItemArgs = new Bundle();
        consumeItemArgs.putString("host", ConsumeOfMonthFragment.class.getName());
        final TintDrawable iconConsume = new TintDrawable(ResourcesCompat.getDrawable(getResources(), R.mipmap.ic_bottomtabbar_record, getTheme()));
        iconConsume.setTintColor(ResourcesCompat.getColorStateList(getResources(), tabColorRes, getTheme()));
        items.add(new MainPagerAdapter.PagerItem(ParentFragment.class, iconConsume, consumeItemArgs));

        final Bundle discoverItemArgs = new Bundle();
        discoverItemArgs.putString("host", SettingFragment.class.getName());
        final TintDrawable iconSetting = new TintDrawable(ResourcesCompat.getDrawable(getResources(), R.mipmap.ic_bottomtabbar_discover, getTheme()));
        iconSetting.setTintColor(ResourcesCompat.getColorStateList(getResources(), tabColorRes, getTheme()));
        items.add(new MainPagerAdapter.PagerItem(ParentFragment.class, iconSetting, discoverItemArgs));

        adapter.addPagerItems(items, true);
        for (int i = 0; i < TAB_NUM; i++) {
            mBinding.mainTab.getTabAt(i).setIcon(items.get(i).getIcon());
        }

        this.setMainTab(true, true);
        this.mBinding.mainViewpager.setCurrentItem(0, false);

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
            moveTaskToBack(true);
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

    @Override
    public void onTabSelected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Nullable
    public String getLatestFragmentName() {
        return mLatestFragmentName;
    }

    public ViewGroup getRootView() {
        return this.mBinding.contentContainer;
    }

    /**
     * 设置 MainTab 表现
     *
     * @param showMainTab
     * 		显示 MainTab 传 true，隐藏传 false
     * @param runAnim
     * 		是否添加动画效果
     */
    public void setMainTab(final boolean showMainTab, final boolean runAnim) {
        if (showMainTab) {
            // MainTab 不可见，要求可见
            mBinding.mainTab.setVisibility(View.VISIBLE);
            if (runAnim) {
                mBinding.mainTab.animate().translationY(0).start();
            } else {
                mBinding.mainTab.setTranslationY(0);
            }
        } else {
            // MainTab 可见，要求不可见
            mBinding.mainTab.setVisibility(View.INVISIBLE);
            if (runAnim) {
                mBinding.mainTab.animate().translationY(mBinding.mainTab.getHeight()).start();
            } else {
                mBinding.mainTab.setTranslationY(mBinding.mainTab.getHeight());
            }
        }
    }
}
