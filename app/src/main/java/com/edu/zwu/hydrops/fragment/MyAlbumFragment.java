package com.edu.zwu.hydrops.fragment;

import android.content.Intent;
import android.os.Bundle;

import com.edu.zwu.hydrops.R;
import com.edu.zwu.hydrops.adapter.AllMessageAdapter;
import com.edu.zwu.hydrops.base.BaseFragment;
import com.edu.zwu.hydrops.bmob.MyUser;
import com.edu.zwu.hydrops.bmob.Talk;
import com.edu.zwu.hydrops.presenter.MyAlbumPresenter;
import com.edu.zwu.hydrops.util.AppUtil;
import com.edu.zwu.hydrops.view.ListRecyclerView;

import java.util.List;

import butterknife.Bind;

/**
 * Created by shengwei.yi on 2016/4/29.
 */
public class MyAlbumFragment extends BaseFragment<MyAlbumPresenter> implements MyAlbumPresenter.MyAlbumView{
    @Bind(R.id.my_album_list)
    ListRecyclerView mAlbumList;
    public AllMessageAdapter mAdapter;
    public MyUser mCurrentUser;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_my_album;
    }

    @Override
    protected void afterViews(Bundle savedInstanceState) {
        mPresenter = new MyAlbumPresenter(this, this.getContext());
        mCurrentUser = mPresenter.getCurrentUser();
        mAdapter = new AllMessageAdapter(thisActivity);
        mAdapter.setHasHeader(true);
        mAlbumList.setAdapter(mAdapter);
        mPresenter.initData(thisActivity);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            mAdapter.refreshCover();
        }
    }

    @Override
    public void showProgressDialog() {
        mProgressDialog = AppUtil.showProgress(thisActivity);
    }

    @Override
    public void dismiss() {
        dismissProgress();
    }

    @Override
    public void refreshListData(List<Talk> list) {
        mAdapter.refreshViewByReplaceData(list);
    }

}
