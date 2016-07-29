package com.wen.magi.baseframe.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.View.OnClickListener;

import com.wen.magi.baseframe.bundles.BaseBundleParams;
import com.wen.magi.baseframe.eventbus.DetachAllFragmentEvent;
import com.wen.magi.baseframe.utils.InjectUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by MVEN on 16/6/14.
 * <p/>
 * email: magiwen@126.com.
 */

public abstract class BaseFragment extends Fragment implements OnClickListener {

    /**
     * 该fragment attach的activity
     */
    protected Activity activity;

    /**
     * 初始化activity，子Fragment代替{@link #getActivity()}方法
     *
     * @param activity
     */
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
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
    public void onResume() {
        super.onResume();
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onFinishAllFragmentEvent(DetachAllFragmentEvent event) {
        getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
    }
}
