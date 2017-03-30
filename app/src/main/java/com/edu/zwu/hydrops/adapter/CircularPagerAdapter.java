package com.edu.zwu.hydrops.adapter;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.edu.zwu.hydrops.R;
import com.edu.zwu.hydrops.entity.Banner;
import com.edu.zwu.hydrops.entity.BaseBean;
import com.edu.zwu.hydrops.entity.ImgSize;
import com.edu.zwu.hydrops.util.AppUtil;
import com.edu.zwu.hydrops.view.IndicatorView;

import java.util.ArrayList;
import java.util.List;

public class CircularPagerAdapter extends PagerAdapter {

    private int fakeSize;
    private List<Banner> bannerList;
    private SparseArray<View> mViewSparseArray;
    private IndicatorView mIndicatorView;
    private Activity mActivity;

    private LayoutInflater mLayoutInflater;

    public CircularPagerAdapter(final ViewPager pager, Activity mActivity, final IndicatorView indicatorView) {
        this.mActivity = mActivity;
        mLayoutInflater = LayoutInflater.from(mActivity);
        bannerList = new ArrayList<>();
        mViewSparseArray = new SparseArray<>();
        this.mIndicatorView = indicatorView;
        if (indicatorView != null) {
            indicatorView.setViewPagerInfo(pager, true);
        }
        pager.setOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                if (indicatorView != null) {
                    int realCount = fakeSize - 2;
                    int realPosition = (position - 1) % realCount;
                    if (realPosition < 0) {
                        realPosition += realCount;
                    }
                    indicatorView.selectPage(realPosition);
                }
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == ViewPager.SCROLL_STATE_IDLE) {
                    int pageCount = getCount();
                    int currentItem = pager.getCurrentItem();
                    if (currentItem == 0) {
                        pager.setCurrentItem(pageCount - 2, false);
                    } else if (currentItem == pageCount - 1) {
                        pager.setCurrentItem(1, false);
                    }
                }
            }
        });
    }

    public List<Banner> getData() {
        return bannerList;
    }

    public void setData(List<Banner> list) {
        mViewSparseArray.clear();
        bannerList = list;
        int realSize = list.size();  // 真实的Banner数量
        fakeSize = realSize + 2;

        if (mIndicatorView != null) {
            mIndicatorView.refreshView();
        }
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return fakeSize == 3 ? 1 : fakeSize;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        int realCount = fakeSize - 2;

        int realPosition = (position - 1) % realCount;
        if (realPosition < 0) {
            realPosition += realCount;
        }
        final int pos = realPosition;

        View view;
        if (mViewSparseArray.indexOfKey(position) >= 0) {
            view = mViewSparseArray.get(position);
        } else {
            final Banner banner = bannerList.get(pos);
            view = mLayoutInflater.inflate(R.layout.banner_image_item, container, false);
            if (banner != null) {
                ImageView imageView = (ImageView) view.findViewById(R.id.network_image_item_img);
                Glide.with(container.getContext()).load(BaseBean.filterImagePath(banner.photoUrl, ImgSize.Original)).error(R.drawable.default_img_small).placeholder(R.drawable.default_img_small).into(imageView);
                imageView.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        String webUrl = banner.activityUrl;
                        if (!TextUtils.isEmpty(webUrl)) {
                            AppUtil.processAction(mActivity, webUrl);
                        }
                    }
                });
                mViewSparseArray.put(position, view);
            }
        }

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(mViewSparseArray.get(position));
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    /**
     * 每次notifyDataSetChanged后调用
     * see http://stackoverflow.com/questions/7263291/viewpager-pageradapter-not-updating-the-view
     * http://stackoverflow.com/questions/10849552/android-viewpager-cant-update-dynamically
     *
     * @param object
     * @return
     */
    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}
