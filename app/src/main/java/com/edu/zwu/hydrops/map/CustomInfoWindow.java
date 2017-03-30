package com.edu.zwu.hydrops.map;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.model.LatLng;
import com.bumptech.glide.Glide;
import com.bumptech.glide.util.Util;
import com.edu.zwu.hydrops.R;
import com.edu.zwu.hydrops.bmob.MarkerData;
import com.edu.zwu.hydrops.view.FontTextView;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by shengwei.yi on 2016/3/10.
 */
public class CustomInfoWindow {
    private static CustomInfoWindow mInstancce;
    private static Context mContext;
    private View mView;
    private MarkerData mMarkerData;

    public static CustomInfoWindow getInstance(Context context) {
        if (mInstancce == null) {
            mInstancce = new CustomInfoWindow();
            mContext = context;
        }
        return mInstancce;
    }

    private View getView(Context context, LatLng latLng) {
        mView = View.inflate(context, R.layout.infowindow_custom, null);
        getMarkerData(latLng);
        return mView;
    }

    private void getMarkerData(LatLng ll){
        BmobQuery<MarkerData> bmobQuery = new BmobQuery<>();
        bmobQuery.addWhereEqualTo("latlng", ll.latitude + "," + ll.longitude);
        bmobQuery.findObjects(new FindListener<MarkerData>() {
            @Override
            public void done(List<MarkerData> list, BmobException e) {
                if (list != null && list.size() > 0){
                    mMarkerData = list.get(0);
                    FontTextView title = (FontTextView) mView.findViewById(R.id.window_title);
                    FontTextView alarm = (FontTextView) mView.findViewById(R.id.window_alarm);
                    ImageView picture = (ImageView) mView.findViewById(R.id.window_picture);
                    FontTextView heightTitle = (FontTextView) mView.findViewById(R.id.window_height_title);
                    FontTextView height = (FontTextView) mView.findViewById(R.id.window_height_content);
                    FontTextView acrTitle = (FontTextView) mView.findViewById(R.id.window_acr_title);
                    FontTextView acr = (FontTextView) mView.findViewById(R.id.window_acr_content);
                    FontTextView stop = (FontTextView) mView.findViewById(R.id.window_stop);
                    if (mMarkerData != null) {
                        title.setText(mMarkerData.address);
                        alarm.setText(mMarkerData.state);
                        if (Util.isOnMainThread()) {
                            Glide.with(mContext.getApplicationContext()).load(mMarkerData.imgUrl.get(0)).into(picture);
                        }
                        heightTitle.setText("最高积水深度");
                        height.setText(mMarkerData.depth);
                        acrTitle.setText("积水面积");
                        acr.setText(mMarkerData.area);
                        stop.setText(mMarkerData.degree);
                    }
                }
            }
        });
    }

    public InfoWindow getInfoWindow(Context context, LatLng latLng, int yOffset) {
        return new InfoWindow(getView(context, latLng), latLng, yOffset);
    }

    public View getLayoutView() {
        return mView;
    }

    public ImageView getPictureView() {
        return (ImageView) mView.findViewById(R.id.window_picture);
    }

    public List<String> getImgUrlList(){
        return mMarkerData.imgUrl;
    }
}
