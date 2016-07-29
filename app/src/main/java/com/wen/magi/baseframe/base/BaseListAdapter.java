package com.wen.magi.baseframe.base;

import android.content.Context;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MVEN on 16/6/17.
 * <p/>
 * email: magiwen@126.com.
 */


public abstract class BaseListAdapter<T> extends BaseAdapter {

    protected Context context;
    protected List<T> datas;

    public BaseListAdapter(Context context, List<T> datas) {
        this.context = context;
        this.datas = new ArrayList<>();
        datas.addAll(datas);
    }

    public void setDatas(List<T> datas) {
        this.datas.clear();
        if (datas != null) {
            this.datas.addAll(datas);
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (datas == null)
            return 0;
        return datas.size();
    }

    @Override
    public Object getItem(int position) {
        if (datas == null)
            return null;
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (holder == null) {
            convertView = View.inflate(context, getItemResourse(), null);
            holder = new ViewHolder(convertView);
        } else
            holder = (ViewHolder) convertView.getTag();
        return getItemView(position, convertView, holder);
    }

    /**
     * 子Adapter复写此方法，对Holder持有的View进行赋值
     *
     * @param position
     * @param convertView
     * @param holder
     * @return
     */
    protected abstract View getItemView(int position, View convertView, ViewHolder holder);

    /**
     * 子Adapter复写此方法，获取itemView的resourseID
     *
     * @return itemViewのresourseID
     */
    public abstract int getItemResourse();

    protected class ViewHolder {
        private SparseArray<View> views = new SparseArray<>();
        private View convertView;

        public ViewHolder(View convertView) {
            this.convertView = convertView;
        }

        /**
         * 通过此方法，可以获取resId对应的View
         *
         * @param resId resourseID
         * @param <T>   View
         * @return resId对应的View
         */
        public <T extends View> T getView(int resId) {
            View v = views.get(resId);

            if (null == v) {
                v = convertView.findViewById(resId);
                views.put(resId, v);
            }
            return (T) v;
        }
    }


    /**
     * 局部刷新可见区域的数据
     *
     * @param view      需要刷新的View
     * @param itemIndex 该view的位置
     */
    protected void updateViews(View view, int itemIndex) {
        if (view == null)
            return;

        ViewHolder holder = (ViewHolder) view.getTag();
        findHolderViewAndRefresh(holder, itemIndex);
    }

    /**
     * 寻找到holder持有view的内容，并进行刷新
     *
     * @param holder    目标holder
     * @param itemIndex 数据datas的位置
     */
    protected void findHolderViewAndRefresh(ViewHolder holder, int itemIndex) {
    }
}
