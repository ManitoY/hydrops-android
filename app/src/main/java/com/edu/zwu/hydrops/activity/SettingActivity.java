package com.edu.zwu.hydrops.activity;


import android.os.Bundle;

import com.edu.zwu.hydrops.base.BaseActivity;
import com.edu.zwu.hydrops.base.BaseFragment;
import com.edu.zwu.hydrops.fragment.SettingFragment;

/**
 * Created by shengwei.yi on 2016/4/12.
 */
public class SettingActivity extends BaseActivity {

    @Override
    protected int getLayoutId() {
        return 0;
    }

    @Override
    protected void afterViews(Bundle savedInstanceState) {
        initActionBar("设置");
        showLeftBtn();
    }

    @Override
    protected BaseFragment getFragment() {
        return new SettingFragment();
    }

}
