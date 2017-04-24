package com.edu.zwu.hydrops.bmob;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;

/**
 * Created by shengwei.yi on 17/4/1.
 */

public class Emoji extends BmobObject implements Serializable {
    public String key;
    public String value;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
