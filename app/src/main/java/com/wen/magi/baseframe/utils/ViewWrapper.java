package com.wen.magi.baseframe.utils;

import android.view.View;

/**
 * Created by MVEN on 16/5/3.
 */
public class ViewWrapper {
    private View mTarget;

    public ViewWrapper(View target) {
        mTarget = target;
    }

    public int getWidth() {
        return mTarget.getLayoutParams().width;
    }

    public void setWidth(int width) {
        mTarget.getLayoutParams().width = width;
        mTarget.requestLayout();
    }

    public int getHeight() {
        return mTarget.getLayoutParams().height;
    }

    public void setHeight(int height) {
        mTarget.getLayoutParams().height = height;
        mTarget.requestLayout();
    }

    public float getAlpha() {
        return mTarget.getAlpha();
    }

    public void setAlpha(float alpha) {
        mTarget.setAlpha(alpha);
        mTarget.requestLayout();
    }

    public float getScale() {
        return mTarget.getScaleX();
    }

    public void setScale(float scale) {
        mTarget.setScaleX(scale);
        mTarget.setScaleY(scale);
        mTarget.requestLayout();
    }
}
