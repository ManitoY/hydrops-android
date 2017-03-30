package com.edu.zwu.hydrops;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.preference.PreferenceManager;

import com.edu.zwu.hydrops.libraries.jpush.Jpush;
import com.edu.zwu.hydrops.util.AppUtil;

import java.io.File;


/**
 * Created by shengwei.yi on 2015/11/16.
 */
public class MyApplication extends Application {
    public static Context mContext;
    public static SharedPreferences config;

    public static final String EXTRA_IS_FROM_PUSH = "is_from_push";

    /**
     * config
     */
    public static final String ACCOUNT = "account";
    public static final String PASSWORD = "password";
    public static final String LOGIN = "login";
    public static final String TALKCOUNT = "talkcount";
    public static final String SETTINGPUSH = "setting_push";
    public static final String SETTINGVOICE = "setting_voice";
    public static final String SETTINGVIBRATES = "setting_vibrates";
    public static final String SETTINGFRIENDFRESH = "setting_friend_fresh";
    public static final String SETTINGSHOWSAFE = "setting_show_safe";
    public static final String SETTINGSHOWNOPASS = "setting_show_no_pass";

    public MyApplication() {
        super();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this.getApplicationContext();
        config = PreferenceManager.getDefaultSharedPreferences(this);
        Jpush.init(this);
    }

    /**
     * 是否有SD卡
     *
     * @return
     */
    public static boolean isSDPresent() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * 获得一个临时的文件
     *
     * @return
     */
    public static String getTempFileDir() {
        return (isSDPresent() ? mContext.getExternalCacheDir().getAbsolutePath() : mContext.getCacheDir().getAbsolutePath()) + File.separator;
    }

    /**
     * 添加是否登录
     *
     * @param isLogin
     */
    public static void addIsLogin(boolean isLogin) {
        config.edit().putBoolean(LOGIN, isLogin).apply();
    }

    /**
     * 得到是否登录
     *
     * @return
     */
    public static boolean getIsLogin() {
        return config.getBoolean(LOGIN, false);
    }

    /**
     * 清除是否登录
     */
    public static void clearIsLogin() {
        config.edit().remove(LOGIN).apply();
    }

    /**
     * 添加用户名
     *
     * @param account
     */
    public static void addAccount(String account) {
        config.edit().putString(ACCOUNT, account).apply();
    }

    /**
     * 得到用户名
     *
     * @return
     */
    public static String getAccount() {
        return config.getString(ACCOUNT, null);
    }

    /**
     * 添加密码
     *
     * @param password
     */
    public static void addPassword(String password) {
        config.edit().putString(PASSWORD, password).apply();
    }

    /**
     * 得到密码
     *
     * @return
     */
    public static String getPassword() {
        return config.getString(PASSWORD, null);
    }

    /**
     * 清除密码
     */
    public static void clearPassword() {
        config.edit().remove(PASSWORD).apply();
    }

    /**
     * 添加朋友圈列表数量
     *
     * @param count
     */
    public static void addTalkCount(int count) {
        config.edit().putInt(TALKCOUNT, count).apply();
    }

    /**
     * 得到朋友圈列表数量
     *
     * @return
     */
    public static int getTalkCount() {
        return config.getInt(TALKCOUNT, 0);
    }

    /**
     * 设置是否允许推送消息
     *
     * @param isPush
     */
    public static void addPushSetting(boolean isPush) {
        config.edit().putBoolean(SETTINGPUSH, isPush).apply();
    }

    public static boolean getPushSetting() {
        return config.getBoolean(SETTINGPUSH, true);
    }

    /**
     * 设置提示声音
     *
     * @param isVoice
     */
    public static void addVoiceSetting(boolean isVoice) {
        config.edit().putBoolean(SETTINGVOICE, isVoice).apply();
    }

    public static boolean getVoiceSetting() {
        return config.getBoolean(SETTINGVOICE, true);
    }

    /**
     * 设置是否振动
     *
     * @param isVibrator
     */
    public static void addVibratorSetting(boolean isVibrator) {
        config.edit().putBoolean(SETTINGVIBRATES, isVibrator).apply();
    }

    public static boolean getVibratorSetting() {
        return config.getBoolean(SETTINGVIBRATES, true);
    }

    /**
     * 设置朋友圈更新提示
     *
     * @param isFriendFresh
     */
    public static void addFriendFreshSetting(boolean isFriendFresh) {
        config.edit().putBoolean(SETTINGFRIENDFRESH, isFriendFresh).apply();
    }

    public static boolean getFriendFreshSetting() {
        return config.getBoolean(SETTINGFRIENDFRESH, true);
    }

    /**
     * 设置是否不显示安全积水点位置
     *
     * @param isShowSafe
     */
    public static void addShowSafeSetting(boolean isShowSafe) {
        config.edit().putBoolean(SETTINGSHOWSAFE, isShowSafe).apply();
    }

    public static boolean getShowSafeSetting() {
        return config.getBoolean(SETTINGSHOWSAFE, false);
    }

    /**
     * 设置是否只显示不可通行积水位置
     *
     * @param isShowNoPass
     */
    public static void addShowNoPassSetting(boolean isShowNoPass) {
        config.edit().putBoolean(SETTINGSHOWNOPASS, isShowNoPass).apply();
    }

    public static boolean getShowNoPassSetting() {
        return config.getBoolean(SETTINGSHOWNOPASS, false);
    }
}
