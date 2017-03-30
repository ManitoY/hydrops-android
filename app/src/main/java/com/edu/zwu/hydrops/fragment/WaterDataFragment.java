package com.edu.zwu.hydrops.fragment;

import android.content.Context;
import android.os.Bundle;
import android.text.SpannableString;

import com.baidu.mapapi.model.LatLng;
import com.edu.zwu.hydrops.R;
import com.edu.zwu.hydrops.activity.WaterDataActivity;
import com.edu.zwu.hydrops.adapter.CircularPagerAdapter;
import com.edu.zwu.hydrops.base.BaseFragment;
import com.edu.zwu.hydrops.entity.Banner;
import com.edu.zwu.hydrops.presenter.WaterDataPresenter;
import com.edu.zwu.hydrops.view.BannerViewPager;
import com.edu.zwu.hydrops.view.FontTextView;
import com.edu.zwu.hydrops.view.IndicatorView;

import java.util.List;

import butterknife.Bind;
import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;

/**
 * Created by shengwei.yi on 2016/5/11.
 */
public class WaterDataFragment extends BaseFragment<WaterDataPresenter> implements WaterDataPresenter.WaterDataView {

    @Bind(R.id.water_data_image_viewpager)
    BannerViewPager mBannerViewPager;
    @Bind(R.id.water_data_image_banner_indicator)
    IndicatorView mIndicatorView;
    @Bind(R.id.water_data_depth)
    FontTextView mDepthText;
    @Bind(R.id.water_data_acreage)
    FontTextView mAcreageText;
    @Bind(R.id.water_data_expect)
    FontTextView mExpectText;
    @Bind(R.id.chart)
    LineChartView mChartView;

    private CircularPagerAdapter mBannerAdapter;

    private FragmentCallBack mFragmentCallBack;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_water_data;
    }

    @Override
    protected void afterViews(Bundle savedInstanceState) {
        mPresenter = new WaterDataPresenter(this, this.getContext());
        mPresenter.getMarkerData(new LatLng(thisActivity.getIntent().getDoubleExtra("latitude", 0), thisActivity.getIntent().getDoubleExtra("longitude", 0)));
    }

    @Override
    public void initActionBar(String address) {
        mFragmentCallBack.setAcitonBar(address);
    }

    @Override
    public void setBanner(List<String> imgUrlList) {
        initAdapter();
        mPresenter.initData(imgUrlList);
    }

    @Override
    public void setChartView(List<Float> dataList) {
        mChartView.setZoomType(ZoomType.HORIZONTAL);
        mChartView.setMaxZoom(4);
        mChartView.setValueSelectionEnabled(true);

        mPresenter.setChartData(dataList);

        mChartView.setLineChartData(mPresenter.mChartData);

        Viewport v = new Viewport(mChartView.getMaximumViewport());
        v.right = dataList.size();
        v.left = dataList.size() - 7;
        v.bottom = 0;
        mChartView.setCurrentViewportWithAnimation(v);
    }

    @Override
    public void setInfoText(SpannableString depthSpan, SpannableString acrSpan, SpannableString expectSpan) {
        mDepthText.setText(depthSpan);
        mAcreageText.setText(acrSpan);
        mExpectText.setText(expectSpan);
    }

    @Override
    public void setBannerData(List<Banner> bannerList) {
        mBannerAdapter.setData(bannerList);
        mBannerViewPager.stopLoopingBanner();
        mBannerViewPager.setOffscreenPageLimit(3);
        mBannerViewPager.setCurrentItem(1, false);
        mBannerViewPager.startLoopingBanner();
    }

    private void initAdapter() {
        mBannerAdapter = new CircularPagerAdapter(mBannerViewPager, thisActivity, mIndicatorView);
        mBannerViewPager.setAdapter(mBannerAdapter);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mFragmentCallBack = (WaterDataActivity) context;
    }

    public interface FragmentCallBack {
        void setAcitonBar(String address);
    }

}
