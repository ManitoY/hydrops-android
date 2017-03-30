package com.edu.zwu.hydrops.entity;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by shengwei.yi on 2015/12/2.
 */
public class BaseBean implements Serializable {

    public static String filterImagePath(String path, ImgSize imgSize) {
        if (!TextUtils.isEmpty(path) && !path.contains("!")) {
            switch (imgSize) {
                case Original:
                    break;
                case Big:
                    path += "!400x400";
                    break;
                case Medium:
                    path += "!200x200";
                    break;
                case Small:
                    path += "!100x100";
                    break;
                case NSmall:
                    path += "!small";
                    break;
                default:
                    break;
            }
        }
        return path;
    }

    protected static <T extends BaseBean> List<T> fromJsonToBeanList(String arrayStr, final Class<T> classOfT) {
        List<T> resultList;
        Type tType = new ParameterizedType() {

            @Override
            public Type getRawType() {
                return List.class;
            }

            @Override
            public Type getOwnerType() {
                return null;
            }

            @Override
            public Type[] getActualTypeArguments() {
                return new Type[]{classOfT};
            }
        };
        try {
            resultList = new Gson().fromJson(arrayStr, tType);
            if (resultList == null) {
                resultList = new ArrayList<>();
            }
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            resultList = new ArrayList<>();
        }
        return resultList;
    }
}
