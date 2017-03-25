package com.classic.car.entity;

import android.support.annotation.IntDef;

/**
 * 应用名称: CarAssistant
 * 包 名 称: com.classic.car.entity
 *
 * 文件描述: 图表类型
 * 创 建 人: 续写经典
 * 创建时间: 2017/3/25 11:41
 */
@IntDef({ChartType.BAR_CHART, ChartType.PIE_CHART, ChartType.LINE_CHART})
public @interface ChartType {
    int BAR_CHART  = 1;
    int PIE_CHART  = 2;
    int LINE_CHART = 3;
}
