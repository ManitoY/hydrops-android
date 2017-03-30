package com.edu.zwu.hydrops.activity;

import android.content.Intent;
import android.os.Bundle;

import com.edu.zwu.hydrops.base.BaseActivity;
import com.edu.zwu.hydrops.base.BaseFragment;
import com.edu.zwu.hydrops.fragment.CoverChooseFragment;

/**
 * Created by shengwei.yi on 2016/4/1.
 */
public class CoverChooseActivity extends BaseActivity{
    private CoverChooseFragment mCoverChooseFragment;

    @Override
    protected int getLayoutId() {
        return 0;
    }

    @Override
    protected void afterViews(Bundle savedInstanceState) {
        initActionBar("更换相册封面");
        showLeftBtn();
    }

    @Override
    protected BaseFragment getFragment() {
        mCoverChooseFragment = new CoverChooseFragment();
        return mCoverChooseFragment;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCoverChooseFragment.onActivityResult(requestCode, resultCode, data);
    }
}
