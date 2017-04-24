package com.edu.zwu.hydrops.presenter;

import android.content.Context;

import com.edu.zwu.hydrops.base.BasePresenter;
import com.edu.zwu.hydrops.base.BaseView;
import com.edu.zwu.hydrops.bmob.News;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobWrapper;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by shengwei.yi on 2016/4/29.
 */
public class NewsPresenter extends BasePresenter<NewsPresenter.NewsView> {

    public NewsPresenter(NewsView view, Context context) {
        super(view, context);
    }

    public void initData() {
        if (BmobWrapper.getInstance() != null) {
            mView.showProgressDialog();
            BmobQuery<News> query = new BmobQuery<News>();
            query.findObjects(new FindListener<News>() {
                @Override
                public void done(List<News> list, BmobException e) {
                    if (e == null) {
                        mView.dismiss();
                        mView.getNewList(list);
                    } else {
                        mView.dismiss();
                        mView.renderViewByResult();
                    }
                }
            });
            mView.setRefreshing();
        }
    }

    public interface NewsView extends BaseView {

        void showProgressDialog();

        void dismiss();

        void getNewList(List<News> list);

        void renderViewByResult();

        void setRefreshing();
    }
}
