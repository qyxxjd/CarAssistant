package com.classic.car.ui.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.classic.android.rx.RxTransformer;
import com.classic.android.rx.RxUtil;
import com.classic.car.R;
import com.classic.car.app.CarApplication;
import com.classic.car.consts.Consts;
import com.classic.car.db.dao.ConsumerDao;
import com.classic.car.entity.ChartType;
import com.classic.car.entity.ConsumerDetail;
import com.classic.car.ui.activity.ChartActivity;
import com.classic.car.ui.activity.MainActivity;
import com.classic.car.ui.base.AppBaseFragment;
import com.classic.car.ui.chart.BarChartDisplayImpl;
import com.classic.car.ui.chart.IChartDisplay;
import com.classic.car.ui.chart.LineChartDisplayImpl;
import com.classic.car.ui.chart.PieChartDisplayImpl;
import com.classic.car.ui.widget.RelativePopupWindow;
import com.classic.car.ui.widget.YearsPopup;
import com.classic.car.utils.DateUtil;
import com.classic.car.utils.ToastUtil;
import com.classic.car.utils.Util;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * 应用名称: CarAssistant
 * 包 名 称: com.classic.car.ui.fragment
 *
 * 文件描述：图表统计页面
 * 创 建 人：续写经典
 * 创建时间：16/5/29 下午2:21
 */
@SuppressWarnings("unchecked") public class ChartFragment extends AppBaseFragment {
    private static final int    ANIMATE_DURATION = 400;
    private static final String EMPTY_VALUE      = "0";

    @BindView(R.id.chart_fuel_linechart)      LineChart    mFuelLineChart;
    @BindView(R.id.chart_consumer_barchart)   BarChart     mConsumerBarChart;
    @BindView(R.id.chart_percentage_piechart) PieChart     mPercentagePieChart;
    @BindView(R.id.chart_min_money)           TextView     mMinMoney;
    @BindView(R.id.chart_max_money)           TextView     mMaxMoney;
    @BindView(R.id.chart_min_oilmess)         TextView     mMinOilMess;
    @BindView(R.id.chart_max_oilmess)         TextView     mMaxOilMess;
    @BindView(R.id.chart_consumer_save)       TextView     mSaveConsumer;
    @BindView(R.id.chart_fuel_save)           TextView     mSaveFuel;
    @BindView(R.id.chart_percentage_save)     TextView     mSavePercentage;
    @BindView(R.id.chart_percentage_detail)   LinearLayout mPercentageDetail;
    @Inject                                   ConsumerDao  mConsumerDao;

    private LayoutInflater mLayoutInflater;
    private IChartDisplay  mBarChartDisplay;
    private IChartDisplay  mPieChartDisplay;
    private IChartDisplay  mLineChartDisplay;

    private long       mStartTime;
    private long       mEndTime;
    private int        mCurrentYear;
    private YearsPopup mYearsPopup;

    public static ChartFragment newInstance() {
        return new ChartFragment();
    }

    @Override public int getLayoutResId() {
        return R.layout.fragment_chart;
    }

    @Override public void initView(View parentView, Bundle savedInstanceState) {
        ((CarApplication) mAppContext).getDbComponent().inject(this);
        super.initView(parentView, savedInstanceState);
        setHasOptionsMenu(true);
        mCurrentYear = Calendar.getInstance().get(Calendar.YEAR);
        initChart();
        loadData(mCurrentYear);
    }

