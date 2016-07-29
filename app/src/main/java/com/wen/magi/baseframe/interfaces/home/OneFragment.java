package com.wen.magi.baseframe.interfaces.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wen.magi.baseframe.R;
import com.wen.magi.baseframe.base.BaseFragment;
import com.wen.magi.baseframe.base.BaseLazyLoadFragment;

/**
 * Created by MVEN on 16/7/6.
 * <p/>
 * email: magiwen@126.com.
 */


public class OneFragment extends BaseLazyLoadFragment {

    @Nullable
    @Override
    public View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.dialog_test_fragment, container, false);
        return root;
    }

    @Override
    protected void lazyLoad() {

    }

    @Override
    protected void OnClickView(View v) {

    }
}
