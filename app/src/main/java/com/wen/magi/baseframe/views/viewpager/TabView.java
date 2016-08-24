package com.wen.magi.baseframe.views.viewpager;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.wen.magi.baseframe.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MVEN on 16/8/8.
 * <p/>
 * email: magiwen@126.com.
 */
public class TabView extends LinearLayout implements View.OnClickListener {


    private ViewPager mViewPager;
    private ViewPager.OnPageChangeListener mOnPageChangeListener;
    private PagerAdapter mPagerAdapter;
    private int mChildSize;
    private List<TabItem> mTabItems;
    private OnItemIconTextSelectListener mListener;
    private OnItemClick itemCLickListener;

    private int mTextSize = 12;
    private int mTextColorSelect/* = 0xff45c01a*/;
    private int mTextColorNormal/* = 0xff777777*/;
    private int mPadding = 10;
    private int imageMargin = 3;

    public TabView(Context context) {
        this(context, null);
    }

    public TabView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    // api >= 11
    public TabView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setBackgroundColor(context.getResources().getColor(R.color.main_theme_color));
        mTextColorSelect = getResources().getColor(R.color.orange);
        mTextColorNormal = getResources().getColor(R.color.white);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TabView);
        int N = typedArray.getIndexCount();
        for (int i = 0; i < N; i++) {
            switch (typedArray.getIndex(i)) {
                case R.styleable.TabView_text_size:
                    mTextSize = (int) typedArray.getDimension(i,
                            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                                    mTextSize, getResources().getDisplayMetrics()));
                    break;
                case R.styleable.TabView_text_normal_color:
                    mTextColorNormal = typedArray.getColor(i, mTextColorNormal);
                    break;
                case R.styleable.TabView_text_select_color:
                    mTextColorSelect = typedArray.getColor(i, mTextColorSelect);
                    break;
                case R.styleable.TabView_item_padding:
                    mPadding = (int) typedArray.getDimension(i,
                            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                                    mPadding, getResources().getDisplayMetrics()));
                    break;
                case R.styleable.TabView_image_text_margin:
                    imageMargin = (int) typedArray.getDimension(i,
                            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                                    imageMargin, getResources().getDisplayMetrics()));
                    break;
            }
        }

        setGravity(Gravity.CENTER_VERTICAL);
        typedArray.recycle();
        mTabItems = new ArrayList<>();
    }

    public void setViewPager(final ViewPager mViewPager) {
        if (mViewPager == null) {
            return;
        }
        this.mViewPager = mViewPager;
        this.mPagerAdapter = mViewPager.getAdapter();
        if (this.mPagerAdapter == null) {
            throw new RuntimeException("set TabViewViewPager before ViewPagerPagerAdapter");
        }
        this.mChildSize = this.mPagerAdapter.getCount();
        this.mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @SuppressLint("NewApi")//api >= 11
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                View leftView;
                View rightView;

                if (positionOffset > 0) {
                    leftView = mViewPager.getChildAt(position);
                    rightView = mViewPager.getChildAt(position + 1);
                    leftView.setAlpha(1 - positionOffset);
                    rightView.setAlpha(positionOffset);
                    mTabItems.get(position).setTabAlpha(1 - positionOffset);
                    mTabItems.get(position + 1).setTabAlpha(positionOffset);
                } else {
                    mViewPager.getChildAt(position).setAlpha(1);
                    mTabItems.get(position).setTabAlpha(1 - positionOffset);
                }
                if (mOnPageChangeListener != null) {
                    mOnPageChangeListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
                }
            }

            @Override
            public void onPageSelected(int position) {
                if (mOnPageChangeListener != null) {
                    mOnPageChangeListener.onPageSelected(position);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (mOnPageChangeListener != null) {
                    mOnPageChangeListener.onPageScrollStateChanged(state);
                }
            }
        });
        if (mPagerAdapter instanceof OnItemIconTextSelectListener) {
            mListener = (OnItemIconTextSelectListener) mPagerAdapter;
        } else {
            throw new RuntimeException("pageAdapter implements OnItemIconTextSelectListener");
        }
        initItem();
    }

    public void setOnPageChangeListener(ViewPager.OnPageChangeListener mOnPageChangeListener) {
        this.mOnPageChangeListener = mOnPageChangeListener;
    }

    private void initItem() {
        for (int i = 0; i < mChildSize; i++) {
            TabItem tabItem = new TabItem(getContext());
            LayoutParams params = new LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1);
            tabItem.setPadding(mPadding, mPadding, mPadding, mPadding);
            tabItem.setIconText(mListener == null ? null : mListener.onIconSelect(i), mListener.onTextSelect(i));
            tabItem.setTextSize(mListener.onIconSelect(i) != null ? mTextSize : 16);
            tabItem.setTextColorNormal(mTextColorNormal);
            tabItem.setTextColorSelect(mTextColorSelect);
            tabItem.setLayoutParams(params);
            tabItem.setTag(i);
            tabItem.setOnClickListener(this);
            tabItem.setMargin(imageMargin);
            mTabItems.add(tabItem);
            addView(tabItem);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void setCurrentPage(int index) {
        for (TabItem tabItem : mTabItems) {
            tabItem.setTabAlpha(0);
        }
        mTabItems.get(index).setTabAlpha(1);
        mViewPager.setCurrentItem(index, false);
    }

    public void setCircleVisible(boolean bVisible, int index) {
        mTabItems.get(index).setCircleVisible(bVisible);
        ;
    }


    public void setCircleDigitVisible(boolean bVisible, int index) {
        mTabItems.get(index).setCircleDigitVisible(bVisible);
        ;
    }

    public void setOnItemClickListener(OnItemClick onItemClickListener) {
        this.itemCLickListener = onItemClickListener;
    }

    @Override
    public void onClick(View v) {
        int position = (Integer) v.getTag();
        if (mViewPager.getCurrentItem() == position) {
            return;
        }
        for (TabItem tabItem : mTabItems) {
            tabItem.setTabAlpha(0);
        }
        mTabItems.get(position).setTabAlpha(1);
        mViewPager.setCurrentItem(position, false);
        if (itemCLickListener != null)
            itemCLickListener.onItemClick(position);
    }

    public interface OnItemClick {
        void onItemClick(int position);
    }

    public interface OnItemIconTextSelectListener {

        int[] onIconSelect(int position);

        String onTextSelect(int position);
    }
}
