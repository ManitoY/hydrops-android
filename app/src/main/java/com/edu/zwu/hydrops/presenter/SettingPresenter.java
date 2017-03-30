package com.edu.zwu.hydrops.presenter;

import android.app.Service;
import android.content.Context;
import android.os.Vibrator;

import com.edu.zwu.hydrops.MyApplication;
import com.edu.zwu.hydrops.base.BasePresenter;
import com.edu.zwu.hydrops.base.BaseView;

/**
 * Created by shengwei.yi on 2016/5/11.
 */
public class SettingPresenter extends BasePresenter<SettingPresenter.SettingView> {

    public SettingPresenter(SettingView view, Context context) {
        super(view, context);
    }

    public boolean getPushSetting() {
        return MyApplication.getPushSetting();
    }

    public boolean getVoiceSetting() {
        return MyApplication.getVoiceSetting();
    }

    public boolean getVibratorSetting() {
        return MyApplication.getVibratorSetting();
    }

    public boolean getFriendFreshSetting() {
        return MyApplication.getFriendFreshSetting();
    }

    public boolean getShowSafeSetting() {
        return MyApplication.getShowSafeSetting();
    }

    public boolean getShowNoPassSetting() {
        return MyApplication.getShowNoPassSetting();
    }

    public void setPushSetting(boolean state) {
        MyApplication.addPushSetting(state);
    }

    public void setVoiceSetting(boolean state) {
        MyApplication.addVoiceSetting(state);
    }

    public void setVibratorSetting(boolean state) {
        Vibrator vibrator = (Vibrator) mContext.getSystemService(Service.VIBRATOR_SERVICE);
        if (state) {
            //根据指定的模式进行震动
            //第一个参数：该数组中第一个元素是等待多长的时间才启动震动，
            //之后将会是开启和关闭震动的持续时间，单位为毫秒
            //第二个参数：重复震动时在pattern中的索引，如果设置为-1则表示不重复震动
            vibrator.vibrate(new long[]{100, 200, 300, 400}, -1);
        } else {
            //关闭振动
            vibrator.cancel();
        }
        MyApplication.addVibratorSetting(state);
    }

    public void setFriendFreshSetting(boolean state) {
        MyApplication.addFriendFreshSetting(state);
    }

    public void setShowSafeSetting(boolean state) {
        MyApplication.addShowSafeSetting(state);
    }

    public void setShowNoPassSetting(boolean state) {
        MyApplication.addShowNoPassSetting(state);
    }

    public interface SettingView extends BaseView {

    }
}
