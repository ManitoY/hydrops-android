package com.edu.zwu.hydrops.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.edu.zwu.hydrops.MyApplication;
import com.edu.zwu.hydrops.R;
import com.edu.zwu.hydrops.activity.CoverChooseActivity;
import com.edu.zwu.hydrops.activity.MyAlbumActivity;
import com.edu.zwu.hydrops.activity.PictureMagnifiedActivity;
import com.edu.zwu.hydrops.base.BaseActivity;
import com.edu.zwu.hydrops.bmob.MyUser;
import com.edu.zwu.hydrops.bmob.Talk;
import com.edu.zwu.hydrops.emoticon.BitmapLruCache;
import com.edu.zwu.hydrops.util.AppUtil;
import com.edu.zwu.hydrops.view.CircleImageView;
import com.edu.zwu.hydrops.view.FontTextView;
import com.kogitune.activity_transition.ActivityTransitionLauncher;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.BmobWrapper;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by shengwei.yi on 2015/12/1.
 */
public class AllTalkAdapter extends BaseRecyclerListAdapter<Talk, AllTalkAdapter.ViewHolder> {

    public BaseActivity mActivity;

    private View mHeaderView;

    private LinearLayout mInputLayout;

    public PopupWindow mMagnifiedPopupWindow;

    private EditText mInputView;

    public static final int TAKECOVER = 0;

    private boolean mIsReply;

    private String mPetName;

    private String mReplyName;

    private List<String> mHintNameTotal = new ArrayList<>();

    private int mButtonId;

    private int mLastCount = 0;

    private ProgressDialog mProgressDialog;

    public AllTalkAdapter(BaseActivity activity) {
        this.mActivity = activity;
        getUserInfo();
    }

