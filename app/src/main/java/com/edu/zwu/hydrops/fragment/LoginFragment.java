package com.edu.zwu.hydrops.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.edu.zwu.hydrops.R;
import com.edu.zwu.hydrops.base.BaseFragment;
import com.edu.zwu.hydrops.presenter.LoginPresenter;
import com.edu.zwu.hydrops.util.ActivityUtil;
import com.edu.zwu.hydrops.util.AppUtil;
import com.edu.zwu.hydrops.view.CircleImageView;
import com.edu.zwu.hydrops.view.LoginLayout;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by shengwei.yi on 2016/4/28.
 */
public class LoginFragment extends BaseFragment<LoginPresenter> implements LoginLayout.OnSizeChangedListener, TextWatcher, TextView.OnEditorActionListener, LoginPresenter.LoginView{

    @Bind(R.id.login_total_scrollview)
    LoginLayout mLoginLayout;
    @Bind(R.id.login_head)
    CircleImageView mLoginHead;
    @Bind(R.id.login_activity_btn)
    Button mLoginActivityBtn;
    @Bind(R.id.login_input_account)
    EditText mInputAccount;
    @Bind(R.id.login_input_password)
    EditText mInputPassword;

    @OnClick({R.id.login_activity_btn, R.id.login_register_btn, R.id.login_forget_btn})
    void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_activity_btn:
                mPresenter.login(mInputAccount.getText(), mInputPassword.getText());
                break;
            case R.id.login_register_btn:
                ActivityUtil.launchRegisterActivity(this);
                break;
            case R.id.login_forget_btn:

                break;
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_login;
    }

    @Override
    protected void afterViews(Bundle savedInstanceState) {
        mPresenter = new LoginPresenter(this, this.getContext());
        init();
        if (mPresenter.isEmptyOfAccount()) {
            mLoginActivityBtn.setEnabled(false);
        }
        if (mPresenter.isEmptyOfPassword()) {
            mLoginActivityBtn.setEnabled(false);
        }
        /** show头像*/
        mPresenter.queryHeadImage(mInputAccount.getText().length(), mInputAccount.getText().toString());
    }

    @Override
    public void getAccount(String account) {
        mInputAccount.setText(account);
        mInputAccount.setSelection(account.length());
    }

    @Override
    public void getPassword(String password) {
        mInputPassword.setText(password);
        mInputPassword.setSelection(password.length());
    }

    @Override
    public void setSuccessHead(String path) {
        /** 设置圆形头像的边框*/
        mLoginHead.setImageUrl(path, thisActivity);
        mLoginHead.setBorderColor(Color.WHITE);
        mLoginHead.setBorderWidth(3);
    }

    @Override
    public void setErrorHead() {
        mLoginHead.setBorderWidth(0);
        mLoginHead.setImageUrl(null, thisActivity);
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
    public void setActivityResult() {
        thisActivity.setResult(PersonalFragment.LOGIN);
        thisActivity.finish();
    }

    /**
     * 初始化界面
     */
    private void init() {
        /** 设置默认头像和错误头像*/
        mLoginHead.setDefaultImageResId(R.drawable.icon_register_head);
        mLoginHead.setErrorImageResId(R.drawable.icon_register_head);
        /** 使能控件 false为不激活 true为激活*/
        mLoginActivityBtn.setEnabled(false);
        /** 设置监听*/
        setMonitoring();
    }

    /**
     * 设置界面的监听事件
     */
    private void setMonitoring() {
        mInputAccount.addTextChangedListener(this);
        mInputPassword.addTextChangedListener(this);
        mInputPassword.setOnEditorActionListener(this);
        mLoginLayout.setOnSizeChangedListener(this);
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
        mLoginLayout.scrollTo(0, oldh - h);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        mLoginActivityBtn.setEnabled(!TextUtils.isEmpty(mInputAccount.getText()) && !TextUtils.isEmpty(mInputPassword.getText()));
        mPresenter.queryHeadImage(mInputAccount.getText().length(), mInputAccount.getText().toString());
    }

    /**
     * 监听密码键盘
     * @param v
     * @param actionId
     * @param event
     * @return
     */
    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_DONE || event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
            mPresenter.login(mInputAccount.getText(), mInputPassword.getText());
        }
        return false;
    }
}
