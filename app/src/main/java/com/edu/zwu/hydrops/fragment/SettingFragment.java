package com.edu.zwu.hydrops.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.edu.zwu.hydrops.R;
import com.edu.zwu.hydrops.base.BaseFragment;
import com.edu.zwu.hydrops.libraries.jpush.Jpush;
import com.edu.zwu.hydrops.presenter.SettingPresenter;
import com.edu.zwu.hydrops.view.SettingSwitch;

import butterknife.Bind;

/**
 * Created by shengwei.yi on 2016/5/11.
 */
public class SettingFragment extends BaseFragment<SettingPresenter> implements SettingPresenter.SettingView, SettingSwitch.OnChangeListener{

    @Bind(R.id.push_switch)
    SettingSwitch mPushSwitch;
    @Bind(R.id.voice_switch)
    SettingSwitch mVoiceSwitch;
    @Bind(R.id.vibrates_switch)
    SettingSwitch mVibratesSwitch;
    @Bind(R.id.friend_fresh_switch)
    SettingSwitch mFriendFreshSwitch;
    @Bind(R.id.show_safe_switch)
    SettingSwitch mShowSafeSwitch;
    @Bind(R.id.show_nopass_switch)
    SettingSwitch mShowNoPassSwitch;

    @Bind(R.id.setting_voice_type)
    LinearLayout mVoiceTypeLayout;
    @Bind(R.id.setting_voice_line)
    ImageView mVoiceLineView;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_setting;
    }

    @Override
    protected void afterViews(Bundle savedInstanceState) {
        mPresenter = new SettingPresenter(this, this.getContext());
        setOnSwitchChangeListener();
        setOnSwitch();
    }
    private void setOnSwitchChangeListener() {
        mPushSwitch.setOnChangeListener(this);
        mVoiceSwitch.setOnChangeListener(this);
        mVibratesSwitch.setOnChangeListener(this);
        mFriendFreshSwitch.setOnChangeListener(this);
        mShowSafeSwitch.setOnChangeListener(this);
        mShowNoPassSwitch.setOnChangeListener(this);
    }

    private void setOnSwitch(){
        mPushSwitch.setSwitchOn(mPresenter.getPushSetting());
        mVoiceSwitch.setSwitchOn(mPresenter.getVoiceSetting());
        mVibratesSwitch.setSwitchOn(mPresenter.getVibratorSetting());
        mFriendFreshSwitch.setSwitchOn(mPresenter.getFriendFreshSetting());
        mShowSafeSwitch.setSwitchOn(mPresenter.getShowSafeSetting());
        mShowNoPassSwitch.setSwitchOn(mPresenter.getShowNoPassSetting());

        mVoiceTypeLayout.setVisibility(mPresenter.getVoiceSetting() ? View.VISIBLE : View.GONE);
        mVoiceLineView.setVisibility(mPresenter.getVoiceSetting() ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onChange(SettingSwitch sb, boolean state) {
        switch (sb.getId()) {
            case R.id.push_switch:
                mPresenter.setPushSetting(state);
                if(!state){
                    Jpush.onStop(getActivity().getApplication());
                } else {
                    Jpush.resumePush(getActivity().getApplication());
                }
//                AppUtil.showShortMessage(thisActivity, "退出App才会生效!");
                break;
            case R.id.voice_switch:
                mVoiceTypeLayout.setVisibility(state ? View.VISIBLE : View.GONE);
                mVoiceLineView.setVisibility(state ? View.VISIBLE : View.GONE);
                mPresenter.setVoiceSetting(state);
                break;
            case R.id.vibrates_switch:
                mPresenter.setVibratorSetting(state);
                break;
            case R.id.friend_fresh_switch:
                mPresenter.setFriendFreshSetting(state);
                break;
            case R.id.show_safe_switch:
                mPresenter.setShowSafeSetting(state);
                break;
            case R.id.show_nopass_switch:
                mPresenter.setShowNoPassSetting(state);
                break;
        }
    }
}
