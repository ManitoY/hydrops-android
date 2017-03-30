package com.edu.zwu.hydrops.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.edu.zwu.hydrops.R;
import com.edu.zwu.hydrops.adapter.AllSearchAdapter;
import com.edu.zwu.hydrops.adapter.BaseRecyclerListAdapter;
import com.edu.zwu.hydrops.base.BaseFragment;
import com.edu.zwu.hydrops.presenter.SearchPresenter;
import com.edu.zwu.hydrops.view.ListRecyclerView;

import butterknife.Bind;

/**
 * Created by shengwei.yi on 2016/5/11.
 */
public class SearchFragment extends BaseFragment<SearchPresenter> implements SearchPresenter.SearchView{
    @Bind(R.id.search_list)
    ListRecyclerView mList;

    private AllSearchAdapter mAdapter;

    public SuggestionSearch mSuggestionSearch;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_search;
    }

    @Override
    protected void afterViews(Bundle savedInstanceState) {
        mPresenter = new SearchPresenter(this, this.getContext());
        mSuggestionSearch = SuggestionSearch.newInstance();
        mSuggestionSearch.setOnGetSuggestionResultListener(mPresenter.listener);
        mAdapter = new AllSearchAdapter();
        mList.setAdapter(mAdapter);
        mAdapter.setOnRecyclerViewItemClickListener(new BaseRecyclerListAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClickListener(View view, int position) {
                mPresenter.setBundle(mAdapter.getData().get(position).pt.latitude, mAdapter.getData().get(position).pt.longitude);
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mSuggestionSearch.destroy();
    }

    @Override
    public void refreshViewByReplaceData(SuggestionResult res) {
        mAdapter.refreshViewByReplaceData(res.getAllSuggestions());
    }

    @Override
    public void setActivityResult(Intent i) {
        thisActivity.setResult(Activity.RESULT_OK, i);
        thisActivity.finish();
    }
}
