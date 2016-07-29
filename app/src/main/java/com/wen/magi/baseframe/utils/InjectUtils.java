package com.wen.magi.baseframe.utils;

import android.view.View;

import com.wen.magi.baseframe.annotations.From;
import com.wen.magi.baseframe.base.BaseActivity;
import com.wen.magi.baseframe.base.BaseFragment;

import java.lang.reflect.Field;

/**
 * Created by MVEN on 16/6/16.
 * <p/>
 * email: magiwen@126.com.
 */

public class InjectUtils {
    /**
     * 自动注入findViewById()
     *
     * @param activity 当前的activity
     */
    public static void autoInjectR(BaseActivity activity) {
        if (activity == null || activity.isFinishing()) {
            return;
        }
        Class<?> classAct = activity.getClass();
        //获取所有的变量
        Field[] fields = classAct.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(From.class)) {//判断是否为InjectView注解
                From injectView = field.getAnnotation(From.class);//获取InjectView注解
                int id = injectView.value();//获取注解的值
                if (id > 0) {
                    field.setAccessible(true);//允许范围私有变量
                    try {
                        field.set(activity, activity.findViewById(id));//给当前的变量赋值
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * 自动注入findViewById()
     *
     * @param baseFragment 当前的Fragment
     */
    public static void autoInjectR(BaseFragment baseFragment) {
        if (baseFragment == null || baseFragment.getView() == null) {
            return;
        }
        Class<?> classAct = baseFragment.getClass();
        //获取所有的变量
        Field[] fields = classAct.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(From.class)) {//判断是否为InjectView注解
                From injectView = field.getAnnotation(From.class);//获取InjectView注解
                int id = injectView.value();//获取注解的值
                if (id > 0) {
                    field.setAccessible(true);//允许范围私有变量
                    try {
                        field.set(baseFragment, baseFragment.getView().findViewById(id));//给当前的变量赋值
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * 自动注入findViewById()
     *
     * @param view 当前的View
     */
    public static void autoInjectR(View view) {
        if (view == null || view.getContext() == null) {
            return;
        }
        Class<?> classAct = view.getClass();
        //获取所有的变量
        Field[] fields = classAct.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(From.class)) {//判断是否为InjectView注解
                From injectView = field.getAnnotation(From.class);//获取InjectView注解
                int id = injectView.value();//获取注解的值
                if (id > 0) {
                    field.setAccessible(true);//允许范围私有变量
                    try {
                        field.set(view, view.findViewById(id));//给当前的变量赋值
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * 自动注入 setOnclickListener()
     *
     * @param activity
     */
    public static void autoInjectO(BaseActivity activity) {

    }
}
