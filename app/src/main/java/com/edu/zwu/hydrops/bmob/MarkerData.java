package com.edu.zwu.hydrops.bmob;


import java.io.Serializable;
import java.util.List;

import cn.bmob.v3.BmobObject;

/**
 * Created by shengwei.yi on 2016/4/10.
 */
public class MarkerData extends BmobObject implements Serializable {
    public String address;
    public String state;
    public List<String> imgUrl;
    public String depth;
    public String area;
    public String degree;
    public String latlng;
    public String expectDepth;
    public String region;
    public String regionLatlng;
    public List<Float> depthList;


    public String getExpectDepth() {
        return expectDepth;
    }

    public void setExpectDepth(String expectDepth) {
        this.expectDepth = expectDepth;
    }
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public List<String> getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(List<String> imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getDepth() {
        return depth;
    }

    public void setDepth(String depth) {
        this.depth = depth;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public String getLatlng() {
        return latlng;
    }

    public void setLatlng(String latlng) {
        this.latlng = latlng;
    }

    public List<Float> getDepthList() {
        return depthList;
    }

    public void setDepthList(List<Float> depthList) {
        this.depthList = depthList;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getRegionLatlng() {
        return regionLatlng;
    }

    public void setRegionLatlng(String regionLatlng) {
        this.regionLatlng = regionLatlng;
    }
}
