package com.edu.zwu.hydrops.base;

import android.content.Context;

/**
 * Created by shengwei.yi on 2016/4/27.
 */
public abstract class BasePresenter<T extends BaseView> {
    protected T mView;
    protected Context mContext;

    public BasePresenter(T view, Context context) {
        mView = view;
        mContext = context;
    }
}
