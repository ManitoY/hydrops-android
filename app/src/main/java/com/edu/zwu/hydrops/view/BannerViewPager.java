/*
 * Copyright (C) 2013 Leszek Mzyk
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.edu.zwu.hydrops.view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.LinearInterpolator;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;

/**
 * Created by shengwei.yi on 2015/12/1.
 */
public class BannerViewPager extends ViewPager {

    private static final int LOOPING_BANNER = 0;
    private static final int LOOPING_DELAY = 5000;

    private MyHandler mHandler;

    private OnTouchEventListener onTouchEventListener;

    public interface OnTouchEventListener {
        void onTouchEvent(MotionEvent event);
    }

    public void setOnTouchEventListener(OnTouchEventListener listener) {
        this.onTouchEventListener = listener;
    }

    public BannerViewPager(Context context) {
        super(context);
        init();
    }

    public BannerViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BannerViewPager(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs);
        init();
    }

    private void init() {
        try {
            mHandler = new MyHandler(this);
            Field field = ViewPager.class.getDeclaredField("mScroller");
            field.setAccessible(true);
            FixedSpeedScroller scroller = new FixedSpeedScroller(getContext(), new LinearInterpolator());
            field.set(this, scroller);
            scroller.setmDuration(300);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //TODO change to static
//    private Handler mHandler = new Handler() {
//
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            if (msg.what == LOOPING_BANNER && getAdapter() != null) {
//                int currentItem = getCurrentItem();
//                setCurrentItem(currentItem + 1);
//                startLoopingBanner();
//            }
//        }
//
//    };

    public static class MyHandler extends Handler {

        private final WeakReference<BannerViewPager> bannerViewPagerWeakReference;

        public MyHandler(BannerViewPager bannerViewPager) {
            this.bannerViewPagerWeakReference = new WeakReference<>(bannerViewPager);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            BannerViewPager bannerViewPager = bannerViewPagerWeakReference.get();
            if (bannerViewPager != null) {
                if (msg.what == LOOPING_BANNER && bannerViewPager.getAdapter() != null) {
                    int currentItem = bannerViewPager.getCurrentItem();
                    bannerViewPager.setCurrentItem(currentItem + 1);
                    bannerViewPager.startLoopingBanner();
                }
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                stopLoopingBanner();
                break;
            case MotionEvent.ACTION_MOVE:
                stopLoopingBanner();
                break;
            case MotionEvent.ACTION_UP:
                startLoopingBanner();
                break;
            case MotionEvent.ACTION_CANCEL:
                stopLoopingBanner();
                break;

            default:
                break;
        }
        if (onTouchEventListener != null) {
            onTouchEventListener.onTouchEvent(event);
        }
        return super.onTouchEvent(event);
    }

    public void startLoopingBanner() {
        mHandler.sendEmptyMessageDelayed(LOOPING_BANNER, LOOPING_DELAY);
    }

    public void stopLoopingBanner() {
        mHandler.removeMessages(LOOPING_BANNER);
    }

}
