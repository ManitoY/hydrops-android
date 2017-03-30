package com.edu.zwu.hydrops.bmob;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;

/**
 * Created by shengwei.yi on 2016/3/19.
 */
public class News extends BmobObject implements Serializable{
    public String imgStr;
    public String title;
    public String content;
    public String address;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getImgStr() {
        return imgStr;
    }

    public void setImgStr(String imgStr) {
        this.imgStr = imgStr;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
