package com.classic.car.utils;

import android.content.Context;
import android.graphics.Color;

import com.classic.car.R;
import com.classic.car.consts.Consts;
import com.classic.car.entity.ConsumerDetail;
import com.classic.car.entity.FuelConsumption;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 应用名称: CarAssistant
 * 包 名 称: com.classic.car.utils
 *
 * 文件描述：图表初始化
 * 创 建 人：续写经典
 * 创建时间：16/7/4 下午8:05
 */
public final class ChartUtil {
    /** 超过这个值，不显示value */
    private static final int    MAX_VISIBLE_VALUE_COUNT = 40;
    /** 图表显示的最低值 */
    private static final int    MINIMUM_VALUE           = 0;
    private static final int    TEXT_SIZE               = 8;
    private static final String EMPTY_LABEL             = "";

    /** 油耗曲线数据显示 */
    private static final IValueFormatter OIL_MESS_FORMATTER = new IValueFormatter() {
        @Override public String getFormattedValue(float value, Entry entry, int dataSetIndex,
                                                  ViewPortHandler viewPortHandler) {
            return MoneyUtil.replace(MoneyUtil.newInstance(value).round(2).create());
        }
    };

    /** 百分比格式 */
    private static final IValueFormatter PERCENTAGE_FORMATTER = new IValueFormatter() {
        @Override public String getFormattedValue(float value, Entry entry, int dataSetIndex,
                                                  ViewPortHandler viewPortHandler) {
            return Util.formatPercentage(value);
        }
    };

    private static String getString(Context context, int resId){
        return context.getResources().getString(resId);
    }

    private static int getColor(Context context, int resId){
        //noinspection deprecation
        return context.getResources().getColor(resId);
    }

    private static int[] sColorTemplate;
    private static int[] getColorTemplate(Context context){
        if(null == sColorTemplate){
            sColorTemplate = new int[]{
                    getColor(context,R.color.blue_light),
                    getColor(context,R.color.pale_red),
                    getColor(context,R.color.chartreuse_light),
                    getColor(context,R.color.saffron_light),
                    getColor(context,R.color.mediumorchid_light),
                    getColor(context,R.color.green_light),
                    getColor(context,R.color.orange_light),
                    getColor(context,R.color.sienna_light),
                    getColor(context,R.color.purple_light),
                    getColor(context,R.color.pink_light)
            };
        }
        return sColorTemplate;
    }

    public static void initBarChart(Context context, BarChart barChart) {
        YAxis leftAxis = barChart.getAxisLeft();
        leftAxis.setAxisMinimum(MINIMUM_VALUE);
        // 网格线以虚线模式绘制
        leftAxis.enableGridDashedLine(10f, 10f, 0f);
        // leftAxis.setDrawZeroLine(true);

        barChart.setNoDataText(getString(context, R.string.no_data_hint));
        barChart.getAxisRight().setEnabled(false);

        barChart.setDrawBarShadow(false);
        barChart.setDrawValueAboveBar(true);
        //打开或者关闭与图表的所有触摸交互
        // barChart.setTouchEnabled(false);
        //图表拖动
        // barChart.setDragEnabled(true);
        // //打开或关闭对图表所有轴的的缩放
        // barChart.setScaleEnabled(true);
        // barChart.setPinchZoom(true);
        // barChart.setDoubleTapToZoomEnabled(false);
        // barChart.setHighlightFullBarEnabled(false);

        barChart.setDescription(createDescription(getString(context, R.string.chart_unit_money),
                                                  getColor(context, R.color.gray_dark)));

        //超过这个值,不显示value
        barChart.setMaxVisibleValueCount(MAX_VISIBLE_VALUE_COUNT);
        barChart.setDrawGridBackground(false);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        // xAxis.setEnabled(true);

        barChart.getLegend().setEnabled(false);
    }

    public static BarData convertBarData(Context context, List<ConsumerDetail> list){
        if(DataUtil.isEmpty(list)){
            return null;
        }
        ArrayList<BarEntry> entries = new ArrayList<>();
        final int size = list.size();
        for (int i = 0; i < size; i++) {
            entries.add(new BarEntry(i, list.get(i).getMoney()));
        }
        BarDataSet ds = new BarDataSet(entries, EMPTY_LABEL);
        ds.setColors(getColorTemplate(context));
        return new BarData(ds);
    }


    public static void initPieChart(Context context, PieChart pieChart) {
        pieChart.setNoDataText(getString(context, R.string.no_data_hint));
        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);
        pieChart.setExtraOffsets(5, 10, 5, 5);
        // 环形图不绘制描述信息
        pieChart.setDrawEntryLabels(false);

        //pieChart.setCenterText(EMPTY_LABEL);
        pieChart.setDrawCenterText(false);

        pieChart.setHoleRadius(48f);
        pieChart.setTransparentCircleRadius(52f);
        pieChart.setHoleColor(Color.TRANSPARENT);

        // pieChart.setRotationAngle(0);
        // 通过触摸启用图表的旋转, 默认：true
        // pieChart.setRotationEnabled(true);
        // 突出显示, 默认：true
        // pieChart.setHighlightPerTapEnabled(true);
        // pieChart.setEntryLabelTextSize(8f);
        // pieChart.setEnabled(false);
        // pieChart.setEntryLabelColor(getColor(context, R.color.gray_dark));