    @Override
    protected AllTalkAdapter.ViewHolder onCreateItemViewHolder(ViewGroup viewGroup) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.all_talk_item, viewGroup, false);
        return new ViewHolder(view);
    }

    private void getUserInfo() {
        if(BmobWrapper.getInstance() != null) {
            MyUser currentUser = BmobUser.getCurrentUser(MyUser.class);
            mPetName = currentUser.getPetName();
        }
    }

    @Override
    protected void onBindItemViewHolder(final AllTalkAdapter.ViewHolder viewHolder, int position) {
        final Talk talk = mDataList.get(position);
        if (TextUtils.isEmpty(talk.imgUrl) || talk.imgUrl == null) {
            viewHolder.mItemImage.setVisibility(View.GONE);
        } else {
            viewHolder.mItemImage.setVisibility(View.VISIBLE);
            Glide.with(mActivity).load(talk.imgUrl).placeholder(R.drawable.default_picture).into(viewHolder.mItemImage);
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
        }
        viewHolder.mItemHead.setImageUrl(talk.headImg, mActivity);
        viewHolder.mItemHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("petName", talk.name);
                mActivity.pushView(MyAlbumActivity.class, bundle);
            }
        });
        viewHolder.mItemName.setText(talk.name);
        viewHolder.mItemText.setText(talk.text);
        viewHolder.mItemTime.setText(talk.getCreatedAt());
        viewHolder.mItemSend.setTag(position);
        mInputLayout = (LinearLayout) mActivity.findViewById(R.id.input_layout);
        mInputView = (EditText) mActivity.findViewById(R.id.input_view);
        Button button = (Button) mActivity.findViewById(R.id.send_btn);
        if (button != null) {
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (TextUtils.isEmpty(mInputView.getText())) {
                        return;
                    }
                    hideInputView();
                    Talk talk = mDataList.get(mButtonId);
                    if (talk.replyName == null) {
                        talk.replyName = new ArrayList<>();
                    }
                    talk.replyName.add(mIsReply ? mPetName + "," + mReplyName : mPetName);
                    if (talk.replyContent == null) {
                        talk.replyContent = new ArrayList<>();
                    }
                    talk.replyContent.add(mInputView.getText().toString());
                    talk.setReplyName(talk.replyName);
                    talk.setReplyContent(talk.replyContent);
                    talk.update(talk.getObjectId(), new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                refreshData();
                                mInputView.setText("");
                            } else {
                                AppUtil.showShortMessage(mActivity, e.toString());
                            }
                        }
                    });
                }
            });
        }
        viewHolder.mItemSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mButtonId = (int) v.getTag();
                Log.i("你点击的是：", mButtonId + "");
                showInputView();
                mIsReply = false;
            }
        });
        viewHolder.mTotalItem.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideInputView();
                return true;
            }
        });
        viewHolder.mReplyLayout.setVisibility(talk.replyContent != null && talk.replyContent.size() != 0 ? View.VISIBLE : View.GONE);
        chartList(talk.replyName, talk.replyContent, viewHolder.mReplyLayout, position);
    }

    private void showInputView() {
        if (mInputLayout.getVisibility() == View.GONE) {
            mInputView.setFocusableInTouchMode(true);
            mInputView.requestFocus();
            InputMethodManager imm = (InputMethodManager) mActivity.getSystemService(Activity.INPUT_METHOD_SERVICE);
            imm.showSoftInput(mInputView, InputMethodManager.SHOW_FORCED);
            mInputLayout.setVisibility(View.VISIBLE);
        }
    }

    public void hideInputView() {
        if (mInputLayout.getVisibility() == View.VISIBLE) {
            InputMethodManager imm = (InputMethodManager) mActivity.getSystemService(Activity.INPUT_METHOD_SERVICE);
            if (!imm.hideSoftInputFromWindow(mInputView.getWindowToken(), 0)) {
                imm.hideSoftInputFromInputMethod(mInputView.getWindowToken(), 0);
            }
            mInputLayout.setVisibility(View.GONE);
        }
    }

    private void refreshData() {
        mProgressDialog = AppUtil.showProgress(mActivity);
        BmobQuery<Talk> bmobQuery = new BmobQuery<Talk>();
        bmobQuery.findObjects(new FindListener<Talk>() {
            @Override
            public void done(List<Talk> list, BmobException e) {
                if (e == null) {
                    refreshViewByReplaceData(list);
                    dismissProgressDialog();
                } else {
                    dismissProgressDialog();
                    AppUtil.showShortMessage(mActivity, e.toString());
                }
            }
        });
    }

    private void chartList(List<String> replyName, List<String> replyContent, LinearLayout replyLayout, int position) {
        replyLayout.removeAllViews();
        if (replyName != null && replyName.size() != 0 && replyContent != null && replyContent.size() != 0) {
            replyLayout.setVisibility(View.VISIBLE);
            for (int i = 0; i < replyName.size(); i++) {
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                LinearLayout ll = new LinearLayout(mActivity);
                ll.setOrientation(LinearLayout.HORIZONTAL);
                ll.setPadding(AppUtil.convertDpToPx(5), AppUtil.convertDpToPx(2.5f), AppUtil.convertDpToPx(5), AppUtil.convertDpToPx(2.5f));
                ll.setLayoutParams(params);
                ll.setBackgroundResource(R.drawable.friend_reply_layout_btn_selector);
                if (replyName.get(i).contains(",")) {
                    String[] str = replyName.get(i).split(",");
                    final FontTextView textViewAbove = new FontTextView(mActivity);
                    textViewAbove.setText(str[0]);
                    textViewAbove.setBackgroundResource(R.drawable.friend_reply_name_btn_selector);
                    textViewAbove.setTextColor(mActivity.getResources().getColor(R.color.actionbar_blue));
                    textViewAbove.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Bundle bundle = new Bundle();
                            bundle.putString("petName", textViewAbove.getText().toString());
                            mActivity.pushView(MyAlbumActivity.class, bundle);
                        }
                    });
                    FontTextView textViewReply = new FontTextView(mActivity);
                    textViewReply.setText("回复");
                    final FontTextView textViewBehind = new FontTextView(mActivity);
                    textViewBehind.setText(str[1]);
                    textViewBehind.setBackgroundResource(R.drawable.friend_reply_name_btn_selector);
                    textViewBehind.setTextColor(mActivity.getResources().getColor(R.color.actionbar_blue));
                    textViewBehind.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Bundle bundle = new Bundle();
                            bundle.putString("petName", textViewBehind.getText().toString());
                            mActivity.pushView(MyAlbumActivity.class, bundle);
                        }
                    });
                    FontTextView textViewContent = new FontTextView(mActivity);
                    textViewContent.setText(":" + replyContent.get(i));
                    ll.addView(textViewAbove);
                    ll.addView(textViewReply);
                    ll.addView(textViewBehind);
                    ll.addView(textViewContent);
                    ll.setTag(position + i + mLastCount);
                    mHintNameTotal.add(replyName.get(i).split(",")[0]);
                } else {
                    final FontTextView textViewName = new FontTextView(mActivity);
                    textViewName.setText(replyName.get(i));
                    textViewName.setBackgroundResource(R.drawable.friend_reply_name_btn_selector);
                    textViewName.setTextColor(mActivity.getResources().getColor(R.color.actionbar_blue));
                    textViewName.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Bundle bundle = new Bundle();
                            bundle.putString("petName", textViewName.getText().toString());
                            mActivity.pushView(MyAlbumActivity.class, bundle);
                        }
                    });
                    FontTextView textViewContent = new FontTextView(mActivity);
                    textViewContent.setText(":" + replyContent.get(i));
                    ll.addView(textViewName);
                    ll.addView(textViewContent);
                    ll.setTag(position + i + mLastCount);
                    mHintNameTotal.add(replyName.get(i));
                }
                ll.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mReplyName = mHintNameTotal.get((int) v.getTag());
                        if (mReplyName.equals(mPetName)) {
                            return;
                        }
                        showInputView();
                        mInputView.setHint("回复" + mReplyName + ":");
                        mIsReply = true;
                    }
                });
                replyLayout.addView(ll);
            }
            mLastCount = replyName.size() - 1 + mLastCount;
        }
    }

    private void showMagnifiedPopupWindow(String path) {
        if (mMagnifiedPopupWindow == null) {
            View view = mActivity.getLayoutInflater().inflate(R.layout.show_magnified_popupwindow, null);
            ImageView imageView = (ImageView) view.findViewById(R.id.magnified_img);
            imageView.setAdjustViewBounds(true);
            imageView.setMaxWidth(mActivity.width);
            imageView.setMaxHeight(mActivity.height);
            final ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.progress);
            progressBar.setVisibility(View.VISIBLE);
            Glide.with(mActivity).load(path).into(new GlideDrawableImageViewTarget(imageView) {
                @Override
                public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> animation) {
                    progressBar.setVisibility(View.GONE);
                    super.onResourceReady(resource, animation);
                }
            });
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    progressBar.setVisibility(View.GONE);
                    mMagnifiedPopupWindow.dismiss();
                    mMagnifiedPopupWindow = null;
                }
            });
            mMagnifiedPopupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//            mMagnifiedPopupWindow.setAnimationStyle(R.style.popwin_anim_style);
        }
        mMagnifiedPopupWindow.showAtLocation(mHeaderView, Gravity.BOTTOM, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    }

    @Override
    protected RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup viewGroup) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cover_view, viewGroup, false);
        mHeaderView = view;
        return new HeadViewHolder(view);
    }

    class HeadViewHolder extends RecyclerView.ViewHolder {

        public HeadViewHolder(View root) {
            super(root);
            MyUser currentUser = BmobUser.getCurrentUser(MyUser.class);
            ImageView coverBackground = (ImageView) root.findViewById(R.id.cover_background);
            Glide.with(mActivity).load(currentUser.getCoverImage()).placeholder(R.drawable.default_picture).into(coverBackground);
            CircleImageView coverHead = (CircleImageView) root.findViewById(R.id.cover_head);
            coverHead.setImageUrl(currentUser.getHeadImage(), mActivity);
            coverHead.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putString("petName", mPetName);
                    mActivity.pushView(MyAlbumActivity.class, bundle);
                }
            });
            FontTextView coverName = (FontTextView) root.findViewById(R.id.cover_name);
            coverName.setText(currentUser.getPetName());
            coverBackground.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDialog();
                }
            });
        }
    }

    public void refreshCover() {
        MyUser currentUser = BmobUser.getCurrentUser(MyUser.class);
        ImageView coverBackground = (ImageView) mHeaderView.findViewById(R.id.cover_background);
        Glide.with(mActivity).load(currentUser.getCoverImage()).placeholder(R.drawable.default_picture).into(coverBackground);
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

    private void dismissProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.total_item)
        RelativeLayout mTotalItem;

        @Bind(R.id.item_head)
        CircleImageView mItemHead;

        @Bind(R.id.item_name)
        FontTextView mItemName;

        @Bind(R.id.item_text)
        FontTextView mItemText;

        @Bind(R.id.item_time)
        FontTextView mItemTime;

        @Bind(R.id.item_image)
        ImageView mItemImage;

        @Bind(R.id.item_send)
        ImageView mItemSend;

        @Bind(R.id.reply_layout)
        LinearLayout mReplyLayout;

        public ViewHolder(View root) {
            super(root);
            ButterKnife.bind(this, root);
        }
    }
}
