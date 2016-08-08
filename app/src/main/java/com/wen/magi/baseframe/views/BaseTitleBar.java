package com.wen.magi.baseframe.views;


import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wen.magi.baseframe.R;
import com.wen.magi.baseframe.utils.ViewUtils;


/**
 * titleBar对象，公共使用的titleBar
 * <p/>
 * Created by MVEN on 16/8/5.
 * <p/>
 * email: magiwen@126.com.
 */
public class BaseTitleBar extends RelativeLayout {
    public LinearLayout llLeftContent;
    public ImageView ivBackPressed;
    public TextView tvTitle;
    public LinearLayout llRightContent;
    public TextView tvRight;
    public ImageView ivRightBtn;

    public BaseTitleBar(Context context) {
        super(context);
        init(context);
    }

    public BaseTitleBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        inflate(context, R.layout.base_title_bar, this);
        setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        llLeftContent = (LinearLayout) findViewById(R.id.ll_left_content);
        ivBackPressed = (ImageView) findViewById(R.id.iv_left);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        llRightContent = (LinearLayout) findViewById(R.id.ll_right_content);
        tvRight = (TextView) findViewById(R.id.tv_right);
        ivRightBtn = (ImageView) findViewById(R.id.iv_right);
    }

    /**
     * 设置标题
     *
     * @param title 标题内容
     */
    public void setTitle(String title) {
        setTitle(title, false);
    }

    /**
     * 设置标题
     *
     * @param title    标题内容
     * @param showBack 是否有返回键
     */
    public void setTitle(String title, boolean showBack) {
        setTitle(title, showBack, (View[]) null);
    }

    /**
     * 设置标题
     *
     * @param title       标题内容
     * @param showBack    是否有返回键
     * @param rightParams 标题右侧内容
     */
    public void setTitle(String title, boolean showBack, View... rightParams) {
        tvTitle.setText(title);
        llLeftContent.setVisibility(showBack ? View.VISIBLE : View.GONE);

        addRightViews(rightParams);
    }

    private void addRightViews(View... params) {
        if (params == null || params.length == 0) {
            return;
        }

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-2, -2);
        layoutParams.leftMargin = ViewUtils.dp2pix(6);
        llRightContent.removeAllViews();
        for (int i = 0; i < params.length; i++) {
            if (params[i] != null) {
                llRightContent.addView(params[i], layoutParams);
            }
        }
    }

    /**
     * 设置标题字体颜色
     *
     * @param color 标题字体颜色
     */
    public void setTitleColor(int color) {
        tvTitle.setTextColor(color);
    }

    /**
     * 设置返回键图片
     *
     * @param resId 返回键图片id
     */
    public void setBackPressedImageResource(int resId) {
        ivBackPressed.setImageResource(resId);
    }

    /**
     * 设置右侧textView内容
     *
     * @param text 右侧textView内容
     */
    public void setRightText(String text) {
        tvRight.setText(text);
    }

    /**
     * @param resId
     */
    public void setRightImage(int resId) {
        if (resId == 0)
            return;
        if (ivRightBtn.getVisibility() != View.VISIBLE)
            ivRightBtn.setVisibility(View.VISIBLE);
        ivRightBtn.setImageResource(resId);
    }
}