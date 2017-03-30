package com.edu.zwu.hydrops.presenter;

import android.content.Context;

import com.edu.zwu.hydrops.base.BaseActivity;
import com.edu.zwu.hydrops.base.BasePresenter;
import com.edu.zwu.hydrops.base.BaseView;
import com.edu.zwu.hydrops.bmob.MyUser;
import com.edu.zwu.hydrops.bmob.Talk;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by shengwei.yi on 2016/4/29.
 */
public class MyAlbumPresenter extends BasePresenter<MyAlbumPresenter.MyAlbumView>{

    public MyAlbumPresenter(MyAlbumView view, Context context) {
        super(view, context);
    }

    public MyUser getCurrentUser(){
        return BmobUser.getCurrentUser(MyUser.class);
    }

    public void initData(BaseActivity activity) {
        mView.showProgressDialog();
        BmobQuery<Talk> bmobQuery = new BmobQuery<Talk>();
        bmobQuery.addWhereEqualTo("name", activity.getIntent().getStringExtra("petName"));
        bmobQuery.findObjects(new FindListener<Talk>() {
            @Override
            public void done(List<Talk> list, BmobException e) {
                if(e == null){
                    mView.dismiss();
                    mView.refreshListData(list);
                } else {
                    mView.dismiss();
                }
            }
        });
    }

    public interface MyAlbumView extends BaseView{

        void showProgressDialog();

        void dismiss();

        void refreshListData(List<Talk> list);
    }
}
