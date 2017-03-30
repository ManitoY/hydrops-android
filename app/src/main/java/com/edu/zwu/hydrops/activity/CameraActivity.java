package com.edu.zwu.hydrops.activity;


import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import com.edu.zwu.hydrops.R;
import com.edu.zwu.hydrops.base.BaseActivity;
import com.edu.zwu.hydrops.base.BaseFragment;
import com.edu.zwu.hydrops.camera.CameraManager;
import com.edu.zwu.hydrops.view.CameraPreView;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by shengwei.yi on 2016/4/21.
 */
public class CameraActivity extends BaseActivity implements View.OnTouchListener{

    private CameraPreView mPreView;
    private CameraManager mCameraManager;
    private Camera mCamera;
    private int startValue;
    private int endValue;

    private int mPoint = 0;

    @Bind(R.id.camera_preview)
    FrameLayout mPreViewLayout;

    @OnClick(R.id.button_capture)
    void OnClick() {
        // get an image from the camera
        mCameraManager.takePicture();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_camera;
    }

    @Override
    protected void afterViews(Bundle savedInstanceState) {
        mCameraManager = CameraManager.getInstance(this);
        // Create an instance of Camera
        mCamera = mCameraManager.getCameraInstance();

        // Create our Preview view and set it as the content of our activity.
        mPreView = new CameraPreView(this, mCamera);

        mPreView.setOnTouchListener(this);
        mPreViewLayout.addView(mPreView);
        Log.i("zoom", mCamera.getParameters().getMaxZoom() + "");
    }

    @Override
    protected BaseFragment getFragment() {
        return null;
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK){
            case MotionEvent.ACTION_DOWN:
                mPoint = 1;
                Log.i("ACTION_DOWN", mPoint + "");
                break;
            case MotionEvent.ACTION_UP:
                mPoint = 0;
                mCamera.autoFocus(mCameraManager.mAutoFocusCallback);
                Log.i("ACTION_UP", mPoint + "");
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                mPoint += 1;
                startValue = twoPointDistance(event);
                Log.i("ACTION_POINTER_DOWN", mPoint + "");
                break;
            case MotionEvent.ACTION_POINTER_UP:
                mPoint -= 1;
                Log.i("ACTION_POINTER_UP", mPoint + "");
                break;
            case MotionEvent.ACTION_MOVE:
                if(mPoint == 2){
                    endValue = twoPointDistance(event) - startValue;
                    if(endValue > mCamera.getParameters().getMaxZoom() || endValue < 0){
                        return false;
                    }
                    mCameraManager.setZoom(endValue);

                    Log.i("ACTION_MOVE", twoPointDistance(event) + "");
                }
                break;
        }
        return true;
    }

    private int twoPointDistance(MotionEvent event){
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (int) Math.sqrt(x * x + y * y);
    }
}
