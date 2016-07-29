package com.wen.magi.baseframe.views;

import android.content.Context;
import android.util.AttributeSet;

import com.android.volley.toolbox.NetworkImageView;
import com.wen.magi.baseframe.managers.RequestQueueManager;

/**
 * Created by MVEN on 16/6/17.
 * <p/>
 * email: magiwen@126.com.
 */


public class NetImageView extends NetworkImageView {
    public NetImageView(Context context) {
        super(context);
    }

    public NetImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NetImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * 为ImageView设置URL
     *
     * @param imageKey image url
     */
    public void setImageKey(String imageKey) {
        super.setImageUrl(imageKey, RequestQueueManager.getImageLoader());
    }
}
