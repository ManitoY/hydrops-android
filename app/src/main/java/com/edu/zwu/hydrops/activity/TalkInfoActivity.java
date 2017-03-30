package com.edu.zwu.hydrops.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;

import com.edu.zwu.hydrops.base.BaseActivity;
import com.edu.zwu.hydrops.base.BaseFragment;
import com.edu.zwu.hydrops.fragment.TalkInfoFragment;

/**
 * Created by shengwei.yi on 2015/12/1.
 */
public class TalkInfoActivity extends BaseActivity implements TalkInfoFragment.FragmentCallBack{

    private TalkInfoFragment mTalkInfoFragment;

    @Override
    protected int getLayoutId() {
        return 0;
    }

    @Override
    protected void afterViews(Bundle savedInstanceState) {
        initActionBar(" ");
        showLeftBtn();
        showRightBtn();
        actionBarRightBtn.setEnabled(false);
        actionBarRightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(mTalkInfoFragment.mHeadImgLocalPath)) {
                    mTalkInfoFragment.saveBitmap();
                } else {
                    mTalkInfoFragment.uploadTalk();
                }
            }
        });
    }

    @Override
    protected BaseFragment getFragment() {
        mTalkInfoFragment = new TalkInfoFragment();
        return mTalkInfoFragment;
    }

    @Override
    public void setActionBarRightBtnEnabled(Editable s) {
        actionBarRightBtn.setEnabled(!TextUtils.isEmpty(s));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mTalkInfoFragment.onActivityResult(requestCode, resultCode, data);
    }
}
