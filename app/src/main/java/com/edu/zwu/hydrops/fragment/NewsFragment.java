package com.edu.zwu.hydrops.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.edu.zwu.hydrops.R;
import com.edu.zwu.hydrops.adapter.AllNewsAdapter;
import com.edu.zwu.hydrops.adapter.BaseRecyclerListAdapter;
import com.edu.zwu.hydrops.base.BaseFragment;
import com.edu.zwu.hydrops.bmob.News;
import com.edu.zwu.hydrops.presenter.NewsPresenter;
import com.edu.zwu.hydrops.util.ActivityUtil;
import com.edu.zwu.hydrops.util.AppUtil;
import com.edu.zwu.hydrops.view.ListRecyclerView;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by shengwei.yi on 2016/4/29.
 */
public class NewsFragment extends BaseFragment<NewsPresenter> implements NewsPresenter.NewsView {
    @Bind(R.id.news_refreshLayout)
    SwipeRefreshLayout mRefreshLayout;

    @Bind(R.id.news_list)
    ListRecyclerView mNewsList;

    @Bind(R.id.loading_fail_layout)
    LinearLayout mLoadingFailLayout;

    @Bind(R.id.loading_empty_layout)
    RelativeLayout mLoadingEmptyLayout;

    private AllNewsAdapter mAdapter;
    private List<News> mList;

    @OnClick(R.id.loading_fail_retry)
    void OnClick(){
        mPresenter.initData();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_news;
    }

    @Override
    protected void afterViews(Bundle savedInstanceState) {
        mPresenter = new NewsPresenter(this, this.getContext());
        mAdapter = new AllNewsAdapter(thisActivity);
        mRefreshLayout.setColorSchemeResources(R.color.compat_holo_blue_bright, R.color.compat_holo_green_light, R.color.compat_holo_orange_light, R.color.compat_holo_red_light);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.initData();
            }
        });
        mNewsList.setAdapter(mAdapter);
        mNewsList.setEmptyView(mLoadingEmptyLayout);
        mNewsList.setFailedView(mLoadingFailLayout);
        mAdapter.setOnRecyclerViewItemClickListener(new BaseRecyclerListAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClickListener(View view, int position) {
                News news = mList.get(position);
                ActivityUtil.launchWebPagerActivity(NewsFragment.this, news.address, news.title);
            }
        });
        mRefreshLayout.setRefreshing(true);
        mPresenter.initData();
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
    public void getNewList(List<News> list) {
        mList = list;
        mAdapter.refreshViewByReplaceData(list);
        mNewsList.renderViewByResult(false, -1, list.size() == 0);
    }

    @Override
    public void renderViewByResult() {
        mNewsList.renderViewByResult(true);
    }

    @Override
    public void setRefreshing() {
        mRefreshLayout.setRefreshing(false);
    }
}
