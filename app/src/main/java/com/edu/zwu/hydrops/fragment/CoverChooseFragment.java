package com.edu.zwu.hydrops.fragment;

import android.os.Bundle;
import android.view.View;

import com.edu.zwu.hydrops.R;

import butterknife.OnClick;

/**
 * Created by shengwei.yi on 2016/4/28.
 */
public class CoverChooseFragment extends TakePhotoFragment {

    @OnClick({R.id.album, R.id.camera})
    void onClick(View v){
        switch (v.getId()){
            case R.id.album:
                clickAlbum();
                break;
            case R.id.camera:
                clickCamera();
                break;
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_cover_choose;
    }

    @Override
    protected void afterViews(Bundle savedInstanceState) {

    }

    @Override
    public void getBitmapSuccess() {
        saveBitmap();
    }

    @Override
    public void uploadSuccess() {
        updateCoverImage();
    }

    @Override
    public void updateSuccess() {
        thisActivity.finish();
    }
}
