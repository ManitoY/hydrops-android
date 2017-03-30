package com.edu.zwu.hydrops.bmob;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;

/**
 * Created by shengwei.yi on 16/11/16.
 */

public class Location extends BmobObject implements Serializable {
    public double latitude;
    public double longitude;

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
