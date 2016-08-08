package com.wen.magi.baseframe.views.viewpager;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.wen.magi.baseframe.R;
import com.wen.magi.baseframe.utils.LangUtils;
import com.wen.magi.baseframe.utils.ViewUtils;

/**
 * Created by MVEN on 16/8/8.
 * <p/>
 * email: magiwen@126.com.
 */
public class TabItem extends View {

  /*
   * 字体大小
   * */

    private int mTextSize;

    /*
     * 字体选中的颜色
     * */
    private int mTextColorSelect;

    /*
     * 字体未选择的时候的颜色
     * */
    private int mTextColorNormal;

    /*
     * 绘制未选中时字体的画笔
     * */
    private Paint mTextPaintNormal;

    /*
     * 绘制已选中时字体的画笔
     * */
    private Paint mTextPaintSelect;

    /*
     * 每个 item 的宽和高，包括字体和图标一起
     * */
    private int mViewHeight, mViewWidth;

    /*
     * 字体的内容
     * */
    private String mTextValue;

    /*
     * 已选中时的图标
     * */
    private Bitmap mIconNormal;

    /*
     * 未选中时的图标
     * */
    private Bitmap mIconSelect;

    /*
     * 用于记录字体大小
     * */
    private Rect mBoundText;

    /*
     * 已选中是图标的画笔
     * */
    private Paint mIconPaintSelect;

    /*
     * 为选中时图标的画笔
     * */
    private Paint mIconPaintNormal;
    /*
     * text和image间距
     * */
    private int margin;

    private Paint mPaintCircle;
    private boolean isCircleVisible;
    private boolean isCircleDigitVisible;
    private int circleRadius;

    /*
     * 用于记录circleDigit字体大小
     * */
    private Rect mBoundTextDigit;
    private int mTextDigitHeight;
    private Paint mTextPaintDigit;

    public TabItem(Context context) {
        this(context, null);
    }

    public TabItem(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TabItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
        initText();
    }

    /*
     * 初始化一些东西
     * */
    private void initView() {
        mBoundText = new Rect();
        mBoundTextDigit = new Rect();
    }

    /*
     * 初始化画笔，并设置出是内容
     * */
    private void initText() {
        mTextPaintNormal = new Paint();
        mTextPaintNormal.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, mTextSize, getResources().getDisplayMetrics()));
        mTextPaintNormal.setColor(mTextColorNormal);
        mTextPaintNormal.setAntiAlias(true);
        mTextPaintNormal.setAlpha(0xff);

        mTextPaintSelect = new Paint();
        mTextPaintSelect.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, mTextSize, getResources().getDisplayMetrics()));
        mTextPaintSelect.setColor(mTextColorSelect);
        mTextPaintSelect.setAntiAlias(true);
        mTextPaintSelect.setAlpha(0);

        mIconPaintSelect = new Paint(Paint.ANTI_ALIAS_FLAG);
        mIconPaintSelect.setAlpha(0);

        mIconPaintNormal = new Paint(Paint.ANTI_ALIAS_FLAG);
        mIconPaintNormal.setAlpha(0xff);
