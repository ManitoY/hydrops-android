package com.edu.zwu.hydrops.activity;

import android.os.Bundle;

import com.edu.zwu.hydrops.base.BaseActivity;
import com.edu.zwu.hydrops.base.BaseFragment;
import com.edu.zwu.hydrops.fragment.LoginFragment;

/**
 * Created by shengwei.yi on 2015/11/2.
 */
public class LoginActivity extends BaseActivity {

    @Override
    protected int getLayoutId() {
        return 0;
    }

    @Override
    protected void afterViews(Bundle savedInstanceState) {
        initActionBar("登录");
        showLeftBtn();
    }

    @Override
    protected BaseFragment getFragment() {
        return new LoginFragment();
    }

}
