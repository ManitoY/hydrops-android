package com.edu.zwu.hydrops.fragment;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.edu.zwu.hydrops.R;
import com.edu.zwu.hydrops.activity.TalkInfoActivity;
import com.edu.zwu.hydrops.presenter.TalkInfoPresenter;
import com.edu.zwu.hydrops.util.AppUtil;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by shengwei.yi on 2016/5/11.
 */
public class TalkInfoFragment extends TakePhotoFragment<TalkInfoPresenter> implements TalkInfoPresenter.TalkInfoView, TextWatcher {

    @Bind(R.id.talk_info_img)
    ImageView mTalkInfoImg;

    @Bind(R.id.talk_info_edit)
    EditText mTalkInfoEdit;

    private FragmentCallBack mFragmentCallBack;

    @OnClick(R.id.talk_info_img)
    void onClick(View v) {
        showDialog();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_talk_info;
    }

    @Override
    protected void afterViews(Bundle savedInstanceState) {
        mPresenter = new TalkInfoPresenter(this, this.getContext());
        mTalkInfoEdit.addTextChangedListener(this);
    }

    public void uploadTalk() {
        mPresenter.uploadTalk(mTalkInfoEdit.getText().toString(), mBmobFile);
    }

    @Override
    public void getBitmapSuccess() {
        Glide.with(this).load(mHeadImgLocalPath).into(mTalkInfoImg);
    }

    @Override
    public void uploadSuccess() {
        uploadTalk();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        mFragmentCallBack.setActionBarRightBtnEnabled(s);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mFragmentCallBack = (TalkInfoActivity) context;
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
    public void activityFinish() {
        thisActivity.finish();
    }

    public interface FragmentCallBack{
        void setActionBarRightBtnEnabled(Editable s);
    }
}
