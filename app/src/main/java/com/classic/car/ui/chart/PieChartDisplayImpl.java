package com.classic.car.ui.chart;

import android.graphics.Color;
import android.util.SparseArray;
import com.classic.car.R;
import com.classic.car.consts.Consts;
import com.classic.car.entity.ConsumerDetail;
import com.classic.car.utils.DataUtil;
import com.classic.car.utils.MoneyUtil;
import com.classic.car.utils.Util;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.ChartData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;
import java.util.ArrayList;
import java.util.List;

/**
 * 应用名称: CarAssistant
 * 包 名 称: com.classic.car.ui.chart
 *
 * 文件描述: TODO
 * 创 建 人: 续写经典
 * 创建时间: 2017/3/28 19:19
 */
public class PieChartDisplayImpl extends BaseChartDisplayImpl<PieChart,
        PieChartDisplayImpl.PieChartData>{

    @Override public void init(PieChart chart, boolean touchEnable) {
        super.init(chart, touchEnable);
        chart.setUsePercentValues(true);
        chart.setExtraOffsets(5, 10, 5, 5);
        // 环形图不绘制描述信息
        chart.setDrawEntryLabels(false);

        //pieChart.setCenterText(EMPTY_LABEL);
        chart.setDrawCenterText(false);

        chart.setHoleRadius(48f);
        chart.setTransparentCircleRadius(52f);
        chart.setHoleColor(Color.TRANSPARENT);

        // pieChart.setRotationAngle(0);
        // 通过触摸启用图表的旋转, 默认：true
        // pieChart.setRotationEnabled(true);
        // 突出显示, 默认：true
        // pieChart.setHighlightPerTapEnabled(true);
        // pieChart.setEntryLabelTextSize(8f);
        // pieChart.setEnabled(false);
        // pieChart.setEntryLabelColor(getColor(context, R.color.gray_dark));

        Legend l = chart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setTextSize(mTextSize);
        l.setTextColor(Util.getColor(mAppContext, R.color.gray_dark));
    }

    @Override public PieChartData convert(List<ConsumerDetail> list) {
        if (DataUtil.isEmpty(list)) { return null; }
        PieChartData pieChartData = new PieChartData();
        pieChartData.groupMoney = new SparseArray<>();
        final int size = list.size();
        for (int i = 0; i < size; i++) {
            float money = list.get(i).getMoney();
            pieChartData.totalMoney += money;
            int type = list.get(i).getType();
            pieChartData.groupMoney.put(type, pieChartData.groupMoney.get(type, 0F) + money);
        }
        ArrayList<PieEntry> pieEntries = new ArrayList<>();
        final int groupSize = pieChartData.groupMoney.size();
        for (int i = 0; i < groupSize; i++) {
            pieEntries.add(new PieEntry(pieChartData.totalMoney == 0F ? 0F :
                                                MoneyUtil.newInstance(pieChartData.groupMoney.valueAt(i))
                                                         .divide(pieChartData.totalMoney, 4)
                                                         .create()
                                                         .floatValue(),
                                        Consts.TYPE_MENUS[pieChartData.groupMoney.keyAt(i)]));
        }
        PieDataSet dataSet = new PieDataSet(pieEntries, EMPTY_LABEL);
        // 环形之间的间隔
        dataSet.setSliceSpace(2f);
        dataSet.setSelectionShift(8f);
        dataSet.setColors(Util.getColorTemplate(mAppContext));

        PieData data = new PieData(dataSet);
        data.setValueFormatter(PERCENTAGE_FORMATTER);
        data.setValueTextSize(mTextSize);
        data.setValueTextColor(Color.WHITE);
        pieChartData.pieData = data;
        return pieChartData;
    }

    @Override ChartData getChartData(PieChartData pieChartData) {
        return null == pieChartData ? null : pieChartData.pieData;
    }

    public static class PieChartData {
        public float              totalMoney;
        public PieData            pieData;
        public SparseArray<Float> groupMoney;
    }

    /** 百分比格式 */
    private static final IValueFormatter PERCENTAGE_FORMATTER = new IValueFormatter() {
        @Override public String getFormattedValue(float value, Entry entry, int dataSetIndex,
                                                  ViewPortHandler viewPortHandler) {
            return Util.formatPercentage(value);
        }
    };
}
