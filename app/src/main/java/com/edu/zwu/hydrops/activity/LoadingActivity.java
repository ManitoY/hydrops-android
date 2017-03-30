package com.edu.zwu.hydrops.activity;

import android.os.Bundle;

import com.edu.zwu.hydrops.base.BaseActivity;
import com.edu.zwu.hydrops.base.BaseFragment;
import com.edu.zwu.hydrops.fragment.LoadingFragment;


/**
 * Created by shengwei.yi on 2016/3/15.
 */
public class LoadingActivity extends BaseActivity {
    @Override
    protected int getLayoutId() {
        return 0;
    }

    @Override
    protected void afterViews(Bundle savedInstanceState) {
    }

    @Override
    protected BaseFragment getFragment() {
        return new LoadingFragment();
    }
}
