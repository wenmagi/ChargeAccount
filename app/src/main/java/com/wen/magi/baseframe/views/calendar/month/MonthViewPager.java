package com.wen.magi.baseframe.views.calendar.month;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.wen.magi.baseframe.utils.Constants;

/**
 * Created by MVEN on 16/7/29.
 * <p/>
 * email: magiwen@126.com.
 */
public class MonthViewPager extends ViewPager {

    // ******* Declaration *********
    public static final int OFFSET = Constants.MAX_MONTH_SCROLL_COUNT / 2;

    /**
     * Enable swipe
     */
    private boolean enabled = true;

    // ******* Setter and getters *********
    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    // ************** Constructors ********************
    public MonthViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MonthViewPager(Context context) {
        super(context);
    }

    @Override
    public void setAdapter(PagerAdapter adapter) {
        super.setAdapter(adapter);
        // offset first element so that we can scroll to the left
        setCurrentItem(OFFSET);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (enabled) {
            return super.onTouchEvent(event);
        }
        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (enabled) {
            try {
                return super.onInterceptTouchEvent(event);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * ViewPager does not respect "wrap_content". The code below tries to
     * measure the height of the child and set the height of viewpager based on
     * child height
     * <p/>
     * It was customized from
     * http://stackoverflow.com/questions/9313554/measuring-a-viewpager
     * <p/>
     * Thanks Delyan for his brilliant code
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int calHeight = getMeasuredWidth() / 7 * 6;

        heightMeasureSpec = MeasureSpec.makeMeasureSpec(calHeight,
                MeasureSpec.EXACTLY);

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

}
