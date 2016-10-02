package com.wen.magi.baseframe.fragments;

import android.os.Bundle;
import android.support.annotation.MainThread;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.wen.magi.baseframe.R;
import com.wen.magi.baseframe.base.BaseFragment;
import com.wen.magi.baseframe.interfaces.BackPressedConcerned;

/**
 * @author MVEN @ Zhihu Inc.
 * @since 10-02-2016
 */

public class ParentFragment extends Fragment implements FragmentManager.OnBackStackChangedListener {


    private static final String KEY_HOST_INITIALIZED = "note_heart:parent_fragment:host_initialized";

    private String mHostFragmentClassName;

    private boolean mInitializedHost = false;

    public ParentFragment() {
    }

    @Override
    public void onCreate(@Nullable final Bundle SavedInstanceState) {
        super.onCreate(SavedInstanceState);

        final Bundle arguments = this.getArguments();

        if (arguments != null) {
            this.mHostFragmentClassName = arguments.getString("host");
        } else {
            throw new IllegalStateException("must set host fragment class name");
        }
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater pLayoutInflater, @Nullable final ViewGroup pContainer, @Nullable final Bundle pSavedInstanceState) {

        final FrameLayout layout = new FrameLayout(this.getContext());

        layout.setId(android.R.id.content);

        return layout;
    }

    @Override
    public void onViewCreated(final View pView, @Nullable final Bundle pSavedInstanceState) {
        super.onViewCreated(pView, pSavedInstanceState);

        if (pSavedInstanceState != null) {
            this.mInitializedHost = pSavedInstanceState.getBoolean(ParentFragment.KEY_HOST_INITIALIZED);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        this.getChildFragmentManager().removeOnBackStackChangedListener(this);
    }

    private void initializeHostIfNeed() {
        if (this.mInitializedHost) {
            return;
        }

        this.getChildFragmentManager().addOnBackStackChangedListener(this);

        final FragmentTransaction transaction = this.getChildFragmentManager().beginTransaction();

        // 简单添加动画
        transaction.setCustomAnimations(R.anim.none_animation, R.anim.fragment_exit,
                R.anim.none_animation, R.anim.none_animation);

        transaction.add(android.R.id.content, Fragment.instantiate(this.getContext(), this.mHostFragmentClassName), "host");

        transaction.commitAllowingStateLoss();

        this.mInitializedHost = true;
    }

    @Override
    public void onBackStackChanged() {
        Fragment fragment = this.getCurrentChild();

        if (fragment instanceof BaseFragment) {
            ((BaseFragment) fragment).onScreenDisplaying();
        }
    }

    @Override
    public void onSaveInstanceState(final Bundle pOutState) {
        super.onSaveInstanceState(pOutState);

        pOutState.putBoolean(ParentFragment.KEY_HOST_INITIALIZED, this.mInitializedHost);
    }

    public boolean onBackPressed(boolean isForce) {
        if (getCurrentChild() instanceof BackPressedConcerned && !isForce) {
            boolean handle = ((BackPressedConcerned) getCurrentChild()).onBackPressed();
            if (handle) {
                return true;
            }
        }

        try {
            return this.getChildFragmentManager().popBackStackImmediate();
        } catch (IllegalStateException ex) {
            // java.lang.IllegalStateException - Can not perform this action after onSaveInstanceState
            return false;
        }
    }

    public void addChild(final Fragment fragment, final String pTag) {

        this.initializeHostIfNeed();

        final FragmentTransaction transaction = this.getChildFragmentManager().beginTransaction();

        // transaction.setCustomAnimations(R.anim.none_animation, R.anim.none_animation, R.anim.none_animation, R.anim.none_animation);

        // transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);

        final Fragment currentFragment = this.getCurrentChild();

        // setCustomAnimations() 四个参数的区别在于,
        // 前两个参数用于启动 Fragment 的切换过程,
        // 后两个参数用于 back 操作的切换过程。
        //
        // TODO: 暂时取消动画的 Fragment 都是因为视觉效果不好
//        if (currentFragment instanceof SearchFragment
//                || currentFragment instanceof ArticleFragment
//                || fragment instanceof ColumnFragment) {
//            transaction.setCustomAnimations(R.anim.none_animation, R.anim.none_animation,
//                    R.anim.none_animation, R.anim.none_animation);
//        } else {
            transaction.setCustomAnimations(R.anim.fragment_enter, R.anim.fragment_exit,
                    R.anim.none_animation, R.anim.none_animation);
//        }

        if (currentFragment != null) {
            transaction.hide(currentFragment);
        }

        transaction.add(android.R.id.content, fragment, pTag);

        transaction.addToBackStack(pTag);

        transaction.commitAllowingStateLoss();
    }

    public void clearAllChildren() {
        //noinspection StatementWithEmptyBody
        try {
            while (this.getChildFragmentManager().popBackStackImmediate()) {
            }
        } catch (IllegalStateException ex) {
            // java.lang.IllegalStateException - Can not perform this action after onSaveInstanceState
            ex.printStackTrace();
        }
    }

    public Fragment getCurrentChild() {
        final int count = this.getChildFragmentManager().getBackStackEntryCount();

        if (count > 0) {
            final String currentChildName = this.getChildFragmentManager().getBackStackEntryAt(count - 1).getName();

            return this.getChildFragmentManager().findFragmentByTag(currentChildName);
        } else {
            return this.getChildFragmentManager().findFragmentByTag("host");
        }
    }

    @MainThread
    public void onSelected() {
        Fragment fragment = this.getCurrentChild();

        if (fragment instanceof BaseFragment) {
            // MainTab 切换时调用 GA 统计
            ((BaseFragment) fragment).sendView();
            ((BaseFragment) fragment).onScreenDisplaying();
        }

        this.initializeHostIfNeed();
    }

    @MainThread
    public void onReselected() {
        if (this.getChildFragmentManager().getBackStackEntryCount() > 0) {
            this.clearAllChildren();
        } else {
//            final Fragment hostFragment = getCurrentChild();
//
//            if (hostFragment instanceof BaseAdvancePagingFragment) {
//                ((BaseAdvancePagingFragment) hostFragment).scrollToTopOrRefresh(true);
//            } else if (hostFragment instanceof BaseTabsFragment) {
//                final Fragment currentChildTabItem = ((BaseTabsFragment) hostFragment).getCurrentTabItem();
//
//                if (currentChildTabItem != null && currentChildTabItem instanceof BaseAdvancePagingFragment) {
//                    ((BaseAdvancePagingFragment) currentChildTabItem).scrollToTopOrRefresh(true);
//                }
//            }
        }
    }

    public interface Child {

        boolean isShowBottomNavigation();
    }
}
