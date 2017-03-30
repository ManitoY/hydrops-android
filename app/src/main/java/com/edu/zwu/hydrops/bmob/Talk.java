package com.edu.zwu.hydrops.bmob;

import java.io.Serializable;
import java.util.List;

import cn.bmob.v3.BmobObject;

/**
 * Created by shengwei.yi on 2015/12/1.
 */
public class Talk extends BmobObject implements Serializable {
    public String imgUrl;
    public String headImg;
    public String name;
    public String text;
    public List<String> replyContent;
    public List<String> replyName;

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<String> getReplyContent() {
        return replyContent;
    }

    public void setReplyContent(List<String> replyContent) {
        this.replyContent = replyContent;
    }

    public List<String> getReplyName() {
        return replyName;
    }

    public void setReplyName(List<String> replyName) {
        this.replyName = replyName;
    }


}
