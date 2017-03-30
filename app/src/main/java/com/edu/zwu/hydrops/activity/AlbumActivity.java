package com.edu.zwu.hydrops.activity;

import android.os.Bundle;

import com.edu.zwu.hydrops.base.BaseActivity;
import com.edu.zwu.hydrops.base.BaseFragment;
import com.edu.zwu.hydrops.fragment.AlbumFragment;


/**
 * Created by shengwei.yi on 2015/12/1.
 */
public class AlbumActivity extends BaseActivity {

    @Override
    protected int getLayoutId() {
        return 0;
    }

    @Override
    protected void afterViews(Bundle savedInstanceState) {
        initActionBar("图片");
        showLeftBtn();
    }

    @Override
    protected BaseFragment getFragment() {
        return new AlbumFragment();
    }

}
