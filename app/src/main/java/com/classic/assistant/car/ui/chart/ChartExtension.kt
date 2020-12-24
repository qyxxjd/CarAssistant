@file:Suppress("unused")

package com.classic.assistant.car.ui.chart

import android.content.Context
import android.graphics.Color
import android.util.SparseArray
import com.classic.assistant.car.R
import com.classic.assistant.car.data.source.local.db.ConsumptionInfo
import com.classic.assistant.car.data.source.local.db.FuelConsumptionDetail
import com.classic.assistant.car.data.source.local.db.FuelConsumptionSummary
import com.classic.assistant.car.extension.*
import com.classic.assistant.car.util.CarLog
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.ValueFormatter
import java.util.*
import kotlin.collections.HashMap
import kotlin.collections.set
import kotlin.math.ceil

/**
 * Chart extensions
 *
 * @author Classic
 * @version v1.0, 2019-09-20 15:48
 */

const val MAX_VISIBLE_VALUE_COUNT = 30
const val Y_MIN_VALUE = 0F
const val Y_MAX_VALUE = 1000F
const val MINIMUM_VALUE = 0F
const val TEXT_SIZE = 8F
const val LARGE_TEXT_SIZE = 16F
const val LARGE_AXIS_SIZE = 12F
const val QUALITY = 100
const val EMPTY_LABEL = ""

var ALL_CHART_COLORS: MutableList<Int> = mutableListOf()

fun chartColors(context: Context): MutableList<Int> {
    if (ALL_CHART_COLORS.isEmpty()) {
        ALL_CHART_COLORS.add(context.color(R.color.chartreuse_light))
        ALL_CHART_COLORS.add(context.color(R.color.saffron_light))
        ALL_CHART_COLORS.add(context.color(R.color.sienna_light))
        ALL_CHART_COLORS.add(context.color(R.color.green_light))
        ALL_CHART_COLORS.add(context.color(R.color.purple_light))
        ALL_CHART_COLORS.add(context.color(R.color.orange_light))
        ALL_CHART_COLORS.add(context.color(R.color.blue_light))
        ALL_CHART_COLORS.add(context.color(R.color.pale_red))
        ALL_CHART_COLORS.add(context.color(R.color.pink_light))
        ALL_CHART_COLORS.add(context.color(R.color.mediumorchid_light))
    }
    return ALL_CHART_COLORS
}

fun BarChart.init(touchEnable: Boolean = true, applyLargeSize: Boolean = false) {
    val ctx = context.applicationContext
    setNoDataText(ctx.string(R.string.hint_chart_empty))
    description.isEnabled = false
    setTouchEnabled(touchEnable)
    if (touchEnable) {
        // 图表拖动
        isDragEnabled = true
        // 打开或关闭对图表所有轴的的缩放
        setScaleEnabled(true)
        setPinchZoom(true)
        isDoubleTapToZoomEnabled = false
        isHighlightFullBarEnabled = false
    }
    val targetAxisSize = chartAxisSize(applyLargeSize)
    val color = ctx.color(R.color.gray_dark)
    val transparent = ctx.color(R.color.transparent)
    // 超过这个值,不显示value
    setMaxVisibleValueCount(MAX_VISIBLE_VALUE_COUNT)
    setDrawGridBackground(false)
    val leftAxis = axisLeft
    // 设置左侧(Y轴)最小值为0
    leftAxis.axisMinimum = Y_MIN_VALUE
    // 设置左侧(Y轴)最大值为1000，避免个别数据较大时，其它数据完全看不到线条
    // leftAxis.axisMaximum = Y_MAX_VALUE
    // leftAxis.axisMaximum = 600F
    // 网格线以虚线模式绘制
    leftAxis.enableGridDashedLine(10f, 10f, 0f)
    leftAxis.textSize = targetAxisSize

    leftAxis.textColor = color
    axisRight.isEnabled = false
    xAxis.apply {
        position = XAxis.XAxisPosition.BOTTOM
        setDrawGridLines(false)
        // textSize = targetAxisSize
        textColor = transparent
    }
    legend.isEnabled = false
}
fun BarChart.display(list: MutableList<ConsumptionInfo>, applyLargeSize: Boolean = false, duration: Int = 0) {
    if (list.isEmpty()) {
        clear()
        return
    }
    val entries = ArrayList<BarEntry>()
    val size = list.size
    for (i in 0 until size) {
        entries.add(BarEntry(i.toFloat(), list[i].amount))
    }
    val ds = BarDataSet(entries, EMPTY_LABEL)
    ds.valueTextSize = chartTextSize(applyLargeSize)
    ds.colors = chartColors(context)
    data = BarData(ds)
    if (duration > 0) {
        animateXY(0, duration)
    }
}
fun BarChart.displayMonth(list: MutableList<ConsumptionInfo>?, applyLargeSize: Boolean = false, duration: Int = 0) : HashMap<Int, Float> {
    val map = HashMap<Int, Float>()
    if (list.isNullOrEmpty()) {
        clear()
        return map
    }

    list.forEach {
        // 0-11
        val month = calendar(it.time).get(Calendar.MONTH)
        val v: Float = map[month] ?: 0F
        map[month] = v + it.amount
    }

    val entries = ArrayList<BarEntry>()
    for (i in 0..11) {
        entries.add(BarEntry((i + 1).toFloat(), map[i] ?: 0F))
    }

    val ds = BarDataSet(entries, EMPTY_LABEL)
    ds.valueTextSize = chartTextSize(applyLargeSize)
    ds.colors = chartColors(context)
    data = BarData(ds)
    if (duration > 0) {
        animateXY(0, duration)
    }
    return map
}

