package com.edu.zwu.hydrops.presenter;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;

import com.edu.zwu.hydrops.MyApplication;
import com.edu.zwu.hydrops.base.BasePresenter;
import com.edu.zwu.hydrops.base.BaseView;
import com.edu.zwu.hydrops.bmob.Emoji;
import com.edu.zwu.hydrops.bmob.MyUser;
import com.edu.zwu.hydrops.util.AppUtil;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.LogInListener;

/**
 * Created by shengwei.yi on 2016/4/28.
 */
public class LoginPresenter extends BasePresenter<LoginPresenter.LoginView> {

    public LoginPresenter(LoginView view, Context context) {
        super(view, context);
        getEmojiList();
    }

    private void getEmojiList(){
        BmobQuery<Emoji> query = new BmobQuery<>();
        query.findObjects(new FindListener<Emoji>() {
            @Override
            public void done(List<Emoji> list, BmobException e) {
                if (e == null){
                    if (!AppUtil.isListEmpty(list)) {

                    } else {
                        mView.setErrorHead();
                    }
                } else {
                    mView.setErrorHead();
                }
            }
        });
    }

    public boolean isEmptyOfAccount() {
        String account = MyApplication.getAccount();
        boolean isEmpty = TextUtils.isEmpty(account);
        if (!isEmpty) {
            mView.getAccount(account);
        }
        return isEmpty;
    }

    public boolean isEmptyOfPassword() {
        String password = MyApplication.getPassword();
        boolean isEmpty = TextUtils.isEmpty(password);
        if (!isEmpty) {
            mView.getPassword(password);
        }
        return isEmpty;
    }

    /**
     * 根据手机号查询头像
     */
    public void queryHeadImage(int accountLength, String number) {
        if (accountLength == 11) {
            BmobQuery<MyUser> query = new BmobQuery<MyUser>();
            query.addWhereEqualTo("mobilePhoneNumber", number);
            query.findObjects(new FindListener<MyUser>() {
                @Override
                public void done(List<MyUser> list, BmobException e) {
                    if (e == null){
                        if (!TextUtils.isEmpty(list.get(0).getHeadImage())) {
                            mView.setSuccessHead(list.get(0).getHeadImage());
                        } else {
                            mView.setErrorHead();
                        }
                    } else {
                        mView.setErrorHead();
                    }
                }
            });
        } else {
            mView.setErrorHead();
        }
    }

    /**
     * 登录操作
     */
    public void login(Editable account, Editable password) {
        if (TextUtils.isEmpty(account)) {
            AppUtil.showShortMessage(mContext, "请输入手机号");
            return;
        }

        if (TextUtils.isEmpty(password)) {
            AppUtil.showShortMessage(mContext, "请输入密码");
            return;
        }

        mView.showProgressDialog();

        final String accountStr = account.toString();
        final String passwordStr = password.toString();

        BmobUser.loginByAccount(accountStr, passwordStr, new LogInListener<MyUser>() {
            @Override
            public void done(MyUser myUser, BmobException e) {
                if (myUser != null) {
                    mView.dismiss();
                    saveData(accountStr, passwordStr);
                    mView.setActivityResult();
                } else {
                    mView.dismiss();
                    AppUtil.showShortMessage(MyApplication.mContext, e.toString());
                }
            }
        });
    }

    /**
     * 保存信息
     */
    private void saveData(String account, String password) {
        MyApplication.addAccount(account);
        MyApplication.addPassword(password);
        MyApplication.addIsLogin(true);
    }

    public interface LoginView extends BaseView {

        void getAccount(String account);

        void getPassword(String password);

        void setSuccessHead(String path);

        void setErrorHead();

        void showProgressDialog();

        void dismiss();

        void setActivityResult();
    }
}
