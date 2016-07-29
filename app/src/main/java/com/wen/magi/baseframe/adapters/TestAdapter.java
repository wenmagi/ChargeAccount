package com.wen.magi.baseframe.adapters;

import android.content.Context;
import android.view.View;

import com.wen.magi.baseframe.base.BaseListAdapter;

import java.util.List;

/**
 * Created by MVEN on 16/6/30.
 * <p/>
 * email: magiwen@126.com.
 */


public class TestAdapter extends BaseListAdapter<String> {

    public TestAdapter(Context context, List<String> datas) {
        super(context, datas);
    }

    @Override
    protected View getItemView(int position, View convertView, BaseListAdapter<String>.ViewHolder holder) {
        return null;
    }

    @Override
    public int getItemResourse() {
        return 0;
    }

}
