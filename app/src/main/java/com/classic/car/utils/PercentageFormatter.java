package com.classic.car.utils;

import com.classic.core.utils.MoneyUtil;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.formatter.YAxisValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

/**
 * 应用名称: CarAssistant
 * 包 名 称: com.classic.car.utils
 *
 * 文件描述：PieChart自定义百分比格式
 * 创 建 人：续写经典
 * 创建时间：16/7/3 下午5:04
 */
public class PercentageFormatter implements ValueFormatter, YAxisValueFormatter {

    // ValueFormatter
    @Override
    public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
        return MoneyUtil.replace(MoneyUtil.newInstance(value).round(2).create()) + "%";
    }

    // YAxisValueFormatter
    @Override public String getFormattedValue(float value, YAxis yAxis) {
        return MoneyUtil.replace(MoneyUtil.newInstance(value).round(2).create()) + "%";
    }
}
