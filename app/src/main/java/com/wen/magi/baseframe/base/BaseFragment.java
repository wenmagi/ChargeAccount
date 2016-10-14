package com.wen.magi.baseframe.base;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.ColorInt;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.view.View.OnClickListener;

import com.wen.magi.baseframe.R;
import com.wen.magi.baseframe.activities.MainActivity;
import com.wen.magi.baseframe.bundles.BaseBundleParams;
import com.wen.magi.baseframe.eventbus.DetachAllFragmentEvent;
import com.wen.magi.baseframe.managers.ThemeManager;
import com.wen.magi.baseframe.utils.InjectUtils;
import com.wen.magi.baseframe.utils.SysUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by MVEN on 16/6/14.
 * <p/>
 * email: magiwen@126.com.
 */

public abstract class BaseFragment extends Fragment implements OnClickListener {

    // 用于与 Fragment 显示相关的非 Fragment/View 组件回调
    public interface OnScreenDisplayingCallback {
        void onScreenDisplaying();
    }

    private OnScreenDisplayingCallback mOnScreenDisplayingCallback;

    /**
     * 该fragment attach的activity
     */
    protected BaseActivity activity;

    /**
     * 初始化activity，子Fragment代替{@link #getActivity()}方法
     *
     * @param activity
     */
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = (BaseActivity) activity;
    }

    /**
     * 注册EventBus
     *
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    /**
     * 得到Fragment填充View后，添加注解
     *
     * @param savedInstanceState
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        InjectUtils.autoInjectR(this);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onViewCreated(final View view, final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        onSendView();
    }

    @Override
    public void onResume() {
        super.onResume();

        if (getClass().getName().equals(getMainActivity().getLatestFragmentName())) {
            onScreenDisplaying();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public MainActivity getMainActivity() {
        if (this.getActivity() instanceof BaseActivity) {
            return (MainActivity) this.getActivity();
        } else {
            throw new IllegalStateException("Must be added to MainActivity: Current is " + getActivity().getClass().getSimpleName());
        }
    }

    // 此方法一定可以保证屏幕上显示 Fragment 就是这个 Fragment
    @CallSuper
    public void onScreenDisplaying() {
        if (isSystemUiFullscreen()) {
            switchToTranslucentStatus();
        } else {
            switchToNormalStatus();
        }

        if (mOnScreenDisplayingCallback != null) {
            mOnScreenDisplayingCallback.onScreenDisplaying();
        }
    }

    protected void onSendView() {

    }

    public void sendView() {
        onSendView();
    }

    /**
     * 跳转到其他页面
     *
     * @param clazz     目标activity
     * @param pageParam 目标页面参数
     */
    protected void startActivity(Class<? extends BaseActivity> clazz, BaseBundleParams pageParam) {
        startActivity(clazz, pageParam, 0);
    }

    /**
     * 跳转到其他页面
     *
     * @param clazz       目标activity
     * @param requestCode 目标activity参数
     */
    protected void startActivity(Class<? extends BaseActivity> clazz, int requestCode) {
        startActivity(clazz, null, requestCode);
    }

    /**
     * 跳转到其他页面
     *
     * @param clazz       目标activity
     * @param params      每个activity之间的参数需封装成{@link BaseBundleParams}
     * @param requestCode 请求码
     */
    protected void startActivity(Class<? extends BaseActivity> clazz, BaseBundleParams params, int requestCode) {
        if (!isValidActivity()) {
            return;
        }
        Intent intent = new Intent(getActivity(), clazz);
        if (params != null) {
            Bundle bundle = new Bundle();
            bundle.putSerializable(BaseBundleParams.PARAM_SKEY, params);
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, requestCode);
    }

    /**
     * {@link #activity} 是否可用
     *
     * @return
     */
    public boolean isValidActivity() {
        if (isRemoving())
            return false;

        return activity != null && !activity.isFinishing();
    }

    /**
     * 防止极端情况的出现:Activity结束,事件还没有开始处理
     *
     * @param v targetView
     */
    @Override
    public void onClick(View v) {
        if (!isValidActivity()) {
            return;
        }

        OnClickView(v);
    }

    /**
     * 子类必须复写，代替onClick事件
     *
     * @param v 目标View
     */
    protected abstract void OnClickView(View v);

    /**
     * 是否结束所有Frament
     *
     * @param event event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onFinishAllFragmentEvent(DetachAllFragmentEvent event) {
        getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
    }

    /**
     * 判断该Frament是否是填充整个屏幕
     *
     * @return false
     */
    public boolean isSystemUiFullscreen() {
        return false;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    protected void switchToTranslucentStatus() {
        if (SysUtils.nowSDKINTBigger(Build.VERSION_CODES.LOLLIPOP)) {
            getActivity().getWindow().setStatusBarColor(ContextCompat.getColor(getContext(), android.R.color.transparent));
            forceToFitsSystemWindows(true);
        }
    }

    @TargetApi (Build.VERSION_CODES.LOLLIPOP)
    private void switchToNormalStatus() {
        if (SysUtils.nowSDKINTBigger(Build.VERSION_CODES.LOLLIPOP)) {
            getActivity().getWindow().setStatusBarColor(provideStatusBarColor());
            forceToFitsSystemWindows(false);
        }
    }

    @TargetApi (Build.VERSION_CODES.LOLLIPOP)
    protected void forceToFitsSystemWindows(boolean fits) {
        if (!SysUtils.nowSDKINTBigger(Build.VERSION_CODES.LOLLIPOP)) {
            return;
        }

        View view = getMainActivity().getRootView();

        if (fits) {
            view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        } else {
            view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
        }

        ViewCompat.setFitsSystemWindows(view, fits);
        ViewCompat.requestApplyInsets(view);
    }

    @ColorInt
    protected int provideStatusBarColor() {
        boolean isDark = ThemeManager.getInstance().getCurrentTheme(getContext()) == ThemeManager.DARK;
        return ContextCompat.getColor(getContext(), isDark ? R.color.colorPrimaryDark_dark : R.color.colorPrimaryDark_light);
    }
}
