package com.classic.car.ui.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.classic.car.R;
import com.classic.car.app.CarApplication;
import com.classic.car.consts.Consts;
import com.classic.car.db.dao.ConsumerDao;
import com.classic.car.entity.ConsumerDetail;
import com.classic.car.entity.FuelConsumption;
import com.classic.car.utils.PercentageFormatter;
import com.classic.car.utils.Util;
import com.classic.core.fragment.BaseFragment;
import com.classic.core.utils.AppInfoUtil;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * 应用名称: CarAssistant
 * 包 名 称: com.classic.car.ui.fragment
 *
 * 文件描述：TODO
 * 创 建 人：续写经典
 * 创建时间：16/5/29 下午2:21
 */
public class ChartFragment extends BaseFragment {
    private static final int ANIMATE_DURATION = 1500;
    @BindView(R.id.chart_fuel_linechart)      LineChart mFuelLinechart;
    @BindView(R.id.chart_consumer_barchart)   BarChart mConsumerBarchart;
    @BindView(R.id.chart_percentage_piechart) PieChart mPercentagePiechart;
    @Inject ConsumerDao mConsumerDao;
    private String mNoDataHint;
    private float mTotalMoney = 0f;
    private Map<Integer, Float> mValuesMap;
    private Observable<List<ConsumerDetail>> mAllData;

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
        mNoDataHint = getResources().getString(R.string.no_data_hint);

        initLineChart();
        initBarChart();
        initPieChart();

