package com.edu.zwu.hydrops.activity;

import android.os.Bundle;

import com.edu.zwu.hydrops.base.BaseActivity;
import com.edu.zwu.hydrops.base.BaseFragment;
import com.edu.zwu.hydrops.fragment.WaterDataFragment;

/**
 * Created by shengwei.yi on 2015/12/2.
 */
public class WaterDataActivity extends BaseActivity implements WaterDataFragment.FragmentCallBack {

    @Override
    protected int getLayoutId() {
        return 0;
    }

    @Override
    protected void afterViews(Bundle savedInstanceState) {
        initActionBar("积水详情");
        showLeftBtn();
    }

    @Override
    protected BaseFragment getFragment() {
        return new WaterDataFragment();
    }

    @Override
    public void setAcitonBar(String address) {
        initActionBar(address);
        showLeftBtn();
    }
}
