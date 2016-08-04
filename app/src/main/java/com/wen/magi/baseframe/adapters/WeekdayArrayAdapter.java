package com.wen.magi.baseframe.adapters;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.wen.magi.baseframe.R;

/**
 * Created by MVEN on 16/7/29.
 * <p/>
 * email: magiwen@126.com.
 */
public class WeekdayArrayAdapter extends ArrayAdapter<String> {
    private Context mContext;

    public WeekdayArrayAdapter(Context context, int textViewResourceId,
                               List<String> objects) {
        super(context, textViewResourceId, objects);
        mContext = context;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEnabled(int position) {
        return false;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        TextView textView = (TextView) super.getView(position, convertView,
                parent);
        String item = getItem(position);
        if (item.equals("日") || item.equals("六"))
            textView.setTextColor(mContext.getResources().getColor(
                    R.color.green));
        else
            textView.setTextColor(mContext.getResources().getColor(
                    R.color.green));
        textView.setBackgroundColor(mContext.getResources().getColor(
                R.color.white));
        return textView;
    }

}
