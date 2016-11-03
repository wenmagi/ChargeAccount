package com.wen.magi.baseframe.activities;

import android.Manifest;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.wen.magi.baseframe.R;
import com.wen.magi.baseframe.adapters.MainPagerAdapter;
import com.wen.magi.baseframe.base.BaseActivity;
import com.wen.magi.baseframe.base.BaseFragment;
import com.wen.magi.baseframe.base.graphics.TintDrawable;
import com.wen.magi.baseframe.databinding.ActivityMainBinding;
import com.wen.magi.baseframe.fragments.ParentFragment;
import com.wen.magi.baseframe.fragments.home.ConsumeOfMonthFragment;
import com.wen.magi.baseframe.fragments.home.SettingFragment;
import com.wen.magi.baseframe.fragments.home.calendar.CalendarFragment;
import com.wen.magi.baseframe.managers.AppManager;
import com.wen.magi.baseframe.managers.ThemeManager;
import com.wen.magi.baseframe.models.Income;
import com.wen.magi.baseframe.utils.AppIntent;
import com.wen.magi.baseframe.utils.ConsumeTypeUtils;
import com.wen.magi.baseframe.utils.LogUtils;
import com.wen.magi.baseframe.utils.ViewUtils;
import com.wen.magi.baseframe.widgets.FooterBehavior;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends BaseActivity implements TabLayout.OnTabSelectedListener, FragmentManager.OnBackStackChangedListener {

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

    private FooterBehavior footerBehavior;
    private List<TabLayout.OnTabSelectedListener> mTabObservers = new ArrayList<>();
    private int currentTab;

    public static MainActivity from(final Context pContext) {
        if (pContext instanceof MainActivity) {
            return (MainActivity) pContext;
        } else if (pContext instanceof ContextWrapper) {
            Context baseContext = ((ContextWrapper) pContext).getBaseContext();
            if (baseContext instanceof MainActivity) {
                return (MainActivity) baseContext;
            }
        }
        throw new IllegalArgumentException(pContext.getClass().getName() + " is not MainActivity");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_PHONE_STATE)) {
                LogUtils.e("wwwwwwwww permission denied, show dialog");
            } else {
                LogUtils.e("wwwwwwwwwwww request requestPermissions failed");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, 1);
            }
        } else {
            LogUtils.e("wwwwwwwwwwww request permission failed");
        }
        initPager();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                }
                return;
            }
        }
    }


    private void accessContacts() {
        //your code once you receive permission
    }

    @Override
    protected void onStart() {
        super.onStart();
        LogUtils.e("wwwwwwwwwwww onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        LogUtils.e("wwwwwwwwwwww onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (getCurrentDisplayFragment() instanceof BaseFragment) {
            mLatestFragmentName = getCurrentDisplayFragment().getClass().getName();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getSupportFragmentManager().removeOnBackStackChangedListener(this);
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

        pagerAdapter = new MainPagerAdapter(this);
        mBinding.mainViewpager.setAdapter(pagerAdapter);
        mBinding.mainViewpager.setOffscreenPageLimit(TAB_NUM);
        mBinding.mainTab.setupWithViewPager(mBinding.mainViewpager);
        mBinding.mainTab.addOnTabSelectedListener(this);
        Income income = new Income(1l, 10l, "title", "descddddddddd", 109l, new Date(), new Date(), new Date(), null, null, null);
        AppManager.dbManager.saveIncome(income);
        Income dbIncome = AppManager.dbManager.loadIncomeByIncomeId(109l);
        if (dbIncome != null)
            LogUtils.e("wwwwwwwwww %s", dbIncome.getDesc());
        initTabForPager();
    }

    private void initTabForPager() {

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

        pagerAdapter.addPagerItems(items, true);
        for (int i = 0; i < TAB_NUM; i++) {
            mBinding.mainTab.getTabAt(i).setIcon(pagerAdapter.getPagerItem(i).getIcon());
        }

        this.setMainTab(true, true);
        this.mBinding.mainViewpager.setCurrentItem(0, false);
        footerBehavior = new FooterBehavior(this, null);
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

    /**
     * 需要监听 Tab 事件的地方，实现 {@link TabLayout.OnTabSelectedListener}，在这里进行注册。
     */
    public void registerTabObserver(final TabLayout.OnTabSelectedListener tabObserver) {
        if (!mTabObservers.contains(tabObserver)) {
            mTabObservers.add(tabObserver);
        }
    }

    /**
     * 需要监听 Tab 事件的地方，实现 {@link TabLayout.OnTabSelectedListener}，在这里取消注册。
     */
    public void unregisterTabObserver(final TabLayout.OnTabSelectedListener tabObserver) {
        if (mTabObservers.contains(tabObserver)) {
            mTabObservers.remove(tabObserver);
        }
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        this.mBinding.mainViewpager.setCurrentItem(tab.getPosition(), false);

        for (TabLayout.OnTabSelectedListener tabObserver : mTabObservers) {
            tabObserver.onTabSelected(tab);
        }

        this.mBinding.mainTab.post(new Runnable() {

            @Override
            public void run() {
                if ((Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1 && MainActivity.this.isFinishing()) || (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && MainActivity.this.isDestroyed())) {
                    // Bug Fix https://fabric.io/zhihubeta/android/apps/com.zhihu.android/issues/573b376bffcdc04250fb4c72
                    return;
                }

                final ParentFragment currentTabItemContainer = MainActivity.this.getCurrentTabItemContainer();

                if (currentTabItemContainer != null) {
                    currentTabItemContainer.onSelected();
                }
            }
        });
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {
        for (TabLayout.OnTabSelectedListener tabObserver : mTabObservers) {
            tabObserver.onTabUnselected(tab);
        }
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
     * @param showMainTab 显示 MainTab 传 true，隐藏传 false
     * @param runAnim     是否添加动画效果
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

    @Override
    public void onBackStackChanged() {
    }

    /**
     * 启动新的Fragment
     *
     * @param pIntent 启动数据
     */
    @SuppressWarnings("unused")
    public void startFragment(@Nullable final AppIntent pIntent) {
        if (pIntent == null)
            return;

        mLatestFragmentName = null;

        if (pIntent.isPopSelf())
            popupCurrentDisplayFragment();

        boolean startTopLevelFragment = tryToStartTopLevelFragment(pIntent);

        if (startTopLevelFragment)
            return;

        int tab = pIntent.getPriorityTab();
        if (tab != 0 && tab != getCurrentTab())
            mBinding.mainViewpager.setCurrentItem(tab, false);

        final Fragment currentFragment = getCurrentDisplayFragment();
        if (currentFragment != null && pIntent.getTag().equals(getCurrentDisplayFragment().getTag()))
            return;

        this.tryFinishActionMode();

        if (pIntent.isOverlay())
            addFragmentToOverlay(pIntent);
        else if (getCurrentTabItemContainer() != null) {
            final Fragment fragment = Fragment.instantiate(this, pIntent.getClazz().getName(), pIntent.getArguments());
            addFragmentToBackStack(fragment, pIntent.getTag());
        }
    }

    private void addFragmentToBackStack(final Fragment fragment, final String tag) {
        if (fragment instanceof ParentFragment.Child) {
            final boolean showBottomNavigation = ((ParentFragment.Child) fragment).isShowBottomNavigation();
            setMainTab(showBottomNavigation, true);
        } else {
            setMainTab(true, true);
        }

        removeSnackBar();

        getCurrentTabItemContainer().addChild(fragment, tag);
    }

    private void removeSnackBar() {
        if (((ViewGroup) getSnackBarContainer()).getChildCount() > 0) {
            ((ViewGroup) getSnackBarContainer()).removeAllViews();
        }
    }

    public View getSnackBarContainer() {
        return this.mBinding.contentContainer;
    }


    private void addFragmentToOverlay(final AppIntent pIntent) {
        addFragmentToOverlay(pIntent, null, 0);
    }

    public void addFragmentToOverlay(@Nullable final AppIntent pIntent, final Fragment pTargetFragment, final int pRequestCode) {
        if (pIntent == null)
            return;
        final FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        final Fragment fragment = Fragment.instantiate(this, pIntent.getClazz().getName(), pIntent.getArguments());
        //Optional for fragment,when fragment is finished,pTargetFragment can receive the results.
        if (pTargetFragment != null)
            fragment.setTargetFragment(pTargetFragment, pRequestCode);

        fragmentTransaction.add(R.id.overlay_container, fragment, pIntent.getTag());
        fragmentTransaction.addToBackStack(pIntent.getTag());

        fragmentTransaction.commitAllowingStateLoss();
    }

    private boolean tryToStartTopLevelFragment(final AppIntent pIntent) {
        return false;
    }

    private void popupCurrentDisplayFragment() {

    }

    public int getCurrentTab() {
        return mBinding.mainViewpager.getCurrentItem();
    }
}
