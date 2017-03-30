package com.edu.zwu.hydrops.presenter;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;

import com.baidu.mapapi.model.LatLng;
import com.edu.zwu.hydrops.R;
import com.edu.zwu.hydrops.base.BasePresenter;
import com.edu.zwu.hydrops.base.BaseView;
import com.edu.zwu.hydrops.bmob.MarkerData;
import com.edu.zwu.hydrops.entity.Banner;
import com.edu.zwu.hydrops.util.AppUtil;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;

/**
 * Created by shengwei.yi on 2016/5/11.
 */
public class WaterDataPresenter extends BasePresenter<WaterDataPresenter.WaterDataView>{

    public LineChartData mChartData;

    private String[] mTime = {"00:00", "01:00", "02:00", "03:00", "04:00", "05:00", "06:00", "07:00",
            "08:00", "09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00",
            "18:00", "19:00", "20:00", "21:00", "22:00", "23:00", "24:00"};


    public WaterDataPresenter(WaterDataView view, Context context) {
        super(view, context);
    }

    public void getMarkerData(LatLng ll){
        BmobQuery<MarkerData> bmobQuery = new BmobQuery<>();
        bmobQuery.addWhereEqualTo("latlng", ll.latitude + "," + ll.longitude);
        bmobQuery.findObjects(new FindListener<MarkerData>() {
            @Override
            public void done(List<MarkerData> list, BmobException e) {
                if(e == null){
                    MarkerData markerData = list.get(0);
                    mView.initActionBar(markerData.address);
                    mView.setBanner(markerData.imgUrl);
                    mView.setChartView(markerData.depthList);
                    SpannableString depthSpan = setTextSpan("最高积水深度\n" + markerData.depth, 25, 6, 6 + markerData.depth.length(), mContext.getResources().getColor(R.color.water_data_blue_text));
                    SpannableString acrSpan = setTextSpan("积水面积\n" + markerData.area, 25, 4, 4 + markerData.area.length(), mContext.getResources().getColor(R.color.water_data_blue_text));
                    SpannableString expectSpan = setTextSpan("预计积水深度\n" + markerData.expectDepth, 25, 6, 6 + markerData.expectDepth.length(), mContext.getResources().getColor(R.color.water_data_blue_text));
                    mView.setInfoText(depthSpan, acrSpan, expectSpan);
                } else {
                    AppUtil.showShortMessage(mContext, e.toString());
                }
            }
        });
    }

    public void initData(List<String> imgUrlList) {
        List<Banner> list = new ArrayList<>();
        for (String str : imgUrlList) {
            Banner banner = new Banner();
            banner.photoUrl = str;
            list.add(banner);
        }
        mView.setBannerData(list);
    }

    public void setChartData(List<Float> dataList) {
        List<Line> lines = new ArrayList<>();
        List<PointValue> values = new ArrayList<>();
        List<AxisValue> axisValues = new ArrayList<>();
        List<AxisValue> axisValuesY = new ArrayList<>();

        int maxValue = 0;

        for (int i = 0; i < dataList.size(); i++) {
            values.add(new PointValue(i, dataList.get(i)));
            axisValues.add(new AxisValue(i).setLabel(mTime[i]));
            maxValue = Math.max(maxValue, dataList.get(i).intValue());
        }
        String unit = "cm";
        int size = String.valueOf(maxValue).length();

        for (int i = 0; i < 11; i++) {
            axisValuesY.add(new AxisValue(maxValue / 10 * i).setLabel(String.format("%1$.1f", maxValue / 10 * i / Math.pow(10, size - 2))));
        }

        Line line = new Line(values);
        line.setColor(0xff1fabe1);
        line.setColor(0xff1fabe1);
        line.setHasLines(true);
        line.setHasLabels(true);
        line.setHasLabelsOnlyForSelected(true);
        lines.add(line);
        mChartData = new LineChartData(lines);
        mChartData.setAxisXBottom(new Axis(axisValues).setHasLines(true));
        mChartData.setAxisYLeft(new Axis(axisValuesY).setHasLines(true).setName(unit));
    }

    /**
     * 设置字体大小
     *
     * @param str
     * @param fontSize
     * @param start
     * @param end
     * @param color
     */
    private SpannableString setTextSpan(String str, int fontSize, int start, int end, int color) {

        SpannableString span = new SpannableString(str);

        span.setSpan(new AbsoluteSizeSpan(fontSize, true), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        span.setSpan(new ForegroundColorSpan(color), start, end, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);

        return span;

    }

    public interface WaterDataView extends BaseView{
        void initActionBar(String address);

        void setBanner(List<String> imgUrl);

        void setChartView(List<Float> depthList);

        void setInfoText(SpannableString depthSpan, SpannableString acrSpan, SpannableString expectSpan);

        void setBannerData(List<Banner> list);
    }
}
