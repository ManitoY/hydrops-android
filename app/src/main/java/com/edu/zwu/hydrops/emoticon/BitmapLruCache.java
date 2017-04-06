package com.edu.zwu.hydrops.emoticon;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.util.LruCache;
import android.util.Pair;

import com.bumptech.glide.disklrucache.DiskLruCache;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

/**
 * 表情包LRU缓存类
 * Created by shengwei.yi on 17/3/31.
 */

public class BitmapLruCache {
    private static final String EMOJI_FOLDER = "bitmap"; // Bitmap的缓存文件夹
    private static final int CACHE_VERSION = 1; // 缓存文件版本
    private static final int CACHE_SIZE = 1024 * 1024 * 20; // 缓存文件大小

    private LruCache<String, Bitmap> mMemoryCache; // 内存缓存
    private DiskLruCache mDiskCache; // DiskLruCache, 硬盘缓存
    private final Context mContext; // 上下文

    private static BitmapLruCache sInstance; // 单例

    private BitmapLruCache(@NonNull final Context context) {
        mContext = context.getApplicationContext();

        initMemoryCache(); // 初始化内存缓存
        initDiskCache(mContext); // 初始化磁盘缓存
    }

    /**
     * 初始化内存缓存
     */
    private void initMemoryCache() {
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        final int cacheSize = maxMemory / 4;
        mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override protected int sizeOf(String key, Bitmap value) {
                return value.getRowBytes() * value.getHeight() / 1024;
            }
        };
    }

    /**
     * 初始化外存缓存
     *
     * @param context 上下文
     */
    private void initDiskCache(@NonNull final Context context) {
        // 获取缓存文件
        File diskCacheDir = getDiskCacheDir(context);
        // 如果文件不存在, 则创建
        if (!diskCacheDir.exists()) {
            if (!diskCacheDir.mkdirs()) {
                Log.e("BitmapLruCache", "ERROR: 创建缓存失败");
            }
        }

        try {
            // 创建缓存地址
            mDiskCache = DiskLruCache.open(diskCacheDir, CACHE_VERSION, 1, CACHE_SIZE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 将Bitmap写入缓存
     *
     * @param url Bitmap的网络Url(唯一标识)
     * @throws IOException
     */
    public void addBitmapToCache(final @NonNull String url) throws IOException {
        if (mDiskCache == null || TextUtils.isEmpty(url)) {
            return;
        }

        String key = hashKeyFormUrl(url); // Url的Key

        DiskLruCache.Editor editor = mDiskCache.edit(key); // 得到Editor对象
        if (editor != null) {
            OutputStream outputStream = editor.newOutputStream(0);
            // 根据输出流的返回值决定是否提交至缓存
            if (downloadUrlToStream(url, outputStream)) {
                // 提交写入操作
                editor.commit();
            } else {
                // 撤销写入操作
                editor.abort();
            }
            mDiskCache.flush(); // 更新缓存
        }

        getBitmapFromCache(url); // 加载内存缓存
    }

    /**
     * 从缓存中取出Bitmap
     *
     * @param url 网络Url的地址, 图片的唯一标识
     * @return url匹配的Bitmap
     * @throws IOException
     */
    public Bitmap getBitmapFromCache(final @NonNull String url) throws IOException {
        //如果缓存中为空  直接返回为空
        if (mDiskCache == null || mMemoryCache == null || TextUtils.isEmpty(url)) {
            return null;
        }

        // 通过key值在缓存中找到对应的Bitmap
        String key = hashKeyFormUrl(url);

        Bitmap bitmap = mMemoryCache.get(key);
        if (bitmap == null) {
            // 通过key得到Snapshot对象
            DiskLruCache.Snapshot snapShot = mDiskCache.get(key);
            if (snapShot != null) {
                // 得到文件输入流
                InputStream ins = snapShot.getInputStream(0);
                bitmap = BitmapFactory.decodeStream(ins);
            }

            if (bitmap != null) {
                // 设置图片大小, 防止内存缓存溢出, 节省内存
                int size = AppUtils.spToPx(mContext, mBitmapSize); // 默认18
                bitmap = Bitmap.createScaledBitmap(bitmap, size, size, true);
                mMemoryCache.put(key, bitmap);
            }
        }
        return bitmap;
    }

    /**
     * 下载并缓存Emoji标签
     *
     * @param context 上下文
     * @param pairs   表情对[Emoji符号, Emoji下载地址]
     */
    private void saveEmoji(@NonNull Context context, @NonNull ArrayList<Pair<String, String>> pairs) {
        // 当未提供数据时, 不刷新Emoji的数据
        if (pairs.size() == 0) {
            return;
        }
        ArrayList<String> urls = new ArrayList<>();
        for (Pair<String, String> pair : pairs) {
            urls.add(pair.second);
        }
        new EmojiDownloadAsyncTasks(context, urls).execute();
    }

    // Emoji表情的异步下载链接, 存储至缓存
    public static class EmojiDownloadAsyncTasks extends AsyncTask<Void, Void, Void> {
        private final Context mContext;
        private final ArrayList<String> mUrls;

        public EmojiDownloadAsyncTasks(
                final @NonNull Context context,
                final @NonNull ArrayList<String> urls) {
            mContext = context.getApplicationContext();
            mUrls = urls;
        }

        @Override
        protected @Nullable
        Void doInBackground(Void... params) {
            BitmapLruCache cache = BitmapLruCache.getInstance(mContext);
            for (int i = 0; i < mUrls.size(); ++i) {
                try {
                    cache.addBitmapToCache(mUrls.get(i));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
    }

}
