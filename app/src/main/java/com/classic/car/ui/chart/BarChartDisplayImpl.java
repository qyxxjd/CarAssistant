package com.classic.car.ui.chart;

import android.content.Context;

import com.classic.car.R;
import com.classic.car.entity.ConsumerDetail;
import com.classic.car.utils.DataUtil;
import com.classic.car.utils.Util;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;
import java.util.List;

/**
 * 应用名称: CarAssistant
 * 包 名 称: com.classic.car.ui.chart
 *
 * 文件描述: TODO
 * 创 建 人: 续写经典
 * 创建时间: 2017/3/28 18:35
 */
public class BarChartDisplayImpl implements IChartDisplay<BarChart, BarData, ConsumerDetail>{

    private Context mAppContext;

    @Override public void init(BarChart chart, boolean touchEnable) {
        if (null == chart) { return; }
        mAppContext = chart.getContext().getApplicationContext();
        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setAxisMinimum(MINIMUM_VALUE);
        // 网格线以虚线模式绘制
        leftAxis.enableGridDashedLine(10f, 10f, 0f);
        // leftAxis.setDrawZeroLine(true);

        chart.setNoDataText(Util.getString(mAppContext, R.string.no_data_hint));
        chart.getAxisRight().setEnabled(false);

        chart.setDrawBarShadow(false);
        chart.setDrawValueAboveBar(true);
        //打开或者关闭与图表的所有触摸交互
        chart.setTouchEnabled(touchEnable);
        if (touchEnable) {
            //图表拖动
            chart.setDragEnabled(true);
            //打开或关闭对图表所有轴的的缩放
            chart.setScaleEnabled(true);
            chart.setPinchZoom(true);
            chart.setDoubleTapToZoomEnabled(false);
            chart.setHighlightFullBarEnabled(false);
        }

        chart.getDescription().setEnabled(false);
        // chart.setDescription(createDescription(Util.getString(mAppContext, R.string.chart_unit_money),
        //                                           Util.getColor(mAppContext, R.color.gray_dark)));


        //超过这个值,不显示value
        chart.setMaxVisibleValueCount(MAX_VISIBLE_VALUE_COUNT);
        chart.setDrawGridBackground(false);

        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        // xAxis.setEnabled(true);

        chart.getLegend().setEnabled(false);
    }

    @Override public BarData convert(List<ConsumerDetail> list) {
        if (!DataUtil.isEmpty(list)) {
            ArrayList<BarEntry> entries = new ArrayList<>();
            final int size = list.size();
            for (int i = 0; i < size; i++) {
                entries.add(new BarEntry(i, list.get(i).getMoney()));
            }
            BarDataSet ds = new BarDataSet(entries, EMPTY_LABEL);
            ds.setColors(Util.getColorTemplate(mAppContext));
            return new BarData(ds);
        }
        return null;
    }

    @Override public void display(BarChart chart, BarData barData) {
        animationDisplay(chart, barData, 0);
    }

    @Override public void animationDisplay(BarChart chart, BarData barData, int duration) {
        if (null == chart || null == barData) {
            return;
        }
        chart.setData(barData);
        if (duration > 0) {
            chart.animateXY(duration, duration);
        }
    }

    // private Description createDescription(String text, int colorId) {
    //     Description description = new Description();
    //     description.setText(text);
    //     description.setTextColor(colorId);
    //     return description;
    // }
}
