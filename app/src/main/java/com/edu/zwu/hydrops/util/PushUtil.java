package com.edu.zwu.hydrops.util;

import android.text.TextUtils;

import com.edu.zwu.hydrops.MyApplication;
import com.edu.zwu.hydrops.libraries.jpush.Jpush;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by shengwei.yi on 2015/11/16.
 */
public class PushUtil {

    /**
     * 设置要push的别名和标签
     *
     * @param alias
     * @param tag
     */
    private static void setPushAliasAndTag(String alias, String tag) {
        if (TextUtils.isEmpty(alias)) {
            return;
        }
        /** Set存放的是对象的引用，没有重复对象*/
        Set<String> tagSet = new HashSet<>();
        if (!TextUtils.isEmpty(tag)) {
            /** ","隔开的多个转换成 Set*/
            String[] sArray = tag.split(",");
            for (String sTagItme : sArray) {
                if (!isValidTagAndAlias(sTagItme)) {
                    return;
                }
                tagSet.add(sTagItme);
            }
        }
        /** 调用JPush API设置Tag*/
        Jpush.setAliasAndTags(MyApplication.mContext, alias, tagSet);
    }

    /**
     * 校验Tag Alias 只能是数字和英文字母
     *
     * @param s
     * @return
     */
    public static boolean isValidTagAndAlias(String s) {
        Pattern p = Pattern.compile("^[\u4E00-\u9FA50-9a-zA-Z_-]*$");
        Matcher m = p.matcher(s);
        return m.matches();
    }

    /**
     * 在jpush服务器上绑定用户与设备
     *
     * @param
     */
    public static void setPushUser() {
        setPushAliasAndTag(String.valueOf(0), String.valueOf(123));
    }

    /**
     * 绑定-1
     */
    public static void clearAlias() {
        setPushAliasAndTag("all", null);
    }
}
