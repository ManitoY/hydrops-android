package com.edu.zwu.hydrops.fragment;

import android.os.Bundle;
import android.os.Handler;

import com.edu.zwu.hydrops.R;
import com.edu.zwu.hydrops.base.BaseFragment;
import com.edu.zwu.hydrops.util.ActivityUtil;

/**
 * Created by shengwei.yi on 2016/4/28.
 */
public class LoadingFragment extends BaseFragment {
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_loading;
    }

    @Override
    protected void afterViews(Bundle savedInstanceState) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ActivityUtil.launchMapActivity(LoadingFragment.this);
                thisActivity.finish();
            }
        }, 1000);
    }
}
