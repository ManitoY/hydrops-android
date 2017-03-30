package com.edu.zwu.hydrops.adapter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.PopupWindow;

import com.bumptech.glide.Glide;
import com.edu.zwu.hydrops.R;
import com.edu.zwu.hydrops.activity.CoverChooseActivity;
import com.edu.zwu.hydrops.activity.PictureMagnifiedActivity;
import com.edu.zwu.hydrops.base.BaseActivity;
import com.edu.zwu.hydrops.bmob.MyUser;
import com.edu.zwu.hydrops.bmob.Talk;
import com.edu.zwu.hydrops.util.AppUtil;
import com.edu.zwu.hydrops.view.CircleImageView;
import com.edu.zwu.hydrops.view.FontTextView;
import com.kogitune.activity_transition.ActivityTransitionLauncher;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by shengwei.yi on 2015/12/1.
 */
public class AllMessageAdapter extends BaseRecyclerListAdapter<Talk, AllMessageAdapter.ViewHolder> {

    public static final int TAKECOVER = 0;

    private View mHeaderView;

    public BaseActivity mActivity;

    public PopupWindow mMagnifiedPopupWindow;

    private MyUser mUser;

    public AllMessageAdapter(BaseActivity activity) {
        this.mActivity = activity;
    }

    @Override
    protected AllMessageAdapter.ViewHolder onCreateItemViewHolder(ViewGroup viewGroup) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.all_message_item, viewGroup, false);
        return new ViewHolder(view);
    }


    @Override
    protected void onBindItemViewHolder(final AllMessageAdapter.ViewHolder viewHolder, int position) {
        Talk talk = mDataList.get(position);
        SpannableString span = new SpannableString(talk.getCreatedAt().split(" ")[0].split("-")[2] + talk.getCreatedAt().split(" ")[0].split("-")[1] + "月");
        span.setSpan(new AbsoluteSizeSpan(30, true), 0, 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        viewHolder.mItemDate.setText(span);
        viewHolder.mItemContent.setText(talk.text);
        if (talk.imgUrl != null && !TextUtils.isEmpty(talk.imgUrl)) {
            viewHolder.mItemImage.setVisibility(View.VISIBLE);
            Glide.with(mActivity).load(talk.imgUrl).into(viewHolder.mItemImage);
            viewHolder.mItemImage.setTag(R.id.image_tag, position);
            viewHolder.mItemImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    showMagnifiedPopupWindow(mDataList.get((int) v.getTag(R.id.image_tag)).imgUrl);
                    Intent intent = new Intent(mActivity, PictureMagnifiedActivity.class);
                    intent.putExtra("imgUrl", mDataList.get((int) v.getTag(R.id.image_tag)).imgUrl);
                    final ImageView imageView = (ImageView) v;
                    imageView.getDrawable();
                    Bitmap bitmap = imageView.getDrawingCache();
                    ActivityTransitionLauncher.with(mActivity).from(v).image(bitmap).launch(intent);
                }
            });
        } else {
            viewHolder.mItemImage.setVisibility(View.GONE);
            viewHolder.mItemContent.setBackgroundColor(mActivity.getResources().getColor(R.color.message_text_color));
            viewHolder.mItemContent.setPadding(AppUtil.convertDpToPx(5), AppUtil.convertDpToPx(5),AppUtil.convertDpToPx(5),AppUtil.convertDpToPx(5));
        }
        if (position != 0) {
            Talk lastTalk = mDataList.get(position - 1);
            String talkdate = talk.getCreatedAt().split(" ")[0];
            String lasttalkdate = lastTalk.getCreatedAt().split(" ")[0];
            viewHolder.mItemDate.setVisibility(talkdate.equals(lasttalkdate) ? View.INVISIBLE : View.VISIBLE);
        }
    }

    @Override
    protected RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup viewGroup) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cover_view, viewGroup, false);
        mHeaderView = view;
        return new HeadViewHolder(view);
    }

//    private void showMagnifiedPopupWindow(String path) {
//        if (mMagnifiedPopupWindow == null) {
//            View view = mActivity.getLayoutInflater().inflate(R.layout.show_magnified_popupwindow, null);
//            ImageView imageView = (ImageView) view.findViewById(R.id.magnified_img);
//            Glide.with(mActivity).load(path).into(imageView);
//            imageView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    mMagnifiedPopupWindow.dismiss();
//                    mMagnifiedPopupWindow = null;
//                }
//            });
//            mMagnifiedPopupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//            mMagnifiedPopupWindow.setAnimationStyle(R.style.popwin_anim_style);
//        }
//        mMagnifiedPopupWindow.showAtLocation(mHeaderView, Gravity.BOTTOM, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//    }

    public void refreshCover() {
        MyUser currentUser = BmobUser.getCurrentUser(MyUser.class);
        ImageView coverBackground = (ImageView) mHeaderView.findViewById(R.id.cover_background);
        Glide.with(mActivity).load(currentUser.getCoverImage()).into(coverBackground);
    }

    class HeadViewHolder extends RecyclerView.ViewHolder {
        public HeadViewHolder(final View root) {
            super(root);
            BmobQuery<MyUser> query = new BmobQuery<>();
            query.addWhereEqualTo("petName", mActivity.getIntent().getStringExtra("petName"));
            query.findObjects(new FindListener<MyUser>() {
                @Override
                public void done(List<MyUser> list, BmobException e) {
                    if(e == null){
                        mUser = list.get(0);
                        if (mUser != null) {
                            MyUser currentUser = BmobUser.getCurrentUser(MyUser.class);
                            ImageView coverBackground = (ImageView) root.findViewById(R.id.cover_background);
                            Glide.with(mActivity).load(mUser.getCoverImage()).into(coverBackground);
                            CircleImageView coverHead = (CircleImageView) root.findViewById(R.id.cover_head);
                            coverHead.setImageUrl(mUser.getHeadImage(), mActivity);
                            coverHead.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
//                                showMagnifiedPopupWindow(mUser.getHeadImage());
                                    Intent intent = new Intent(mActivity, PictureMagnifiedActivity.class);
                                    intent.putExtra("imgUrl", mUser.getCoverImage());
                                    final ImageView imageView = (ImageView) v;
                                    imageView.getDrawable();
                                    Bitmap bitmap = imageView.getDrawingCache();
                                    ActivityTransitionLauncher.with(mActivity).from(v).image(bitmap).launch(intent);
                                }
                            });
                            FontTextView coverName = (FontTextView) root.findViewById(R.id.cover_name);
                            coverName.setText(mUser.getPetName());
                            if (mUser.getPetName().equals(currentUser.getPetName())) {
                                coverBackground.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        showDialog();
                                    }
                                });
                            }
                        }
                    } else {
                        AppUtil.showShortMessage(mActivity, e.toString());
                    }
                }
            });
        }
    }

    private void showDialog() {
        ArrayAdapter arrayAdapter = new ArrayAdapter<>(mActivity, R.layout.select_dialog_item, new String[]{"更换相册封面"});
        AlertDialog.Builder alerDialog = new AlertDialog.Builder(mActivity).setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                mActivity.pushForResultView(CoverChooseActivity.class, null, TAKECOVER);
            }
        });
        alerDialog.show();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.item_date)
        FontTextView mItemDate;

        @Bind(R.id.item_image)
        ImageView mItemImage;

        @Bind(R.id.item_content)
        FontTextView mItemContent;

        public ViewHolder(View root) {
            super(root);
            ButterKnife.bind(this, root);
        }
    }
}
