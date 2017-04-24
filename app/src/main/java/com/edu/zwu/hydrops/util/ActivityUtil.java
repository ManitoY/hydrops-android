package com.edu.zwu.hydrops.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.edu.zwu.hydrops.R;
import com.edu.zwu.hydrops.activity.AboutWeActivity;
import com.edu.zwu.hydrops.activity.AlbumActivity;
import com.edu.zwu.hydrops.activity.CameraActivity;
import com.edu.zwu.hydrops.activity.FriendActivity;
import com.edu.zwu.hydrops.activity.LoginActivity;
import com.edu.zwu.hydrops.activity.MapActivity;
import com.edu.zwu.hydrops.activity.NewsActivity;
import com.edu.zwu.hydrops.activity.PictureMagnifiedActivity;
import com.edu.zwu.hydrops.activity.RegisterActivity;
import com.edu.zwu.hydrops.activity.RegisterSuccessActivity;
import com.edu.zwu.hydrops.activity.SettingActivity;
import com.edu.zwu.hydrops.activity.TalkInfoActivity;
import com.edu.zwu.hydrops.activity.WebPagerActivity;
import com.edu.zwu.hydrops.system.AppStatusConstant;

/**
 * Created by shengwei.yi on 2016/4/28.
 */
public class ActivityUtil {

    public static final String WEBURL = "webUrl";
    public static final String WEBTITLE = "title";
    public static final String TALKIMGURL = "imgUrl";

    public static Intent createTargetIntent(Fragment context, Class<?> targetActivity) {
        return new Intent(context.getActivity(), targetActivity);
    }

    private static void launch(Fragment context, Intent intent, ActivityOptionsCompat optionsCompat) {
        if (optionsCompat == null) {
            context.startActivity(intent);
            context.getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.activity_close_enter);
        } else {
            ActivityCompat.startActivity(context.getActivity(), intent, optionsCompat.toBundle());
        }
    }

    private static void launchForResult(Fragment context, Intent intent, int requestCode, ActivityOptionsCompat optionsCompat) {
        if (optionsCompat == null) {
            context.getActivity().startActivityForResult(intent, requestCode);
            context.getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.activity_close_enter);
        } else {
            ActivityCompat.startActivityForResult(context.getActivity(), intent, requestCode, optionsCompat.toBundle());
        }
    }

    public static void launchAlbumActivity(Fragment context, int requestCode){
        launchForResult(context, createTargetIntent(context, AlbumActivity.class), requestCode, null);
    }

    public static void launchMapActivity(Fragment context){
        launch(context, createTargetIntent(context, MapActivity.class), null);
    }

    public static void launchRegisterActivity(Fragment context){
        launch(context, createTargetIntent(context, RegisterActivity.class), null);
    }

    public static void launchRegisterSuccessActivity(Fragment context){
        launch(context, createTargetIntent(context, RegisterSuccessActivity.class), null);
    }

    public static void launchWebPagerActivity(Fragment context, String webUrl, String title){
        Intent intent = createTargetIntent(context, WebPagerActivity.class);
        intent.putExtra(WEBURL, webUrl);
        intent.putExtra(WEBTITLE, title);
        launch(context, intent, null);
    }

    public static void launchTalkInfoActivity(Fragment context, int requestCode){
        launchForResult(context, createTargetIntent(context, TalkInfoActivity.class), requestCode, null);
    }

    public static void launchLoginActivity(Fragment context, int requestCode){
        launchForResult(context, createTargetIntent(context, LoginActivity.class), requestCode, null);
    }

    public static void launchCameraActivity(Fragment context){
        launch(context, createTargetIntent(context, CameraActivity.class), null);
    }

    public static void launchFriendActivity(Fragment context){
        launch(context, createTargetIntent(context, FriendActivity.class), null);
    }

    public static void launchNewsActivity(Fragment context){
        launch(context, createTargetIntent(context, NewsActivity.class), null);
    }

    public static void launchSettingActivity(Fragment context){
        launch(context, createTargetIntent(context, SettingActivity.class), null);
    }

    public static void launchAboutWeActivity(Fragment context){
        launch(context, createTargetIntent(context, AboutWeActivity.class), null);
    }

    public static void launchPictureMagnifiedActivity(Context context, View view, String imgUrl) {
        //让新的Activity从一个小的范围扩大到全屏
        ActivityOptionsCompat options =
                ActivityOptionsCompat.makeScaleUpAnimation(view,
                        view.getWidth() / 2, view.getHeight() / 2, //拉伸开始的坐标
                        0, 0);//拉伸开始的区域大小，这里用（0，0）表示从无到全屏
        Intent intent = new Intent(context, PictureMagnifiedActivity.class);
        intent.putExtra(TALKIMGURL, imgUrl);
        ActivityCompat.startActivity((Activity) context, intent, options.toBundle());
    }
}
