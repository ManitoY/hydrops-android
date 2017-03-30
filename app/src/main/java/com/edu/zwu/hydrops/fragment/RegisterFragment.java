package com.edu.zwu.hydrops.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.edu.zwu.hydrops.R;
import com.edu.zwu.hydrops.presenter.RegisterPresenter;
import com.edu.zwu.hydrops.util.ActivityUtil;
import com.edu.zwu.hydrops.view.CircleImageView;
import com.edu.zwu.hydrops.view.LoginLayout;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by shengwei.yi on 2016/5/11.
 */
public class RegisterFragment extends TakePhotoFragment<RegisterPresenter> implements RegisterPresenter.RegisterView, TextWatcher, LoginLayout.OnSizeChangedListener {

    private String headImagePath = null;

    @Bind(R.id.register_total_scrollview)
    LoginLayout mRegisterLayout;

    @Bind(R.id.register_head)
    CircleImageView mHeadImageView;

    @Bind(R.id.register_input_account)
    EditText mInputAccount;
    @Bind(R.id.register_input_password)
    EditText mInputPassword;
    @Bind(R.id.register_input_password_again)
    EditText mInputPasswordAgain;
    @Bind(R.id.register_input_identifying_code)
    EditText mInputIdentifyingCode;

    @Bind(R.id.register_identifying_code_send_btn)
    Button mIdentifyingCodeSendBtn;
    @Bind(R.id.register_activity_btn)
    Button mRegisterActivityBtn;

    @OnClick({R.id.register_head, R.id.register_identifying_code_send_btn, R.id.register_activity_btn})
    void onClick(View v) {
        switch (v.getId()) {
            case R.id.register_head:
                showDialog();
                break;
            case R.id.register_identifying_code_send_btn:
                mPresenter.sendSMS(mInputAccount.getText().toString());
                break;
            case R.id.register_activity_btn:
                mPresenter.register(headImagePath, mInputAccount.getText(), mInputPassword.getText(), mInputPasswordAgain.getText(), mInputIdentifyingCode.getText());
                break;
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_register;
    }

    @Override
    protected void afterViews(Bundle savedInstanceState) {
        init();
        mPresenter = new RegisterPresenter(this, this.getContext());
    }

    @Override
    public void getBitmapSuccess() {
        saveBitmap();
    }

    @Override
    public void uploadSuccess() {
        headImagePath = mBmobFile.getUrl();
        mHeadImageView.setImageUrl(headImagePath, thisActivity);
        mHeadImageView.setBorderColor(Color.WHITE);
        mHeadImageView.setBorderWidth(3);
    }

    /**
     * 初始化界面
     */
    private void init() {
        /** 使能控件 false为不激活 true为激活*/
        mIdentifyingCodeSendBtn.setEnabled(false);
        mRegisterActivityBtn.setEnabled(false);
        /** 设置监听*/
        setMonitoring();
    }

    /**
     * 设置界面的监听事件
     */
    private void setMonitoring() {
        mInputAccount.addTextChangedListener(this);
        mInputPassword.addTextChangedListener(this);
        mInputPasswordAgain.addTextChangedListener(this);
        mInputIdentifyingCode.addTextChangedListener(this);
        mRegisterLayout.setOnSizeChangedListener(this);
    }

    /**
     * ScrollView的滑动监听
     *
     * @param w
     * @param h
     * @param oldw
     * @param oldh
     */
    @Override
    public void onSizeChanged(int w, int h, int oldw, int oldh) {
        mRegisterLayout.scrollTo(0, oldh - h);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        mIdentifyingCodeSendBtn.setEnabled(mPresenter.setIdentifyingCodeSendBtnEnabled(mInputAccount.getText()));
        mRegisterActivityBtn.setEnabled(mPresenter.setRegisterActivityBtn(mInputAccount.getText(), mInputPassword.getText(), mInputPasswordAgain.getText(), mInputIdentifyingCode.getText()));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.cancelCountDownTimer();
    }

    @Override
    public void setIdentifyingCodeSendBtnEnabledFalse() {
        mIdentifyingCodeSendBtn.setEnabled(false);
    }

    @Override
    public void setIdentifyingCodeSendBtnTickText(long secondUntilFinished) {
        mIdentifyingCodeSendBtn.setText("倒计时 " + secondUntilFinished);
    }

    @Override
    public void setIdentifyingCodeSendBtnEnabledTrue() {
        mIdentifyingCodeSendBtn.setEnabled(true);
    }

    @Override
    public void setIdentifyingCodeSendBtnFinishText() {
        mIdentifyingCodeSendBtn.setText("获取验证码");
    }

    @Override
    public void launchRegisterSuccessActivity() {
        ActivityUtil.launchRegisterSuccessActivity(RegisterFragment.this);
        thisActivity.finish();
    }
}