//      mIconNormal.setWidth(ViewUtils.rp(7));

        circleRadius = ViewUtils.dp2pix(3);
        mPaintCircle = new Paint();
        mPaintCircle.setColor(Color.RED);
        mPaintCircle.setStyle(Paint.Style.FILL);
        mPaintCircle.setAntiAlias(true);


        mTextPaintDigit = new Paint();
        mTextPaintDigit.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 10, getResources().getDisplayMetrics()));
        mTextPaintDigit.setColor(Color.WHITE);
        mTextPaintDigit.setAntiAlias(true);
        mTextPaintDigit.setFakeBoldText(true);
        mTextPaintDigit.setTextAlign(Paint.Align.CENTER);
    }

    /*
     * 测量字体的大小
     * */
    private void measureText() {
        mTextPaintNormal.getTextBounds(mTextValue, 0, mTextValue.length(), mBoundText);

        Rect tmp = new Rect();
        mTextPaintDigit.getTextBounds("8", 0, 1, tmp);
        mTextDigitHeight = tmp.height();
    }


    /*
     * 测量字体和图标的大小，并设置自身的宽和高
     * */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width = 0, height = 0;

        measureText();
        int contentWidth = Math.max(mBoundText != null ? mBoundText.width() : 0, mIconNormal.getWidth());
        int desiredWidth = getPaddingLeft() + getPaddingRight() + contentWidth;
        switch (widthMode) {
            case MeasureSpec.AT_MOST:
                width = Math.min(widthSize, desiredWidth);
                break;
            case MeasureSpec.EXACTLY:
                width = widthSize;
                break;
            case MeasureSpec.UNSPECIFIED:
                width = desiredWidth;
                break;
        }
        int contentHeight = mBoundText.height() + mIconNormal.getHeight();
        int desiredHeight = getPaddingTop() + getPaddingBottom() + contentHeight;
        switch (heightMode) {
            case MeasureSpec.AT_MOST:
                height = Math.min(heightSize, desiredHeight);
                break;
            case MeasureSpec.EXACTLY:
                height = heightSize;
                break;
            case MeasureSpec.UNSPECIFIED:
                height = contentHeight;
                break;
        }
        setMeasuredDimension(width, height);
        mViewWidth = getMeasuredWidth();
        mViewHeight = getMeasuredHeight();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.WHITE);
        drawBitmap(canvas);
        drawText(canvas);
        drawCircle(canvas);
        drawCircleDigit(canvas);
    }

    public void setMargin(int margin) {
        this.margin = margin;
    }

    /*
     * 画图标，先画为选中的图标，在画已选中的图标
     * */
    private void drawBitmap(Canvas canvas) {
        int left = (mViewWidth - mIconNormal.getWidth()) / 2;
        int top = (mViewHeight - mIconNormal.getHeight() - mBoundText.height()) / 2;
        canvas.drawBitmap(mIconNormal, left, top, mIconPaintNormal);
        canvas.drawBitmap(mIconSelect, left, top, mIconPaintSelect);
    }

    /*
     * 画字体
     * */
    private void drawText(Canvas canvas) {
        float x = (mViewWidth - mBoundText.width()) / 2.0f;
        float y = (mViewHeight + mIconNormal.getHeight() + mBoundText.height()) / 2.0F + margin;
        canvas.drawText(mTextValue, x, y, mTextPaintNormal);
        canvas.drawText(mTextValue, x, y, mTextPaintSelect);
    }

    private void drawCircle(Canvas canvas) {
        if (this.isCircleVisible) {
            int centerX = (mViewWidth + mIconNormal.getWidth()) / 2;
            int centerY = (mViewHeight - mIconNormal.getHeight() - mBoundText.height()) / 2;
            canvas.drawCircle(centerX, centerY, circleRadius, mPaintCircle);
        }
    }

    private void drawCircleDigit(Canvas canvas) {
        if (this.isCircleDigitVisible) {
            //TODO在此处获取未读消息数
            int nDigit = 0;
            if (nDigit <= 0)
                return;

            int left = (mViewWidth + mIconNormal.getWidth()) / 2;
            int right = 0;
            int top = ViewUtils.dp2pix(5);
            int bottom = top + mTextDigitHeight/*mBoundTextDigit.height()*/;

            int paddingRight = ViewUtils.dp2pix(6);
            int paddingLeft = ViewUtils.dp2pix(6);
            int paddingBottom = ViewUtils.dp2pix(4);
            int paddingTop = paddingBottom;


            if (mBoundTextDigit.width() >= mBoundTextDigit.height()) {
                right = left + mBoundTextDigit.width();
            } else {
                right = left + mBoundTextDigit.height();
                paddingRight = paddingBottom;
                paddingLeft = paddingBottom;
            }

            Rect drawableRect = new Rect(left - paddingLeft, top - paddingTop, right + paddingRight, bottom + paddingBottom);

            float r = drawableRect.height() / 2;
            Drawable drawable = getDefaultBackground(r);
            drawable.setBounds(drawableRect);
            drawable.draw(canvas);

            String strDigit = LangUtils.parseString(nDigit);
            if (nDigit >= 100)
                strDigit = "...";
            canvas.drawText(strDigit, drawableRect.centerX(),
                    drawableRect.centerY() + ((float) mBoundTextDigit.height()) / 2, mTextPaintDigit);
        }
    }

    private ShapeDrawable getDefaultBackground(float radius) {

        float[] outerR = new float[]{radius, radius, radius, radius, radius, radius, radius, radius};

        RoundRectShape rr = new RoundRectShape(outerR, null, null);
        ShapeDrawable drawable = new ShapeDrawable(rr);
        drawable.getPaint().setColor(getResources().getColor(R.color.red));

        return drawable;

    }

    public void setTextSize(int textSize) {
        this.mTextSize = textSize;
        mTextPaintNormal.setTextSize(textSize);
        mTextPaintSelect.setTextSize(textSize);
    }

    public void setTextColorSelect(int mTextColorSelect) {
        this.mTextColorSelect = mTextColorSelect;
        mTextPaintSelect.setColor(mTextColorSelect);
        mTextPaintSelect.setAlpha(0);
    }

    public void setTextColorNormal(int mTextColorNormal) {
        this.mTextColorNormal = mTextColorNormal;
        mTextPaintNormal.setColor(mTextColorNormal);
        mTextPaintNormal.setAlpha(0xff);
    }

    public void setTextValue(String TextValue) {
        this.mTextValue = TextValue;
    }

    public void setIconText(int[] iconSelId, String TextValue) {
        this.mIconSelect = BitmapFactory.decodeResource(getResources(), iconSelId[0]);
        this.mIconNormal = BitmapFactory.decodeResource(getResources(), iconSelId[1]);
        this.mTextValue = TextValue;
    }


    /*
     * 通过 alpha 来设置 每个画笔的透明度，从而实现显示的效果
     * */
    public void setTabAlpha(float alpha) {
        int paintAlpha = (int) (alpha * 255);
        mIconPaintSelect.setAlpha(paintAlpha);
        mIconPaintNormal.setAlpha(255 - paintAlpha);
        mTextPaintSelect.setAlpha(paintAlpha);
        mTextPaintNormal.setAlpha(255 - paintAlpha);
        invalidate();
    }

    public void setCircleVisible(boolean bVisible) {
        isCircleVisible = bVisible;
        invalidate();
    }

    public void setCircleDigitVisible(boolean bVisible) {
        isCircleDigitVisible = bVisible;
        int unReadCount = 0;
        String digit = LangUtils.parseString(unReadCount);
        if (unReadCount >= 100)
            digit = "...";
        if (unReadCount == 0)
            isCircleDigitVisible = false;
        mTextPaintDigit.getTextBounds(digit, 0, digit.length(), mBoundTextDigit);
        invalidate();
    }
}

