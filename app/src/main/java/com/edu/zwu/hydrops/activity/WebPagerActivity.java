package com.edu.zwu.hydrops.activity;

import android.os.Bundle;

import com.edu.zwu.hydrops.base.BaseActivity;
import com.edu.zwu.hydrops.base.BaseFragment;
import com.edu.zwu.hydrops.fragment.WebPagerFragment;

/**
 * Created by shengwei.yi on 2016/3/19.
 */
public class WebPagerActivity extends BaseActivity{

    @Override
    protected int getLayoutId() {
        return 0;
    }

    @Override
    protected void afterViews(Bundle savedInstanceState) {
        String title = mIntent.getStringExtra("title");
        initActionBar(title);
        showLeftBtn();
    }

    @Override
    protected BaseFragment getFragment() {
        return new WebPagerFragment();
    }

}
