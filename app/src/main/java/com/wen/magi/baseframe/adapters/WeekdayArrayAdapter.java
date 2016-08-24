package com.wen.magi.baseframe.adapters;

import android.content.Context;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.wen.magi.baseframe.R;

import java.util.List;

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
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                mContext.getResources().getDimensionPixelSize(R.dimen.text_size_describe));
        String item = getItem(position);
        if (item.equals("日") || item.equals("六"))
            textView.setTextColor(mContext.getResources().getColor(
                    R.color.text_color_gray_99));
        else
            textView.setTextColor(mContext.getResources().getColor(
                    R.color.text_color_gray_99));
        textView.setBackgroundColor(mContext.getResources().getColor(
                R.color.white));
        return textView;
    }

}
