package com.edu.zwu.hydrops.util;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.Toast;

import com.edu.zwu.hydrops.Constants;
import com.edu.zwu.hydrops.MyApplication;
import com.edu.zwu.hydrops.activity.MapActivity;
import com.edu.zwu.hydrops.activity.NewsActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by shengwei.yi on 2015/11/2.
 */
public class AppUtil {

    /**
     * 解析String类型的Json数据(有默认值)
     *
     * @param jsonObject
     * @param key
     * @param defaultValue
     * @return
     */
    public static String getJsonStringValue(JSONObject jsonObject, String key, String defaultValue) {
        try {
            if (jsonObject != null && jsonObject.has(key)) {
                String value = jsonObject.getString(key).trim();
                if (value.equals("null")) {
                    value = "";
                }
                return value;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return defaultValue;
    }

    /**
     * 解析String类型的Json数据(无默认值)
     *
     * @param jsonObject
     * @param key
     * @return
     */
    public static String getJsonStringValue(JSONObject jsonObject, String key) {
        return getJsonStringValue(jsonObject, key, "");
    }

    /**
     * 解析int类型的Json数据(有默认值)
     *
     * @param jsonObject
     * @param key
     * @param defaultValue
     * @return
     */
    public static int getJsonIntegerValue(JSONObject jsonObject, String key, int defaultValue) {
        try {
            if (jsonObject != null && jsonObject.has(key)) {
                return jsonObject.getInt(key);
            }
        } catch (JSONException e) {
            return defaultValue;
        }
        return defaultValue;
    }

    /**
     * 解析int类型的Json数据(无默认值)
     *
     * @param jsonObject
     * @param key
     * @return
     */
    public static int getJsonIntegerValue(JSONObject jsonObject, String key) {
        return getJsonIntegerValue(jsonObject, key, 0);
    }

    /**
     * 进度对话框
     *
     * @param activity
     * @param hintText
     * @param cancelable
     * @return
     */
    public static ProgressDialog showProgress(Activity activity, String hintText, boolean cancelable) {
        Activity mActivity = null;
        if (activity != null && activity.getParent() != null) {
            mActivity = activity.getParent();
            if (mActivity.getParent() != null) {
                mActivity = mActivity.getParent();
            }
        } else {
            mActivity = activity;
        }
        final Activity finalActivity = mActivity;
        if (finalActivity.isFinishing()) {
            return null;
        }
        ProgressDialog window = ProgressDialog.show(finalActivity, "", hintText);
        window.getWindow().setGravity(Gravity.CENTER);
        window.setCancelable(cancelable);
        return window;
    }

    public static ProgressDialog showProgress(Activity activity) {
        return showProgress(activity, "努力加载中，请稍候...", true);
    }

    /**
     * 长吐司
     *
     * @param mContext
     * @param text
     */
    public static void showLongMessage(Context mContext, CharSequence text) {
        if (text != null && text.length() > 0) {
            Toast.makeText(mContext, text, Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 短吐司
     *
     * @param mContext
     * @param text
     */
    public static void showShortMessage(Context mContext, CharSequence text) {
        if (!TextUtils.isEmpty(text)) {
            Toast.makeText(mContext, text, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * dp 转换成 px
     *
     * @param dp
     * @return
     */
    public static int convertDpToPx(float dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, MyApplication.mContext.getResources().getDisplayMetrics());
    }

    /**
     * 处理推送消息的action
     *
     * @param context
     * @param type
     * @param value
     */
    public static void processAction(Context context, int type, String value) {
        Intent i = new Intent();
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        /** 表示未知类*/
        Class<?> mClass;
        Bundle bundle = new Bundle();
        bundle.putBoolean(MyApplication.EXTRA_IS_FROM_PUSH, true);
        switch (type) {
            case Constants.PUSH_CODE_FOR_MAP:
                mClass = MapActivity.class;
                break;
            default:
                mClass = NewsActivity.class;
                break;
        }
        i.setClass(context, mClass);
        i.putExtras(bundle);
        context.startActivity(i);
    }

    public static void processAction(Activity mActivity, String url) {
        if (TextUtils.isEmpty(url) || (!url.startsWith("tqmall") && !url.startsWith("http"))) {
            return;
        }
        Map<String, String> urlParams;
        if (url.startsWith("tqmall")) {
            if (url.contains("&=&")) {
                // 5.0上query的值(貌似判断的是=)会被encode，&&被encode成&=&
                urlParams = AppUtil.getRequestParamsMap(url, "&=&");
            } else {
                urlParams = AppUtil.getRequestParamsMap(url, "&&");
            }
        } else {
            urlParams = new HashMap<>();
            urlParams.put("type", String.valueOf(Constants.PUSH_CODE_FOR_BANNER));
            urlParams.put("value", url);
        }

        try {
            processAction(mActivity, Integer.valueOf(urlParams.get("type")), urlParams.get("value"));
        } catch (NumberFormatException e) {
//            Intent i = new Intent();
//            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            i.setClass(mActivity, MainActivity.class);
//            mActivity.startActivity(i);
//            mActivity.overridePendingTransition(R.anim.slide_in_right, R.anim.activity_close_enter);

        }
    }

    public static Map<String, String> getRequestParamsMap(String url, String splitChar) {
        Map<String, String> map = new HashMap<>();
        try {
            URI urlObj = new URI(url);
            String queryMapStr = urlObj.getQuery();
            map = getRequestParamsMapByString(queryMapStr, splitChar);
        } catch (Exception e) {
            e.printStackTrace();
            // 不做处理
        }
        return map;
    }

    public static Map<String, String> getRequestParamsMapByString(String queryMapStr, String splitChar) {
        Map<String, String> map = new HashMap<>();
        if (queryMapStr != null) {
            String[] params = queryMapStr.split(splitChar);
            for (String param : params) {
                int firstEqual = param.indexOf("=");
                if (firstEqual > 0) {
                    String name = param.substring(0, firstEqual);
                    String value = param.substring(firstEqual + 1, param.length());
                    map.put(name, value);
                }
            }
        }
        return map;
    }

}
