package com.wen.magi.baseframe.annotations;

import android.view.View;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用来绑定事件到组件上
 * @author gdpancheng@gmail.com 2013-10-22 下午1:26:27
 */
@Target({ElementType.FIELD,ElementType.LOCAL_VARIABLE})
@Retention(RetentionPolicy.RUNTIME)
public @interface InjectBinder {

	/**
	 * 当前类里面所对应的点击事件
	 * @return
	 */
	String method();

	/**
	 * 当前类里面所对应的点击事件的类型
	 * @return
	 */
	Class<? extends View.OnClickListener>[] listeners();

}