class PieChartData {
    var totalAmount: Float = 0F
    var pieData: PieData? = null
    var groupAmount: SparseArray<Float> = SparseArray()
}
fun PieChart.init(touchEnable: Boolean = true, applyLargeSize: Boolean = false) {
    val ctx = context.applicationContext
    setNoDataText(ctx.string(R.string.hint_chart_empty))
    description.isEnabled = false
    setTouchEnabled(touchEnable)

    setUsePercentValues(true)
    setExtraOffsets(5f, 10f, 5f, 5f)
    // 环形图不绘制描述信息
    setDrawEntryLabels(false)
    // setCenterText(EMPTY_LABEL);
    setDrawCenterText(false)
    holeRadius = 48f
    transparentCircleRadius = 52f
    setHoleColor(Color.TRANSPARENT)
    // setRotationAngle(0);
    // 通过触摸启用图表的旋转, 默认：true
    // setRotationEnabled(true);
    // 突出显示, 默认：true
    // setHighlightPerTapEnabled(true);
    // setEntryLabelTextSize(8f);
    // setEnabled(false);
    // setEntryLabelColor(getColor(context, R.color.gray_dark));
    legend.apply {
        verticalAlignment = Legend.LegendVerticalAlignment.TOP
        horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
        orientation = Legend.LegendOrientation.VERTICAL
        textSize = chartTextSize(applyLargeSize)
        textColor = ctx.color(R.color.gray_dark)
    }
}
fun PieChart.convert(list: MutableList<ConsumptionInfo>?, applyLargeSize: Boolean = false): PieChartData? {
    if (list.isNullOrEmpty()) {
        clear()
        return null
    }
    val pieChartData = PieChartData()
    val size = list.size
    for (i in 0 until size) {
        val money = list[i].amount
        pieChartData.totalAmount += money
        val type = list[i].type
        pieChartData.groupAmount.put(type, pieChartData.groupAmount.get(type, 0f) + money)
    }
    val pieEntries = ArrayList<PieEntry>()
    val groupSize = pieChartData.groupAmount.size()
    for (i in 0 until groupSize) {
        val v = if (pieChartData.totalAmount > 0f) {
            (pieChartData.groupAmount.valueAt(i) / pieChartData.totalAmount).round(4)
        } else 0F
        pieEntries.add(PieEntry(v, UIManager.label(pieChartData.groupAmount.keyAt(i))))
    }
    val dataSet = PieDataSet(pieEntries, EMPTY_LABEL)
    // 环形之间的间隔
    dataSet.sliceSpace = 1f
    dataSet.selectionShift = 8f
    dataSet.colors = chartColors(context)
    // TODO 值显示在内部，还是外部
    // dataSet.yValuePosition = PieDataSet.ValuePosition.OUTSIDE_SLICE
    val pieData = PieData(dataSet)
    pieData.setValueFormatter(PERCENTAGE_FORMATTER)
    pieData.setValueTextSize(chartTextSize(applyLargeSize))
    pieData.setValueTextColor(Color.WHITE)
    pieChartData.pieData = pieData
    return pieChartData
}
fun PieChart.display(pieData: PieData?, duration: Int = 0) {
    data = pieData
    if (duration > 0) {
        animateXY(duration, duration)
    }
}
/** 百分比格式  */
val PERCENTAGE_FORMATTER = object : ValueFormatter() {
    override fun getFormattedValue(value: Float): String {
        return value.replace(2) + "%"
    }
}

