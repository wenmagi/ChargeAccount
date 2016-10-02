package com.wen.magi.baseframe.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;

import com.wen.magi.baseframe.utils.LangUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MVEN on 16/8/19.
 * <p/>
 * email: magiwen@126.com.
 */


public class MainPagerAdapter extends FragmentPagerAdapter {

    private final List<PagerItem> mPagerItems;
    private Context mContext;
    @SuppressWarnings("unused")
    private Fragment mCurrentPrimaryItem;

    //已经存在的fragment实例
    private SparseArray<Fragment> mFragments = new SparseArray<>();

    @SuppressWarnings("unused")
    public MainPagerAdapter(final FragmentActivity activity) {
        super(activity.getSupportFragmentManager());

        mContext = activity;
        mPagerItems = new ArrayList<>();
    }

    @SuppressWarnings("unused")
    public MainPagerAdapter(final Fragment fragment) {
        super(fragment.getChildFragmentManager());

        mContext = fragment.getContext();
        mPagerItems = new ArrayList<>();
    }

    @SuppressWarnings("unused")
    public void addPagerItem(final PagerItem pagerItem) {
        mPagerItems.add(pagerItem);
        notifyDataSetChanged();
    }

    @SuppressWarnings("unused")
    public void addPagerItems(final List<PagerItem> pagerItems, final boolean clearOld) {
        if (clearOld) {
            clearAllItems();
        }

        for (PagerItem pagerItem : pagerItems) {
            mPagerItems.add(pagerItem);
        }
        notifyDataSetChanged();

    }

    public PagerItem getPagerItem(final int position) {
        return mPagerItems.get(position);
    }

    @Override
    public Fragment getItem(int position) {
        final PagerItem pagerItem = getPagerItem(position);

        Fragment fragment = Fragment.instantiate(mContext, pagerItem.getFragmentClass().getName(), pagerItem.getArguments());
        mFragments.put(position, fragment);

        return fragment;
    }

    /**
     * 强制触发 notifyDataSetChanged 时刷新 fragment
     *
     * @param object 获取该位置已经存在的实例
     * @return int
     */
    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return getPagerItem(position).getTitle();
    }

    @Override
    public int getCount() {
        return mPagerItems.size();
    }

    /**
     * 获取position位置已经存在的Fragment实例
     *
     * @param position 位置
     * @return Fragment实例
     */
    @SuppressWarnings("unused")
    public Fragment retrieveFragment(int position) {
        return mFragments.get(position);
    }

    /**
     * 获取当前展示的Fragment
     *
     * @return Fragment
     */
    @SuppressWarnings("unused")
    public Fragment getCurrentPrimaryItem() {
        return mCurrentPrimaryItem;
    }

    /**
     * 不断刷新当前展示的Fragment
     *
     * @param pContainer pContainer
     * @param pPosition pPosition
     * @param pObject pObject
     */
    @Override
    public void setPrimaryItem(final ViewGroup pContainer, final int pPosition, final Object pObject) {
        super.setPrimaryItem(pContainer, pPosition, pObject);

        this.mCurrentPrimaryItem = (Fragment) pObject;
    }

    /**
     * 清除所有Item
     */
    private void clearAllItems() {
        if (LangUtils.isEmpty(mPagerItems))
            return;

        mPagerItems.clear();
        notifyDataSetChanged();
    }

    /**
     * RecyclerView数据类
     */
    public static class PagerItem {
        private final Class<? extends Fragment> _fragmentClass;
        private CharSequence _title;
        private final int _iconResId;
        private final Drawable _icon;
        private final Bundle _arguments;

        @SuppressWarnings("unused")
        public PagerItem(final Class<? extends Fragment> fragmentClass, final CharSequence title) {
            this(fragmentClass, title, null);
        }

        @SuppressWarnings("unused")
        public PagerItem(final Class<? extends Fragment> fragmentClass, final CharSequence title, final Bundle arguments) {
            this(fragmentClass, title, 0, arguments);
        }

        @SuppressWarnings("unused")
        public PagerItem(final Class<? extends Fragment> fragmentClass, final int iconResId) {
            this(fragmentClass, iconResId, null);
        }

        @SuppressWarnings("unused")
        public PagerItem(final Class<? extends Fragment> fragmentClass, final Drawable icon) {
            this(fragmentClass, icon, null);
        }

        @SuppressWarnings("unused")
        public PagerItem(final Class<? extends Fragment> fragmentClass, final int iconResId, final Bundle arguments) {
            this(fragmentClass, null, iconResId, arguments);
        }

        @SuppressWarnings("unused")
        public PagerItem(final Class<? extends Fragment> fragmentClass, final Drawable icon, final Bundle arguments) {
            this(fragmentClass, null, icon, arguments);
        }

        @SuppressWarnings("unused")
        public PagerItem(final Class<? extends Fragment> fragmentClass, final CharSequence title, final int iconResId, final Bundle arguments) {
            _fragmentClass = fragmentClass;
            _title = title;
            _iconResId = iconResId;
            _arguments = arguments;
            _icon = null;
        }

        @SuppressWarnings("unused")
        public PagerItem(final Class<? extends Fragment> fragmentClass, final CharSequence title, final Drawable icon, final Bundle arguments) {
            _fragmentClass = fragmentClass;
            _title = title;
            _iconResId = 0;
            _arguments = arguments;
            _icon = icon;
        }

        public Class<? extends Fragment> getFragmentClass() {
            return _fragmentClass;
        }

        public CharSequence getTitle() {
            return _title;
        }

        @SuppressWarnings("unused")
        public int getIconResId() {
            return _iconResId;
        }

        public Drawable getIcon() {
            return _icon;
        }

        public Bundle getArguments() {
            return _arguments;
        }
    }


}