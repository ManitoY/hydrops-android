package com.edu.zwu.hydrops.activity;


import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;


import com.edu.zwu.hydrops.R;
import com.edu.zwu.hydrops.base.BaseActivity;
import com.edu.zwu.hydrops.base.BaseFragment;
import com.edu.zwu.hydrops.fragment.FriendFragment;
import com.edu.zwu.hydrops.util.ActivityUtil;

/**
 * Created by shengwei.yi on 2015/11/19.
 */
public class FriendActivity extends BaseActivity {
    private final static int SENDTALK = 1;

    private FriendFragment mFriendFragment;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            if (mFriendFragment.mInputLayout.getVisibility() == View.VISIBLE) {
                mFriendFragment.mInputLayout.setVisibility(View.GONE);
                return true;
            }
//            if (mFriendFragment.mAdapter.mMagnifiedPopupWindow != null && mFriendFragment.mAdapter.mMagnifiedPopupWindow.isShowing()) {
//                mFriendFragment.mAdapter.mMagnifiedPopupWindow.dismiss();
//                mFriendFragment.mAdapter.mMagnifiedPopupWindow = null;
//                return true;
//            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected int getLayoutId() {
        return 0;
    }

    @Override
    protected void afterViews(Bundle savedInstanceState) {
        initActionBar("汛友圈");
        showLeftBtn();
        setRightImage(R.drawable.icon_friend_edit);
        actionBarRightImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityUtil.launchTalkInfoActivity(mFriendFragment, SENDTALK);
            }
        });
    }

    @Override
    protected BaseFragment getFragment() {
        mFriendFragment = new FriendFragment();
        return mFriendFragment;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mFriendFragment.onActivityResult(requestCode, resultCode, data);
    }
}
