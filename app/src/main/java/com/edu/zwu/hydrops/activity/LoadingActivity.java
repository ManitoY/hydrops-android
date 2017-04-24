package com.edu.zwu.hydrops.activity;

import android.os.Bundle;

import com.edu.zwu.hydrops.base.BaseActivity;
import com.edu.zwu.hydrops.base.BaseFragment;
import com.edu.zwu.hydrops.fragment.LoadingFragment;
import com.edu.zwu.hydrops.system.AppStatusConstant;
import com.edu.zwu.hydrops.system.AppStatusManager;


/**
 * Created by shengwei.yi on 2016/3/15.
 */
public class LoadingActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppStatusManager.getInstance().setAppStatus(AppStatusConstant.STATUS_NORMAL); //进入应用初始化设置成未登录状态
        super.onCreate(savedInstanceState);
    }

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
