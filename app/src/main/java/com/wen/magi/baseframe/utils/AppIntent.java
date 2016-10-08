package com.wen.magi.baseframe.utils;

import android.os.Bundle;
import android.support.v4.app.Fragment;

/**
 * 统一封装所有Intent
 *
 * @author MVEN @ Zhihu Inc.
 * @since 10-08-2016
 */

public class AppIntent {
    private Class<? extends Fragment> mClazz;

    private Bundle mArguments;

    private String mTag;

    private boolean mClearTask = false;

    private boolean mOverlay = false;

    private boolean mPopSelf = false;

    private int mPriorityTab =  0;

    private AppIntent(final Class<? extends Fragment> pClazz,final Bundle pArguments,final String pTag){
        mClazz =  pClazz;
        mArguments = pArguments;
        mTag = pTag;
    }

    public Class<? extends Fragment> getClazz() {
        return mClazz;
    }

    public AppIntent setClazz(Class<? extends Fragment> mClazz) {
        this.mClazz = mClazz;
        return this;
    }

    public Bundle getArguments() {
        return mArguments;
    }

    public AppIntent setArguments(Bundle mArguments) {
        this.mArguments = mArguments;
        return this;
    }

    public String getTag() {
        return mTag;
    }

    public AppIntent setTag(String mTag) {
        this.mTag = mTag;
        return this;
    }

    public boolean isClearTask() {
        return mClearTask;
    }

    public AppIntent setClearTask(boolean mClearTask) {
        this.mClearTask = mClearTask;
        return this;
    }

    public boolean isOverlay() {
        return mOverlay;
    }

    public AppIntent setOverlay(boolean mOverlay) {
        this.mOverlay = mOverlay;
        return this;
    }

    public boolean isPopSelf() {
        return mPopSelf;
    }

    public AppIntent setPopSelf(boolean mPopSelf) {
        this.mPopSelf = mPopSelf;
        return this;
    }

    public int getPriorityTab() {
        return mPriorityTab;
    }

    public AppIntent setPriorityTab(int mPriorityTab) {
        this.mPriorityTab = mPriorityTab;
        return this;
    }
}
