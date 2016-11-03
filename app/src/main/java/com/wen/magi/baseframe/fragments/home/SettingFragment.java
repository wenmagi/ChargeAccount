package com.wen.magi.baseframe.fragments.home;

import android.animation.LayoutTransition;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.percent.PercentRelativeLayout;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.wen.magi.baseframe.R;
import com.wen.magi.baseframe.activities.MainActivity;
import com.wen.magi.baseframe.base.BaseLazyLoadFragment;
import com.wen.magi.baseframe.managers.ThemeManager;
import com.wen.magi.baseframe.utils.SysUtils;
import com.wen.magi.baseframe.utils.ViewUtils;

/**
 * @author MVEN @ Zhihu Inc.
 * @since 10-02-2016
 */

public class SettingFragment extends BaseLazyLoadFragment implements TabLayout.OnTabSelectedListener {

    private LinearLayout container;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.dialog_test_fragment, container, false);
        initCoordinatorLayout(root);
        return root;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)

    private void initCoordinatorLayout(View root) {
        container = (LinearLayout) root.findViewById(R.id.container_layout);
        LayoutTransition layoutTransition = new LayoutTransition();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            layoutTransition.disableTransitionType(LayoutTransition.APPEARING);
            layoutTransition.disableTransitionType(LayoutTransition.DISAPPEARING);
        }

        container.setLayoutTransition(layoutTransition);
        //设置ImageView表面蒙层
        ImageView imageView = (ImageView) root.findViewById(R.id.image_view);
        imageView.setColorFilter(getResources().getColor(R.color.alpha_20_percent_main_theme_color, activity.getTheme()), PorterDuff.Mode.SRC_ATOP);

        //设置Toolbar
        Toolbar mToolbar = (Toolbar) root.findViewById(R.id.toolbar);
        mToolbar.inflateMenu(R.menu.menu_main);
        mToolbar.setOnMenuItemClickListener(onMenuItemClick);
        mToolbar.setNavigationIcon(R.mipmap.icon_today);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewUtils.showToast(getContext(), "back");
            }
        });

        //使用CollapsingToolbarLayout必须把title设置到CollapsingToolbarLayout上，设置到Toolbar上则不会显示
        CollapsingToolbarLayout mCollapsingToolbarLayout = (CollapsingToolbarLayout) root.findViewById(R.id.collapsing_toolbar_layout);
        mCollapsingToolbarLayout.setTitle("CollapsingToolbarLayout");
        //通过CollapsingToolbarLayout修改字体颜色
        mCollapsingToolbarLayout.setExpandedTitleColor(SysUtils.nowSDKINTBigger(Build.VERSION_CODES.M) ? getResources().getColor(R.color.main_theme_color, activity.getTheme()) : getResources().getColor(R.color.main_theme_color));//设置还没收缩时状态下字体颜色
        mCollapsingToolbarLayout.setCollapsedTitleTextColor(SysUtils.nowSDKINTBigger(Build.VERSION_CODES.M) ? getResources().getColor(R.color.white, activity.getTheme()) : getResources().getColor(R.color.white));//设置收缩后Toolbar上字体的颜色
    }

    private Toolbar.OnMenuItemClickListener onMenuItemClick = new Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            String msg = "";
            switch (menuItem.getItemId()) {
                case R.id.action_edit:
                    msg += "Click edit";
                    View view = View.inflate(activity, R.layout.activity_dialog_test, null);
                    container.addView(view);
                    break;
                case R.id.action_share:
                    msg += "Click share";
                    container.removeViewAt(0);
                    break;
                case R.id.action_settings:
                    msg += "Click setting";
                    break;
            }

            if (!msg.equals("")) {
                Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
            }
            return true;
        }
    };

    @Override
    protected void lazyLoad() {

    }

    @Override
    public void onPause() {
        super.onPause();
        MainActivity.from(getContext()).unregisterTabObserver(this);
    }

    @Override
    protected void OnClickView(View v) {

    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
}
