package com.edu.zwu.hydrops.presenter;

import android.content.Context;
import android.os.Handler;

import com.edu.zwu.hydrops.base.BasePresenter;
import com.edu.zwu.hydrops.base.BaseView;

/**
 * Created by shengwei.yi on 2016/5/11.
 */
public class RegisterSuccessPresenter extends BasePresenter<RegisterSuccessPresenter.RegisterSuccessView> {

    private int mTime = 3;

    public RegisterSuccessPresenter(RegisterSuccessView view, Context context) {
        super(view, context);
    }

    public void countDown() {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mTime--;
                mView.setCountDownText(mTime);
                if (mTime == 0) {
                    mView.setActivityFinish();
                } else {
                    handler.postDelayed(this, 1000);
                }
            }
        }, 1000);
    }

    public interface RegisterSuccessView extends BaseView {

        void setCountDownText(int time);

        void setActivityFinish();

    }
}
