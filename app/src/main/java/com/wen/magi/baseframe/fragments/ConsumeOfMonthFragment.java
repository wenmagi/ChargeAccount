package com.wen.magi.baseframe.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wen.magi.baseframe.R;
import com.wen.magi.baseframe.annotations.From;
import com.wen.magi.baseframe.base.BaseLazyLoadFragment;
import com.wen.magi.baseframe.utils.LogUtils;
import com.wen.magi.baseframe.utils.ViewUtils;
import com.wen.magi.baseframe.views.ultra.PtrFrameLayout;
import com.wen.magi.baseframe.views.ultra.PtrHandler;
import com.wen.magi.baseframe.views.ultra.PtrUIHandler;
import com.wen.magi.baseframe.views.ultra.header.StoreHouseHeader;
import com.wen.magi.baseframe.views.ultra.indicator.PtrIndicator;

/**
 * Created by MVEN on 16/9/14.
 * <p/>
 * email: magiwen@126.com.
 */


public class ConsumeOfMonthFragment extends BaseLazyLoadFragment {

    @From(R.id.store_house_ptr_frame)
    private PtrFrameLayout ptrFrameLayout;

    final String[] mStringList = {"NOTEHEART", "MAGI"};

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_consume_of_month, null);
        return rootView;
    }

    @Override
    protected void lazyLoad() {
        initPtrFrame();
    }

    @Override
    protected void OnClickView(View v) {

    }

    private void initPtrFrame() {

        final StoreHouseHeader header = new StoreHouseHeader(activity);
        header.setPadding(0, ViewUtils.dp2pix(15), 0, 0);

        /**
         * using a string, support: A-Z 0-9 - .
         * you can add more letters by {@link in.srain.cube.views.ptr.header.StoreHousePath#addChar}
         */
        header.initWithString(mStringList[0]);
        // for changing string
        ptrFrameLayout.addPtrUIHandler(new PtrUIHandler() {

            private int mLoadTime = 0;

            @Override
            public void onUIReset(PtrFrameLayout frame) {
                mLoadTime++;
                String string = mStringList[mLoadTime % mStringList.length];
                header.initWithString(string);
            }

            @Override
            public void onUIRefreshPrepare(PtrFrameLayout frame) {
                String string = mStringList[mLoadTime % mStringList.length];
//                setHeaderTitle(mTitlePre + string);
            }

            @Override
            public void onUIRefreshBegin(PtrFrameLayout frame) {

            }

            @Override
            public void onUIRefreshComplete(PtrFrameLayout frame) {

            }

            @Override
            public void onUIPositionChange(PtrFrameLayout frame, boolean isUnderTouch, byte status, PtrIndicator ptrIndicator) {

            }
        });

        ptrFrameLayout.setDurationToCloseHeader(3000);
        ptrFrameLayout.setHeaderView(header);
        ptrFrameLayout.addPtrUIHandler(header);
        ptrFrameLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                ptrFrameLayout.autoRefresh(false);
            }
        }, 100);

        ptrFrameLayout.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return true;
            }

            @Override
            public void onRefreshBegin(final PtrFrameLayout frame) {
                frame.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        frame.refreshComplete();
                    }
                }, 2000);
            }
        });
    }

}
