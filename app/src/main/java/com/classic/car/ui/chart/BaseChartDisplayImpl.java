package com.classic.car.ui.chart;

import android.content.Context;
import com.classic.car.R;
import com.classic.car.entity.ConsumerDetail;
import com.classic.car.ui.activity.ChartActivity;
import com.classic.car.utils.Util;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.data.ChartData;

abstract class BaseChartDisplayImpl<C extends Chart, T>  implements IChartDisplay<C, T,
        ConsumerDetail> {

    Context mAppContext;
    int     mTextSize;
    int     mAxisSize;

    @Override public void init(C chart, boolean touchEnable) {
        if (null == chart) { return; }
        mAppContext = chart.getContext().getApplicationContext();
        boolean isBigChart = chart.getContext() instanceof ChartActivity;
        mTextSize = isBigChart ? LARGE_TEXT_SIZE : TEXT_SIZE;
        mAxisSize = isBigChart ? LARGE_AXIS_SIZE : TEXT_SIZE;

        chart.setNoDataText(Util.getString(mAppContext, R.string.no_data_hint));
        chart.getDescription().setEnabled(false);
        chart.setTouchEnabled(touchEnable);
    }

    @Override public void display(C chart, T t) {
        animationDisplay(chart, t, 0);
    }

    @Override public void animationDisplay(C chart, T t, int duration) {
        if (null == chart) {
            return;
        }
        ChartData data;
        if (null == t || (data = getChartData(t)) == null) {
            chart.clear();
            return;
        }
        //noinspection unchecked
        chart.setData(data);
        if (duration > 0) {
            chart.animateXY(duration, duration);
        }
    }

    abstract ChartData getChartData(T t);
}