        // pieChart.getLegend().setEnabled(false);
        Legend l = pieChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        // l.setXEntrySpace(4f);
        // l.setYEntrySpace(0f);
        // l.setYOffset(0f);
        // l.setTextColor(Color.WHITE);
        l.setTextSize(TEXT_SIZE);
        l.setTextColor(getColor(context, R.color.gray_dark));
    }

    public static PieData convertPieData(Context context, float totalMoney, Map<Integer, Float> map){
        if(DataUtil.isEmpty(map)){
            return null;
        }
        ArrayList<PieEntry> pieEntries = new ArrayList<>();
        for (Integer key : map.keySet()) {
            pieEntries.add(new PieEntry(totalMoney == 0F ? 0 : MoneyUtil.newInstance(map.get(key))
                                                                        .divide(totalMoney, 4)
                                                                        .create()
                                                                        .floatValue(),
                                        Consts.TYPE_MENUS[key]));
        }
        PieDataSet dataSet = new PieDataSet(pieEntries, EMPTY_LABEL);
        // 环形之间的间隔
        dataSet.setSliceSpace(2f);
        dataSet.setSelectionShift(8f);
        dataSet.setColors(getColorTemplate(context));

        PieData data = new PieData(dataSet);
        data.setValueFormatter(PERCENTAGE_FORMATTER);
        data.setValueTextSize(TEXT_SIZE);
        data.setValueTextColor(Color.WHITE);
        return data;
    }


    public static void initLineChart(Context context, LineChart lineChart) {
        lineChart.setNoDataText(getString(context, R.string.no_data_hint));
        // enable touch gestures
        // lineChart.setTouchEnabled(false);
        lineChart.getDescription().setEnabled(false);
        //超过这个值，不显示value
        lineChart.setMaxVisibleValueCount(MAX_VISIBLE_VALUE_COUNT);

        // enable scaling and dragging
        // lineChart.setDragEnabled(true);
        // lineChart.setScaleEnabled(true);
        lineChart.setDrawGridBackground(false);
        // lineChart.setHighlightPerDragEnabled(true);
        // if disabled, scaling can be done on x- and y-axis separately
        lineChart.setPinchZoom(true);

        lineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        lineChart.getXAxis().setDrawGridLines(false);
        lineChart.getXAxis().setAvoidFirstLastClipping(true);

        YAxis leftAxis = lineChart.getAxisLeft();
        leftAxis.removeAllLimitLines(); // reset all limit lines to avoid overlapping lines
        leftAxis.setAxisMinimum(MINIMUM_VALUE);
        leftAxis.enableGridDashedLine(10f, 10f, 0f);
        // leftAxis.setDrawZeroLine(false);

        lineChart.getAxisRight().setEnabled(false);

        lineChart.getLegend().setForm(Legend.LegendForm.LINE);
    }

    public static LineData convertLineData(Context context, List<FuelConsumption> list){
        if(DataUtil.isEmpty(list)){
            return null;
        }

        ArrayList<Entry> moneyValues = new ArrayList<>();
        ArrayList<Entry> oilMessValues = new ArrayList<>();
        final int size = list.size();
        for (int i = 0; i < size; i++) {
            moneyValues.add(new Entry(i, list.get(i).getMoney()));
            oilMessValues.add(new Entry(i, list.get(i).getOilMass()));
        }

        LineDataSet moneySet = new LineDataSet(moneyValues, getString(context, R.string.chart_fuel_consumption_money));
        //moneySet.enableDashedLine(10f, 5f, 0f); //启用虚线
        //moneySet.enableDashedHighlightLine(10f, 5f, 0f); //启用高亮虚线
        moneySet.setAxisDependency(YAxis.AxisDependency.LEFT);
        moneySet.setColor(getColor(context, R.color.colorAccent));
        moneySet.setCircleColor(getColor(context, R.color.colorAccent));
        moneySet.setValueTextSize(TEXT_SIZE);
        moneySet.setValueFormatter(OIL_MESS_FORMATTER);
        //moneySet.setCircleRadius(3f); //圆点半径
        //moneySet.setDrawCircleHole(false); //圆点是否空心
        //moneySet.setHighlightEnabled(true); //选中高亮
        // moneySet.setValueFormatter(new OilMessFormatter());

        LineDataSet oilMessSet = new LineDataSet(oilMessValues, getString(context, R.string.chart_fuel_consumption));
        oilMessSet.setAxisDependency(YAxis.AxisDependency.LEFT);
        oilMessSet.setColor(getColor(context, R.color.blue));
        oilMessSet.setCircleColor(getColor(context, R.color.blue));
        oilMessSet.setValueTextSize(TEXT_SIZE);
        oilMessSet.setValueFormatter(OIL_MESS_FORMATTER);
        return new LineData(moneySet, oilMessSet);
    }

    private static Description createDescription(String text, int colorId) {
        Description description = new Description();
        description.setText(text);
        description.setTextColor(colorId);
        return description;
    }
}
