package com.wen.magi.baseframe.annotations;

import android.view.View;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by MVEN on 16/6/14.
 * <p/>
 * email: magiwen@126.com.
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@BaseAnnotation(listenerSetter = "setOnclickListener",
        listenerType = View.OnClickListener.class,
        methodName = "onclick")

public @interface OnClick {
    int[] values();
}