        mAllData = mConsumerDao.queryByType(null);
        processLineChartData();
        processBarChartData();
        processPieChartData();
    }

    private void processBarChartData(){
        mAllData
            .flatMap(new Func1<List<ConsumerDetail>, Observable<BarData>>() {
                @Override public Observable<BarData> call(List<ConsumerDetail> list) {
                    ArrayList<IBarDataSet> sets = new ArrayList<>();
                    ArrayList<BarEntry> entries = new ArrayList<>();
                    ArrayList<String> xVals = new ArrayList<>();
                    BarDataSet ds = new BarDataSet(entries, "");
                    ArrayList<Integer> colors = new ArrayList<>();
                    for (int i = 0; i < list.size(); i++) {
                        xVals.add("");
                        entries.add(new BarEntry(list.get(i).getMoney(), i));
                        final int type = list.get(i).getType();
                        if(!colors.contains(type)){
                            colors.add(type);
                            ds.addColor(getResources().getColor(Util.getColorByType(type)));
                        }
                    }
                    colors.clear();
                    colors = null;
                    sets.add(ds);
                    return Observable.just(new BarData(xVals, sets));
                }
            })
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .unsubscribeOn(Schedulers.io())
            .subscribe(new Action1<BarData>() {
                @Override public void call(BarData barData) {
                    if (null != barData) {
                        mConsumerBarchart.setData(barData);
                        mConsumerBarchart.animateXY(ANIMATE_DURATION, ANIMATE_DURATION);
                    }
                }
            });
    }

    private void initBarChart() {
        YAxis leftAxis = mConsumerBarchart.getAxisLeft();
        leftAxis.setAxisMinValue(0f);
        leftAxis.enableGridDashedLine(10f, 10f, 0f);
        leftAxis.setDrawZeroLine(false);

        mConsumerBarchart.setNoDataText(mNoDataHint);
        mConsumerBarchart.getAxisRight().setEnabled(false);

        mConsumerBarchart.setDrawBarShadow(false);
        mConsumerBarchart.setDrawValueAboveBar(true);

        mConsumerBarchart.setDescription("单位:元");
        mConsumerBarchart.setDescriptionColor(getResources().getColor(R.color.gray_dark));

        //超过这个值,不显示value
        mConsumerBarchart.setMaxVisibleValueCount(60);
        mConsumerBarchart.setDrawGridBackground(false);

        XAxis xAxis = mConsumerBarchart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setEnabled(false);

    }

    private void processPieChartData(){
        mAllData
                .flatMap(new Func1<List<ConsumerDetail>, Observable<Map<Integer, Float>>>() {
                    @Override public Observable<Map<Integer, Float>> call(List<ConsumerDetail> list) {
                        mValuesMap = new HashMap<>();
                        for (int i = 0; i < list.size(); i++) {
                            final int type = list.get(i).getType();
                            if (!mValuesMap.containsKey(type)) {
                                mValuesMap.put(type, 0f);
                            }
                            final float money = list.get(i).getMoney();
                            mTotalMoney = MoneyUtil.newInstance(mTotalMoney).add(money).create().floatValue();
                            mValuesMap.put(type,
                                    MoneyUtil.newInstance(mValuesMap.get(type)).add(money).create().floatValue()
                                    );
                        }
                        return Observable.just(mValuesMap);
                    }
                })
                .flatMap(new Func1<Map<Integer, Float>, Observable<PieData>>() {
                    @Override public Observable<PieData> call(Map<Integer, Float> map) {
                        ArrayList<String> xVals = new ArrayList<>();
                        ArrayList<Entry> yVals1 = new ArrayList<>();

                        PieDataSet dataSet = new PieDataSet(yVals1, "");
                        dataSet.setSliceSpace(3f);
                        dataSet.setSelectionShift(5f);
                        int i = 0;
                        for( Integer key : map.keySet() ){
                            yVals1.add(new Entry(
                                    MoneyUtil.newInstance(map.get(key)).divide(mTotalMoney).create().floatValue(), i));
                            xVals.add(Consts.TYPE_MENUS[key]);
                            dataSet.addColor(getResources().getColor(Util.getColorByType(key)));
                            i++;
                        }

                        PieData data = new PieData(xVals, dataSet);
                        data.setValueFormatter(new PercentageFormatter());
                        data.setValueTextSize(8f);
                        data.setValueTextColor(Color.WHITE);
                        return Observable.just(data);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Action1<PieData>() {
                    @Override public void call(PieData pieData) {
                        if (null != pieData) {
                            mPercentagePiechart.setData(pieData);
                            mPercentagePiechart.animateXY(ANIMATE_DURATION, ANIMATE_DURATION);
                        }
                    }
                });
    }

    private void initPieChart() {
        mPercentagePiechart.setNoDataText(mNoDataHint);
        mPercentagePiechart.setUsePercentValues(true);
        mPercentagePiechart.setDescription("");
        mPercentagePiechart.setExtraOffsets(5, 10, 5, 5);

        mPercentagePiechart.setDragDecelerationFrictionCoef(0.95f);
        //mPercentagePiechart.setCenterText("");
        mPercentagePiechart.setDrawCenterText(false);
        //mPercentagePiechart.setDrawHoleEnabled(true);
        //mPercentagePiechart.setHoleColor(Color.TRANSPARENT);

        //mPercentagePiechart.setTransparentCircleColor(Color.WHITE);
        //mPercentagePiechart.setTransparentCircleAlpha(110);

        mPercentagePiechart.setHoleRadius(48f);
        mPercentagePiechart.setTransparentCircleRadius(52f);

        mPercentagePiechart.setRotationAngle(0);
        // enable rotation of the chart by touch
        mPercentagePiechart.setRotationEnabled(true);
        mPercentagePiechart.setHighlightPerTapEnabled(true);

        // mPercentagePiechart.setUnit(" €");
        // mPercentagePiechart.setDrawUnitsInChart(true);

        // add a selection listener
        //mPercentagePiechart.setOnChartValueSelectedListener(this);

        Legend l = mPercentagePiechart.getLegend();
        l.setPosition(Legend.LegendPosition.RIGHT_OF_CHART);
        l.setXEntrySpace(4f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);
    }

    private void processLineChartData(){
        mConsumerDao.queryByType(Consts.TYPE_FUEL)
                    .flatMap(new Func1<List<ConsumerDetail>, Observable<List<FuelConsumption>>>() {
                        @Override public Observable<List<FuelConsumption>> call(List<ConsumerDetail> list) {
                            List<FuelConsumption> result = new ArrayList<>();
                            for (int i = 0; i < list.size() - 1; i++) {
                                ConsumerDetail startItem = list.get(i);
                                ConsumerDetail endItem = list.get(i + 1);
                                final long mileage = endItem.getCurrentMileage() - startItem.getCurrentMileage();
                                final float money = MoneyUtil.newInstance(startItem.getMoney())
                                                              .divide(mileage)
                                                              .multiply(100).create()
                                                              .floatValue();
                                final float oilMass = MoneyUtil.newInstance(money)
                                                               .divide(startItem.getUnitPrice())
                                                               .create()
                                                               .floatValue();
                                result.add(new FuelConsumption(mileage,
                                        Float.valueOf(MoneyUtil.replace(money)),
                                        Float.valueOf(MoneyUtil.replace(oilMass))));
                            }
                            return Observable.just(result);
                        }
                    })
                    .flatMap(new Func1<List<FuelConsumption>, Observable<LineData>>() {
                        @Override public Observable<LineData> call(List<FuelConsumption> list) {
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
                            moneySet.setColor(getResources().getColor(R.color.colorAccent));
                            moneySet.setCircleColor(getResources().getColor(R.color.colorAccent));
                            moneySet.setLineWidth(1f);
                            //moneySet.setCircleRadius(3f); //圆点半径
                            //moneySet.setDrawCircleHole(false); //圆点是否空心
                            moneySet.setValueTextSize(8f);
                            //moneySet.setHighlightEnabled(true); //选中高亮

                            LineDataSet oilMessSet = new LineDataSet(oilMessValues, "百公里耗油曲线(升)");
                            oilMessSet.setColor(getResources().getColor(R.color.colorPrimary));
                            oilMessSet.setCircleColor(getResources().getColor(R.color.colorPrimary));
                            oilMessSet.setValueTextSize(8f);

                            ArrayList<ILineDataSet> dataSets = new ArrayList<>();
                            dataSets.add(moneySet);
                            dataSets.add(oilMessSet);

                            return Observable.just(new LineData(xVals, dataSets));
                        }
                    })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .unsubscribeOn(Schedulers.io())
                    .subscribe(new Action1<LineData>() {
                        @Override public void call(LineData lineData) {
                            if(null != lineData){
                                mFuelLinechart.setData(lineData);
                                mFuelLinechart.animateXY(ANIMATE_DURATION, ANIMATE_DURATION);
                            }
                        }
                    });
    }
    
    private void initLineChart(){
        mFuelLinechart.setNoDataTextDescription(mNoDataHint);
        mFuelLinechart.setDescription("");
        mFuelLinechart.setDescriptionColor(getResources().getColor(R.color.secondary_text));
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
        leftAxis.setAxisMinValue(0f);
        leftAxis.enableGridDashedLine(10f, 10f, 0f);
        leftAxis.setDrawZeroLine(false);

        // limit lines are drawn behind data (and not on top)
        //leftAxis.setDrawLimitLinesBehindData(true);
        mFuelLinechart.getAxisRight().setEnabled(false);

        mFuelLinechart.getLegend().setForm(Legend.LegendForm.LINE);
    }

    @OnClick(R.id.chart_fuel_lable) public void click1(){
        mFuelLinechart.saveToPath(System.currentTimeMillis()+"", "/"+AppInfoUtil.getPackageName(activity
                .getApplicationContext())
                +"/images/");
    }
    @OnClick(R.id.chart_consumer_lable) public void click2(){
        mConsumerBarchart.saveToPath(System.currentTimeMillis()+"", "/"+AppInfoUtil.getPackageName(activity
                .getApplicationContext())
                +"/images/");
    }
    @OnClick(R.id.chart_percentage_lable) public void click3(){
        mPercentagePiechart.saveToPath(System.currentTimeMillis()+"", "/"+AppInfoUtil.getPackageName(activity
                .getApplicationContext())
                +"/images/");
    }
}
