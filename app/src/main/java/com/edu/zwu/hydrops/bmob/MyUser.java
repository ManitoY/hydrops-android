package com.edu.zwu.hydrops.bmob;

import cn.bmob.v3.BmobUser;

/**
 * Created by shengwei.yi on 2016/3/7.
 */
public class MyUser extends BmobUser {
    private String headImage;
    private String petName;
    private String sex;
    private String coverImage;

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getHeadImage() {
        return headImage;
    }

    public void setHeadImage(String headImage) {
        this.headImage = headImage;
    }

    public String getPetName() {
        return petName;
    }

    public void setPetName(String petName) {
        this.petName = petName;
    }

    public String getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }
}
