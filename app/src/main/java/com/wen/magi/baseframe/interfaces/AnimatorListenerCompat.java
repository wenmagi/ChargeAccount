package com.wen.magi.baseframe.interfaces;

import android.animation.Animator;


/**
 * @author zhangzhaowen @ Zhihu Inc.
 * @since 11-18-2016
 */

public abstract class AnimatorListenerCompat implements Animator.AnimatorListener {
    /**
     * <p>Notifies the start of the animation.</p>
     *
     * @param animation The started animation.
     */
    public void onAnimationStart(Animator animation){}

    /**
     * <p>Notifies the end of the animation. This callback is not invoked
     * for animations with repeat count set to INFINITE.</p>
     *
     * @param animation The animation which reached its end.
     */
    public void onAnimationEnd(Animator animation){}

    /**
     * <p>Notifies the cancellation of the animation. This callback is not invoked
     * for animations with repeat count set to INFINITE.</p>
     *
     * @param animation The animation which was canceled.
     */
    public void onAnimationCancel(Animator animation){}

    /**
     * <p>Notifies the repetition of the animation.</p>
     *
     * @param animation The animation which was repeated.
     */
    public void onAnimationRepeat(Animator animation){}

}
