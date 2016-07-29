package com.wen.magi.baseframe.views;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.widget.ListView;

import com.wen.magi.baseframe.utils.LogUtils;
import com.wen.magi.baseframe.utils.SysUtils;

/**
 * Created by MVEN on 16/6/23.
 * <p/>
 * email: magiwen@126.com.
 */

/**
 * 仿iOS弹性ListView
 */

public class BounceListView extends ListView {
    private static final int MAX_Y_OVERSCROLL_DISTANCE = 200;

    private int mMaxYOverscrollDistance;

    public BounceListView(Context context) {
        super(context);
        initBounceListView();
    }

    public BounceListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initBounceListView();
    }

    public BounceListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initBounceListView();
    }

    private void initBounceListView() {
        //get the density of the screen and do some maths with it on the max overscroll distance
        //variable so that you get similar behaviors no matter what the screen size

        mMaxYOverscrollDistance = (int) (SysUtils.DENSITY * MAX_Y_OVERSCROLL_DISTANCE);
    }

    @Override
    protected boolean overScrollBy(int deltaX, int deltaY, int scrollX,
                                   int scrollY, int scrollRangeX, int scrollRangeY,
                                   int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {
        if (!isTouchEvent) { // 禁止惯性滑动
            if ((scrollY < 0 && deltaX < 0)
                    || (scrollY > getHeight() && deltaX > 0)) {
                deltaY = 0;
            }
        }
        LogUtils.e("wwwwwwwwww %s", scrollY);
        return super.overScrollBy(deltaX, (deltaY + 1) / 2, scrollX, scrollY, scrollRangeX, scrollRangeY, maxOverScrollX, mMaxYOverscrollDistance, isTouchEvent);
    }


}
