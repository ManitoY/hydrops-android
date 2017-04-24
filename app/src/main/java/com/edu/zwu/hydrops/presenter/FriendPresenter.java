package com.edu.zwu.hydrops.presenter;

import android.content.Context;

import com.edu.zwu.hydrops.MyApplication;
import com.edu.zwu.hydrops.base.BasePresenter;
import com.edu.zwu.hydrops.base.BaseView;
import com.edu.zwu.hydrops.bmob.Talk;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobWrapper;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by shengwei.yi on 2016/5/11.
 */
public class FriendPresenter extends BasePresenter<FriendPresenter.FriendView> {

    public FriendPresenter(FriendView view, Context context) {
        super(view, context);
    }

    public void initData() {
        mView.setRefreshingTrue();
        mView.showProgressDialog();
        if (BmobWrapper.getInstance() != null) {
            BmobQuery<Talk> bmobQuery = new BmobQuery<Talk>();
            bmobQuery.findObjects(new FindListener<Talk>() {
                @Override
                public void done(List<Talk> list, BmobException e) {
                    if (e == null) {
                        mView.refreshViewByReplaceData(list);
                        MyApplication.addTalkCount(list.size());
                        mView.dismiss();
                    } else {
                        mView.dismiss();
                    }
                }
            });
        }
    }

    public interface FriendView extends BaseView {
        void setRefreshingTrue();

        void showProgressDialog();

        void dismiss();

        void refreshViewByReplaceData(List<Talk> list);
    }
}
