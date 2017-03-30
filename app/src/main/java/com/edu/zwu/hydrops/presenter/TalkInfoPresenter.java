package com.edu.zwu.hydrops.presenter;

import android.content.Context;

import com.edu.zwu.hydrops.base.BasePresenter;
import com.edu.zwu.hydrops.base.BaseView;
import com.edu.zwu.hydrops.bmob.MyUser;
import com.edu.zwu.hydrops.bmob.Talk;
import com.edu.zwu.hydrops.util.AppUtil;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by shengwei.yi on 2016/5/11.
 */
public class TalkInfoPresenter extends BasePresenter<TalkInfoPresenter.TalkInfoView>{

    public TalkInfoPresenter(TalkInfoView view, Context context) {
        super(view, context);
    }

    public void uploadTalk(String talkInfoText, BmobFile bmobFile) {
        mView.showProgressDialog();
        MyUser currentUser = BmobUser.getCurrentUser(MyUser.class);
        Talk talk = new Talk();
        talk.setName(currentUser.getPetName());
        talk.setHeadImg(currentUser.getHeadImage());
        talk.setText(talkInfoText);
        if (bmobFile == null) {
            talk.setImgUrl(null);
        } else {
            talk.setImgUrl(bmobFile.getUrl());
        }
        talk.setReplyName(null);
        talk.setReplyContent(null);
        talk.save(new SaveListener<String>() {
            @Override
            public void done(String o, BmobException e) {
                if (e == null){
                    mView.dismiss();
                    mView.activityFinish();
                } else {
                    mView.dismiss();
                    AppUtil.showShortMessage(mContext, "发送消息失败：" + e.toString());
                }
            }
        });
    }

    public interface TalkInfoView extends BaseView{
        void showProgressDialog();

        void dismiss();

        void activityFinish();
    }
}