    @Override public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.chart_menu, menu);
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_date) {
            showYears();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override public void onFragmentShow() {
        super.onFragmentShow();
        setHasOptionsMenu(true);
        mActivity.setTitle(getString(R.string.consumer_title, mCurrentYear));
    }

    @Override public void onFragmentHide() {
        super.onFragmentHide();
        setHasOptionsMenu(false);
        mActivity.setTitle(R.string.app_name);
    }

    private void showYears() {
        if (null == mYearsPopup) {
            mYearsPopup = new YearsPopup.Builder()
                    .context(mActivity)
                    .years(Consts.YEARS)
                    .fitInScreen(true)
                    .horizontalPosition(RelativePopupWindow.HorizontalPosition.RIGHT)
                    .verticalPosition(RelativePopupWindow.VerticalPosition.BELOW)
                    .listener(new YearsPopup.Listener() {
                        @Override public void onYearSelected(int year) {
                            // ToastUtil.showToast(mAppContext, String.valueOf(year));
                            if (mCurrentYear != year) {
                                mCurrentYear = year;
                                loadData(mCurrentYear);
                            }
                        }
                    })
                    .build();
        }
        if (mYearsPopup.isShowing()) {
            mYearsPopup.dismiss();
        } else {
            mYearsPopup.show(((MainActivity)mActivity).getToolbar());
        }
    }

    @SuppressWarnings("unchecked") private void initChart() {
        mBarChartDisplay = new BarChartDisplayImpl();
        mPieChartDisplay = new PieChartDisplayImpl();
        mLineChartDisplay = new LineChartDisplayImpl();
        mBarChartDisplay.init(mConsumerBarChart, true);
        mPieChartDisplay.init(mPercentagePieChart, true);
        mLineChartDisplay.init(mFuelLineChart, true);
        recycle(processAccidentalClick(mSaveConsumer, mConsumerBarChart));
        recycle(processAccidentalClick(mSaveFuel, mFuelLineChart));
        recycle(processAccidentalClick(mSavePercentage, mPercentagePieChart));
        mConsumerBarChart.setOnChartValueSelectedListener(new ChartValueSelectedListener(ChartType.BAR_CHART));
        mPercentagePieChart.setOnChartValueSelectedListener(new ChartValueSelectedListener(ChartType.PIE_CHART));
        mFuelLineChart.setOnChartValueSelectedListener(new ChartValueSelectedListener(ChartType.LINE_CHART));
    }

    private final class ChartValueSelectedListener implements OnChartValueSelectedListener {
        private @ChartType int type;

        public ChartValueSelectedListener(@ChartType int type) {
            this.type = type;
        }

        @Override public void onValueSelected(Entry entry, Highlight highlight) {
            ChartActivity.start(mActivity, type, mStartTime, mEndTime);
        }

        @Override public void onNothingSelected() {

        }
    }

    private void loadData(int year) {
        mActivity.setTitle(getString(R.string.consumer_title, mCurrentYear));
        mStartTime = DateUtil.getTime(year);
        mEndTime = DateUtil.getTime(year + 1) - 1;
        recycle(loadConsumerDetail(mStartTime, mEndTime));
        recycle(loadFuelConsumption(mStartTime, mEndTime));
    }

    /** 加载消费信息 */
    private Disposable loadConsumerDetail(long startTime, long endTime) {
        // TODO 检查空数据
        return mConsumerDao.queryBetween(startTime, endTime)
                           .compose(RxTransformer.<List<ConsumerDetail>>applySchedulers(RxTransformer.Observable.IO_ON_UI))
                           .flatMap(new Function<List<ConsumerDetail>, ObservableSource<?>>() {
                               @Override public ObservableSource<?> apply(@NonNull List<ConsumerDetail> consumerDetails)
                                       throws Exception {
                                   // if (DataUtil.isEmpty(consumerDetails)) { return Observable.just(null); }
                                   return Observable.just(mBarChartDisplay.convert(consumerDetails),
                                                          mPieChartDisplay.convert(consumerDetails));
                               }
                           })
                           .subscribe(new Consumer<Object>() {
                               @Override public void accept(@NonNull Object data) throws Exception {
                                   // if (null == data) {
                                   //     mConsumerBarChart.clear();
                                   //     mPercentagePieChart.clear();
                                   //     refreshConsumerView(null);
                                   //     refreshPercentageView(null);
                                   //     return;
                                   // }
                                   if (data instanceof BarData) {
                                       mBarChartDisplay.animationDisplay(mConsumerBarChart, data, ANIMATE_DURATION);
                                       refreshConsumerView((BarData)data);
                                   } else if (data instanceof PieChartDisplayImpl.PieChartData) {
                                       mPieChartDisplay.animationDisplay(mPercentagePieChart, data, ANIMATE_DURATION);
                                       refreshPercentageView((PieChartDisplayImpl.PieChartData)data);
                                   }
                               }
                           }, new Consumer<Throwable>() {
                               @Override public void accept(@NonNull Throwable throwable) throws Exception {
                                   if (throwable instanceof NullPointerException) {
                                       mConsumerBarChart.clear();
                                       mPercentagePieChart.clear();
                                       refreshConsumerView(null);
                                       refreshPercentageView(null);
                                   }
                               }
                           });
    }

    /** 加载油耗信息 */
    private Disposable loadFuelConsumption(long startTime, long endTime) {
        // TODO 检查空数据
        return mConsumerDao.query(Consts.TYPE_FUEL, startTime, endTime, false, true)
                           .compose(RxTransformer.<List<ConsumerDetail>>applySchedulers(RxTransformer.Observable.IO_ON_UI))
                           .map(new Function<List<ConsumerDetail>, LineChartDisplayImpl.LineChartData>() {
                               @Override public LineChartDisplayImpl.LineChartData apply(
                                       @NonNull List<ConsumerDetail> list) throws Exception {
                                   return (LineChartDisplayImpl.LineChartData)mLineChartDisplay.convert(list);
                               }
                           })
                           .subscribe(new Consumer<LineChartDisplayImpl.LineChartData>() {
                               @Override public void accept(@NonNull LineChartDisplayImpl.LineChartData lineChartData)
                                       throws Exception {
                                   mLineChartDisplay.animationDisplay(mFuelLineChart, lineChartData, ANIMATE_DURATION);
                                   refreshOilMessView(lineChartData);
                               }
                           }, Util.ERROR);
    }

    private Disposable processAccidentalClick(TextView view, final Chart chart){
        return RxView.clicks(view)
                     .throttleFirst(Consts.SHIELD_TIME, TimeUnit.SECONDS)
                     .subscribe(new Consumer<Object>() {
                         @Override public void accept(@NonNull Object o) throws Exception {
                             ToastUtil.showToast(mAppContext, chart.saveToGallery(Util.createImageName(),
                                                                                  IChartDisplay.QUALITY)
                                                              ? R.string.chart_save_success
                                                              : R.string.chart_save_fail);
                         }
                     });
    }

    private void refreshConsumerView(BarData barData) {
        mSaveConsumer.setVisibility(null != barData ? View.VISIBLE : View.GONE);
    }

    /**
     * 添加消费百分比详细信息
     */
    private void refreshPercentageView(PieChartDisplayImpl.PieChartData pieChartData) {
        if (mPercentageDetail.getChildCount() > 0) {
            mPercentageDetail.removeAllViews();
        }
        mSavePercentage.setVisibility(null != pieChartData && null != pieChartData.pieData ? View.VISIBLE : View.GONE);
        if (null == pieChartData || null == pieChartData.groupMoney) {
            return;
        }
        if(null == mLayoutInflater){
            mLayoutInflater = LayoutInflater.from(mActivity);
        }
        List<Float> values = new ArrayList<>();
        for(int i =0; i < pieChartData.groupMoney.size();i++){
            values.add(pieChartData.groupMoney.valueAt(i));
        }
        // 顺序
        // Collections.sort(values);
        // 倒序
        Collections.sort(values, new Comparator<Float>() {
            @Override public int compare(Float o1, Float o2) {
                return o2.compareTo(o1);
            }
        });
        int rows = 1;
        for (Float item : values) {
            int key = pieChartData.groupMoney.keyAt(pieChartData.groupMoney.indexOfValue(item));
            @SuppressLint("InflateParams")
            View itemView = mLayoutInflater.inflate(R.layout.item_table, null);
            ((TextView) itemView.findViewById(R.id.item_table_lable)).setText(Consts.TYPE_MENUS[key]);
            ((TextView) itemView.findViewById(R.id.item_table_total_money)).setText(
                    Util.formatRMB(item));
            ((TextView) itemView.findViewById(R.id.item_table_percentage)).setText(
                    Util.formatPercentage(item, pieChartData.totalMoney));
            itemView.findViewById(R.id.item_table_bottom_divider)
                    .setVisibility(rows == values.size() ? View.VISIBLE : View.GONE);
            mPercentageDetail.addView(itemView, LinearLayout.LayoutParams.MATCH_PARENT,
                                      LinearLayout.LayoutParams.WRAP_CONTENT);
            rows++;
        }
        @SuppressLint("InflateParams")
        View totalView = mLayoutInflater.inflate(R.layout.item_total_table, null);
        ((TextView) totalView.findViewById(R.id.item_total_table_value)).setText(
                Util.formatRMB(pieChartData.totalMoney));
        mPercentageDetail.addView(totalView, LinearLayout.LayoutParams.MATCH_PARENT,
                                  LinearLayout.LayoutParams.WRAP_CONTENT);

    }

    private void refreshOilMessView(LineChartDisplayImpl.LineChartData lineChartData) {
        if (null != lineChartData && null != lineChartData.minFuelConsumption) {
            mMinMoney.setText(Util.formatRMB(lineChartData.minFuelConsumption.getMoney()));
            mMinOilMess.setText(Util.formatOilMess(lineChartData.minFuelConsumption.getOilMass()));
        } else {
            mMinMoney.setText(EMPTY_VALUE);
            mMinOilMess.setText(EMPTY_VALUE);
        }
        if (null != lineChartData && null != lineChartData.maxFuelConsumption) {
            mMaxMoney.setText(Util.formatRMB(lineChartData.maxFuelConsumption.getMoney()));
            mMaxOilMess.setText(Util.formatOilMess(lineChartData.maxFuelConsumption.getOilMass()));
        } else {
            mMaxMoney.setText(EMPTY_VALUE);
            mMaxOilMess.setText(EMPTY_VALUE);
        }
        mSaveFuel.setVisibility(null != lineChartData && null != lineChartData.lineData ? View.VISIBLE : View.GONE);
    }
}
