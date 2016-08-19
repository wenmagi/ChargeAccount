package com.wen.magi.baseframe.views.fortest;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.widget.Scroller;

/**
 * Created by MVEN on 16/8/15.
 * <p/>
 * email: magiwen@126.com.
 */


public class MoneyScaleView extends View {

    private static final String TAG = "MoneyScale";
    int startScroll;
    private Paint mLinePaint;
    private Paint mValuePaint;
    private Paint mVerticalPaint;     //垂直的线的画笔
    private Paint textPaint;
    private int mStartX = 0, mStartY, mStopX, mFirstX = 0;
    private int mWidth;
    private boolean isDrawText;
    private int moveX = 0;
    private VelocityTracker velocityTracker;
    private Scroller scroller;
    private float currentX = 200;
    private boolean isLeft;
    private int lastX;
    private int lastMoveX;
    private int upX;

    public MoneyScaleView(Context context) {
        super(context);
        init();
        scroller = new Scroller(context);
    }

    public MoneyScaleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
        scroller = new Scroller(context);
    }

    public MoneyScaleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
        scroller = new Scroller(context);
    }

    private void init() {
        mLinePaint = new Paint();
        mLinePaint.setAntiAlias(true);
        mLinePaint.setStyle(Paint.Style.STROKE);
        mLinePaint.setStrokeWidth(3);
        mLinePaint.setColor(0xFF2B2B2B);
        mVerticalPaint = new Paint();
        mVerticalPaint.setAntiAlias(true);
        mVerticalPaint.setStyle(Paint.Style.STROKE);
        mVerticalPaint.setStrokeWidth(3);
        mVerticalPaint.setColor(0xFF2B2B2B);

        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setStrokeWidth(3);
        textPaint.setTextSize(30);
        textPaint.setColor(0xFF2B2B2B);
        textPaint.setTextAlign(Paint.Align.CENTER);

        mValuePaint = new Paint();
        mValuePaint.setAntiAlias(true);
        mValuePaint.setStyle(Paint.Style.STROKE);
        mValuePaint.setStrokeWidth(1);
        mValuePaint.setColor(0xFFFA6064);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        initSize();
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private void initSize() {
        mStartY = getHeight() / 2;
        mWidth = getWidth();
        mStopX = mWidth + 1000;
    }

    boolean isCanMoveRight=true;
    @Override
    protected void onDraw(Canvas canvas) {
        mFirstX = mStartX;
        if (mStartX != 0) {
            mStartX = 0;

        }
        if (moveX -lastMoveX > 360&&!isLeft) {
            isCanMoveRight = false;

        } else {
            isCanMoveRight = true;
        }
        Log.e(TAG, " computeScroll mStartX" + mStartX + "moveX" + moveX + "lastMoveX" + lastMoveX + "--" + (mStartX - moveX + lastMoveX));
        canvas.drawLine(360, mStartY, 360, mStartY - 90, mValuePaint);
        if ((-moveX + lastMoveX) < 0) {
            if (!isCanMoveRight) {
                canvas.drawLine(360, mStartY, getWidth(), mStartY, mLinePaint);
            } else {
                canvas.drawLine(-(mStartX - moveX + lastMoveX), mStartY, getWidth(), mStartY, mLinePaint);
            }

        } else {
            canvas.drawLine(0, mStartY, getWidth(), mStartY, mLinePaint);
        }

        for (; mStartX < mWidth; mStartX += 13) {
            int top = mStartY - 10;
            Log.e(TAG, "i" + mStartX);
            if ((mStartX - moveX + lastMoveX) % 130 == 0) {
                top = top - 20;
                Log.e(TAG, "top:" + top);
                isDrawText = true;
            } else {
                isDrawText = false;
            }
            Log.e(TAG, "onDraw" + (mStartX - moveX + lastMoveX));
            if ((mStartX - moveX + lastMoveX) >= 0) {
                canvas.drawLine(mStartX, mStartY, mStartX, top, mVerticalPaint);
                if (isDrawText) {

                    canvas.drawText((mStartX - moveX + lastMoveX) / 13 + "", mStartX, top - 8, textPaint);
                }
            }

        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (velocityTracker == null) {
            velocityTracker = VelocityTracker.obtain();
        }
        velocityTracker.addMovement(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastX = (int) event.getX();
                if (!scroller.isFinished()) {

                    Log.e(TAG, "computeScroll currX:  isFinished");
//                         return true;
                    scroller.abortAnimation();
                }
                lastX = (int) event.getX();
                lastMoveX = lastMoveX - moveX;
                int i = Math.abs(lastMoveX) % 13;
                lastMoveX = lastMoveX + i;
//                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                moveX = (int) (event.getX() - lastX);
                currentX = currentX + Math.abs(moveX);
                if (moveX < 0) {
                    Log.e(TAG, "向左移动" + moveX);
                    isLeft = true;
                    //向左移动
                    isCanMoveRight = true;
//                    mStartX = mFirstX - moveX;
//                    mStopX = mStopX - moveX;
                    mStartX = 0;
                    i = Math.abs(moveX) % 13;
                    moveX = moveX + i;
                    Log.e(TAG, "向左移动 mStartX" + mStartX + "..moveX" + moveX);
                    invalidate();
                } else {
                    Log.e(TAG, "右边" + moveX + "  lastmove" + lastMoveX + "相减" + (moveX + lastMoveX)+"left"+isLeft);
                    if (moveX - lastMoveX >= 360+20||!isCanMoveRight) {
                        moveX = moveX - lastMoveX;
                        lastMoveX = 0;
                        isCanMoveRight = false;
                        return true;
                    }
                    isLeft = false;
                    Log.e(TAG, "向右移动" + (mStartX - moveX + lastMoveX));
                    if (mStartX - moveX + lastMoveX <= 0) {
                        return false;
                    }
                    //向右移动
                    mStartX = 0;
                    i = Math.abs(moveX) % 13;
                    moveX = moveX - i;

                    invalidate();
                    mStopX = mStopX - moveX;
                }
                upX = (int) event.getX();
                break;
            case MotionEvent.ACTION_UP:
                Log.e(TAG, "开始 moveX" + moveX + "upX" + upX + "currentX" + event.getX());
                if (moveX == 0) {
                    return true;
                }
                startScroll = 0;
                /**
                 *  计算那些已经发生触摸事件点的当前速率。这个函数只有在你需要得到速率消息的情况下才调用，因为使用它需要消耗很大的性能。
                 *   units:  你使用的速率单位.1的意思是，以一毫秒运动了多少个像素的速率， 1000表示 一秒时间内运动了多少个像素。
                 */
                velocityTracker.computeCurrentVelocity(1000);
                float xVelocity = velocityTracker.getXVelocity();
                float yVelocity = velocityTracker.getYVelocity();
                if (Math.abs(xVelocity) < 800) {
                    return true;
                }
                Log.e(TAG, "computeScroll xVelocity: " + xVelocity + " yVelocity" + yVelocity);
                /**
                 * startY 滚动起始点Y坐标

                 　　velocityX   当滑动屏幕时X方向初速度，以每秒像素数计算

                 　　velocityY   当滑动屏幕时Y方向初速度，以每秒像素数计算

                 　　minX    X方向的最小值，scroller不会滚过此点。

                 　　maxX    X方向的最大值，scroller不会滚过此点。

                 　　minY    Y方向的最小值，scroller不会滚过此点。

                 　　maxY    Y方向的最大值，scroller不会滚过此点。
                 */
                scroller.fling(130, mStartY, (int) (-Math.abs(xVelocity) + 0.5), (int) (Math.abs(yVelocity) + 0.5), 000, 10080, 0, 1920);
                velocityTracker.recycle();
                velocityTracker = null;


//                invalidate();
                break;
        }
        return true;
    }

    @Override
    public void computeScroll() {
        if (scroller.computeScrollOffset()) {
            startScroll++;
            Log.e(TAG, "开始" + startScroll);
            Log.e(TAG, "开始 正式执行" + startScroll);
            int currX = scroller.getCurrX();
            int startX = scroller.getStartX();
            int finalX = scroller.getFinalX();
            float currVelocity = scroller.getCurrVelocity();
            mStartX = 0;
            if (isLeft) {
                moveX = moveX - currX;
                int i = Math.abs(moveX) % 13;
                moveX = moveX + i;
                Log.e(TAG, "移动距离" + moveX + "..lastMove" + lastMoveX);
            } else {
                Log.e(TAG, "移动距离" + moveX);
                moveX = moveX + currX;
                int i = Math.abs(moveX) % 13;
                moveX = moveX -i;
                Log.e(TAG, "移动距离" + moveX + "..lastMove" + lastMoveX);
            }

            Log.e(TAG, "computeScroll currX: " + currX + " /moveX" + moveX + "startX" + startX + "finalX" + finalX + "currentX" + currentX);
            invalidate();

        }
    }

}
