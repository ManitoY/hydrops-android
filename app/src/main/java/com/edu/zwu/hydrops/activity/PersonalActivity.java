package com.edu.zwu.hydrops.activity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.edu.zwu.hydrops.base.BaseActivity;
import com.edu.zwu.hydrops.base.BaseFragment;
import com.edu.zwu.hydrops.fragment.PersonalFragment;

/**
 * Created by shengwei.yi on 2015/11/19.
 */
public class PersonalActivity extends BaseActivity {

    private PersonalFragment mPersonalFragment;

    @Override
    protected int getLayoutId() {
        return 0;
    }

    @Override
    protected void afterViews(Bundle savedInstanceState) {
        initActionBar(" ");
        showLeftBtn();
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
        if (Build.VERSION.SDK_INT > 16) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_LAYOUT_FLAGS | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
            getWindow().getDecorView().setFitsSystemWindows(true);
        }
//        actionBarLeftBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mPersonalFragment.mExitTransition.exit(PersonalActivity.this);
//            }
//        });
    }

    @Override
    protected BaseFragment getFragment() {
        mPersonalFragment = new PersonalFragment();
        return mPersonalFragment;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mPersonalFragment.onActivityResult(requestCode, resultCode, data);
    }
}
