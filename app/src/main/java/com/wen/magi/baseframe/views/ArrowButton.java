package com.wen.magi.baseframe.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.RippleDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.StateListDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.graphics.drawable.shapes.Shape;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.ImageButton;

import com.wen.magi.baseframe.R;
import com.wen.magi.baseframe.managers.ThemeManager;
import com.wen.magi.baseframe.utils.ViewUtils;

/**
 * @author zhangzhaowen @ Zhihu Inc.
 * @since 10-13-2016
 */

public class ArrowButton extends ImageButton {

    private static final int ARROW_DIRECTION_TOP = 90;

    private static final int ARROW_DIRECTION_RIGHT = 0;

    private static final int ARROW_DIRECTION_BOTTOM = 270;

    private static final int ARROW_DIRECTION_LEFT = 180;

    // TODO 写入 XML
    private static final int COLOR_NORMAL_LIGHT = 0xFFFAFAFA;

    private static final int COLOR_NORMAL_DARK = 0xFF455A64;

    private static final int COLOR_FOCUSED_LIGHT = 0xFFFAFAFA;

    private static final int COLOR_FOCUSED_DARK = 0xFF455A64;

    private static final int COLOR_PRESSED_LIGHT = 0xFFFAFAFA;

    private static final int COLOR_PRESSED_DARK = 0xFF455A64;

    private static final int COLOR_RIPPLE_LIGHT = 0x40000000;

    private static final int COLOR_RIPPLE_DARK = 0x40FFFFFF;

    private static final int COLOR_SHADOW = 0x37000000;

    private static final int CIRCLE_SIZE = 32; // dp

    private static final int SHADOW_RADIUS = 2; // dp

    private static final int SHADOW_X_OFFSET = 1; // dp

    private static final int SHADOW_Y_OFFSET = 2; // dp

    private int mCircleSize = ViewUtils.dp2pix(CIRCLE_SIZE);

    private int mShadowRadius = ViewUtils.dp2pix(SHADOW_RADIUS);

    private int mShadowXOffset = ViewUtils.dp2pix(SHADOW_X_OFFSET);

    private int mShadowYOffset = ViewUtils.dp2pix(SHADOW_Y_OFFSET);

    private Drawable mArrowDrawable;

    private int mArrowAngle;

    private int mArrowColor;

    public ArrowButton(Context pContext) {
        super(pContext);
        init(null);
    }

    public ArrowButton(Context pContext, AttributeSet pAttributeSet) {
        super(pContext, pAttributeSet);
        init(pAttributeSet);
    }

    public ArrowButton(Context pContext, AttributeSet pAttributeSet, int pDefaultStyle) {
        super(pContext, pAttributeSet, pDefaultStyle);
        init(pAttributeSet);
    }

    private void init(AttributeSet pAttributeSet) {
        if (pAttributeSet != null) {
            TypedArray array = getContext().obtainStyledAttributes(pAttributeSet, R.styleable.ArrowButton);
            mArrowDrawable = ContextCompat.getDrawable(getContext(), R.mipmap.ic_keyboard_arrow_right_white_24dp);
            mArrowAngle = Direction.fromId(array.getInt(R.styleable.ArrowButton_arrowDirection, 2)).getAngle();
            mArrowColor = array.getColor(R.styleable.ArrowButton_arrowColor, ContextCompat.getColor(getContext(), R.color.text_highlight_light));
            array.recycle();
        } else {
            mArrowDrawable = ContextCompat.getDrawable(getContext(), R.mipmap.ic_keyboard_arrow_right_white_24dp);
            mArrowAngle = Direction.fromId(2).getAngle();
            mArrowColor = ContextCompat.getColor(getContext(), R.color.text_highlight_light);
        }

        setImageDrawable(mArrowDrawable);
        setArrowAngle(mArrowAngle);
        setArrowColor(mArrowColor);
    }

    @Override
    protected void onMeasure(int pWidthMeasureSpec, int pHeightMeasureSpec) {
        setMeasuredDimension(calculateMeasuredWidth(), calculateMeasuredHeight());
    }

    @Override
    public void setImageDrawable(Drawable pDrawable) {
        mArrowDrawable = pDrawable;
        setArrowColor(mArrowColor);
        updateBackground();
    }

