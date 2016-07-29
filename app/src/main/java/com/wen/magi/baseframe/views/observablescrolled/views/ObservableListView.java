package com.wen.magi.baseframe.views.observablescrolled.views;

import android.content.Context;
import android.util.AttributeSet;
import android.util.SparseIntArray;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

import com.wen.magi.baseframe.views.observablescrolled.ObservableScrollCallback;
import com.wen.magi.baseframe.views.observablescrolled.ScrollState;
import com.wen.magi.baseframe.views.observablescrolled.Scrollable;

/**
 * Created by MVEN on 16/6/23.
 * <p/>
 * email: magiwen@126.com.
 */


public class ObservableListView extends ListView implements Scrollable {

    private ObservableScrollCallback mCallback;

    private SparseIntArray mChildHeights;

    private int mPreFirstVisiblePosition;

    private int mPrevScrolledChildrenHeight;

    private int mPrevFirstVisibleChildHeight;

    private int mScrollY;
    private int mPrevScrollY;

    private ScrollState mScrollState;

    private OnScrollListener onScrollListener = new OnScrollListener() {
        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {

        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            onScrolling(firstVisibleItem);
        }
    };

    public ObservableListView(Context context) {
        super(context);
        init();
    }

    public ObservableListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ObservableListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mChildHeights = new SparseIntArray();
        setOnScrollListener(onScrollListener);
    }

    @Override
    public int getCurrentScrollY() {
        return 0;
    }

    @Override
    public void setScrollCallback(ObservableScrollCallback callback) {
        mCallback = callback;
    }

    private void onScrolling(int firstVisibleItem) {

        if (hasNoCallbacks() || getChildCount() <= 0)
            return;

        for (int i = firstVisibleItem, j = 0; i < getLastVisiblePosition(); i++, j++) {
            if (getChildAt(j).getHeight() != mChildHeights.indexOfKey(i) || mChildHeights.indexOfKey(i) < 0) {
                mChildHeights.put(i, getChildAt(j).getHeight());
            }
        }

        View firstVisibleChild = getChildAt(0);
        if (firstVisibleChild == null)
            return;
        if (mPreFirstVisiblePosition < firstVisibleItem) {
            // scroll up
            int skippedChildrenHeight = 0;
            if (firstVisibleItem - mPreFirstVisiblePosition != 1) {
                for (int i = firstVisibleItem - 1; i > mPreFirstVisiblePosition; i--) {
                    if (0 < mChildHeights.indexOfKey(i)) {
                        skippedChildrenHeight += mChildHeights.get(i);
                    } else {
                        skippedChildrenHeight += firstVisibleChild.getHeight();
                    }
                }
            }
            mPrevScrolledChildrenHeight += mPrevFirstVisibleChildHeight + skippedChildrenHeight;
            mPrevFirstVisibleChildHeight = firstVisibleChild.getHeight();
        } else if (firstVisibleItem < mPreFirstVisiblePosition) {
            // scroll down
            int skippedChildrenHeight = 0;
            if (mPreFirstVisiblePosition - firstVisibleItem != 1) {
                for (int i = mPreFirstVisiblePosition - 1; i > firstVisibleItem; i--) {
                    if (0 < mChildHeights.indexOfKey(i)) {
                        skippedChildrenHeight += mChildHeights.get(i);
                    } else {
                        skippedChildrenHeight += firstVisibleChild.getHeight();
                    }
                }
            }
            mPrevScrolledChildrenHeight -= firstVisibleChild.getHeight() + skippedChildrenHeight;
            mPrevFirstVisibleChildHeight = firstVisibleChild.getHeight();
        } else if (firstVisibleItem == 0) {
            mPrevFirstVisibleChildHeight = firstVisibleChild.getHeight();
            mPrevScrolledChildrenHeight = 0;
        }

        if (mPrevFirstVisibleChildHeight < 0) {
            mPrevFirstVisibleChildHeight = 0;
        }
        mScrollY = mPrevScrolledChildrenHeight - firstVisibleChild.getTop() +
                firstVisibleItem * getDividerHeight() + getPaddingTop();
        mPreFirstVisiblePosition = firstVisibleItem;

        dispatchOnScrollChanged(mScrollY);

        if (mPrevScrollY < mScrollY) {
            mScrollState = ScrollState.DOWN;
        } else if (mScrollY < mPrevScrollY) {
            mScrollState = ScrollState.UP;
        } else {
            mScrollState = ScrollState.STOP;
        }
        mPrevScrollY = mScrollY;
    }

    private boolean hasNoCallbacks() {
        return mCallback == null;
    }

    private void dispatchOnScrollChanged(int scrollY) {
        if (mCallback != null) {
            mCallback.onScrollChanged(scrollY);
            mCallback.onScrollStateChanged(mScrollState);
        }
    }
}
