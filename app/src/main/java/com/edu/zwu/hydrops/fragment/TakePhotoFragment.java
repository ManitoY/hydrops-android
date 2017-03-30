package com.edu.zwu.hydrops.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.edu.zwu.hydrops.MyApplication;
import com.edu.zwu.hydrops.R;
import com.edu.zwu.hydrops.base.BaseFragment;
import com.edu.zwu.hydrops.base.BasePresenter;
import com.edu.zwu.hydrops.bmob.MyUser;
import com.edu.zwu.hydrops.util.ActivityUtil;
import com.edu.zwu.hydrops.util.AppUtil;

import java.io.File;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

/**
 * Created by shengwei.yi on 2016/3/8.
 */
public abstract class TakePhotoFragment<T extends BasePresenter> extends BaseFragment {
    private AlertDialog.Builder mAlerDialog;
    private ArrayAdapter<String> mArrayAdapter;
    private String[] array = new String[]{"相册", "拍照"};
    public ProgressDialog mProgressDialog;
    public String mHeadImgLocalPath;
    private int mTempIncrementNumber;
    public BmobFile mBmobFile;
    protected T mPresenter;


    private final int CONTEXT_MENU_ALBUM = 0;
    private final int CONTEXT_MENU_CAMERA = 1;

    @Override
    protected int getLayoutId() {
        return 0;
    }

    @Override
    protected void afterViews(Bundle savedInstanceState) {

    }

    public abstract void getBitmapSuccess();

    public abstract void uploadSuccess();

    /**
     * 对话框选项
     */
    protected void showDialog() {
        if (mAlerDialog == null) {
            mArrayAdapter = new ArrayAdapter<>(thisActivity, R.layout.select_dialog_item, array);
            mAlerDialog = new AlertDialog.Builder(thisActivity).setAdapter(mArrayAdapter, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case CONTEXT_MENU_ALBUM:
                            dialog.cancel();
                            ActivityUtil.launchAlbumActivity(thisFragment, CONTEXT_MENU_ALBUM);
                            break;
                        case CONTEXT_MENU_CAMERA:
                            dialog.cancel();
                            openImageCapture();
                            break;
                    }
                }
            });
        }
        mAlerDialog.show();
    }

    protected void clickCamera() {
        openImageCapture();
    }

    protected void clickAlbum() {
        ActivityUtil.launchAlbumActivity(this, CONTEXT_MENU_ALBUM);
    }

    public void dismissProgress() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    /**
     * 将图片上传到Bmob文件库
     */
    public void saveBitmap() {
        mProgressDialog = AppUtil.showProgress(thisActivity, "图片上传中，请稍后...", false);
        final BmobFile bmobFile = new BmobFile(new File(mHeadImgLocalPath));
        bmobFile.uploadblock(new UploadFileListener() {

            @Override
            public void done(BmobException e) {
                if (e == null) {
                    dismissProgress();
                    mBmobFile = bmobFile;
                    uploadSuccess();
                } else {
                    dismissProgress();
                    AppUtil.showShortMessage(thisActivity, "上传失败：" + e);
                    Log.e("上传图片失败：", e.toString());
                }
            }

            @Override
            public void onProgress(Integer value) {
                // 返回的上传进度（百分比）
                Log.i("图片上传进度：", value + "%");
            }
        });
    }

    /**
     * 只能用于更新头像
     */
    public void updateHeadImage() {
        MyUser myUser = new MyUser();
        myUser.setHeadImage(mBmobFile.getUrl());
        MyUser currentUser = BmobUser.getCurrentUser(MyUser.class);
        myUser.update(currentUser.getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    updateSuccess();
                } else {
                    AppUtil.showShortMessage(thisActivity, "修改头像失败：" + e);
                }
            }
        });
    }

    /**
     * 只能用于更新封面
     */
    public void updateCoverImage() {
        MyUser myUser = new MyUser();
        myUser.setCoverImage(mBmobFile.getUrl());
        MyUser currentUser = BmobUser.getCurrentUser(MyUser.class);
        myUser.update(currentUser.getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    updateSuccess();
                } else {
                    AppUtil.showShortMessage(thisActivity, "修改封面失败：" + e);
                }
            }
        });
    }

    public void updateSuccess() {

    }

    /**
     * 打开照相机
     */
    private void openImageCapture() {
        mTempIncrementNumber++;
        try {
            File imageFile = new File(getCurrentTempFilePath());
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imageFile));
            intent.putExtra("outputFormat", "JPEG");
            startActivityForResult(intent, CONTEXT_MENU_CAMERA);
        } catch (Exception e) {
            AppUtil.showShortMessage(thisActivity, "抱歉，打开照相机失败");
        }
    }

    /**
     * 得到一个临时的文件路径
     *
     * @return
     */
    private String getCurrentTempFilePath() {
        File imagesFolder = new File(MyApplication.getTempFileDir());
        imagesFolder.mkdirs();
        return imagesFolder.getPath() + "/temp" + mTempIncrementNumber + ".jpg";
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case CONTEXT_MENU_ALBUM:
                if (data != null && data.getStringExtra("imagePath") != null) {
                    Log.i("album", "album");
                    mHeadImgLocalPath = data.getStringExtra("imagePath");
                    getBitmapSuccess();
                }
                break;

            case CONTEXT_MENU_CAMERA:
                mHeadImgLocalPath = getCurrentTempFilePath();
                getBitmapSuccess();
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}
