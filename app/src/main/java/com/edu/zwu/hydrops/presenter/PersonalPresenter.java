package com.edu.zwu.hydrops.presenter;

import android.content.Context;
import android.view.View;

import com.edu.zwu.hydrops.MyApplication;
import com.edu.zwu.hydrops.base.BasePresenter;
import com.edu.zwu.hydrops.base.BaseView;
import com.edu.zwu.hydrops.bmob.MyUser;
import com.edu.zwu.hydrops.bmob.Talk;
import com.edu.zwu.hydrops.util.AppUtil;
import com.edu.zwu.hydrops.util.PushUtil;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.BmobWrapper;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by shengwei.yi on 2016/5/11.
 */
public class PersonalPresenter extends BasePresenter<PersonalPresenter.PersonalView> {

    private int count;
    private long time;

    public PersonalPresenter(PersonalView view, Context context) {
        super(view, context);
    }

    /**
     * 是否双击
     *
     * @return
     */
    public void isDoubleClick() {
        count++;
        if (count == 1) {
            time = System.currentTimeMillis();
        } else if (count == 2) {
            if (System.currentTimeMillis() - time < 1000) {
                mView.showHeadEditView();
            }
            count = 0;
        }
    }

    public boolean isLogin(){
        return MyApplication.getIsLogin();
    }

    public void checkHeadText(final String headEditView) {
        BmobQuery<MyUser> query = new BmobQuery<>();
        query.addWhereEqualTo("petName", headEditView);
        query.findObjects(new FindListener<MyUser>() {
            @Override
            public void done(List<MyUser> object, BmobException e) {
                if (e == null){
                    if (object.size() == 0) {
                        updateHeadText(headEditView);
                    } else {
                        AppUtil.showShortMessage(mContext, "该昵称已存在");
                    }
                }
            }
        });
    }

    /**
     * 修改昵称
     */
    private void updateHeadText(final String headEditView) {
        MyUser myUser = new MyUser();
        myUser.setPetName(headEditView);
        MyUser currentUser = BmobUser.getCurrentUser(MyUser.class);
        myUser.update(currentUser.getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null){
                    mView.updateHeadText(headEditView);
                } else {
                    AppUtil.showShortMessage(mContext, "修改昵称失败：" + e.toString());
                }
            }
        });
    }

    public void exitLogin(){
        BmobUser.logOut();   //清除缓存用户对象
        PushUtil.clearAlias();
        MyApplication.clearPassword();
        MyApplication.clearIsLogin();
        getUserData();
    }

    /**
     * 获取用户数据
     */
    public void getUserData() {
        mView.visibility();
        if (MyApplication.getIsLogin() && BmobWrapper.getInstance() != null) {
            MyUser currentUser = BmobUser.getCurrentUser(MyUser.class);
            mView.setHeadText(currentUser.getPetName());
            mView.setHeadImage(currentUser.getHeadImage());

        }
    }

    public void isTalkUpdate(){
        if (MyApplication.getIsLogin() && MyApplication.getFriendFreshSetting() && BmobWrapper.getInstance() != null) {
            getTalkUpdate();
        }
    }

    private void getTalkUpdate() {
        BmobQuery<Talk> bmobQuery = new BmobQuery<Talk>();
        bmobQuery.findObjects(new FindListener<Talk>() {
            @Override
            public void done(List<Talk> list, BmobException e) {
                if (e == null){
                    if(list !=null && list.size() != 0) {
                        int count = MyApplication.getTalkCount();
                        mView.showUpdateHead(count < list.size() ? View.VISIBLE : View.GONE, list.get(list.size() - 1).headImg);
                    }
                }
            }
        });
    }

    public interface PersonalView extends BaseView{
        void updateHeadText(String headEditView);

        void showHeadEditView();

        void visibility();

        void setHeadText(String petName);

        void setHeadImage(String headImage);

        void showUpdateHead(int show, String imgPath);
    }
}
