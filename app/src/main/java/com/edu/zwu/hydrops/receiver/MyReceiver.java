package com.edu.zwu.hydrops.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.edu.zwu.hydrops.activity.MapActivity;
import com.edu.zwu.hydrops.libraries.jpush.Jpush;
import com.edu.zwu.hydrops.util.AppUtil;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by shengwei.yi on 2015/11/16.
 */
public class MyReceiver extends BroadcastReceiver {
    /**
     * 打印Tag为MyReceiver的Log
     */
    public static final String TAG = "MyReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent == null || context == null) {
            return;
        }
        Bundle bundle = intent.getExtras();
        if (Jpush.ACTION_REGISTRATION_ID.equals(intent.getAction())) {

        }else if (Jpush.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {

        } else if (Jpush.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {

        } else if (Jpush.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            if (bundle == null) {
                return;
            }
            /** 获得Json数据类型的推送消息*/
            String extraMessage = bundle.getString(Jpush.EXTRA_EXTRA);
            JSONObject json;
            Jpush.reportNotificationOpened(context, bundle.getString(Jpush.EXTRA_MSG_ID));
            try {
                json = new JSONObject(extraMessage);
                int type = AppUtil.getJsonIntegerValue(json, "type");
                String value = AppUtil.getJsonStringValue(json, "value");
                AppUtil.processAction(context, type, value);
            } catch (JSONException e) {
                e.printStackTrace();
                Intent eIntent = new Intent(context, MapActivity.class);
                /** 默认的跳转类型，它会重新创建一个新的Activity*/
                eIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(eIntent);
            }
        } else {
            Log.e(TAG, "Unhandled intent - " + intent.getAction());
        }
    }
}
