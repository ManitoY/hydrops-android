package com.edu.zwu.hydrops.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.RelativeLayout;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolygonOptions;
import com.baidu.mapapi.map.Stroke;
import com.baidu.mapapi.model.LatLng;
import com.bumptech.glide.Glide;
import com.bumptech.glide.util.Util;
import com.edu.zwu.hydrops.MyApplication;
import com.edu.zwu.hydrops.R;
import com.edu.zwu.hydrops.adapter.CircularPagerAdapter;
import com.edu.zwu.hydrops.base.BaseActivity;
import com.edu.zwu.hydrops.base.BaseFragment;
import com.edu.zwu.hydrops.bmob.MarkerData;
import com.edu.zwu.hydrops.bmob.MyUser;
import com.edu.zwu.hydrops.entity.Banner;
import com.edu.zwu.hydrops.map.CustomInfoWindow;
import com.edu.zwu.hydrops.map.LocationManager;
import com.edu.zwu.hydrops.system.AppStatusConstant;
import com.edu.zwu.hydrops.system.AppStatusManager;
import com.edu.zwu.hydrops.util.AppUtil;
import com.edu.zwu.hydrops.view.BannerViewPager;
import com.edu.zwu.hydrops.view.CircleImageView;
import com.edu.zwu.hydrops.view.IndicatorView;
import com.kogitune.activity_transition.ActivityTransitionLauncher;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by shengwei.yi on 2015/11/2.
 */
public class MapActivity extends BaseActivity implements SensorEventListener, BaiduMap.OnMapStatusChangeListener {

    private static final int USERAROUND = 0;
    private static final int USESERIOUS = 1;
    private static final int CITYAROUND = 2;
    private static final int CITYSERIOUS = 3;

    public static final int HEAD = 4;
    private static final int SEARCH = 5;
    private int mType;

    @Bind(R.id.mapView)
    MapView mMapView;
    @Bind(R.id.map_head)
    CircleImageView mHeadImage;
    @Bind(R.id.tool_layout)
    RelativeLayout mToolLayout;
    @Bind(R.id.window_big_picture)
    RelativeLayout mWindowBigPicture;
    @Bind(R.id.map_detail_image_viewpager)
    BannerViewPager mBannerViewPager;
    @Bind(R.id.map_banner_indicator)
    IndicatorView mIndicatorView;

    private CircularPagerAdapter mBannerAdapter;

    private BaiduMap mBMap;

    private SensorManager mSensorManager;
    private Sensor mSensor;

    private Bundle mBundle;

    private Overlay mOverlay;

    private long lastTime = 0;
    private final static int TIME_SENSOR = 100;
    private float mAngle;

    private boolean isFirstLocation = true;

    private LatLng mLatLng;

    private int popViewPaddingTop = AppUtil.convertDpToPx(170);

    private BDLocation mLocation;

    public LocationClient mLocationClient = null;
    public BDLocationListener myListener = new MyLocationListener();

    @OnClick({R.id.map_head, R.id.map_user, R.id.map_serious_user, R.id.map_city, R.id.map_serious_city,
            R.id.map_location, R.id.window_big_picture, R.id.map_search})
    void onClick(View v) {
        switch (v.getId()) {
            case R.id.map_head:
                Intent intent = new Intent(this, PersonalActivity.class);
                Bundle transitionBundle = ActivityTransitionLauncher.with(this).from(mHeadImage).createBundle();
                intent.putExtras(transitionBundle);
                startActivityForResult(intent, HEAD);
                // you should prevent default activity transition animation
                overridePendingTransition(0, 0);
//                pushForResultView(PersonalActivity.class, null, HEAD);
                break;
            case R.id.map_user:
                searchCoordinate(mLocation, USERAROUND);
                MyApplication.addShowSafeSetting(false);
                MyApplication.addShowNoPassSetting(false);
                break;
            case R.id.map_serious_user:
                searchCoordinate(mLocation, USESERIOUS);
                MyApplication.addShowSafeSetting(false);
                MyApplication.addShowNoPassSetting(false);
                break;
            case R.id.map_city:
                searchCoordinate(mLocation, CITYAROUND);
                MyApplication.addShowSafeSetting(false);
                MyApplication.addShowNoPassSetting(false);
                break;
            case R.id.map_serious_city:
                searchCoordinate(mLocation, CITYSERIOUS);
                MyApplication.addShowSafeSetting(false);
                MyApplication.addShowNoPassSetting(false);
                break;
            case R.id.map_location:
                isFirstLocation = true;
                requestLocation();
                break;
            case R.id.window_big_picture:

                break;
            case R.id.map_search:
                pushForResultView(SearchActivity.class, null, SEARCH);
                break;
        }
    }

