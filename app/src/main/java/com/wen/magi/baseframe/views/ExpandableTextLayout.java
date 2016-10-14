package com.wen.magi.baseframe.views;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.TextView;

import com.wen.magi.baseframe.R;
import com.wen.magi.baseframe.utils.ViewUtils;

/**
 * @author zhangzhaowen @ Zhihu Inc.
 * @since 10-13-2016
 */

public class ExpandableTextLayout extends ViewGroup implements View.OnClickListener{

    public static final int BTN_SIZE = 32; //btn size in dp

    public static final int GRADIENT_HEIGHT = 8; //gradient in dp

    public static final int SHADOW_OFFSET = 3; //gradient in dp

    public static final int MAX_LINE_COUNT = 3;

    private TextView mTextView;

    private ArrowButton mArrowButton;

    private int mContainerColor;

    private View mGradientView;

    private int mMaxLines;

    private int mTextTotalHeight;

    private int mExpandOffset;

    private int mShadowOffset;

    private boolean mExpanded = false;

    private Rect mTextRect;

    public ExpandableTextLayout(final Context pContext) {
        super(pContext);
        this.init(pContext, null);
    }

    public ExpandableTextLayout(final Context pContext, final AttributeSet pAttributeSet) {
        super(pContext, pAttributeSet);
        this.init(pContext, pAttributeSet);
    }

    public ExpandableTextLayout(final Context pContext, final AttributeSet pAttributeSet, final int pDefaultStyle) {
        super(pContext, pAttributeSet, pDefaultStyle);
        this.init(pContext, pAttributeSet);
    }

    private void init(final Context pContext, final AttributeSet pAttributeSet) {
        Resources resources = getResources();
        TypedArray typedArray = pContext.obtainStyledAttributes(pAttributeSet, R.styleable.ExpandableTextLayout);
        int color = typedArray.getColor(R.styleable.ExpandableTextLayout_containerColor, resources.getColor(R.color.colorPrimary));
        this.mMaxLines = typedArray.getInteger(R.styleable.ExpandableTextLayout_maxLines, MAX_LINE_COUNT);
        typedArray.recycle();
        this.mTextRect = new Rect();
        this.setContainerColor(color);
        setClipChildren(false);
        setClipToPadding(false);
        setWillNotDraw(true);
    }

    public void setContainerColor(final int color) {
        this.mContainerColor = color;
        if (this.mArrowButton != null) {
            this.mArrowButton.setArrowColor(this.mContainerColor);
            this.mGradientView.getBackground().setColorFilter(color, PorterDuff.Mode.MULTIPLY);
        }
        this.invalidate();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (getChildCount() > 1 || getChildCount() == 0 || !(getChildAt(0) instanceof TextView)) {
            throw new IllegalStateException("you should add one and only one TextView to ShrinkableTextLayout!");
        }

        int btnSize = ViewUtils.dp2pix( BTN_SIZE);
        int gradientHeight = ViewUtils.dp2pix(GRADIENT_HEIGHT);
        //		int shadowOffset = ViewUtils.dp2pix(getCon
        // text(), SHADOW_OFFSET);

        this.mTextView = (TextView) getChildAt(0);
        LayoutParams textLayoutParams = this.mTextView.getLayoutParams();
        this.mTextView.setLayoutParams(textLayoutParams);

        this.mGradientView = new View(this.getContext());
        this.mGradientView.setBackgroundResource(R.drawable.foreground_opaque_gradient);
        this.mGradientView.getBackground().setColorFilter(this.mContainerColor, PorterDuff.Mode.MULTIPLY);
        LayoutParams gradientLayoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, gradientHeight);
        this.mGradientView.setLayoutParams(gradientLayoutParams);
        this.addView(this.mGradientView);

        this.mArrowButton = new ArrowButton(getContext());
        this.mArrowButton.setDirection(ArrowButton.Direction.BOTTOM);
        this.mArrowButton.setArrowColor(this.mContainerColor);
        LayoutParams btnLayoutParams = new LayoutParams(btnSize, btnSize);
        this.mArrowButton.setLayoutParams(btnLayoutParams);
        this.addView(this.mArrowButton);
        this.mArrowButton.setOnClickListener(this);