    @Override
    public void setImageResource(@DrawableRes int pResourceId) {
        mArrowDrawable = ContextCompat.getDrawable(getContext(), pResourceId);
        setArrowColor(mArrowColor);
        updateBackground();
    }

    public int getArrowAngle() {
        return mArrowAngle;
    }

    public int getArrowColor() {
        return mArrowColor;
    }

    public void setArrowAngle(int pArrowAngle) {
        if (mArrowDrawable == null) {
            return;
        }

        mArrowAngle = pArrowAngle;
        updateBackground();
    }

    public void setDirection(final Direction pDirection) {
        setArrowAngle(pDirection.getAngle());
    }

    public Direction getDirection() {
        Direction direction;
        int angle = getArrowAngle();
        if (angle == ARROW_DIRECTION_TOP) {
            direction = Direction.TOP;
        } else if (angle == ARROW_DIRECTION_BOTTOM) {
            direction = Direction.BOTTOM;
        } else if (angle == ARROW_DIRECTION_LEFT) {
            direction = Direction.LEFT;
        } else {
            direction = Direction.RIGHT;
        }
        return direction;
    }

    public void setArrowColor(int pArrowColor) {
        if (pArrowColor == 0 || mArrowDrawable == null) {
            return;
        }

        mArrowColor = pArrowColor;
        mArrowDrawable.mutate().setColorFilter(pArrowColor, PorterDuff.Mode.SRC_IN);
        updateBackground();
    }

    private boolean elevationSupported() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    private float calculateCenterX() {
        return (float) (getMeasuredWidth() / 2);
    }

    private float calculateCenterY() {
        return (float) (getMeasuredHeight() / 2);
    }

    private int calculateShadowWidth() {
        return !elevationSupported() ? mShadowRadius + Math.abs(mShadowXOffset) * 2 : 0;
    }

    private int calculateShadowHeight() {
        return !elevationSupported() ? mShadowRadius + Math.abs(mShadowYOffset) * 2 : 0;
    }

    private int calculateMeasuredWidth() {
        return mCircleSize + calculateShadowWidth();
    }

