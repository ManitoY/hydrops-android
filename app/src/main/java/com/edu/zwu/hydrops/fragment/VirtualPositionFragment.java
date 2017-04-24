package com.edu.zwu.hydrops.fragment;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.Settings;
import android.view.View;

import com.edu.zwu.hydrops.R;
import com.edu.zwu.hydrops.base.BaseFragment;

import java.util.Date;

import butterknife.OnClick;

/**
 * Created by shengwei.yi on 17/4/5.
 */

public class VirtualPositionFragment extends BaseFragment {
    private static final int INTERVEL = 100;
    private boolean hasAddTestProvider = false;
    private LocationManager mLocationManager;

    @OnClick({R.id.virtual_position_start, R.id.virtual_position_stop})
    void OnClick(View v) {
        switch (v.getId()) {
            case R.id.virtual_position_start:
                new Thread(new RunnableMockLocation()).start();
                break;
            case R.id.virtual_position_stop:
                stopMockLocation();
                break;
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_virtual_position;
    }

    @Override
    protected void afterViews(Bundle savedInstanceState) {
        mLocationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
    }

    private boolean hasAddTestProvider() {
        boolean canMockPosition = (Settings.Secure.getInt(getActivity().getContentResolver(), Settings.Secure.ALLOW_MOCK_LOCATION, 0) != 0)
                || Build.VERSION.SDK_INT > 22;
        if (canMockPosition && !hasAddTestProvider) {
            try {
                String providerStr = LocationManager.GPS_PROVIDER;
                LocationProvider provider = mLocationManager.getProvider(providerStr);
                if (provider != null) {
                    mLocationManager.addTestProvider(
                            provider.getName()
                            , provider.requiresNetwork()
                            , provider.requiresSatellite()
                            , provider.requiresCell()
                            , provider.hasMonetaryCost()
                            , provider.supportsAltitude()
                            , provider.supportsSpeed()
                            , provider.supportsBearing()
                            , provider.getPowerRequirement()
                            , provider.getAccuracy());
                } else {
                    mLocationManager.addTestProvider(
                            providerStr
                            , true, true, false, false, true, true, true
                            , Criteria.POWER_HIGH, Criteria.ACCURACY_FINE);
                }
                mLocationManager.setTestProviderEnabled(providerStr, true);
                mLocationManager.setTestProviderStatus(providerStr, LocationProvider.AVAILABLE, null, System.currentTimeMillis());

                // 模拟位置可用
                hasAddTestProvider = true;
                canMockPosition = true;
            } catch (SecurityException e) {
                canMockPosition = false;
            }
        }
        return canMockPosition;
    }

    private class RunnableMockLocation implements Runnable {
        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(INTERVEL);

                    if (!hasAddTestProvider()) {
                        continue;
                    }

                    try {
                        // 模拟位置（addTestProvider成功的前提下）
                        String providerStr = LocationManager.GPS_PROVIDER;
                        Location mockLocation = new Location(providerStr);
                        mockLocation.setLatitude(22);   // 维度（度）
                        mockLocation.setLongitude(113);  // 经度（度）
                        mockLocation.setAltitude(30);    // 高程（米）
                        mockLocation.setBearing(180);   // 方向（度）
                        mockLocation.setSpeed(10);    //速度（米/秒）
                        mockLocation.setAccuracy(0.1f);   // 精度（米）
                        mockLocation.setTime(new Date().getTime());   // 本地时间
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                            mockLocation.setElapsedRealtimeNanos(SystemClock.elapsedRealtimeNanos());
                        }
                        mLocationManager.setTestProviderLocation(providerStr, mockLocation);
                    } catch (Exception e) {
                        // 防止用户在软件运行过程中关闭模拟位置或选择其他应用
                        stopMockLocation();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 停止模拟位置，以免启用模拟数据后无法还原使用系统位置
     * 若模拟位置未开启，则removeTestProvider将会抛出异常；
     * 若已addTestProvider后，关闭模拟位置，未removeTestProvider将导致系统GPS无数据更新；
     */
    public void stopMockLocation() {
        if (hasAddTestProvider) {
            try {
                mLocationManager.removeTestProvider(LocationManager.GPS_PROVIDER);
            } catch (Exception ex) {
                // 若未成功addTestProvider，或者系统模拟位置已关闭则必然会出错
            }
            hasAddTestProvider = false;
        }
    }

}
