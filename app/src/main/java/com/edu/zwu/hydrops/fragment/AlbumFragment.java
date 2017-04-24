package com.edu.zwu.hydrops.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.edu.zwu.hydrops.R;
import com.edu.zwu.hydrops.adapter.BaseRecyclerListAdapter;
import com.edu.zwu.hydrops.base.BaseFragment;
import com.edu.zwu.hydrops.presenter.AlbumPresenter;
import com.edu.zwu.hydrops.view.ListRecyclerView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by shengwei.yi on 2016/4/28.
 */
public class AlbumFragment extends BaseFragment<AlbumPresenter> implements AlbumPresenter.AlbumView {

    @Bind(R.id.grid_list)
    ListRecyclerView mGridListView;

    private AlbumAdapter mAlbumAdapter;

    private int mHeight;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_album;
    }

    @Override
    protected void afterViews(Bundle savedInstanceState) {
        mPresenter = new AlbumPresenter(this, this.getContext());
        mAlbumAdapter = new AlbumAdapter();
        mAlbumAdapter.setOnRecyclerViewItemClickListener(new BaseRecyclerListAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClickListener(View view, int position) {
                Intent intent = new Intent();
                intent.putExtra("imagePath", mPresenter.mList.get(position));
                thisActivity.setResult(Activity.RESULT_OK, intent);
                thisActivity.finish();
            }
        });
        mGridListView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        mGridListView.setAdapter(mAlbumAdapter);
        mPresenter.getAllPictures();

        final ViewTreeObserver vto = mGridListView.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mGridListView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                mHeight = mGridListView.getHeight();
            }
        });
    }

    @Override
    public void putPicturesToView(List<String> list) {
        mAlbumAdapter.refreshViewByReplaceData(list);
    }

    public class AlbumAdapter extends BaseRecyclerListAdapter<String, AlbumAdapter.ViewHolder> {

        @Override
        protected ViewHolder onCreateItemViewHolder(ViewGroup viewGroup) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.all_album_item, viewGroup, false);
            return new ViewHolder(view);
        }

        @Override
        protected void onBindItemViewHolder(ViewHolder viewHolder, int position) {
            String lp = mDataList.get(position);
            int height = mHeight / 4;
            viewHolder.mTotalLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height));
            Glide.with(thisActivity).load("file://" + lp).placeholder(R.drawable.default_picture).into(viewHolder.mAlbumImageView);
            viewHolder.mAlbumCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    ((View)buttonView.getParent()).findViewById(R.id.album_image_view).setAlpha(isChecked ? 0.7f : 1f);
                }
            });
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            @Bind(R.id.album_layout)
            FrameLayout mTotalLayout;
            @Bind(R.id.album_image_view)
            ImageView mAlbumImageView;
            @Bind(R.id.album_check_box)
            CheckBox mAlbumCheckBox;


            public ViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }
    }

}
