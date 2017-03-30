package com.edu.zwu.hydrops.presenter;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;

import com.edu.zwu.hydrops.base.BasePresenter;
import com.edu.zwu.hydrops.base.BaseView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by shengwei.yi on 2016/4/28.
 */
public class AlbumPresenter extends BasePresenter<AlbumPresenter.AlbumView> {
    private final static int SCAN_OK = 1;
    public List<String> mList = new ArrayList<>();
    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SCAN_OK:
                    Set set = new HashSet();
                    set.addAll(mList);
                    mList.clear();
                    mList.addAll(set);
                    mView.putPicturesToView(mList);
                    break;
            }
        }

    };

    public AlbumPresenter(AlbumView view, Context context) {
        super(view, context);
    }

    public void getAllPictures() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Uri imageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                ContentResolver contentResolver = mContext.getContentResolver();
                Cursor cursor = contentResolver.query(imageUri, null, MediaStore.Images.Media.MIME_TYPE + "=? or " +
                        MediaStore.Images.Media.MIME_TYPE + "=?", new String[]{"image/jpeg", "image/png"}, MediaStore.Images.Media.DATE_MODIFIED);
                if (cursor == null) return;
                while (cursor.moveToNext()) {
                    String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                    mList.add(path);
                }
                imageUri = MediaStore.Images.Media.INTERNAL_CONTENT_URI;
                contentResolver = mContext.getContentResolver();
                cursor = contentResolver.query(imageUri, null, MediaStore.Images.Media.MIME_TYPE + "=? or " +
                        MediaStore.Images.Media.MIME_TYPE + "=?", new String[]{"image/jpeg", "image/png"}, MediaStore.Images.Media.DATE_MODIFIED);
                if (cursor == null) return;
                while (cursor.moveToNext()) {
                    String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                    mList.add(path);
                }
                mHandler.sendEmptyMessage(SCAN_OK);
                cursor.close();
            }
        }).start();
    }

    public interface AlbumView extends BaseView {
        void putPicturesToView(List<String> list);
    }
}
