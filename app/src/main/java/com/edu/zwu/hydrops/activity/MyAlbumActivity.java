package com.edu.zwu.hydrops.activity;


import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;

import com.edu.zwu.hydrops.base.BaseActivity;
import com.edu.zwu.hydrops.base.BaseFragment;
import com.edu.zwu.hydrops.bmob.MyUser;
import com.edu.zwu.hydrops.fragment.MyAlbumFragment;

import cn.bmob.v3.BmobUser;

/**
 * Created by shengwei.yi on 2016/4/7.
 */
public class MyAlbumActivity extends BaseActivity {
    private MyAlbumFragment mMyAlbumFragment;

    @Override
    protected int getLayoutId() {
        return 0;
    }

    @Override
    protected void afterViews(Bundle savedInstanceState) {
        initActionBar(mIntent.getStringExtra("petName").equals(BmobUser.getCurrentUser(MyUser.class).getPetName()) ? "我的相册" : mIntent.getStringExtra("petName"));
        showLeftBtn();
    }

    @Override
    protected BaseFragment getFragment() {
        mMyAlbumFragment = new MyAlbumFragment();
        return mMyAlbumFragment;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            if (mMyAlbumFragment.mAdapter.mMagnifiedPopupWindow != null && mMyAlbumFragment.mAdapter.mMagnifiedPopupWindow.isShowing()) {
                mMyAlbumFragment.mAdapter.mMagnifiedPopupWindow.dismiss();
                mMyAlbumFragment.mAdapter.mMagnifiedPopupWindow = null;
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mMyAlbumFragment.onActivityResult(requestCode, resultCode, data);
    }
}
