package com.edu.zwu.hydrops.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.edu.zwu.hydrops.MyApplication;
import com.edu.zwu.hydrops.R;
import com.edu.zwu.hydrops.presenter.PersonalPresenter;
import com.edu.zwu.hydrops.util.ActivityUtil;
import com.edu.zwu.hydrops.util.AppUtil;
import com.edu.zwu.hydrops.view.CircleImageView;
import com.edu.zwu.hydrops.view.FontTextView;
import com.kogitune.activity_transition.ActivityTransition;
import com.kogitune.activity_transition.ExitActivityTransition;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by shengwei.yi on 2016/5/11.
 */
public class PersonalFragment extends TakePhotoFragment<PersonalPresenter> implements PersonalPresenter.PersonalView{
    public static final int LOGIN = 3;

    @Bind(R.id.personal_head_unlogin_layout)
    LinearLayout mUnLoginLayout;
    @Bind(R.id.personal_head_login_layout)
    LinearLayout mLoginLayout;
    @Bind(R.id.personal_head_edit)
    EditText mHeadEditView;
    @Bind(R.id.personal_head_text)
    FontTextView mHeadTextView;
    @Bind(R.id.personal_head_img)
    CircleImageView mHeadImageView;
    @Bind(R.id.personal_update_head)
    CircleImageView mUpdateHead;
    @Bind(R.id.personal_red_point)
    ImageView mRedPoint;

    private boolean isUpload;

    public ExitActivityTransition mExitTransition;

    @OnClick({R.id.personal_head_btn, R.id.personal_head_login_layout, R.id.personal_head_img, R.id.personal_head_text,
            R.id.personal_upload, R.id.personal_friend, R.id.personal_news, R.id.personal_set, R.id.personal_about, R.id.personal_exit})
    void onClick(View v) {
        switch (v.getId()) {
            /** “登录注册”按钮*/
            case R.id.personal_head_btn:
                ActivityUtil.launchLoginActivity(this, LOGIN);
                break;
            /** 登录后的头布局*/
            case R.id.personal_head_login_layout:
                if (mHeadTextView.getVisibility() == View.GONE && mHeadEditView.getVisibility() == View.VISIBLE) {
                    mHeadTextView.setVisibility(View.VISIBLE);
                    mHeadEditView.setVisibility(View.GONE);
                    if (!mHeadEditView.getText().toString().equals(mHeadTextView.getText().toString()))
                        mPresenter.checkHeadText(mHeadEditView.getText().toString());
                }
                break;
            /** 头像*/
            case R.id.personal_head_img:
                showDialog();
                isUpload = false;
                break;
            /** 昵称*/
            case R.id.personal_head_text:
                mPresenter.isDoubleClick();
                break;
            /** 上传*/
            case R.id.personal_upload:
                showDialog();
                isUpload = true;
//                ActivityUtil.launchCameraActivity(this);
                break;
            /** 汛友圈*/
            case R.id.personal_friend:
                if (mPresenter.isLogin()) {
                    ActivityUtil.launchFriendActivity(this);
                } else {
                    AppUtil.showShortMessage(thisActivity, "该功能只能登录后才可使用");
                }
                break;
            /** 新闻*/
            case R.id.personal_news:
                ActivityUtil.launchNewsActivity(this);
                break;
            /** 设置*/
            case R.id.personal_set:
                ActivityUtil.launchSettingActivity(this);
                break;
            /** 关于我们*/
            case R.id.personal_about:
                ActivityUtil.launchAboutWeActivity(this);
                break;
            /** 退出*/
            case R.id.personal_exit:
                mPresenter.exitLogin();
                break;
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_pesonal;
    }

    @Override
    protected void afterViews(Bundle savedInstanceState) {
        mPresenter = new PersonalPresenter(this,this.getContext());
        mExitTransition = ActivityTransition.with(thisActivity.getIntent()).to(mHeadImageView).start(savedInstanceState);
        mPresenter.getUserData();
    }

    @Override
    public void getBitmapSuccess() {
        saveBitmap();
    }

    @Override
    public void uploadSuccess() {
        if (isUpload) {
            AppUtil.showShortMessage(thisActivity, "上传成功，请等待校验");
        } else {
            updateHeadImage();
        }
    }

    @Override
    public void updateSuccess() {
        mHeadImageView.setImageUrl(mBmobFile.getUrl(), thisActivity);
        mHeadImageView.setBorderColor(Color.WHITE);
        mHeadImageView.setBorderWidth(3);
    }

    /**
     * 布局显示
     */
    @Override
    public void visibility() {
        mUnLoginLayout.setVisibility(MyApplication.getIsLogin() ? View.GONE : View.VISIBLE);
        mLoginLayout.setVisibility(MyApplication.getIsLogin() ? View.VISIBLE : View.GONE);
    }

    @Override
    public void setHeadText(String petName) {
        mHeadTextView.setText(petName);
    }

    @Override
    public void setHeadImage(String headImage) {
        mHeadImageView.setImageUrl(headImage, thisActivity);
        mHeadImageView.setBorderColor(Color.WHITE);
        mHeadImageView.setBorderWidth(3);
    }

    @Override
    public void showUpdateHead(int show, String imgPath) {
        mUpdateHead.setVisibility(show);
        mRedPoint.setVisibility(show);
        Log.i("有新消息图片", imgPath);
        mUpdateHead.setImageUrl(imgPath, thisActivity);
    }

    @Override
    public void updateHeadText(String headEditView) {
        mHeadTextView.setText(headEditView);
    }

    @Override
    public void showHeadEditView() {
        mHeadTextView.setVisibility(View.GONE);
        mHeadEditView.setVisibility(View.VISIBLE);
        mHeadEditView.setText(mHeadTextView.getText().toString());
        mHeadEditView.setSelection(mHeadEditView.getText().toString().length());
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.isTalkUpdate();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LOGIN) {
            mPresenter.getUserData();
        }
    }
}
