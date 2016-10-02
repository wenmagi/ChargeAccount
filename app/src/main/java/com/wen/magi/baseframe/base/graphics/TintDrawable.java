package com.wen.magi.baseframe.base.graphics;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorRes;
import android.support.v7.graphics.drawable.DrawableWrapper;

import java.util.Arrays;

/**
 * @author MVEN @ Zhihu Inc.
 * @since 10-02-2016
 */

public class TintDrawable extends DrawableWrapper {
    private ColorStateList mTintColorStateList;

    public TintDrawable(final Drawable pDrawable) {
        super(pDrawable.mutate());

        this.setBounds(pDrawable.getBounds());
    }

    public void setTintColorRes(final Resources pResources, @ColorRes final int pTintColorRes) {
        this.setTintColor(pResources.getColorStateList(pTintColorRes));
    }

    public void setTintColor(final ColorStateList pColorStateList) {
        if (pColorStateList == null) {
            return;
        }
        this.mTintColorStateList = pColorStateList;

        this.setColorFilter(this.mTintColorStateList.getColorForState(this.getState(), this.mTintColorStateList.getDefaultColor()), PorterDuff.Mode.SRC_IN);
    }

    @Override
    protected boolean onStateChange(final int[] pState) {
        if (this.mTintColorStateList != null) {
            super.setColorFilter(this.mTintColorStateList.getColorForState(pState, this.mTintColorStateList.getDefaultColor()), PorterDuff.Mode.SRC_IN);
        }

        return super.onStateChange(pState);
    }

    @Override
    public boolean setState(final int[] stateSet) {
        if (!Arrays.equals(getWrappedDrawable().getState(), stateSet)) {
            return onStateChange(stateSet) && super.setState(stateSet);
        }
        return super.setState(stateSet);
    }

    @Override
    public boolean isStateful() {
        if (this.mTintColorStateList != null) {
            return this.mTintColorStateList.isStateful();
        } else {
            return super.isStateful();
        }
    }

    /**
     * 避免丢失callback，导致动画失效
     */
    @Override
    public void setWrappedDrawable(Drawable pDrawable) {
        if (pDrawable != null && pDrawable.getCallback() != null) {
            setCallback(pDrawable.getCallback());
        }
        super.setWrappedDrawable(pDrawable);
    }
}
