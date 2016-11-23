package com.wen.magi.baseframe.views.touch3d;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.ShapeDrawable;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.DimenRes;
import android.support.annotation.RequiresApi;
import android.text.method.Touch;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewOutlineProvider;


/**
 * @author zhangzhaowen @ Zhihu Inc.
 * @since 11-18-2016
 */

public class Touch3DViews extends View {

    private static final int LAYER_FLAGS = Canvas.MATRIX_SAVE_FLAG | Canvas.CLIP_SAVE_FLAG
            | Canvas.HAS_ALPHA_LAYER_SAVE_FLAG | Canvas.FULL_COLOR_LAYER_SAVE_FLAG
            | Canvas.CLIP_TO_LAYER_SAVE_FLAG;

    private Activity activity;

    private View mHollowView;

    private PorterDuffXfermode mXfermode;
    private Paint mPaint = new Paint();
    private Paint mBorderPaint = new Paint();

    private
    @ColorInt
    int mBorderOutColor = Color.parseColor("#a8000000");
    private
    @ColorInt
    int mBorderInColor = Color.TRANSPARENT;
    private
    @ColorInt
    int mBorderStrokeColor = Color.WHITE;

    private float mBorderStrokeWidth = 2;

    public Touch3DViews(Context context) {
        this(context, null);
    }

    private Touch3DViews(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    private Touch3DViews(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 设置裁剪区外部颜色
     *
     * @param outColor ColorResId
     */
    private void setBorderOutColor(@ColorInt final int outColor) {
        mBorderOutColor = outColor;
    }

    /**
     * 设置裁剪线颜色
     *
     * @param strokeColor ColorResId
     */
    public void setBorderStrokeColor(@ColorInt final int strokeColor) {
        mBorderStrokeColor = strokeColor;
    }

    /**
     * 设置裁剪区内部颜色
     *
     * @param inColor ColorResId
     */
    private void setBorderInColor(@ColorInt final int inColor) {
        mBorderInColor = inColor;
    }

    private void setHollowImageRes(final int hollowImageResId) {

    }

    /**
     * 设置Stroke宽度
     *
     * @param strokeWidth DimenResId
     */
    private void setBoderStrokeWidth(@DimenRes final int strokeWidth) {
        mBorderStrokeWidth = getContext().getResources().getDimension(strokeWidth);
    }

    private void setmBorderStrokeWidth(final int strokeWidth) {
        mBorderStrokeWidth = strokeWidth;
    }


    private void show() {
        mPaint.setAntiAlias(true);

        mBorderPaint.setAntiAlias(true);
        mBorderPaint.setColor(mBorderStrokeColor);
        mBorderPaint.setStrokeWidth(mBorderStrokeWidth);
        mBorderPaint.setStyle(Paint.Style.STROKE);

        mXfermode = new PorterDuffXfermode(PorterDuff.Mode.DST_OUT);
        invalidate();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = Touch3DUtils.WIDTH;
        int height = Touch3DUtils.HEIGHT;

        canvas.saveLayer(0, 0, width, height, null, LAYER_FLAGS);
        canvas.drawColor(mBorderOutColor);
        mPaint.setXfermode(mXfermode);

        if (mHollowView != null){
            ViewOutlineProvider drawable = mHollowView.getOutlineProvider();
        }
    }

    public static class Touch3DGenerator {

        private Activity mActivity;
        private int mHollowImageResId;
        private
        @ColorInt
        int mBorderInColor;

        private Touch3DGenerator(final Activity activity) {
            mActivity = activity;
        }

        public Touch3DGenerator init(final Activity activity) {
            return new Touch3DGenerator(activity);
        }

        public Touch3DGenerator setHollowImageRes(final int hollowImageResId) {
            mHollowImageResId = hollowImageResId;
            return this;
        }

        public Touch3DGenerator setBorderInColor(@ColorInt final int inColor) {
            mBorderInColor = inColor;
            return this;
        }

        public void show() {

        }

    }

}
