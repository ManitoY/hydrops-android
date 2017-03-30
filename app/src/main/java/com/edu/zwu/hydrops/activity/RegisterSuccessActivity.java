package com.edu.zwu.hydrops.activity;

import android.os.Bundle;

import com.edu.zwu.hydrops.base.BaseActivity;
import com.edu.zwu.hydrops.base.BaseFragment;
import com.edu.zwu.hydrops.fragment.RegisterSuccessFragment;

/**
 * Created by shengwei.yi on 2016/3/8.
 */
public class RegisterSuccessActivity extends BaseActivity {

    @Override
    protected int getLayoutId() {
        return 0;
    }

    @Override
    protected void afterViews(Bundle savedInstanceState) {

    }

    @Override
    protected BaseFragment getFragment() {
        return new RegisterSuccessFragment();
    }
}
