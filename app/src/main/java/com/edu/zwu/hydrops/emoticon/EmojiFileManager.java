package com.edu.zwu.hydrops.emoticon;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.util.Log;
import android.util.Pair;

import com.edu.zwu.hydrops.MyApplication;
import com.edu.zwu.hydrops.emoticon.entity.EmojiIcon;
import com.edu.zwu.hydrops.util.AppUtil;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by shengwei.yi on 17/4/1.
 */

public class EmojiFileManager {
    private static final String TAG = "EmojiFileManager";
    private static final int PAGE_SIZE = 21;
    private static final String EMOJI_REGEX = "(?:\uD83C[\uDF00-\uDFFF])|(?:\uD83D[\uDC00-\uDDFF])";

    private Map<String, String> mEmojiMap;

    private List<List<EmojiIcon>> mEmojiPageLists;

    /**
     * 初始化Emoji的数据
     */
    public void initEmojiData() {
        DailyRequestData data = new DailyRequestData();
        // Emoji的有序列表
        ArrayList<Pair<String, String>> emojiPairList = data.getHydropsEmoji();
        if (!AppUtil.isListEmpty(emojiPairList)) {
            parseData(emojiPairList); // 结构化Emoji数据列表
        }
    }

    /**
     * 解析数据, 提前分页设置, 每页的表情数PAGE_SIZE.
     *
     * @param pairs Emoji的Map
     */
    private void parseData(@NonNull final ArrayList<Pair<String, String>> pairs) {
        // 当解析数据为空时, 直接返回
        if (AppUtil.isListEmpty(pairs)) {
            return;
        }

        // 转换成为HashMap, 快速查找
        mEmojiMap = convertPairList2Map(pairs);
        // 转换为PageList, 用于ViewPager
        mEmojiPageLists = convertPairToPageList(pairs, PAGE_SIZE);
    }

    /**
     * 将有序的PairList转换为无序的Map
     *
     * @param pairs 列表
     * @return 无序Map
     */
    private static Map<String, String> convertPairList2Map(
            final @NonNull ArrayList<Pair<String, String>> pairs) {
        Map<String, String> map = new HashMap<>(); // 快速查找
        for (int i = 0; i < pairs.size(); ++i) {
            map.put(pairs.get(i).first, pairs.get(i).second);
        }
        return map;
    }

    /**
     * 将有序的PairList转换为按页的List数组
     *
     * @param pairs     列表
     * @param page_size 每页数量
     * @return 按页的List数组
     */
    private List<List<EmojiIcon>> convertPairToPageList(
            final @NonNull ArrayList<Pair<String, String>> pairs,
            final int page_size) {
        List<List<EmojiIcon>> emojiPageLists = new ArrayList<>();

        // 保存于内存中的表情集合
        ArrayList<EmojiIcon> emojiIcons = new ArrayList<>();

        EmojiIcon emojiEntry;
        // 遍历列表, 放入列表
        for (Pair<String, String> entry : pairs) {
            emojiEntry = new EmojiIcon();
            emojiEntry.setUnicode(entry.first);
            emojiEntry.setUrl(entry.second);
            emojiIcons.add(emojiEntry);
        }

        // 每一个页数
        int pageCount = (int) Math.ceil(emojiIcons.size() / page_size + 0.1);
        for (int i = 0; i < pageCount; i++) {
            emojiPageLists.add(getListData(emojiIcons, i)); // 获取每页数据
        }

        return emojiPageLists;
    }

    private List<EmojiIcon> getListData(ArrayList<EmojiIcon> emojiIcons, int i) {
        return null;
    }

