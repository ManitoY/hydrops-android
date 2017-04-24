package com.edu.zwu.hydrops.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.baidu.mapapi.SDKInitializer;
import com.edu.zwu.hydrops.R;
import com.edu.zwu.hydrops.activity.CameraActivity;
import com.edu.zwu.hydrops.activity.MapActivity;
import com.edu.zwu.hydrops.activity.PictureMagnifiedActivity;
import com.edu.zwu.hydrops.activity.RegisterSuccessActivity;
import com.edu.zwu.hydrops.libraries.jpush.Jpush;
import com.edu.zwu.hydrops.system.AppStatusConstant;
import com.edu.zwu.hydrops.system.AppStatusManager;
import com.edu.zwu.hydrops.util.ActivityUtil;
import com.edu.zwu.hydrops.view.FontTextView;

import butterknife.ButterKnife;
import cn.bmob.v3.Bmob;

/**
 * Created by shengwei.yi on 2015/11/2.
 */
public abstract class BaseActivity extends AppCompatActivity {
    protected BaseActivity thisActivity;
    protected Context mContext;
    protected Intent mIntent;
    protected ActionBar actionBar;

    protected FontTextView actionBarTitle;
    protected FontTextView actionBarLeftText;
    protected ImageView actionBarLeftBtn;
    protected Button actionBarRightBtn;
    protected ImageView actionBarRightImg;

    protected Bundle mSavedInstanceState;

    public static int width;
    public static int height;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSavedInstanceState = savedInstanceState;
        switch (AppStatusManager.getInstance().getAppStatus()) {
            case AppStatusConstant.STATUS_FORCE_KILLED:
                restartApp();
                break;
            case AppStatusConstant.STATUS_NORMAL:
                launchActivity();
                break;
        }
    }

    protected void restartApp() {
        Intent intent = new Intent(this, MapActivity.class);
        intent.putExtra(AppStatusConstant.KEY_HOME_ACTION, AppStatusConstant.ACTION_RESTART_APP);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.activity_close_enter);
    }

    protected void launchActivity() {
        mContext = this;
        thisActivity = this;
        mIntent = getIntent();
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        //注意该方法要再setContentView方法之前实现
        if (thisActivity instanceof MapActivity) {
            SDKInitializer.initialize(getApplicationContext());
        }
        if (thisActivity instanceof MapActivity || thisActivity instanceof CameraActivity) {
            int layoutId = getLayoutId();
            if (layoutId != 0) {
                setContentView(layoutId);
                ButterKnife.bind(this);
                // 初始化 Bmob SDK
                // 使用时请将第二个参数Application ID替换成你在Bmob服务器端创建的Application ID
                Bmob.initialize(this, "df5d1c8243675d6c4d0fecf2e6979099");
                afterViews(mSavedInstanceState);
            }
        } else {
            setContentView(R.layout.activity_base);
            // 初始化 Bmob SDK
            // 使用时请将第二个参数Application ID替换成你在Bmob服务器端创建的Application ID
            Bmob.initialize(this, "df5d1c8243675d6c4d0fecf2e6979099");
            afterViews(mSavedInstanceState);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.add(R.id.frameLayout, getFragment()).commit();
        }
        /** 获取屏幕宽高*/
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        width = dm.widthPixels;
        height = dm.heightPixels;
    }

    protected abstract int getLayoutId();

    protected abstract void afterViews(Bundle savedInstanceState);

    protected abstract BaseFragment getFragment();

    @Override
    protected void onResume() {
        super.onResume();
        Jpush.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Jpush.onPause(this);
    }

    public void pushView(Class<? extends Activity> activityClass, Bundle bundle, boolean isAnimator) {
        Intent intent = new Intent(thisActivity, activityClass);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
        if (isAnimator) {
            overridePendingTransition(R.anim.slide_in_right, R.anim.activity_close_enter);
        }
    }

    public void pushView(Class<? extends Activity> activityClass, Bundle bundle) {
        pushView(activityClass, bundle, true);
    }

    public void pushForResultView(Class<? extends Activity> activityClass, Bundle bundle, int requestCode) {
        Intent intent = new Intent(thisActivity, activityClass);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, requestCode);
        overridePendingTransition(R.anim.slide_in_right, R.anim.activity_close_enter);
    }

    public void popView() {
        thisActivity.finish();
        overridePendingTransition(R.anim.activity_close_enter, R.anim.slide_out_right);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            if (thisActivity instanceof RegisterSuccessActivity) {
                return false;
            }
            if (!(thisActivity instanceof MapActivity) && !(thisActivity instanceof PictureMagnifiedActivity)) {
                popView();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    protected void initActionBar() {
        initActionBar(null);
    }

    protected void initActionBar(String title) {
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayUseLogoEnabled(false);
        actionBar.setHomeButtonEnabled(false);
        actionBar.setIcon(null);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(R.layout.actionbar);
        actionBarTitle = (FontTextView) findViewById(R.id.actionbar_title);
        actionBarLeftText = (FontTextView) findViewById(R.id.actionbar_left_text);
        actionBarLeftBtn = (ImageView) findViewById(R.id.actionbar_left_btn);
        actionBarRightBtn = (Button) findViewById(R.id.actionbar_right_btn);
        actionBarRightImg = (ImageView) findViewById(R.id.actionbar_right_img);

        if (!TextUtils.isEmpty(title)) {
            actionBarTitle.setText(title);
        }

        actionBarLeftBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                popView();
            }

        });
    }

    protected void showLeftBtn() {
        actionBarLeftBtn.setVisibility(View.VISIBLE);
    }

    protected void setRightImage(int resourcesId) {
        if (actionBarRightImg != null) {
            actionBarRightImg.setVisibility(View.VISIBLE);
            actionBarRightImg.setImageResource(resourcesId);
        }
    }

    protected void showRightBtn() {
        if (actionBarRightBtn != null) {
            actionBarRightBtn.setVisibility(View.VISIBLE);
        }
    }

}
