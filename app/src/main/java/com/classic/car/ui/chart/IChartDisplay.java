package com.classic.car.ui.chart;

import com.github.mikephil.charting.charts.Chart;

import java.util.List;

/**
 * 应用名称: CarAssistant
 * 包 名 称: com.classic.car.ui.chart
 *
 * 文件描述: TODO
 * 创 建 人: 续写经典
 * 创建时间: 2017/3/28 18:29
 */
public interface IChartDisplay<C extends Chart, T, V> {
    int    MAX_VISIBLE_VALUE_COUNT = 30;
    int    MINIMUM_VALUE           = 0;
    int    TEXT_SIZE               = 8;
    int    LARGE_TEXT_SIZE         = 24;
    int    QUALITY                 = 100;
    String EMPTY_LABEL             = "";

    void init(C chart, boolean touchEnable);

    T convert(List<V> list);

    void display(C chart, T t);

    void animationDisplay(C chart, T t, int duration);
}