    /**
     * 获得SpannableString对象, 通过传入的字符串, 进行正则判断
     *
     * @param context 上下文
     * @param str     输入字符串
     * @return 组合字符串
     */
    public SpannableString getExpressionString(
            @NonNull final Context context,
            @NonNull final CharSequence str) {
        SpannableString spannableString = new SpannableString(str);
        // 正则表达式比配字符串里是否含有表情, 通过传入的正则表达式来生成Pattern
        // 注意Pattern的模式, 大小写不敏感, Unicode, 加快检索速度
        Pattern emojiPattern = Pattern.compile(EMOJI_REGEX,
                Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);
        try {
            dealExpression(context, spannableString, emojiPattern, 0);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
        return spannableString;
    }

    /**
     * 对SpannableString进行正则判断，如果符合要求，则以表情图片代替
     *
     * @param context   上下文
     * @param spannable 组合字符串
     * @param patten    模式
     * @param start     递归起始位置
     */
    private void dealExpression(
            @NonNull final Context context, SpannableString spannable,
            Pattern patten, final int start) {

        if (start < 0) {
            return;
        }

        // 将字符串与模式创建匹配
        Matcher matcher = patten.matcher(spannable);

        // 匹配成功
        while (matcher.find()) {
            String key = matcher.group().toLowerCase(); // 默认小写

            // 返回第一个字符的索引的文本匹配整个正则表达式, 如果是true则继续递归
            if (matcher.start() < start) {
                continue;
            }

            // 根据Key获取URL
            String url = mEmojiMap.get(key);

            // 通过上面匹配得到的字符串来生成图片资源id
            if (!TextUtils.isEmpty(url)) {
                // 计算该图片名字的长度，也就是要替换的字符串的长度
                int end = matcher.start() + key.length();
                spannable = addBitmap2Spannable(context, url, spannable, matcher.start(), end);
                if (end < spannable.length()) {
                    // 如果整个字符串还未验证完，则继续
                    dealExpression(context, spannable, patten, end);
                }
                break;
            }
        }
    }

    /**
     * 添加图片至Spannable
     *
     * @param context   上下文
     * @param url       图片网络连接
     * @param spannable 文字
     * @param start     起始修改
     * @param end       终止修改
     * @return 添加图片后的文字
     */
    private SpannableString addBitmap2Spannable(
            Context context, String url,
            SpannableString spannable, int start, int end) {
        // 当bitmap为空时, 无法替换内容
        Bitmap bitmap = null;
        try {
            bitmap = BitmapLruCache.getInstance(context).getBitmapFromCache(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        VerticalImageSpan imageSpan = new VerticalImageSpan(context, bitmap);
        spannable.setSpan(imageSpan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannable;
    }

    /**
     * 竖直居中的ImageSpan
     * <p>
     * Created by wangchenlong on 17/2/7.
     */
    public class VerticalImageSpan extends ImageSpan {
        private WeakReference<Drawable> mDrawableRef;

        private boolean DEBUG = false;
        private Context mContext;

        public VerticalImageSpan(Context context, Bitmap bitmap) {
            super(context, bitmap);
            mContext = context;
        }

        @Override
        public int getSize(Paint paint, CharSequence text,
                           int start, int end,
                           Paint.FontMetricsInt fm) {
            Drawable d = getCachedDrawable();
            Rect rect = d.getBounds();

            if (fm != null) {
                Paint.FontMetricsInt pfm = paint.getFontMetricsInt();
                // keep it the same as paint's fm
                fm.ascent = pfm.ascent;
                fm.descent = pfm.descent;
                fm.top = pfm.top;
                fm.bottom = pfm.bottom;
            }

            return rect.right;
        }

        @Override
        public void draw(@NonNull Canvas canvas, CharSequence text,
                         int start, int end, float x,
                         int top, int y, int bottom, @NonNull Paint paint) {
            Drawable b = getCachedDrawable();
            canvas.save();

            int drawableHeight = b.getIntrinsicHeight();
            int fontAscent = paint.getFontMetricsInt().ascent;
            int fontDescent = paint.getFontMetricsInt().descent;
            int offset = (bottom - top) - drawableHeight - (AppUtil.convertSpToPx(1) + 1);
            int transY = (bottom - offset) - b.getBounds().bottom +  // align bottom to bottom
                    (drawableHeight - fontDescent + fontAscent) / 2;  // align center to center

            canvas.translate(x, transY);
            b.draw(canvas);
            canvas.restore();
        }

        // Redefined locally because it is a private member from DynamicDrawableSpan
        private Drawable getCachedDrawable() {
            WeakReference<Drawable> wr = mDrawableRef;
            Drawable d = null;

            if (wr != null)
                d = wr.get();

            if (d == null) {
                d = getDrawable();
                mDrawableRef = new WeakReference<>(d);
            }

            return d;
        }
    }

    /**
     * 添加表情, 根据URL至BitmapDiskLruCache中匹配
     *
     * @param context 上下文
     * @param url     图片的网络URL
     * @param string  字符串
     * @return
     */
    public SpannableString addIcon(Context context, String url, String string) {
        SpannableString spannable = new SpannableString(string);
        return addBitmap2Spannable(context, url, spannable, 0, string.length());
    }
}
