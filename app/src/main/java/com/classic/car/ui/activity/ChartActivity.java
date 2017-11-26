package com.classic.car.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import com.classic.android.base.RxActivity;
import com.classic.android.rx.RxTransformer;
import com.classic.android.rx.RxUtil;
import com.classic.car.R;
import com.classic.car.app.CarApplication;
import com.classic.car.consts.Consts;
import com.classic.car.db.dao.ConsumerDao;
import com.classic.car.entity.ChartType;
import com.classic.car.entity.ConsumerDetail;
import com.classic.car.ui.chart.BarChartDisplayImpl;
import com.classic.car.ui.chart.IChartDisplay;
import com.classic.car.ui.chart.LineChartDisplayImpl;
import com.classic.car.ui.chart.PieChartDisplayImpl;
import com.classic.car.utils.ToastUtil;
import com.classic.car.utils.Util;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * 应用名称: CarAssistant
 * 包 名 称: com.classic.car.ui.activity
 *
 * 文件描述: TODO
 * 创 建 人: 续写经典
 * 创建时间: 2017/3/25 10:53
 */
@SuppressWarnings("unchecked") public class ChartActivity extends RxActivity {
    private static final String PARAMS_CHART_TYPE = "chartType";
    private static final String PARAMS_START_TIME = "startTime";
    private static final String PARAMS_END_TIME   = "endTime";
    private static final int    ANIMATE_DURATION  = 400;

    @Inject ConsumerDao mConsumerDao;
    @BindView(R.id.chart_download) ImageButton mDownloadBtn;

    private IChartDisplay mChartDisplay;
    private Integer       mDataType;
    private int           mChartType;
    private long          mStartTime;
    private long          mEndTime;
    private Chart         mChart;
    private boolean       isAsc;

    @BindView(R.id.chart_layout) FrameLayout mChartLayout;

    public static void start(@NonNull Activity activity, @ChartType int chartType, long startTime,
                             long endTime) {
        Intent intent = new Intent(activity, ChartActivity.class);
        intent.putExtra(PARAMS_CHART_TYPE, chartType);
        intent.putExtra(PARAMS_START_TIME, startTime);
        intent.putExtra(PARAMS_END_TIME, endTime);
        activity.startActivity(intent);
    }

    @Override public int getLayoutResId() {
        return R.layout.activity_chart;
    }

    @Override public void initView(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                             WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.initView(savedInstanceState);
        ((CarApplication) mAppContext).getDbComponent().inject(this);
        ButterKnife.bind(this);
        getParams();
        createChart(mChartType);
    }

    @Override protected void onStart() {
        super.onStart();
        recycle(loadData());
    }

    @Override protected void onStop() {
        super.onStop();
        unRegister();
    }

    private void getParams() {
        if (!getIntent().hasExtra(PARAMS_CHART_TYPE)) {
            finish();
            return;
        }
        mChartType = getIntent().getIntExtra(PARAMS_CHART_TYPE, ChartType.BAR_CHART);
        mStartTime = getIntent().getLongExtra(PARAMS_START_TIME, 0);
        mEndTime = getIntent().getLongExtra(PARAMS_END_TIME, 0);
        if (mChartType == ChartType.LINE_CHART) {
            mDataType = Consts.TYPE_FUEL;
            isAsc = true;
        }
    }

    private Disposable loadData() {
        // TODO 检查空数据
        return mConsumerDao.query(mDataType, mStartTime, mEndTime, false, isAsc)
                           .compose(RxTransformer.<List<ConsumerDetail>>applySchedulers(RxTransformer.Observable.IO_ON_UI))
                           .map(new Function<List<ConsumerDetail>, Object>() {
                               @Override public Object apply(
                                       @io.reactivex.annotations.NonNull List<ConsumerDetail> list) throws Exception {
                                   return mChartDisplay.convert(list);
                               }
                           })
                           .subscribe(new Consumer<Object>() {
                               @Override public void accept(@io.reactivex.annotations.NonNull Object data)
                                       throws Exception {
                                   mDownloadBtn.setVisibility(null == data ? View.GONE : View.VISIBLE);
                                   mChartDisplay.animationDisplay(mChart, data, ANIMATE_DURATION);
                               }
                           }, Util.ERROR);
    }

    @SuppressWarnings("unused") @OnClick(R.id.chart_back) void onBack(View view) {
        finish();
    }

    @SuppressWarnings("unused") @OnClick(R.id.chart_download) void download(View view) {
        if (null == mChart) { return; }
        final ObservableOnSubscribe subscribe = new ObservableOnSubscribe() {
            @Override public void subscribe(@io.reactivex.annotations.NonNull ObservableEmitter e) throws Exception {
                if (!e.isDisposed()) {
                    e.onNext(mChart.saveToGallery(Util.createImageName(), IChartDisplay.QUALITY));
                    e.onComplete();
                }
            }
        };
        recycle(io.reactivex.Observable.create(subscribe)
                                       .compose(RxTransformer.<Boolean>applySchedulers(RxTransformer.Observable.IO_ON_UI))
                                       .subscribe(new Consumer<Boolean>() {
                                           @Override public void accept(
                                                   @io.reactivex.annotations.NonNull Boolean result)
                                                   throws Exception {
                                               ToastUtil.showToast(mAppContext, result ?
                                                       R.string.chart_save_success : R.string.chart_save_fail);
                                           }
                                       }, new Consumer<Throwable>() {
                                           @Override public void accept(
                                                   @io.reactivex.annotations.NonNull Throwable throwable)
                                                   throws Exception {
                                               ToastUtil.showToast(mAppContext, R.string.chart_save_fail);
                                           }
                                       }));
    }

    private void createChart(int chartType) {
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                                                                       FrameLayout.LayoutParams.MATCH_PARENT);
        final int margin = Util.dp2px(mAppContext, 16);
        params.setMargins(margin, margin, margin, margin);
        switch (chartType) {
            case ChartType.BAR_CHART:
                mChart = new BarChart(mChartLayout.getContext());
                mChart.setLayoutParams(params);
                mChartDisplay = new BarChartDisplayImpl();
                break;
            case ChartType.PIE_CHART:
                mChart = new PieChart(mChartLayout.getContext());
                mChart.setLayoutParams(params);
                mChartDisplay = new PieChartDisplayImpl();
                break;
            case ChartType.LINE_CHART:
                mChart = new LineChart(mChartLayout.getContext());
                mChart.setLayoutParams(params);
                mChartDisplay = new LineChartDisplayImpl();
                break;
        }
        mChartDisplay.init(mChart, true);
        if (null != mChart) {
            mChartLayout.addView(mChart, 0);
        }
    }
}
