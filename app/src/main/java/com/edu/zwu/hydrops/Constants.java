package com.edu.zwu.hydrops;

/**
 * Created by shengwei.yi on 2015/11/13.
 */
public class Constants {
    /**
     * 地图模式--矢量地图模式、卫星地图模式、夜景地图模式
     */
    public static final int NORMAL = 0;
    public static final int SATELLITE = 1;
    public static final int NIGHT = 2;

    /**
     * 地图定位图标旋转间隔时间
     */
    public static final int TIME_SENSOR = 100;

    /**
     * 正式环境
     */
    public static final String FORMAL_ENVIRONMENT_URL = "";
    /**
     * 测试环境
     */
    public static final String TEST_ENVIRONMENT_URL = "http://localhost:8080";
    /**
     * 推送消息到MapActivity
     */
    public static final int PUSH_CODE_FOR_MAP = 1;

    public static final int PUSH_CODE_FOR_BANNER = 2;
}
