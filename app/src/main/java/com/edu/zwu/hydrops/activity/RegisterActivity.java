package com.edu.zwu.hydrops.activity;


import android.content.Intent;
import android.os.Bundle;

import com.edu.zwu.hydrops.base.BaseActivity;
import com.edu.zwu.hydrops.base.BaseFragment;
import com.edu.zwu.hydrops.fragment.RegisterFragment;

/**
 * Created by shengwei.yi on 2016/3/7.
 */
public class RegisterActivity extends BaseActivity {
    private RegisterFragment mRegisterFragment;

    @Override
    protected int getLayoutId() {
        return 0;
    }

    @Override
    protected void afterViews(Bundle savedInstanceState) {
        initActionBar("注册");
        showLeftBtn();
    }

    @Override
    protected BaseFragment getFragment() {
        mRegisterFragment = new RegisterFragment();
        return mRegisterFragment;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mRegisterFragment.onActivityResult(requestCode, resultCode, data);
    }
}
