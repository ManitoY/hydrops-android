package com.edu.zwu.hydrops.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.edu.zwu.hydrops.R;
import com.edu.zwu.hydrops.base.BaseActivity;
import com.edu.zwu.hydrops.base.BaseFragment;
import com.kogitune.activity_transition.ActivityTransition;
import com.kogitune.activity_transition.ExitActivityTransition;

import butterknife.Bind;
import uk.co.senab.photoview.PhotoView;

/**
 * Created by shengwei.yi on 2016/5/3.
 */
public class PictureMagnifiedFragment extends BaseFragment{

    public RelativeLayout mPhotoLayout;

    @Bind(R.id.big_image)
    PhotoView mBigImageView;

    public ProgressBar mProgressBar;

    public ExitActivityTransition mExitActivityTransition;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_picture_magnified;
    }

    @Override
    protected void afterViews(final Bundle savedInstanceState) {
//        Bundle bundle = mIntent.getExtras();
//        TransitionData transitionData = new TransitionData(this, bundle);
//        Drawable drawable = Drawable.createFromPath(transitionData.imageFilePath);
        mPhotoLayout = (RelativeLayout) mView.findViewById(R.id.photo_layout);
        mProgressBar = (ProgressBar) mView.findViewById(R.id.progress);
        String imgUrl = thisActivity.getIntent().getStringExtra("imgUrl");
        Log.i("imgUrl", imgUrl);
        mBigImageView.setAdjustViewBounds(true);
        mBigImageView.setMaxWidth(BaseActivity.width);
        mBigImageView.setMaxHeight(BaseActivity.height);

        Glide.with(thisActivity).load(imgUrl).error(R.drawable.default_picture).fitCenter().into(new SimpleTarget<GlideDrawable>(){
            @Override
            public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> animation) {
                mProgressBar.setVisibility(View.GONE);
                mBigImageView.setImageDrawable(resource);
                mExitActivityTransition = ActivityTransition.with(thisActivity.getIntent()).to(mBigImageView).duration(300).start(savedInstanceState);
            }
        });

    }
}