    private void searchCoordinate(final BDLocation location, final int type) {
        requestLocation();
        mType = type;
        mBMap.clear();
        if (location != null) {
            Log.i("我的坐标", location.getLatitude() + "," + location.getLongitude());
            BmobQuery<MarkerData> bmobQuery = new BmobQuery<>();
            bmobQuery.findObjects(new FindListener<MarkerData>() {
                @Override
                public void done(List<MarkerData> list, BmobException e) {
                    if (list != null && list.size() > 0) {
                        for (MarkerData markerData : list) {
                            switch (type) {
                                case USERAROUND:
                                    addMarker(markerData);
                                    MapStatusUpdate msu_user = MapStatusUpdateFactory.zoomTo(16.0f);
                                    mBMap.setMapStatus(msu_user);
                                    isFirstLocation = true;
                                    requestLocation();
                                    break;
                                case USESERIOUS:
                                    if (markerData.state.equals("告警") && Math.abs(Double.parseDouble(markerData.latlng.split(",")[0]) - location.getLatitude()) < 0.05 && Math.abs(Double.parseDouble(markerData.latlng.split(",")[1]) - location.getLongitude()) < 0.05) {
                                        addMarker(markerData);
                                        MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(16.0f);
                                        mBMap.setMapStatus(msu);
                                        isFirstLocation = true;
                                        requestLocation();
                                    }
                                    break;
                                case CITYAROUND:
                                    if (markerData.region.equals("万达广场") && Math.abs(Double.parseDouble(markerData.latlng.split(",")[0]) - Double.parseDouble(markerData.regionLatlng.split(",")[0])) < 0.05 && Math.abs(Double.parseDouble(markerData.latlng.split(",")[1]) - Double.parseDouble(markerData.regionLatlng.split(",")[1])) < 0.05) {
                                        addMarker(markerData);
                                        MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(11.0f);
                                        mBMap.setMapStatus(msu);
                                    }
                                    break;
                                case CITYSERIOUS:
                                    if (markerData.state.equals("告警") && markerData.region.equals("万达广场") && Math.abs(Double.parseDouble(markerData.latlng.split(",")[0]) - Double.parseDouble(markerData.regionLatlng.split(",")[0])) < 0.05 && Math.abs(Double.parseDouble(markerData.latlng.split(",")[1]) - Double.parseDouble(markerData.regionLatlng.split(",")[1])) < 0.05) {
                                        addMarker(markerData);
                                        MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.zoomTo(11.0f);
                                        mBMap.setMapStatus(mapStatusUpdate);
                                    }
                                    break;
                                case SEARCH:
                                    if (Math.abs(Double.parseDouble(markerData.latlng.split(",")[0]) - location.getLatitude()) < 0.05 && Math.abs(Double.parseDouble(markerData.latlng.split(",")[1]) - location.getLongitude()) < 0.05) {
                                        addMarker(markerData);
                                    }
                                    break;
                            }

                        }
                    }
                }
            });
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        int action = intent.getIntExtra(AppStatusConstant.KEY_HOME_ACTION, AppStatusConstant.ACTION_BACK_TO_HOME);
        switch (action) {
            case AppStatusConstant.ACTION_RESTART_APP:
                launchActivity();
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppStatusManager.getInstance().setAppStatus(AppStatusConstant.STATUS_NORMAL); //进入应用初始化设置成未登录状态
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_map;
    }

    @Override
    protected void afterViews(Bundle savedInstanceState) {
        mMapView.onCreate(this, savedInstanceState);
        Log.i("pxTodp", AppUtil.convertPxToDp(90) + "," + AppUtil.convertPxToDp(35));
        init();
        getUserData();
    }

    @Override
    protected BaseFragment getFragment() {
        return null;
    }

    private void visibility() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mToolLayout.setVisibility(View.VISIBLE);
            }
        }, 1000);
    }

    private void gone() {
        mToolLayout.setVisibility(View.GONE);
    }

    /**
     * 初始化操作
     */
    private void init() {
        mBMap = mMapView.getMap();
        /** 初始化传感器*/
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        mLocationClient = new LocationClient(getApplicationContext());     //声明LocationClient类
        mLocationClient.registerLocationListener(myListener);    //注册监听函数
        initLocation();
        registerSensorListener();
        setMap();
        requestLocation();
        setListener();
        visibility();
    }

    private void getUserData() {
        if (MyApplication.getIsLogin()) {
            MyUser currentUser = BmobUser.getCurrentUser(MyUser.class);
            mHeadImage.setImageUrl(currentUser.getHeadImage(), this);
            Log.i("headimage", currentUser.getHeadImage());
            mHeadImage.setBorderColor(Color.WHITE);
            mHeadImage.setBorderWidth(3);
        } else {
            mHeadImage.setImageResource(R.drawable.icon_map_head);
        }
    }

    private void setMap() {
        //普通地图
        mBMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(16.0f);
        mBMap.setMapStatus(msu);
        mBMap.setBuildingsEnabled(true); // 楼块效果
        mMapView.showZoomControls(false);
        mBMap.getUiSettings().setOverlookingGesturesEnabled(false);
        mBMap.setOnMapStatusChangeListener(this);
    }

    private void setListener() {
        mBMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if (marker == mOverlay) {
                    return false;
                }
                mLatLng = marker.getPosition();
                InfoWindow infoWindow = CustomInfoWindow.getInstance(MapActivity.this).getInfoWindow(MapActivity.this, mLatLng, popViewPaddingTop);
                mBMap.showInfoWindow(infoWindow);
                InfoWindowLayoutClick();
                InfoWindowPictrueClick();
                return true;
            }
        });
        mBMap.setOnMapTouchListener(new BaiduMap.OnMapTouchListener() {
            @Override
            public void onTouch(MotionEvent motionEvent) {
                mBMap.hideInfoWindow();
                if (mWindowBigPicture.getVisibility() == View.VISIBLE) {
                    AlphaAnimation alphaAnimation = new AlphaAnimation(1, 0);
                    alphaAnimation.setDuration(1000);
                    mWindowBigPicture.setAnimation(alphaAnimation);
                    alphaAnimation.startNow();
                    mWindowBigPicture.setVisibility(View.GONE);
                }
            }
        });
    }

    private void InfoWindowLayoutClick() {
        CustomInfoWindow.getInstance(this).getLayoutView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putDouble("latitude", mLatLng.latitude);
                bundle.putDouble("longitude", mLatLng.longitude);
                pushView(WaterDataActivity.class, bundle);
            }
        });
    }

    private void InfoWindowPictrueClick() {
        CustomInfoWindow.getInstance(this).getPictureView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mWindowBigPicture.getVisibility() == View.GONE) {
                    setBanner();
                    AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
                    alphaAnimation.setDuration(1000);
                    mWindowBigPicture.setAnimation(alphaAnimation);
                    alphaAnimation.startNow();
                    mWindowBigPicture.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void setBanner() {
        initAdapter();
        initData();
    }

    private void initData() {
        List<Banner> list = new ArrayList<>();
        List<String> imgUrlList = CustomInfoWindow.getInstance(this).getImgUrlList();
        for (String str : imgUrlList) {
            Banner banner = new Banner();
            banner.photoUrl = str;
            list.add(banner);
        }
        setBannerData(list);
    }

    private void setBannerData(List<Banner> bannerList) {
        mBannerAdapter.setData(bannerList);
        mBannerViewPager.stopLoopingBanner();
        mBannerViewPager.setOffscreenPageLimit(3);
        mBannerViewPager.setCurrentItem(1, false);
        mBannerViewPager.startLoopingBanner();
    }

    public void initAdapter() {
        mBannerAdapter = new CircularPagerAdapter(mBannerViewPager, this, mIndicatorView);
        mBannerViewPager.setAdapter(mBannerAdapter);
    }

    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy
        );//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        int span = 1000;
        option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(true);//可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤GPS仿真结果，默认需要
        mLocationClient.setLocOption(option);
    }

    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            //Receive Location
            mLocation = location;
            location(location);
        }
    }

    private void location(BDLocation location) {
        mLocation = location;
// 开启定位图层
        mBMap.setMyLocationEnabled(true);
//                Log.i("纬度", location.getLatitude() + "");
//                Log.i("经度", location.getLongitude() + "");
        // 构造定位数据
        MyLocationData locData = new MyLocationData.Builder()
                .accuracy(0)
                // 此处设置开发者获取到的方向信息，顺时针0-360
                .direction(-mAngle).latitude(location.getLatitude())
                .longitude(location.getLongitude()).build();
        // 设置定位数据
        mBMap.setMyLocationData(locData);
        // 设置定位图层的配置（定位模式，是否允许方向信息，用户自定义定位图标）
        if (isFirstLocation) {
            MyLocationConfiguration configuration = new MyLocationConfiguration(MyLocationConfiguration.LocationMode.FOLLOWING, true, null);
            mBMap.setMyLocationConfigeration(configuration);
            isFirstLocation = false;
        } else {
            MyLocationConfiguration configuration = new MyLocationConfiguration(MyLocationConfiguration.LocationMode.NORMAL, true, null);
            mBMap.setMyLocationConfigeration(configuration);
        }
    }

    /**
     * 定位监听
     */
    private void requestLocation() {
        LocationManager.getInstance().requestLocation(new LocationManager.LocationChangedListener() {
            @Override
            public void onLocationChanged(BDLocation location) {
                if (mBMap == null) {
                    return;
                }
                location(location);
            }
        });
//        BmobQuery<Location> bmobQuery = new BmobQuery<>();
//        bmobQuery.findObjects(new FindListener<Location>() {
//            @Override
//            public void done(List<Location> list, BmobException e) {
//                if (list != null && list.size() > 0) {
//                    for (Location location : list) {
//                        BDLocation bdLocation = new BDLocation();
//                        bdLocation.setLatitude(location.getLatitude());
//                        bdLocation.setLongitude(location.getLongitude());
//                        location(bdLocation);
//                    }
//                }
//            }
//        });
        mLocationClient.start();
    }

    /**
     * 地图Marker添加
     */
    private void addMarker(MarkerData markerData) {
        double latitude = Double.parseDouble(markerData.latlng.split(",")[0]);
        double longitude = Double.parseDouble(markerData.latlng.split(",")[1]);
        //定义Maker坐标点
        LatLng point = new LatLng(latitude, longitude);
        int resource = R.drawable.icon_map_low;
        if (markerData.state.trim().equals("安全") && MyApplication.getShowSafeSetting()) {
            return;
        }
        if (markerData.state.trim().equals("安全") && MyApplication.getShowNoPassSetting()) {
            return;
        }
        //构建Marker图标
        switch (markerData.state.trim()) {
            case "安全":
                resource = R.drawable.icon_map_low;
                break;
            case "警告":
                resource = R.drawable.icon_map_middle;
                break;
            case "告警":
                resource = R.drawable.icon_map_high;
                break;
        }
        BitmapDescriptor bitmap = BitmapDescriptorFactory
                .fromResource(resource);
        //构建MarkerOption，用于在地图上添加Marker
        OverlayOptions option = new MarkerOptions()
                .position(point)
                .icon(bitmap);
        //在地图上添加Marker，并显示
        mBMap.addOverlay(option);
    }

    private void addGeometry() {
        //定义多边形的五个顶点
        LatLng pt1 = new LatLng(39.93923, 116.357428);
        LatLng pt2 = new LatLng(39.91923, 116.327428);
        LatLng pt3 = new LatLng(39.89923, 116.347428);
        LatLng pt4 = new LatLng(39.89923, 116.367428);
        LatLng pt5 = new LatLng(39.91923, 116.387428);
        List<LatLng> pts = new ArrayList<LatLng>();
        pts.add(pt1);
        pts.add(pt2);
        pts.add(pt3);
        pts.add(pt4);
        pts.add(pt5);
        //构建用户绘制多边形的Option对象
        OverlayOptions polygonOption = new PolygonOptions()
                .points(pts)
                .stroke(new Stroke(5, 0xAA00FF00))
                .fillColor(0xAAFFFF00);
        //在地图上添加多边形Option，用于显示
        mBMap.addOverlay(polygonOption);
    }

    /**
     * 地图搜索后Marker添加
     */
    private void addSearchMarker(double arg0, double arg1) {
        //定义Maker坐标点
        LatLng point = new LatLng(arg0, arg1);
        //构建Marker图标
        BitmapDescriptor bitmap = BitmapDescriptorFactory
                .fromResource(R.drawable.icon_map_search_location);
        //构建MarkerOption，用于在地图上添加Marker
        OverlayOptions option = new MarkerOptions()
                .position(point)
                .icon(bitmap);
        //在地图上添加Marker，并显示
        mOverlay = mBMap.addOverlay(option);
    }

    /**
     * 注册传感器监听
     */
    public void registerSensorListener() {
        mSensorManager.registerListener(this, mSensor,
                SensorManager.SENSOR_DELAY_FASTEST);
    }

    /**
     * 取消传感器监听
     */
    public void unRegisterSensorListener() {
        if (mSensorManager != null) {
            mSensorManager.unregisterListener(this, mSensor);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (System.currentTimeMillis() - lastTime < TIME_SENSOR) {
            return;
        }
        switch (event.sensor.getType()) {
            case Sensor.TYPE_ORIENTATION: {
                float x = event.values[0];

                x += getScreenRotationOnPhone(this);
                x %= 360.0F;
                if (x > 180.0F)
                    x -= 360.0F;
                else if (x < -180.0F)
                    x += 360.0F;
                if (Math.abs(mAngle - x) < 5.0f) {
                    break;
                }
                mAngle = x;

                lastTime = System.currentTimeMillis();
            }
        }

    }

    /**
     * 获取当前屏幕旋转角度
     *
     * @return 0表示是竖屏; 90表示是左横屏; 180表示是反向竖屏; 270表示是右横屏
     */
    public static int getScreenRotationOnPhone(Context context) {
        final Display display = ((WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();

        switch (display.getRotation()) {
            case Surface.ROTATION_0:
                return 0;

            case Surface.ROTATION_90:
                return 90;

            case Surface.ROTATION_180:
                return 180;

            case Surface.ROTATION_270:
                return -90;
        }
        return 0;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            gone();
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        mMapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == HEAD) {
            getUserData();
        }
        if (resultCode == RESULT_OK && requestCode == SEARCH) {
            mBMap.clear();
            init();
            if (data != null) {
                BDLocation location = new BDLocation();
                location.setLatitude(data.getDoubleExtra("latitude", 0));
                location.setLongitude(data.getDoubleExtra("longitude", 0));
                Log.i("搜索", data.getDoubleExtra("latitude", 0) + "," + data.getDoubleExtra("longitude", 0));
                searchCoordinate(location, SEARCH);
                addSearchMarker(data.getDoubleExtra("latitude", 0), data.getDoubleExtra("longitude", 0));
                mBMap.setMapStatus(MapStatusUpdateFactory.newLatLng(new LatLng(data.getDoubleExtra("latitude", 0), data.getDoubleExtra("longitude", 0))));
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (Util.isOnMainThread()) {
            Glide.get(getApplicationContext()).clearMemory();
        }
        if (mMapView != null) {
            mMapView.onDestroy();
        }
        LocationManager.getInstance().destroyLocation();
        unRegisterSensorListener();
    }

    @Override
    public void onMapStatusChangeStart(MapStatus mapStatus) {

    }

    @Override
    public void onMapStatusChangeStart(MapStatus mapStatus, int i) {

    }

    @Override
    public void onMapStatusChange(MapStatus mapStatus) {

    }

    @Override
    public void onMapStatusChangeFinish(MapStatus mapStatus) {
        mBMap.clear();
        final LatLng latLng = mBMap.getMapStatus().target;
        BmobQuery<MarkerData> bmobQuery = new BmobQuery<>();
        bmobQuery.findObjects(new FindListener<MarkerData>() {
            @Override
            public void done(List<MarkerData> list, BmobException e) {
                if (list != null && list.size() > 0) {
                    for (MarkerData markerData : list) {
                        if (Math.abs(Double.parseDouble(markerData.latlng.split(",")[0]) - latLng.latitude) < 0.05 && Math.abs(Double.parseDouble(markerData.latlng.split(",")[1]) - latLng.longitude) < 0.05) {
                            addMarker(markerData);
                        }
                    }
                }
            }
        });
    }
}