    private int calculateMeasuredHeight() {
        return mCircleSize + calculateShadowHeight();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private Drawable createFillDrawable() {
        StateListDrawable drawable = new StateListDrawable();

        if (ThemeManager.getInstance().getCurrentTheme(getContext()) == ThemeManager.LIGHT) {
            drawable.addState(new int[]{android.R.attr.state_focused}, createCircleDrawable(COLOR_FOCUSED_LIGHT));
            drawable.addState(new int[]{android.R.attr.state_pressed}, createCircleDrawable(COLOR_PRESSED_LIGHT));
            drawable.addState(new int[]{}, createCircleDrawable(COLOR_NORMAL_LIGHT));
        } else {
            drawable.addState(new int[]{android.R.attr.state_focused}, createCircleDrawable(COLOR_FOCUSED_DARK));
            drawable.addState(new int[]{android.R.attr.state_pressed}, createCircleDrawable(COLOR_PRESSED_DARK));
            drawable.addState(new int[]{}, createCircleDrawable(COLOR_NORMAL_DARK));
        }

        if (elevationSupported()) {
            RippleDrawable ripple;

            if (ThemeManager.getInstance().getCurrentTheme(getContext()) == ThemeManager.LIGHT) {
                ripple = new RippleDrawable(new ColorStateList(new int[][]{{}}, new int[]{COLOR_RIPPLE_LIGHT}), drawable, null);
            } else {
                ripple = new RippleDrawable(new ColorStateList(new int[][]{{}}, new int[]{COLOR_RIPPLE_DARK}), drawable, null);
            }

            setOutlineProvider(new ViewOutlineProvider() {

                @Override
                public void getOutline(View pView, Outline pOutline) {
                    pOutline.setOval(0, 0, pView.getWidth(), pView.getHeight());
                }
            });

            setClipToOutline(true);

            return ripple;
        }

        return drawable;
    }

    private Drawable createCircleDrawable(int pColor) {
        CircleDrawable drawable = new CircleDrawable(new OvalShape());
        drawable.getPaint().setColor(pColor);

        return drawable;
    }

    private void updateBackground() {
        if (mArrowDrawable == null) {
            return;
        }

        LayerDrawable layerDrawable;
        Drawable rotateDrawable = getRotateDrawable();

        if (!elevationSupported()) {
            layerDrawable = new LayerDrawable(new Drawable[]{new Shadow(), createFillDrawable(), rotateDrawable});
        } else {
            layerDrawable = new LayerDrawable(new Drawable[]{createFillDrawable(), rotateDrawable});
        }

        int drawableSize = Math.max(mArrowDrawable.getIntrinsicWidth(), mArrowDrawable.getIntrinsicHeight());
        int drawableOffset = (mCircleSize - drawableSize) / 2;
        int circleInsetHorizontal = !elevationSupported() ? mShadowRadius + Math.abs(mShadowXOffset) : 0;
        int circleInsetVertical = !elevationSupported() ? mShadowRadius + Math.abs(mShadowYOffset) : 0;

        layerDrawable.setLayerInset(!elevationSupported() ? 2 : 1, circleInsetHorizontal + drawableOffset, circleInsetVertical + drawableOffset, circleInsetHorizontal + drawableOffset, circleInsetVertical + drawableOffset);

        ViewUtils.setBackground(this, layerDrawable);
        ViewCompat.setElevation(this, ViewUtils.dp2pix(4.0f));
    }

    private Drawable getRotateDrawable() {
        if (mArrowDrawable == null) {
            return null;
        }

        Drawable[] drawables = {mArrowDrawable};

        return new LayerDrawable(drawables) {

            @Override
            public void draw(final Canvas canvas) {
                canvas.save();
                canvas.rotate(-mArrowAngle, mArrowDrawable.getBounds().centerX(), mArrowDrawable.getBounds().centerY());
                super.draw(canvas);
                canvas.restore();
            }
        };
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private class CircleDrawable extends ShapeDrawable {

        private int circleInsetHorizontal;

        private int circleInsetVertical;

        private CircleDrawable(Shape pShape) {
            super(pShape);

            circleInsetHorizontal = !elevationSupported() ? mShadowRadius + Math.abs(mShadowXOffset) : 0;
            circleInsetVertical = !elevationSupported() ? mShadowRadius + Math.abs(mShadowYOffset) : 0;
        }

        @Override
        public void draw(Canvas pCanvas) {
            setBounds(circleInsetHorizontal, circleInsetVertical, calculateMeasuredWidth() - circleInsetHorizontal, calculateMeasuredHeight() - circleInsetVertical);

            super.draw(pCanvas);
        }
    }

    private class Shadow extends Drawable {

        private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        private Paint mErase = new Paint(Paint.ANTI_ALIAS_FLAG);

        private float mRadius;

        private Shadow() {
            this.init();
        }

        private void init() {
            setLayerType(LAYER_TYPE_SOFTWARE, null);

            if (ThemeManager.getInstance().getCurrentTheme(getContext()) == ThemeManager.LIGHT) {
                mPaint.setColor(COLOR_NORMAL_LIGHT);
            } else {
                mPaint.setColor(COLOR_NORMAL_DARK);
            }

            mPaint.setStyle(Paint.Style.FILL);
            if (!isInEditMode()) {
                mPaint.setShadowLayer(mShadowRadius, mShadowXOffset, mShadowYOffset, COLOR_SHADOW);
            }

            mErase.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));

            mRadius = mCircleSize / 2;
        }

        @Override
        public void draw(Canvas pCanvas) {
            pCanvas.drawCircle(calculateCenterX(), calculateCenterY(), mRadius, mPaint);
            pCanvas.drawCircle(calculateCenterX(), calculateCenterY(), mRadius, mErase);
        }

        @Override
        public void setAlpha(int pAlpha) {

        }

        @Override
        public void setColorFilter(ColorFilter pColorFilter) {

        }

        @Override
        public int getOpacity() {
            return PixelFormat.TRANSLUCENT;
        }
    }

    public enum Direction {
        TOP(1, ARROW_DIRECTION_TOP),
        RIGHT(2, ARROW_DIRECTION_RIGHT),
        BOTTOM(3, ARROW_DIRECTION_BOTTOM),
        LEFT(4, ARROW_DIRECTION_LEFT);

        private int mId;

        private int mAngle;

        Direction(int pId, int pAngle) {
            mId = pId;
            mAngle = pAngle;
        }

        public static Direction fromId(int id) {
            for (Direction direction : values()) {
                if (direction.mId == id) {
                    return direction;
                }
            }

            return BOTTOM;
        }

        public int getAngle() {
            return mAngle;
        }
    }
}
