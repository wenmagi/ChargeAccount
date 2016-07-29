package com.wen.magi.baseframe.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by MVEN on 16/6/14.
 * <p/>
 * email: magiwen@126.com.
 */

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface BaseAnnotation {
    Class<?> listenerType();

    String listenerSetter();

    String methodName();
}