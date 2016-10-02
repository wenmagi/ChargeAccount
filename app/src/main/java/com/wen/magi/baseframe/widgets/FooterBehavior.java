package com.wen.magi.baseframe.widgets;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import com.wen.magi.baseframe.R;

/**
 * @author MVEN @ Zhihu Inc.
 * @since 10-02-2016
 */

public class FooterBehavior extends CoordinatorLayout.Behavior<View> {
    public interface FooterBehaviorDelegate {
        boolean isFooterBehaviorEnable();
    }


    private final int mShowThreshold;

    private final int mHideThreshold;

    private int mDeltaY;

    private ObjectAnimator mAnimator;

    private boolean mIsShown = true;

    private FooterBehaviorDelegate mDelegate;

    public FooterBehavior(final Context pContext, final AttributeSet pAttributeSet) {
        super(pContext, pAttributeSet);

        this.mShowThreshold = -pContext.getResources().getDimensionPixelSize(R.dimen.tab_show_scroll_threshold);

        this.mHideThreshold = pContext.getResources().getDimensionPixelSize(R.dimen.tab_hide_scroll_threshold);
    }

    public void setFooterBehaviorDelegate(final FooterBehaviorDelegate pDelegate) {
        mDelegate = pDelegate;
    }

    @Override
    public boolean onStartNestedScroll(final CoordinatorLayout pCoordinatorLayout, final View pChild, final View pDirectTargetChild, final View pTarget, final int pNestedScrollAxes) {
        if (mDelegate != null && !mDelegate.isFooterBehaviorEnable()) {
            return false;
        }

        return (pNestedScrollAxes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
    }

    @Override
    public void onNestedPreScroll(final CoordinatorLayout pCoordinatorLayout, final View pChild, final View pTarget, final int pDx, final int pDy, final int[] pConsumed) {
        if (mDelegate != null && !mDelegate.isFooterBehaviorEnable()) {
            return;
        }

        mDeltaY += pDy;

        if (mDeltaY < mShowThreshold || mDeltaY > mHideThreshold ) {
            if (mAnimator != null) {
                if (mAnimator.isRunning()) {
                    if (mDeltaY < 0 == mIsShown) {
                        mDeltaY = 0;
                        return;
                    }
                    else {
                        mAnimator.cancel();
                    }
                }
            }

            mIsShown = mDeltaY < 0;
            mAnimator = ObjectAnimator.ofFloat(pChild, View.TRANSLATION_Y, pChild.getTranslationY(), mIsShown ? 0 : pChild.getHeight());
            mAnimator.setInterpolator(new DecelerateInterpolator());
            mAnimator.start();
            mDeltaY = 0;
        }
    }

    @Override
    public void onStopNestedScroll(final CoordinatorLayout pCoordinatorLayout, final View pChild, final View pTarget) {
        mDeltaY = 0;
    }

    @Override
    public boolean onNestedFling(final CoordinatorLayout coordinatorLayout, final View child, final View target, final float velocityX, final float velocityY, final boolean consumed) {
        if (mDelegate != null && !mDelegate.isFooterBehaviorEnable()) {
            return false;
        }

        if (mAnimator != null) {
            if (mAnimator.isRunning()) {
                if (velocityY < 0 == mIsShown) {
                    return false;
                }
                else {
                    mAnimator.cancel();
                }
            }
        }

        mIsShown = mDeltaY < 0;
        mAnimator = ObjectAnimator.ofFloat(child, View.TRANSLATION_Y, child.getTranslationY(), mIsShown ? 0 : child.getHeight());
        mAnimator.setInterpolator(new DecelerateInterpolator());
        mAnimator.start();
        mDeltaY = 0;

        return false;
    }
}
