package com.edu.zwu.hydrops.fragment;

import android.os.Bundle;

import com.edu.zwu.hydrops.R;
import com.edu.zwu.hydrops.base.BaseFragment;
import com.edu.zwu.hydrops.presenter.RegisterSuccessPresenter;
import com.edu.zwu.hydrops.view.FontTextView;

import butterknife.Bind;

/**
 * Created by shengwei.yi on 2016/5/11.
 */
public class RegisterSuccessFragment extends BaseFragment<RegisterSuccessPresenter> implements RegisterSuccessPresenter.RegisterSuccessView{

    @Bind(R.id.register_success_time)
    FontTextView mCountDown;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_register_success;
    }

    @Override
    protected void afterViews(Bundle savedInstanceState) {
        mPresenter = new RegisterSuccessPresenter(this, this.getContext());
        mPresenter.countDown();
    }

    @Override
    public void setCountDownText(int time) {
        mCountDown.setText(time + "秒后自动跳转");
    }

    @Override
    public void setActivityFinish() {
        thisActivity.finish();
    }
}
