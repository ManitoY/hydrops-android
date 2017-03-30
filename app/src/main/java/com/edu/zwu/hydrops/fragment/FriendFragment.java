package com.edu.zwu.hydrops.fragment;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.edu.zwu.hydrops.R;
import com.edu.zwu.hydrops.adapter.AllTalkAdapter;
import com.edu.zwu.hydrops.base.BaseFragment;
import com.edu.zwu.hydrops.bmob.Talk;
import com.edu.zwu.hydrops.presenter.FriendPresenter;
import com.edu.zwu.hydrops.util.AppUtil;
import com.edu.zwu.hydrops.view.ListRecyclerView;
import com.edu.zwu.hydrops.view.VerticalSwipeRefreshLayout;

import java.util.List;

import butterknife.Bind;

/**
 * Created by shengwei.yi on 2016/5/11.
 */
public class FriendFragment extends BaseFragment<FriendPresenter> implements FriendPresenter.FriendView, TextWatcher {

    @Bind(R.id.friend_list)
    ListRecyclerView mFriendList;

    public LinearLayout mInputLayout;

    @Bind(R.id.friend_refreshLayout)
    VerticalSwipeRefreshLayout mRefreshLayout;

    @Bind(R.id.input_view)
    EditText mInputView;

    @Bind(R.id.send_btn)
    Button mSendBtn;

    public AllTalkAdapter mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_friend;
    }

    @Override
    protected void afterViews(Bundle savedInstanceState) {
        mPresenter = new FriendPresenter(this, this.getContext());
        mInputLayout = (LinearLayout) mView.findViewById(R.id.input_layout);
        mAdapter = new AllTalkAdapter(thisActivity);
        mRefreshLayout.setColorSchemeResources(R.color.compat_holo_blue_bright, R.color.compat_holo_green_light, R.color.compat_holo_orange_light, R.color.compat_holo_red_light);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.initData();
            }
        });
        mInputView.addTextChangedListener(this);
        mAdapter.setHasHeader(true);
        mFriendList.setAdapter(mAdapter);
        mPresenter.initData();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(final Editable s) {
        mSendBtn.setTextColor(TextUtils.isEmpty(s) ? Color.BLACK : Color.WHITE);
        mSendBtn.setBackgroundResource(TextUtils.isEmpty(s) ? R.drawable.friend_send_btn_selector_off : R.drawable.friend_send_btn_selector_on);
    }

    @Override
    public void setRefreshingTrue() {
        mRefreshLayout.setRefreshing(true);
    }

    @Override
    public void showProgressDialog() {
        mProgressDialog = AppUtil.showProgress(thisActivity);
    }

    @Override
    public void dismiss() {
        dismissProgress();
        if (mRefreshLayout != null && mRefreshLayout.isRefreshing()) {
            mRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void refreshViewByReplaceData(List<Talk> list) {
        mAdapter.refreshViewByReplaceData(list);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            mAdapter.refreshCover();
        } else if (requestCode == 1) {
            mPresenter.initData();
        }
    }
}
