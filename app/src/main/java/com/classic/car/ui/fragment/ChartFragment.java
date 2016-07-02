package com.classic.car.ui.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.classic.car.R;
import com.classic.car.app.CarApplication;
import com.classic.car.ui.test.LineChartActivity1;
import com.classic.car.ui.test.LineChartActivity2;
import com.classic.car.ui.test.PieChartActivity;
import com.classic.core.fragment.BaseFragment;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import java.util.ArrayList;

/**
 * 应用名称: CarAssistant
 * 包 名 称: com.classic.car.ui.fragment
 *
 * 文件描述：TODO
 * 创 建 人：续写经典
 * 创建时间：16/5/29 下午2:21
 */
public class ChartFragment extends BaseFragment {
    private static final int ANIMATE_DURATION = 2000;
    @BindView(R.id.chart_fuel_linechart)      LineChart mFuelLinechart;
    @BindView(R.id.chart_consumer_linechart)  LineChart mConsumerLinechart;
    @BindView(R.id.chart_percentage_piechart) PieChart  mPercentagePiechart;

    private int mColor;
    public static ChartFragment newInstance() {
        return new ChartFragment();
    }

    @Override public int getLayoutResId() {
        return R.layout.fragment_chart;
    }

    @Override public void initView(View parentView, Bundle savedInstanceState) {
        ((CarApplication) activity.getApplicationContext()).getAppComponent().inject(this);
        super.initView(parentView, savedInstanceState);
        ButterKnife.bind(this, parentView);

        mColor = getResources().getColor(R.color.colorAccent);

        setLineChart();
        //setPieChart();
    }
    