data class FuelConsumption(
    /** 百公里耗油量,单位:升/公里 */
    val oilMass: Float = 0F,
    /** 百公里花费金额,单位:元/公里 */
    val amount: Float = 0F,
    /** 行驶里程数,单位:公里 */
    val mileage: Long = 0
)
class LineChartData {
    var lineData: LineData? = null
    val summary = FuelConsumptionSummary()
}
/** 百公里油耗格式  */
val OIL_MESS_FORMATTER = object : ValueFormatter() {
    override fun getFormattedValue(value: Float): String {
        return value.replace(2) + "L"
    }
}
fun LineChart.init(touchEnable: Boolean = true, applyLargeSize: Boolean = false) {
    val ctx = context.applicationContext
    setNoDataText(ctx.string(R.string.hint_chart_empty))
    description.isEnabled = false
    setTouchEnabled(touchEnable)

    // 超过这个值，不显示value
    setMaxVisibleValueCount(MAX_VISIBLE_VALUE_COUNT * 2)
    setDrawGridBackground(false)
    if (touchEnable) {
        // enable scaling and dragging
        // setDragEnabled(true);
        // setScaleEnabled(true);
        // setHighlightPerDragEnabled(true);
        // if disabled, scaling can be done on x- and y-axis separately
        setPinchZoom(true)
    }
    val color = ctx.color(R.color.gray_dark)
    val transparent = ctx.color(R.color.transparent)
    val targetAxisSize = chartAxisSize(applyLargeSize)
    axisLeft.apply {
        textSize = targetAxisSize
        enableGridDashedLine(10f, 10f, 0f)
        textColor = color
    }
    axisRight.isEnabled = false
    // xAxis.isEnabled = false
    xAxis.apply {
        position = XAxis.XAxisPosition.BOTTOM
        setDrawGridLines(false)
        textSize = targetAxisSize
        textColor = transparent
    }
    legend.apply {
        form = Legend.LegendForm.LINE
        textSize = chartTextSize(applyLargeSize)
        textColor = color
    }
}
fun LineChart.convert(list: MutableList<ConsumptionInfo>?, applyLargeSize: Boolean = false): LineChartData? {
    if (list.isNullOrEmpty()) {
        return null
    }
    val lineChartData = LineChartData()
    val size = list.size - 1

    // 最小油耗信息
    var min: FuelConsumptionDetail? = null
    // 最高油耗信息
    var max: FuelConsumptionDetail? = null

    for (i in 0 until size) {
        val startItem = list[i]
        val endItem = list[i + 1]
        val newItem = FuelConsumptionDetail().apply {
            time = endItem.time
            days = ceil((endItem.time - startItem.time) / DAY_2_MS.toDouble()).toInt()
            mileage = endItem.mileage - startItem.mileage
            if (mileage < 0) mileage = 0
            dayMileage = mileage / days
            fuelConsumption = (startItem.amount / startItem.oilPrice).round()
            kmFuelConsumption = (fuelConsumption * 100 / mileage).round()
            amount = startItem.amount
            kmAmount = (startItem.amount * 100 / mileage).round()
        }
        CarLog.d("当前油耗：$newItem")
        lineChartData.summary.list.add(newItem)

        // 更新推荐值
        lineChartData.summary.target.apply {
            if (newItem.days > days) days = newItem.days
            if (newItem.mileage > mileage) mileage = newItem.mileage
            if (newItem.dayMileage > dayMileage) dayMileage = newItem.dayMileage
            if (fuelConsumption == 0F || newItem.fuelConsumption < fuelConsumption) fuelConsumption = newItem.fuelConsumption
            if (kmFuelConsumption == 0F || newItem.kmFuelConsumption < kmFuelConsumption) kmFuelConsumption = newItem.kmFuelConsumption
            if (amount == 0F || newItem.amount < amount) amount = newItem.amount
            if (kmAmount == 0F || newItem.kmAmount < kmAmount) kmAmount = newItem.kmAmount
        }

        if (null == min || newItem.kmAmount < min.kmAmount) {
            min = newItem
            // CarLog.d("当前最低油耗：$newItem")
        }
        if (null == max || newItem.kmAmount > max.kmAmount) {
            max = newItem
            // CarLog.d("当前最高油耗：$newItem")
        }
    }
    if (null != min) lineChartData.summary.min = min
    if (null != max) lineChartData.summary.max = max
    CarLog.d("\n 推荐油耗：${lineChartData.summary.target} \n 最低油耗：$min \n 最高油耗：$max ")
    val oilMessValues = ArrayList<Entry>()
    lineChartData.summary.list.apply {
        for (i in 0 until size) {
            oilMessValues.add(Entry(i.toFloat(), this[i].kmFuelConsumption))
        }
    }

    val oilMessSet = LineDataSet(oilMessValues, context.string(R.string.chart_fuel_consumption))
    val targetColor = context.color(R.color.blue)
    oilMessSet.apply {
        axisDependency = YAxis.AxisDependency.LEFT
        color = targetColor
        setCircleColor(targetColor)
        valueTextSize = chartTextSize(applyLargeSize)
        valueFormatter = OIL_MESS_FORMATTER

        // setCircleRadius(5f); //圆点半径
        // setDrawCircleHole(false); //圆点是否空心
        // setCircleHoleRadius(3f); //空心半径
        // setHighlightEnabled(true); //选中高亮
        // enableDashedLine(10f, 5f, 0f); //启用虚线
        // enableDashedHighlightLine(10f, 5f, 0f); //启用高亮虚线
    }
    lineChartData.lineData = LineData(oilMessSet)
    return lineChartData
}
fun LineChart.display(lineData: LineData?, duration: Int = 0) {
    data = lineData
    if (duration > 0) {
        animateXY(duration, duration)
    }
}

fun chartTextSize(applyLargeSize: Boolean = false): Float =
    if (applyLargeSize) LARGE_TEXT_SIZE else TEXT_SIZE
fun chartAxisSize(applyLargeSize: Boolean = false): Float =
    if (applyLargeSize) LARGE_AXIS_SIZE else TEXT_SIZE
