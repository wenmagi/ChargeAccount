package com.wen.magi.baseframe.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

import com.wen.magi.baseframe.utils.SysUtils;

/**
 * Created by MVEN on 16/6/23.
 * <p/>
 * email: magiwen@126.com.
 */

/**
 * 仿iOS弹性ScrollView
 */
public class BounceScrollView extends ScrollView {

    private static final int MAX_Y_OVERSCROLL_DISTANCE = 200;

    private int mMaxYOverscrollDistance;

    public BounceScrollView(Context context) {
        super(context);
        initBounceScrollView();
    }

    public BounceScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initBounceScrollView();
    }

    public BounceScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initBounceScrollView();
    }

    private void initBounceScrollView() {
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
        return super.overScrollBy(deltaX, (deltaY + 1) / 2, scrollX, scrollY, scrollRangeX, scrollRangeY, maxOverScrollX, mMaxYOverscrollDistance, isTouchEvent);
    }
}
