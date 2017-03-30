package com.edu.zwu.hydrops.activity;

import android.os.Bundle;

import com.edu.zwu.hydrops.base.BaseActivity;
import com.edu.zwu.hydrops.base.BaseFragment;
import com.edu.zwu.hydrops.fragment.AboutWeFragment;


/**
 * Created by shengwei.yi on 2016/4/9.
 */
public class AboutWeActivity extends BaseActivity {

    @Override
    protected int getLayoutId() {
        return 0;
    }

    @Override
    protected void afterViews(Bundle savedInstanceState) {
        initActionBar("关于我们");
        showLeftBtn();
    }

    @Override
    protected BaseFragment getFragment() {
        return new AboutWeFragment();
    }
}
