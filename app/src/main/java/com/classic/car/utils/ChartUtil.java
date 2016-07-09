package com.classic.car.utils;

import android.content.Context;
import android.graphics.Color;
import com.classic.car.R;
import com.classic.car.consts.Consts;
import com.classic.car.entity.ConsumerDetail;
import com.classic.car.entity.FuelConsumption;
import com.classic.core.utils.DataUtil;
import com.classic.core.utils.MoneyUtil;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
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
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
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

    private static String getString(Context context, int resId){
        return context.getResources().getString(resId);
    }
    private static int getColor(Context context, int resId){
        return context.getResources().getColor(resId);
    }

    private static int[] sColorTemplate;
    public static int[] getColorTemplate(Context context){
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

    public static void initLineChart(Context context, LineChart lineChart) {
        lineChart.setNoDataText(getString(context, R.string.no_data_hint));
        lineChart.setDescription("");
        lineChart.setDescriptionColor(getColor(context, R.color.secondary_text));
        // enable touch gestures
        lineChart.setTouchEnabled(true);

        lineChart.setDragDecelerationFrictionCoef(0.9f);

        // enable scaling and dragging
        lineChart.setDragEnabled(true);
        lineChart.setScaleEnabled(true);
        lineChart.setDrawGridBackground(false);
        //lineChart.setHighlightPerDragEnabled(true);
        // if disabled, scaling can be done on x- and y-axis separately
        lineChart.setPinchZoom(true);

        lineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        lineChart.getXAxis().setDrawGridLines(false);
        //lineChart.getXAxis().setAvoidFirstLastClipping(true);

        YAxis leftAxis = lineChart.getAxisLeft();
        leftAxis.removeAllLimitLines(); // reset all limit lines to avoid overlapping lines
        leftAxis.setAxisMinValue(0f);
        leftAxis.enableGridDashedLine(10f, 10f, 0f);
        leftAxis.setDrawZeroLine(false);

        // limit lines are drawn behind data (and not on top)
        //leftAxis.setDrawLimitLinesBehindData(true);
        lineChart.getAxisRight().setEnabled(false);

        lineChart.getLegend().setForm(Legend.LegendForm.LINE);
    }

    public static void initBarChart(Context context, BarChart barChart) {
        YAxis leftAxis = barChart.getAxisLeft();
        leftAxis.setAxisMinValue(0f);
        leftAxis.enableGridDashedLine(10f, 10f, 0f);
        leftAxis.setDrawZeroLine(false);

        barChart.setNoDataText(getString(context, R.string.no_data_hint));
        barChart.getAxisRight().setEnabled(false);

        barChart.setDrawBarShadow(false);
        barChart.setDrawValueAboveBar(true);
        //barChart.setTouchEnabled(false);
        //barChart.setDragEnabled(true);
        //barChart.setScaleEnabled(true);

        barChart.setDescription("单位:元");
        barChart.setDescriptionColor(getColor(context, R.color.gray_dark));

        //超过这个值,不显示value
        barChart.setMaxVisibleValueCount(60);
        barChart.setDrawGridBackground(false);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setEnabled(false);
    }


    public static void initPieChart(Context context, PieChart pieChart) {
        pieChart.setNoDataText(getString(context, R.string.no_data_hint));
        pieChart.setUsePercentValues(true);
        pieChart.setDescription("");
        pieChart.setExtraOffsets(5, 10, 5, 5);

        pieChart.setDragDecelerationFrictionCoef(0.95f);
        //pieChart.setCenterText("");
        pieChart.setDrawCenterText(false);
        //pieChart.setDrawHoleEnabled(true);
        //pieChart.setHoleColor(Color.TRANSPARENT);

        //pieChart.setTransparentCircleColor(Color.WHITE);
        //pieChart.setTransparentCircleAlpha(110);

        pieChart.setHoleRadius(48f);
        pieChart.setTransparentCircleRadius(52f);
        pieChart.setHoleColor(Color.TRANSPARENT);

        pieChart.setRotationAngle(0);
        // enable rotation of the chart by touch
        pieChart.setRotationEnabled(true);
        pieChart.setHighlightPerTapEnabled(true);

        Legend l = pieChart.getLegend();
        l.setPosition(Legend.LegendPosition.RIGHT_OF_CHART);
        l.setXEntrySpace(4f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);
    }

    public static LineData convertLineData(Context context, List<FuelConsumption> list){
        if(DataUtil.isEmpty(list)){
            return null;
        }

        ArrayList<String> xVals = new ArrayList<>();
        ArrayList<Entry> moneyValues = new ArrayList<>();
        ArrayList<Entry> oilMessValues = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            xVals.add("");
            moneyValues.add(new Entry(list.get(i).getMoney(), i));
            oilMessValues.add(new Entry(list.get(i).getOilMass(), i));
        }

        LineDataSet moneySet = new LineDataSet(moneyValues, "百公里消费金额曲线(元)");
        //moneySet.enableDashedLine(10f, 5f, 0f); //启用虚线
        //moneySet.enableDashedHighlightLine(10f, 5f, 0f); //启用高亮虚线
        moneySet.setColor(getColor(context, R.color.colorAccent));
        moneySet.setCircleColor(getColor(context, R.color.colorAccent));
        moneySet.setLineWidth(1f);
        //moneySet.setCircleRadius(3f); //圆点半径
        //moneySet.setDrawCircleHole(false); //圆点是否空心
        moneySet.setValueTextSize(8f);
        //moneySet.setHighlightEnabled(true); //选中高亮

        LineDataSet oilMessSet = new LineDataSet(oilMessValues, "百公里耗油曲线(升)");
        oilMessSet.setColor(getColor(context, R.color.blue));
        oilMessSet.setCircleColor(getColor(context, R.color.blue));
        oilMessSet.setValueTextSize(8f);

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(moneySet);
        dataSets.add(oilMessSet);

        return new LineData(xVals, dataSets);
    }
    public static BarData convertBarData(Context context, List<ConsumerDetail> list){
        if(DataUtil.isEmpty(list)){
            return null;
        }
        ArrayList<IBarDataSet> sets = new ArrayList<>();
        ArrayList<BarEntry> entries = new ArrayList<>();
        ArrayList<String> xVals = new ArrayList<>();
        BarDataSet ds = new BarDataSet(entries, "");
        ds.setColors(getColorTemplate(context));
        for (int i = 0; i < list.size(); i++) {
            xVals.add("");
            entries.add(new BarEntry(list.get(i).getMoney(), i));
        }
        sets.add(ds);
        return new BarData(xVals, sets);
    }
    public static PieData convertPieData(Context context, float totalMoney, Map<Integer, Float> map){
        if(DataUtil.isEmpty(map)){
            return null;
        }
        ArrayList<String> xVals = new ArrayList<>();
        ArrayList<Entry> yVals1 = new ArrayList<>();

        PieDataSet dataSet = new PieDataSet(yVals1, "");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);
        dataSet.setColors(getColorTemplate(context));
        int i = 0;
        for (Integer key : map.keySet()) {
            yVals1.add(new Entry(
                    MoneyUtil.newInstance(map.get(key)).divide(totalMoney).create().floatValue(), i));
            xVals.add(Consts.TYPE_MENUS[key]);
            i++;
        }

        PieData data = new PieData(xVals, dataSet);
        data.setValueFormatter(new PercentageFormatter());
        data.setValueTextSize(8f);
        data.setValueTextColor(Color.WHITE);
        return data;
    }
}