    private void setPieChart(){
        mPercentagePiechart.setUsePercentValues(true);
        mPercentagePiechart.setDescription("");
        mPercentagePiechart.setExtraOffsets(5, 10, 5, 5);

        mPercentagePiechart.setDragDecelerationFrictionCoef(0.95f);
        //mPercentagePiechart.setCenterText("");
        mPercentagePiechart.setDrawCenterText(false);

        mPercentagePiechart.setDrawHoleEnabled(true);
        mPercentagePiechart.setHoleColor(Color.TRANSPARENT);

        //mPercentagePiechart.setTransparentCircleColor(Color.WHITE);
        //mPercentagePiechart.setTransparentCircleAlpha(110);

        mPercentagePiechart.setHoleRadius(58f);
        mPercentagePiechart.setTransparentCircleRadius(61f);

        mPercentagePiechart.setRotationAngle(0);
        // enable rotation of the chart by touch
        mPercentagePiechart.setRotationEnabled(true);
        mPercentagePiechart.setHighlightPerTapEnabled(true);

        // mPercentagePiechart.setUnit(" €");
        // mPercentagePiechart.setDrawUnitsInChart(true);

        // add a selection listener
        //mPercentagePiechart.setOnChartValueSelectedListener(this);
        setPieData(3, 100);

        mPercentagePiechart.animateXY(ANIMATE_DURATION, ANIMATE_DURATION);

        Legend l = mPercentagePiechart.getLegend();
        l.setPosition(Legend.LegendPosition.RIGHT_OF_CHART);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);
    }

    final String[] mParties = new String[] {
            "Party A", "Party B", "Party C", "Party D", "Party E", "Party F", "Party G", "Party H",
            "Party I", "Party J", "Party K", "Party L", "Party M", "Party N", "Party O", "Party P",
            "Party Q", "Party R", "Party S", "Party T", "Party U", "Party V", "Party W", "Party X",
            "Party Y", "Party Z"
    };
    private void setPieData(int count, float range) {

        float mult = range;

        ArrayList<Entry> yVals1 = new ArrayList<Entry>();

        // IMPORTANT: In a PieChart, no values (Entry) should have the same
        // xIndex (even if from different DataSets), since no values can be
        // drawn above each other.
        for (int i = 0; i < count + 1; i++) {
            yVals1.add(new Entry((float) (Math.random() * mult) + mult / 5, i));
        }

        ArrayList<String> xVals = new ArrayList<String>();

        for (int i = 0; i < count + 1; i++)
            xVals.add(mParties[i % mParties.length]);

        PieDataSet dataSet = new PieDataSet(yVals1, "Election Results");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);

        // add a lot of colors

        ArrayList<Integer> colors = new ArrayList<Integer>();

        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());

        dataSet.setColors(colors);
        //dataSet.setSelectionShift(0f);

        PieData data = new PieData(xVals, dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.WHITE);
        mPercentagePiechart.setData(data);

        // undo all highlights
        mPercentagePiechart.highlightValues(null);

        mPercentagePiechart.invalidate();
    }
    
    private void setLineChart(){
        //mFuelLinechart.setNoDataTextDescription("You need to provide data for the chart.");
        mFuelLinechart.setDescription("单位：元/公里");
        mFuelLinechart.setDescriptionColor(mColor);
        // enable touch gestures
        mFuelLinechart.setTouchEnabled(true);

        mFuelLinechart.setDragDecelerationFrictionCoef(0.9f);

        // enable scaling and dragging
        mFuelLinechart.setDragEnabled(true);
        mFuelLinechart.setScaleEnabled(true);
        mFuelLinechart.setDrawGridBackground(false);
        //mFuelLinechart.setHighlightPerDragEnabled(true);
        // if disabled, scaling can be done on x- and y-axis separately
        mFuelLinechart.setPinchZoom(true);

        mFuelLinechart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        mFuelLinechart.getXAxis().setDrawGridLines(false);
        //mFuelLinechart.getXAxis().setAvoidFirstLastClipping(true);

        YAxis leftAxis = mFuelLinechart.getAxisLeft();
        leftAxis.removeAllLimitLines(); // reset all limit lines to avoid overlapping lines
        leftAxis.setAxisMaxValue(2f);
        leftAxis.setAxisMinValue(0f);
        leftAxis.enableGridDashedLine(10f, 10f, 0f);
        leftAxis.setDrawZeroLine(false);

        // limit lines are drawn behind data (and not on top)
        //leftAxis.setDrawLimitLinesBehindData(true);
        mFuelLinechart.getAxisRight().setEnabled(false);
        // add data
        setData(20, 2);

        mFuelLinechart.animateXY(ANIMATE_DURATION, ANIMATE_DURATION);
        mFuelLinechart.getLegend().setForm(Legend.LegendForm.LINE);

        setPieChart();
    }

    private void setData(int count, float range) {
        ArrayList<String> xVals = new ArrayList<String>();
        for (int i = 0; i < count; i++) {
            xVals.add("");
        }

        ArrayList<Entry> yVals = new ArrayList<Entry>();

        for (int i = 0; i < count; i++) {
            yVals.add(new Entry(getRandom(), i));
        }

        LineDataSet set1;

        if (mFuelLinechart.getData() != null &&
                mFuelLinechart.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet)mFuelLinechart.getData().getDataSetByIndex(0);
            set1.setYVals(yVals);
            mFuelLinechart.getData().setXVals(xVals);
            mFuelLinechart.getData().notifyDataChanged();
            mFuelLinechart.notifyDataSetChanged();
        } else {
            //创建一条线
            set1 = new LineDataSet(yVals, "油耗走势曲线");
            final int textColor = getResources().getColor(R.color.secondary_text);
            //set1.enableDashedLine(10f, 5f, 0f); //启用虚线
            //set1.enableDashedHighlightLine(10f, 5f, 0f); //启用高亮虚线
            set1.setColor(textColor);
            set1.setCircleColor(textColor);
            set1.setLineWidth(1f);
            //set1.setCircleRadius(3f); //圆点半径
            //set1.setDrawCircleHole(false); //圆点是否空心
            set1.setValueTextSize(8f);
            set1.setHighlightEnabled(true); //选中高亮
            set1.setHighLightColor(mColor);
            set1.setFillColor(mColor);

            ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
            dataSets.add(set1);
            LineData data = new LineData(xVals, dataSets);
            mFuelLinechart.setData(data);
        }
    }
    private final float getRandom(){
        return (float)((Math.random()*200)/100);
    }

    @OnClick(R.id.chart_fuel_lable) public void click1(){
        activity.startActivity(new Intent(activity, LineChartActivity1.class));
    }
    @OnClick(R.id.chart_consumer_lable) public void click2(){
        activity.startActivity(new Intent(activity, LineChartActivity2.class));
    }
    @OnClick(R.id.chart_percentage_lable) public void click3(){
        activity.startActivity(new Intent(activity, PieChartActivity.class));
    }
}
