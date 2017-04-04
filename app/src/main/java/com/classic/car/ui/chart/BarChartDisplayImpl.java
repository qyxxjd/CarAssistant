package com.classic.car.ui.chart;

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
import com.github.mikephil.charting.data.ChartData;
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
public class BarChartDisplayImpl extends BaseChartDisplayImpl<BarChart, BarData>{

    @Override public void init(BarChart chart, boolean touchEnable) {
        super.init(chart, touchEnable);
        if (touchEnable) {
            //图表拖动
            chart.setDragEnabled(true);
            //打开或关闭对图表所有轴的的缩放
            chart.setScaleEnabled(true);
            chart.setPinchZoom(true);
            chart.setDoubleTapToZoomEnabled(false);
            chart.setHighlightFullBarEnabled(false);
        }
        //超过这个值,不显示value
        chart.setMaxVisibleValueCount(MAX_VISIBLE_VALUE_COUNT);
        chart.setDrawGridBackground(false);

        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setAxisMinimum(MINIMUM_VALUE);
        // 网格线以虚线模式绘制
        leftAxis.enableGridDashedLine(10f, 10f, 0f);
        leftAxis.setTextSize(mAxisSize);
        leftAxis.setTextColor(Util.getColor(mAppContext, R.color.gray_dark));
        chart.getAxisRight().setEnabled(false);

        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setTextSize(mAxisSize);
        xAxis.setTextColor(Util.getColor(mAppContext, R.color.gray_dark));

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
            ds.setValueTextSize(mTextSize);
            ds.setColors(Util.getColorTemplate(mAppContext));
            return new BarData(ds);
        }
        return null;
    }

    @Override ChartData getChartData(BarData barData) {
        return barData;
    }
}
