package com.edu.zwu.hydrops.libraries.jpush;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import java.util.Set;
import cn.jpush.android.api.JPushInterface;

public class Jpush {

    public final static String ACTION_REGISTRATION_ID = JPushInterface.ACTION_REGISTRATION_ID;
    public final static String ACTION_MESSAGE_RECEIVED = JPushInterface.ACTION_MESSAGE_RECEIVED;
    public final static String ACTION_NOTIFICATION_RECEIVED = JPushInterface.ACTION_NOTIFICATION_RECEIVED;
    public final static String ACTION_NOTIFICATION_OPENED = JPushInterface.ACTION_NOTIFICATION_OPENED;
    public final static String EXTRA_MSG_ID = JPushInterface.EXTRA_MSG_ID;
    public final static String EXTRA_EXTRA = JPushInterface.EXTRA_EXTRA;

    public static void init(Context context) {
        JPushInterface.init(context);
        JPushInterface.setDebugMode(false);
    }

    public static void setAliasAndTags(Context context, String alias, Set<String> tagSet) {
        JPushInterface.setAliasAndTags(context, alias, tagSet);
    }

    public static void reportNotificationOpened(Context context, String msgId) {
        JPushInterface.reportNotificationOpened(context, msgId);
    }

    public static void onResume(Activity mActivity) {
        JPushInterface.onResume(mActivity);
    }

    public static void onPause(Activity mActivity) {
        JPushInterface.onPause(mActivity);
    }

    public static void onStop(Application application){
        JPushInterface.stopPush(application);
    }

    public static void resumePush(Application application){
        JPushInterface.resumePush(application);
    }
}
