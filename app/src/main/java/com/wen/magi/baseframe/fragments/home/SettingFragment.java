package com.wen.magi.baseframe.fragments.home;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wen.magi.baseframe.R;
import com.wen.magi.baseframe.activities.MainActivity;
import com.wen.magi.baseframe.base.BaseLazyLoadFragment;

/**
 * @author MVEN @ Zhihu Inc.
 * @since 10-02-2016
 */

public class SettingFragment  extends BaseLazyLoadFragment implements TabLayout.OnTabSelectedListener{

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.dialog_test_fragment, container, false);
        return root;
    }

    @Override
    protected void lazyLoad() {

    }

    @Override
    public void onPause() {
        super.onPause();
        MainActivity.from(getContext()).unregisterTabObserver(this);
    }

    @Override
    protected void OnClickView(View v) {

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
}
