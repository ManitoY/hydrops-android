package com.edu.zwu.hydrops.activity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;

import com.edu.zwu.hydrops.base.BaseActivity;
import com.edu.zwu.hydrops.base.BaseFragment;
import com.edu.zwu.hydrops.fragment.PictureMagnifiedFragment;

/**
 * Created by shengwei.yi on 2016/4/15.
 */
public class PictureMagnifiedActivity extends BaseActivity {

    private PictureMagnifiedFragment mPictureMagnifiedFragment;

//    @OnClick(R.id.big_image)
//    void onClick(View v) {
//        mProgress.setVisibility(View.GONE);
//        mExitActivityTransition.exit(this);
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return 0;
    }

    @Override
    protected void afterViews(final Bundle savedInstanceState) {
    }

    @Override
    protected BaseFragment getFragment() {
        mPictureMagnifiedFragment = new PictureMagnifiedFragment();
        return mPictureMagnifiedFragment;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            mPictureMagnifiedFragment.mProgressBar.setVisibility(View.GONE);
            mPictureMagnifiedFragment.mPhotoLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
            if (mPictureMagnifiedFragment.mExitActivityTransition != null) {
                mPictureMagnifiedFragment.mExitActivityTransition.exit(this);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
