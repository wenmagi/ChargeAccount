package com.wen.magi.baseframe.views.calendar.week;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.wen.magi.baseframe.utils.ViewUtils;
import com.wen.magi.baseframe.views.calendar.SlideDrawerLayout;

import java.util.Timer;
import java.util.TimerTask;

public class WeekViewPager extends ViewGroup {

    private float firstDownX;
    private int currId = 1;
    private OnWeekChangedListener onWeekChangedListener;

    private float downX = .0f, downY = .0f;
    private int x = 0, currentX = 0;
    private volatile boolean isAnimation = false;
    private volatile boolean isUpOrDown = false;
    private SlideDrawerLayout mSlidingLayout;
    private int nUpOrDown = -1;// -1: not; 0: up; 1:down
    private Timer animationTimer;
    private static final int ANIMATION_PERIOD = 10;
    private static final int ANIMATION_DURATION = 200;

    private int nMoveDistanceMin = 10;

    public boolean isUpOrDown() {
        return isUpOrDown;
    }

    public void setUpOrDown(boolean isUpOrDown) {
        this.isUpOrDown = isUpOrDown;
    }

    public void setSlidingLayout(SlideDrawerLayout slidingLayout) {
        this.mSlidingLayout = slidingLayout;
    }

    public WeekViewPager(Context context) {
        this(context, null);
    }

    public WeekViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        nMoveDistanceMin = ViewUtils.dp2pix(10);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        for (int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i);
            view.layout(0 + (i - 1) * getWidth(), 0, i * getWidth(),
                    getHeight());
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                firstDownX = downX = event.getX();
                downY = event.getY();
                clearTimer();
            case MotionEvent.ACTION_MOVE:
                float mx = event.getX();
                float my = event.getY();

                float rate = (mx - downX) / (my - downY);
                if (Math.abs(rate) >= 2 && Math.abs(mx - downX) > nMoveDistanceMin) {
                    isUpOrDown = false;
                    nUpOrDown = -1;
                    return true;
                } else if (Math.abs(rate) <= 0.5
                        && Math.abs(my - downY) > nMoveDistanceMin) {

                    isUpOrDown = true;
                    if (my - downY > 0)
                        nUpOrDown = 0;
                    else
                        nUpOrDown = 1;
                    return true;
                } else {
                    isUpOrDown = false;
                    nUpOrDown = -1;
                }
                break;
            case MotionEvent.ACTION_UP:
                break;
            default:
                break;
        }
        return false;
        // return super.onInterceptTouchEvent(ev);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (isAnimation) {
            return false;
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                firstDownX = downX = event.getX();
                downY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                float currentX = event.getX();
                scrollTo((int) (firstDownX - currentX), 0);
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                if (isUpOrDown && mSlidingLayout != null) {
                    if (nUpOrDown == 0)
                        mSlidingLayout.slideToBottom(250);
                    scrollBackToCenter();
                    return true;
                }

                int nextId = 0;
                if (event.getX() - firstDownX > getWidth() / 6) {
                    // move left
                    nextId = (currId - 1) <= 0 ? 0 : currId - 1;
                } else if (firstDownX - event.getX() > getWidth() / 6) {
                    // move right
                    nextId = currId + 1;
                } else {
                    nextId = currId;
                }

                moveToDest(nextId);
                break;
            default:
                break;
        }
        return true;
    }

    /**
     * @param nextId
     */
    private void moveToDest(int nextId) {
        currId = (nextId >= 0) ? nextId : 0;
        currId = (nextId <= getChildCount() - 1) ? nextId
                : (getChildCount() - 1);
        int distanceX = (currId - 1) * getWidth() - getScrollX();
        scrollAnimationStart(getScrollX(), 0, distanceX, 0);
    }

    private void scrollAnimationStart(final int startX, int startY,
                                      final int distanceX, final int distanceY) {
        isAnimation = true;
        x = startX;
        animationTimer = new Timer();
        animationTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                currentX = x + distanceX * ANIMATION_PERIOD
                        / ANIMATION_DURATION;
                if ((startX + distanceX == 0 && ((startX >= 0 && currentX <= 0) || (startX < 0 && currentX >= 0)))
                        || ((startX + distanceX != 0) && ((startX >= 0 && currentX >= startX
                        + distanceX) || (startX < 0 && currentX <= startX
                        + distanceX)))) {
                    clearTimer();
                    if (onWeekChangedListener != null) {
                        if (currId < 1) {
                            onWeekChangedListener.onPreparePrevWeek();
                            scrollBackToCenter();
                            onWeekChangedListener.onPrevWeek();
                        } else if (currId > 1) {
                            onWeekChangedListener.onPrepareNextWeek();
                            scrollBackToCenter();
                            onWeekChangedListener.onNextWeek();
                        }
                    }
                } else {
                    x = currentX;
                    scrollTo((int) currentX, 0);
                }
            }
        }, 0, ANIMATION_PERIOD);
    }

    private void scrollBackToCenter() {
        ViewUtils.post(new Runnable() {
            @Override
            public void run() {
                scrollTo(0, 0);
                currId = 1;
            }
        });
    }

    public void clearTimer() {
        if (animationTimer != null) {
            animationTimer.cancel();
            animationTimer = null;
        }
        isAnimation = false;
        isUpOrDown = false;
    }

    @Override
    public void computeScroll() {
    }

    public OnWeekChangedListener getOnWeekChangedListener() {
        return onWeekChangedListener;
    }

    public void setOnPageChangedListener(
            OnWeekChangedListener onPageChangedListener) {
        this.onWeekChangedListener = onPageChangedListener;
    }

    public interface OnWeekChangedListener {
        void onPrepareNextWeek();

        void onPreparePrevWeek();

        void onNextWeek();

        void onPrevWeek();
    }
}
