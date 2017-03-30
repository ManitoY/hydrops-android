package com.edu.zwu.hydrops.base;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;

/**
 * Created by shengwei.yi on 2015/12/2.
 */
public abstract class BaseFragment<T extends BasePresenter> extends Fragment {

    protected BaseActivity thisActivity;
    protected ProgressDialog mProgressDialog;
    protected T mPresenter;
    protected BaseFragment thisFragment;
    protected View mView;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        int layoutId = getLayoutId();
        thisActivity = (BaseActivity) getActivity();
        thisFragment = this;
        if (layoutId != 0) {
            mView = inflater.inflate(layoutId, container, false);
            ButterKnife.bind(this, mView);
            afterViews(savedInstanceState);
            return mView;
        } else {
            return super.onCreateView(inflater, container, savedInstanceState);
        }
    }

    protected abstract int getLayoutId();

    protected abstract void afterViews(Bundle savedInstanceState);

    /**
     * 取消进度对话框
     */
    public void dismissProgress() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
