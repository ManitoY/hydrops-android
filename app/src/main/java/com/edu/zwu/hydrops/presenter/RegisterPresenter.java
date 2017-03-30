package com.edu.zwu.hydrops.presenter;

import android.content.Context;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextUtils;

import com.edu.zwu.hydrops.MyApplication;
import com.edu.zwu.hydrops.base.BasePresenter;
import com.edu.zwu.hydrops.base.BaseView;
import com.edu.zwu.hydrops.bmob.MyUser;
import com.edu.zwu.hydrops.util.AppUtil;

import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by shengwei.yi on 2016/5/11.
 */
public class RegisterPresenter extends BasePresenter<RegisterPresenter.RegisterView> {
    private static final int COUNTDOWN = 60000;
    private boolean isAnimator = false;
    private CountDownTimer mCountDownTimer;

    public RegisterPresenter(RegisterView view, Context context) {
        super(view, context);
    }

    /**
     * 发送短信
     */
    public void sendSMS(String account) {
        BmobSMS.requestSMSCode(account, "用户注册", new QueryListener<Integer>() {
            @Override
            public void done(Integer integer, BmobException e) {
                AppUtil.showShortMessage(MyApplication.mContext, e == null ? "验证码发送成功" : "验证码发送失败");
                if (e == null) {
                    addIdentifyingCodeAnimator();
                }
            }
        });
    }

    /**
     * 增加获取验证码60秒倒数动画
     */
    private void addIdentifyingCodeAnimator() {
        isAnimator = true;
        mView.setIdentifyingCodeSendBtnEnabledFalse();
        mCountDownTimer = new CountDownTimer(COUNTDOWN, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mView.setIdentifyingCodeSendBtnTickText(millisUntilFinished / 1000);
            }

            @Override
            public void onFinish() {
                mView.setIdentifyingCodeSendBtnEnabledTrue();
                mView.setIdentifyingCodeSendBtnFinishText();
            }
        };
        mCountDownTimer.start();
    }

    public void cancelCountDownTimer() {
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
        }
    }

    public boolean setIdentifyingCodeSendBtnEnabled(Editable inputAccount){
        return inputAccount.length() == 11 && !isAnimator;
    }

    public boolean setRegisterActivityBtn(Editable inputAccount, Editable inputPassword, Editable inputPasswordAgain, Editable inputIdentifyingCode){
        return !TextUtils.isEmpty(inputAccount) && !TextUtils.isEmpty(inputPassword) && !TextUtils.isEmpty(inputPasswordAgain) && !TextUtils.isEmpty(inputIdentifyingCode);
    }

    /**
     * 注册
     */
    public void register(String headImagePath, Editable inputAccount, Editable inputPassword, Editable inputPasswordAgain, Editable inputIdentifyingCode) {
        if (TextUtils.isEmpty(headImagePath)) {
            AppUtil.showShortMessage(mContext, "请设置您的头像");
            return;
        }
        if (TextUtils.isEmpty(inputAccount)) {
            AppUtil.showShortMessage(mContext, "请输入您的手机号码");
            return;
        }
        if (TextUtils.isEmpty(inputPassword)) {
            AppUtil.showShortMessage(mContext, "请输入您的密码");
            return;
        }
        if (TextUtils.isEmpty(inputPasswordAgain)) {
            AppUtil.showShortMessage(mContext, "请再次输入您的密码");
            return;
        }
        if (TextUtils.isEmpty(inputIdentifyingCode)) {
            AppUtil.showShortMessage(mContext, "请输入您的验证码");
            return;
        }
        if (inputAccount.length() != 11) {
            AppUtil.showShortMessage(mContext, "手机号输入不正确");
            return;
        }
        if (inputPassword.length() < 6) {
            AppUtil.showShortMessage(mContext, "密码输入位数不得低于6位");
            return;
        }
        if (!inputPassword.toString().equals(inputPasswordAgain.toString())) {
            AppUtil.showShortMessage(mContext, "两次密码不一致");
            return;
        }
        if (inputIdentifyingCode.length() != 6) {
            AppUtil.showShortMessage(mContext, "验证码输入不正确");
            return;
        }
        MyUser myUser = new MyUser();
        myUser.setMobilePhoneNumber(inputAccount.toString());
        myUser.setPassword(inputPassword.toString());
        myUser.setSex("女");
        myUser.setPetName(inputAccount.toString());
        myUser.setHeadImage(headImagePath);
        myUser.setCoverImage("");
        myUser.signOrLogin(inputIdentifyingCode.toString(), new SaveListener<MyUser>() {
            @Override
            public void done(MyUser user, BmobException e) {
                if(e == null){
                    AppUtil.showShortMessage(MyApplication.mContext, "注册成功");
                    mView.launchRegisterSuccessActivity();
                } else {
                    AppUtil.showShortMessage(MyApplication.mContext, "错误原因：" + e.toString());
                }
            }
        });
    }

    public interface RegisterView extends BaseView {
        void setIdentifyingCodeSendBtnEnabledFalse();

        void setIdentifyingCodeSendBtnTickText(long secondUntilFinished);

        void setIdentifyingCodeSendBtnEnabledTrue();

        void setIdentifyingCodeSendBtnFinishText();

        void launchRegisterSuccessActivity();
    }
}
