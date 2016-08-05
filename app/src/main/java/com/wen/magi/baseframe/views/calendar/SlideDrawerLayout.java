package com.wen.magi.baseframe.views.calendar;

import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.wen.magi.baseframe.utils.LogUtils;
import com.wen.magi.baseframe.utils.ViewUtils;


public class SlideDrawerLayout extends RelativeLayout {

    private int nMoveDistanceMin = 10;

    public interface SlidingEventListener {
        void startSliding(boolean isExpand);

        void onSlidingFinish(boolean expand);
    }

    public SlideDrawerLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        nMoveDistanceMin = ViewUtils.dp2pix(10);
    }

    public SlideDrawerLayout(Context context) {
        super(context);
        nMoveDistanceMin = ViewUtils.dp2pix(10);
    }

    private boolean isExpand = false;
    private SlidingEventListener mSlidingListener;

    public SlidingEventListener getSlidingListener() {
        return mSlidingListener;
    }

    public void setSlidingListener(SlidingEventListener slidingListener) {
        mSlidingListener = slidingListener;
    }

    public float getExpandTop() {
        return expandTop;
    }

    public void setExpandTop(float expandTop, boolean hideCalendar) {
        this.expandTop = expandTop;
        if (hideCalendar)
            updateLayout(expandTop);
    }

    public boolean isExpand() {
        return isExpand;
    }

    public void setExpand(final boolean expand) {
        isExpand = expand;
        final SlidingEventListener l = getSlidingListener();
        ViewUtils.runInHandlerThread(new Runnable() {

            @Override
            public void run() {
                if (l != null) {
                    l.onSlidingFinish(expand);
                }
            }
        });

    }

    /******
     * start top value
     ********/
    private float currentY = .0f;
    private float originalY = .0f;
    private double currentTime = 0l;
    private float speed = 1.0f;
    private float lastSpeed = 1.0f;
    private static final int BACK_DELAY = 10;
    private float expandTop = .0f;

    protected String TAG = "KiwiDrawer";
    private boolean isSliding = false;
    private boolean isAnimation = false;

    private float downX = .0f, downY = .0f;

    public boolean onInterceptTouchEvent(MotionEvent ev) {
        super.onInterceptTouchEvent(ev);

        final int action = ev.getActionMasked();
        if (action == MotionEvent.ACTION_DOWN && ev.getEdgeFlags() != 0) {
            return false;
        }

        boolean isIntercept = false;
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                downX = ev.getX();
                downY = ev.getY();
                originalY = currentY = ev.getRawY();
                currentTime = System.currentTimeMillis();
                clearTimer();
                break;
            case MotionEvent.ACTION_MOVE:
                float mx = ev.getX();
                float my = ev.getY();
                float rate = (my - downY) / (mx - downX);
                if (Math.abs(rate) >= 2 && Math.abs(my - downY) > nMoveDistanceMin) {
                    isIntercept = true;
                }
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:

                break;
            case MotionEvent.ACTION_POINTER_UP:
                break;
        }
        return isIntercept;
    }

    @SuppressLint("ClickableViewAccessibility")
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        if (isAnimation) {
            LogUtils.e("Animation is going ,return");
            return false;
        }
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                originalY = currentY = event.getRawY();
                currentTime = System.currentTimeMillis();
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                isSliding = false;
                float rawY = event.getRawY();
                float offY = .0f;
                speed = Math.abs(lastSpeed) > Math.abs(speed) ? lastSpeed : speed;
                offY = rawY - originalY;
                boolean isBack = speed == 0 ? Math.abs(offY) <= Math.abs(expandTop) / 3
                        : isExpand && speed > 0 || (!isExpand && speed < 0);
                float dis = isBack ? (isExpand ? -latestTop : latestTop)
                        : (isExpand() ? latestTop - expandTop : -expandTop
                        - latestTop);
                slideToEndAnimation(checkDirection(dis), dis, isBack);
                break;
            case MotionEvent.ACTION_MOVE:
                rawY = event.getRawY();
                long t = System.currentTimeMillis();
                lastSpeed = speed;
                speed = (rawY - currentY) / (float) (t - currentTime);
                if (!isSliding) {
                    isSliding = true;
                    SlidingEventListener l = getSlidingListener();
                    if (l != null) {
                        if ((speed < 0 && isExpand) || (speed > 0 && !isExpand))
                            l.startSliding(!isExpand);
                    }
                }
                currentY = rawY;
                currentTime = t;
                offY = rawY - originalY;
                scroll(offY);
                break;

            default:
                break;
        }
        return true;
    }

    private void scroll(float top) {
        if (isExpand) {
            if (top >= 0)
                top = 0;
            else if (top <= expandTop) {
                top = expandTop;
            }
        }
        if (!isExpand) {
            if (top >= -expandTop) {
                top = -expandTop;
            } else if (top <= 0) {
                top = 0;
            }
        }
        latestTop = top;
        scrollTo(0, -(int) top);
    }

    private float latestTop = 0;

    private void updateLayout(float top) {
        if (top >= 0) {
            top = 0;
        }
        if (top <= expandTop) {
            top = expandTop;
        }
        MarginLayoutParams params = (MarginLayoutParams) getLayoutParams();
        params.topMargin = (int) top;

        ViewGroup p = (ViewGroup) getParent();
        if (p != null)
            p.updateViewLayout(this, params);
        if (getScrollY() != 0) {
            scrollTo(0, 0);
        }
    }

    /**
     * true top, false bottom
     *
     * @param dis
     * @return
     */
    private boolean checkDirection(float dis) {
        return speed < 0;
    }

    private float startY = 0.f;
    private float y = 0.f;

    public void slideToBottom(int duration) {
        slideToEndAnimation(false, -getExpandTop(), false, duration);
    }

    private void slideToEndAnimation(final boolean toTop, float dis,
                                     final boolean isBack) {
        slideToEndAnimation(toTop, dis, isBack, 300);
    }

    /**
     * slide the screen to the original
     *
     * @param
     */
    private void slideToEndAnimation(final boolean toTop, float dis,
                                     final boolean isBack, int duration) {
        isAnimation = true;
        if (dis <= 0) {
            if (isBack) {
                isAnimation = false;
                return;
            }
            updateLayout(!isExpand ? 0 : expandTop);
            setExpand(!isExpand);
            isAnimation = false;
            return;
        }
        long dura = (long) (Math.abs(dis * duration / expandTop));

        startY = (float) getScrollY();
        final float s = (toTop ? dis : -dis) / dura;
        startY -= (s * BACK_DELAY);
        timer = new Timer();
        timer.schedule(new TimerTask() {

            public void run() {
                y = (startY + (s * BACK_DELAY));

                startY = y;
                if ((toTop && (isBack ? y >= 0 : y >= -expandTop))
                        || (!toTop && (isBack ? y <= 0 : y <= expandTop))) {
                    if (!isBack) {

                        if (toTop && y >= 0) {
                            setExpand(false);
                        } else if (!toTop && y <= expandTop) {
                            setExpand(true);
                        }
                    }
                    if (timer != null) {
                        timer.cancel();
                        timer = null;
                    }
                    y = 0.f;
                    isAnimation = false;
                }
                final int realY = (int) y;
                ViewUtils.post(new Runnable() {

                    public void run() {
                        if (realY == 0) {
                            updateLayout(isExpand ? 0 : expandTop);
                        }
                        scrollTo(0, realY);
                    }
                });

            }
        }, 0, BACK_DELAY);
    }

    private Timer timer;

    private void clearTimer() {
        int scrollY = getScrollY();
        if (scrollY == 0 && timer != null) {
            timer.cancel();
            timer = null;
            isAnimation = false;
            LogUtils.w("clear Timer maybe exception ,  isExpand %s", isExpand);
            updateLayout(isExpand ? 0 : expandTop);
        }

    }

    private StayViewChanger stayViewChanger = null;
    private float currentWeekMonthHeight = .0f;
    private int currentWeekMonthRowIndex = 0;

    public void computeScroll() {
        if (stayViewChanger != null) {
            int srollY = getScrollY();
            if (srollY >= currentWeekMonthRowIndex * currentWeekMonthHeight) {
                stayViewChanger.onStayViewShow();
                if (srollY != 0 && currentWeekMonthRowIndex != -1)
                    stayViewChanger.onStayViewShow();
                else
                    stayViewChanger.onStayViewGone();
            } else {
                if (getTop() != (int) expandTop)
                    stayViewChanger.onStayViewGone();
            }

            if (srollY < 0) {
                if (srollY < currentWeekMonthRowIndex * currentWeekMonthHeight
                        + expandTop)
                    stayViewChanger.onStayViewGone();
                else
                    stayViewChanger.onStayViewShow();
            }

            if (getTop() == (int) expandTop && getScrollY() >= 0)
                stayViewChanger.onStayViewShow();
            if (getScrollY() == 0 && getTop() == 0)
                stayViewChanger.onStayViewGone();

        }
    }

    public StayViewChanger getStayViewChanger() {
        return stayViewChanger;
    }

    public void setStayViewChanger(StayViewChanger stayViewChanger) {
        this.stayViewChanger = stayViewChanger;
    }

    public float getCurrentWeekMonthHeight() {
        return currentWeekMonthHeight;
    }

    public void setCurrentWeekMonthHeight(float currentWeekMonthHeight) {
        this.currentWeekMonthHeight = currentWeekMonthHeight;
    }

    public int getCurrentWeekMonthRowIndex() {
        return currentWeekMonthRowIndex;
    }

    public void setCurrentWeekMonthRowIndex(int currentWeekMonthRowIndex) {
        this.currentWeekMonthRowIndex = currentWeekMonthRowIndex;
    }

    public interface StayViewChanger {
        void onStayViewShow();

        void onStayViewGone();
    }
}
