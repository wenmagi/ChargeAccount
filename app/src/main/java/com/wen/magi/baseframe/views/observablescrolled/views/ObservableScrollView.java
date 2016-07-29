package com.wen.magi.baseframe.views.observablescrolled.views;

/**
 * Created by MVEN on 16/5/31.
 */

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

import com.wen.magi.baseframe.views.observablescrolled.ObservableScrollCallback;
import com.wen.magi.baseframe.views.observablescrolled.ScrollState;
import com.wen.magi.baseframe.views.observablescrolled.Scrollable;


/**
 * ScrollView that its scroll position can be observed.
 */
public class ObservableScrollView extends ScrollView implements Scrollable {

    private int mPrevScrollY;
    private int mScrollY;

    private ObservableScrollCallback mCallbacks;
    private ScrollState mScrollState;

    public ObservableScrollView(Context context) {
        super(context);
    }

    public ObservableScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ObservableScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (hasNoCallbacks()) {
            return;
        }
        mScrollY = t;

        dispatchOnScrollChanged(t);

        if (mPrevScrollY < t) {
            mScrollState = ScrollState.DOWN;
        } else if (t < mPrevScrollY) {
            mScrollState = ScrollState.UP;
        } else {
            mScrollState = ScrollState.STOP;
        }
        mPrevScrollY = t;
    }

    @Override
    public int getCurrentScrollY() {
        return mScrollY;
    }

    @Override
    public void setScrollCallback(ObservableScrollCallback callback) {
        mCallbacks = callback;
    }


    private void dispatchOnScrollChanged(int scrollY) {
        if (mCallbacks != null) {
            mCallbacks.onScrollChanged(scrollY);
        }
    }


    private boolean hasNoCallbacks() {
        return mCallbacks == null;
    }

}