        this.setOnClickListener(this);
    }

    @Override
    protected void onMeasure(final int widthMeasureSpec, final int heightMeasureSpec) {
        int heightSize;
        int heightMode;

        this.mTextView.measure(widthMeasureSpec, heightMeasureSpec);
        this.mTextTotalHeight = this.mTextView.getMeasuredHeight();

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP && this.canShowButton()) {
            this.mShadowOffset = ViewUtils.dp2pix(SHADOW_OFFSET);
        } else {
            this.mShadowOffset = 0;
        }
        if (this.canShowButton()) {
            this.mGradientView.setVisibility(VISIBLE);
            this.mArrowButton.setVisibility(VISIBLE);

            this.mTextView.getLineBounds(this.mMaxLines - 1, mTextRect);
            heightSize = mTextRect.bottom + ViewUtils.dp2pix(BTN_SIZE / 2) + this.mExpandOffset + this.mShadowOffset;
            this.mTextView.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(mTextRect.bottom + mExpandOffset, MeasureSpec.EXACTLY));
            heightMode = MeasureSpec.EXACTLY;
        } else {
            this.mGradientView.setVisibility(GONE);
            this.mArrowButton.setVisibility(GONE);
            heightSize = this.mTextView.getMeasuredHeight();
            heightMode = MeasureSpec.EXACTLY;
        }
        super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(heightSize, heightMode));
    }

    private boolean canShowButton() {
        return this.mTextView.getLineCount() > this.mMaxLines;
    }

    @Override
    protected void onLayout(final boolean changed, final int l, final int t, final int r, final int b) {
        int btnSize = ViewUtils.dp2pix(BTN_SIZE);
        int gradientHeight = ViewUtils.dp2pix(GRADIENT_HEIGHT);

        int left = getPaddingLeft();
        int top = getPaddingTop();
        int right = r - l - getPaddingRight();
        int bottom = b - t - getPaddingBottom();

        int textViewBottom;
        if (this.canShowButton()) {
            textViewBottom = bottom - btnSize / 2 - this.mShadowOffset;
        } else {
            textViewBottom = bottom;
        }
        this.mTextView.layout(left, top, right, textViewBottom);
        this.mGradientView.layout(left, textViewBottom - gradientHeight, right, textViewBottom);
        this.mArrowButton.layout((left + right) / 2 - btnSize / 2, textViewBottom - btnSize / 2, (left + right) / 2 + btnSize / 2, bottom - this.mShadowOffset);
    }

    public void setExpandOffset(int offset) {
        this.mExpandOffset = offset;
        this.requestLayout();
    }

    public int getExpandOffset() {
        return mExpandOffset;
    }

    /**
     * 展开
     */
    public void expand() {
        if (this.mExpanded) {
            return;
        }

        int targetExpandOffset = this.mTextTotalHeight + ViewUtils.dp2pix(BTN_SIZE / 2) - this.mTextView.getMeasuredHeight();
        long duration = (long) (0.5 * targetExpandOffset);
        ObjectAnimator textExpandAnimator = ObjectAnimator.ofInt(this, "expandOffset", this.mExpandOffset, targetExpandOffset);
        ObjectAnimator arrowButtonRotateAnimator = ObjectAnimator.ofInt(this.mArrowButton, "arrowAngle", ArrowButton.Direction.BOTTOM.getAngle(), ArrowButton.Direction.TOP.getAngle());
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(textExpandAnimator, arrowButtonRotateAnimator);
        animatorSet.setDuration(duration);
        animatorSet.setInterpolator(new AccelerateDecelerateInterpolator());
        animatorSet.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationStart(final Animator animation) {
                ExpandableTextLayout.this.mGradientView.animate().setDuration(animation.getDuration()).alpha(0.f).start();
                super.onAnimationStart(animation);
            }

            @Override
            public void onAnimationEnd(final Animator animation) {
                ExpandableTextLayout.this.mGradientView.setVisibility(View.GONE);
            }
        });
        animatorSet.start();
        this.mExpanded = true;
    }

    /**
     * 收起
     */
    public void collapse() {
        if (!this.mExpanded) {
            return;
        }
        long duration = (long) (0.5 * mExpandOffset);
        ObjectAnimator textExpandAnimator = ObjectAnimator.ofInt(this, "expandOffset", this.mExpandOffset, 0);
        ObjectAnimator arrowButtonRotateAnimator = ObjectAnimator.ofInt(this.mArrowButton, "arrowAngle", ArrowButton.Direction.TOP.getAngle(), ArrowButton.Direction.BOTTOM.getAngle());
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(textExpandAnimator, arrowButtonRotateAnimator);
        animatorSet.setDuration(duration);
        animatorSet.setInterpolator(new AccelerateDecelerateInterpolator());
        animatorSet.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationStart(final Animator animation) {
                super.onAnimationStart(animation);
                ExpandableTextLayout.this.mGradientView.setVisibility(View.VISIBLE);
                ExpandableTextLayout.this.mGradientView.animate().setDuration(animation.getDuration()).alpha(1.f).start();
            }

            @Override
            public void onAnimationEnd(final Animator animation) {
            }
        });
        animatorSet.start();
        this.mExpanded = false;
    }

    /**
     * 切换
     */
    public void toggle() {
        if (this.mExpanded) {
            collapse();
        } else {
            expand();
        }
    }

    @Override
    public void onClick(final View v) {
        this.toggle();
    }
}
