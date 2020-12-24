package com.classic.assistant.car.ui.chart

import android.annotation.SuppressLint
import android.graphics.Typeface
import android.os.Bundle
import android.view.*
import android.widget.LinearLayout
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import com.classic.assistant.car.R
import com.classic.assistant.car.data.source.local.db.ConsumptionInfo
import com.classic.assistant.car.data.source.local.db.FuelConsumptionSummary
import com.classic.assistant.car.databinding.FragmentChartBinding
import com.classic.assistant.car.extension.*
import com.classic.assistant.car.ui.base.AppFragment
import com.classic.assistant.car.util.ChooseCallback
import com.classic.assistant.car.util.DatePickerUtil
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.charts.PieChart
import java.util.*


class ChartFragment : AppFragment() {
    private val vm: ChartViewModel by activityViewModels()
    private val scale = Const.DEFAULT_SCALE
    private val animDuration = 600
    private var barChart: BarChart? = null
    private var pieChart: PieChart? = null
    private var lineChart: LineChart? = null
    private var consumptionPercentageView: ViewGroup? = null
    private var consumptionTopView: ViewGroup? = null
    private var fuelConsumptionListView: ViewGroup? = null
    private var monthConsumptionListView: ViewGroup? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding: FragmentChartBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_chart, container, false)
        binding.model = vm
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        barChart = view.findViewById(R.id.chart_consumer_bar_chart)
        pieChart = view.findViewById(R.id.chart_percentage_pie_chart)
        lineChart = view.findViewById(R.id.chart_fuel_line_chart)
        consumptionPercentageView = view.findViewById(R.id.chart_percentage_detail)
        consumptionTopView = view.findViewById(R.id.chart_top_detail)
        fuelConsumptionListView = view.findViewById(R.id.chart_fuel_list)
        monthConsumptionListView = view.findViewById(R.id.chart_month_detail)
        barChart?.init()
        pieChart?.init()
        lineChart?.init()

        observe(vm.yearConsumptionList, {
            val data = barChart?.displayMonth(it, duration = animDuration)
            displayMonthConsumption(data)
        })
        observe(vm.consumptionList, { displayPieChart(it) })
        observe(vm.topConsumptionList, { displayTopConsumptionInfo(it) })
        observe(vm.fuelConsumptionList, { displayLineChart(it) })
        vm.onRefresh()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.years_menu, menu)
        menu.findItem(R.id.action_year)?.title = vm.year.toString()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        DatePickerUtil.year(requireActivity(), vm.year, object : ChooseCallback<Int> {
            override fun onChoose(t: Int) {
                item.title = t.toString()
                vm.year = t
                vm.onRefresh()
            }
        })
        return true
    }

    // override fun onResume() {
    //     super.onResume()
    //     barChart?.animateXY(animDuration, animDuration)
    //     pieChart?.animateXY(animDuration, animDuration)
    //     lineChart?.animateXY(animDuration, animDuration)
    // }

    @SuppressLint("SetTextI18n")
    private fun displayMonthConsumption(map: HashMap<Int, Float>?) {
        monthConsumptionListView?.apply {
            if (childCount > 0) {
                removeAllViews()
            }
            var lastAmount = 0F
            for (i in 0..11) {
                val amount: Float = map?.get(i) ?: 0F
                @SuppressLint("InflateParams")
                val itemView = LayoutInflater.from(requireContext()).inflate(R.layout.item_top_detail, null)
                (itemView.findViewById(R.id.item_top_index) as TextView).text = "${i + 1}月"
                (itemView.findViewById(R.id.item_top_title) as TextView).text = amount.replace(Const.DEFAULT_SCALE)
                val offset = amount - lastAmount
                val offsetLabel: String = when {
                    i == 0 -> "/" // 从2月份开始才会有差异金额
                    offset > 0 -> "+${offset.replace(Const.DEFAULT_SCALE)}"
                    offset < 0 -> offset.replace(Const.DEFAULT_SCALE)
                    else -> "/"
                }
                (itemView.findViewById(R.id.item_top_value) as TextView).let {
                    it.text = offsetLabel
                    val colorResId: Int = when {
                        offsetLabel.startsWith("+") -> R.color.red
                        offsetLabel.startsWith("-") -> R.color.green
                        else -> R.color.secondary_text
                    }
                    it.setTextColor(appContext.color(colorResId))
                }
                itemView.findViewById<View>(R.id.item_top_bottom_divider).visibility =
                    if (i == 11) View.VISIBLE else View.GONE
                addView(
                    itemView, LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                lastAmount = amount
            }
        }
    }

    // private fun displayBarChart(list: MutableList<ConsumptionInfo>) {
    //     barChart?.display(list, duration = animDuration)
    // }
    private fun displayPieChart(list: MutableList<ConsumptionInfo>?) {
        pieChart?.apply {
            val data = convert(list)
            display(data?.pieData, duration = animDuration)
            if (null != data) displayConsumptionPercentageDetail(data)
        }
    }
    private fun displayLineChart(list: MutableList<ConsumptionInfo>?) {
        lineChart?.apply {
            val data = convert(list)
            display(data?.lineData, duration = animDuration)
            if (null != data) {
                displayFuelConsumptionDetail(data)
                displayFuelConsumptionList(data.summary)
            }
        }
    }
    private fun displayTopConsumptionInfo(list: MutableList<ConsumptionInfo>?) {
        if (list.isNullOrEmpty()) return
        consumptionTopView?.apply {
            if (childCount > 0) {
                removeAllViews()
            }
            list.forEachIndexed { index, item ->
                @SuppressLint("InflateParams")
                val itemView = LayoutInflater.from(requireContext()).inflate(R.layout.item_top_detail, null)
                (itemView.findViewById(R.id.item_top_index) as TextView).text =
                    getString(R.string.format_top, index + 1)
                (itemView.findViewById(R.id.item_top_title) as TextView).text =
                    UIManager.label(item.type)
                (itemView.findViewById(R.id.item_top_value) as TextView).text =
                    formatAmount(item.amount)
                itemView.findViewById<View>(R.id.item_top_bottom_divider).visibility =
                    if (index == list.size - 1) View.VISIBLE else View.GONE
                addView(
                    itemView, LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
            }
        }
    }

    // 显示各类型消费百分比详情
    private fun displayConsumptionPercentageDetail(data: PieChartData) {
        consumptionPercentageView?.apply {
            if (childCount > 0) {
                removeAllViews()
            }
            // mSavePercentage.setVisibility(if (null != pieChartData && null != pieChartData.pieData) View.VISIBLE else View.GONE)
            val inflater = LayoutInflater.from(requireContext())

            val values = ArrayList<Float>()
            val amountArray = data.groupAmount
            for (i in 0 until amountArray.size()) {
                values.add(amountArray.valueAt(i))
            }
            // 顺序
            // Collections.sort(values);
            // 倒序
            values.sortWith { o1, o2 -> o2!!.compareTo(o1!!) }
            var rows = 1
            for (item in values) {
                val key = amountArray.keyAt(amountArray.indexOfValue(item))
                @SuppressLint("InflateParams")
                val itemView = inflater.inflate(R.layout.item_percentage_detail, null)
                (itemView.findViewById(R.id.item_percentage_title) as TextView).text =
                    UIManager.label(key)
                (itemView.findViewById(R.id.item_percentage_amount) as TextView).text =
                    formatAmount(item)
                (itemView.findViewById(R.id.item_percentage_value) as TextView).text =
                    formatPercentage(item * 100 / data.totalAmount)
                itemView.findViewById<View>(R.id.item_percentage_bottom_divider).visibility =
                    if (rows == values.size) View.VISIBLE else View.GONE
                addView(
                    itemView, LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                rows++
            }
            @SuppressLint("InflateParams")
            val totalView = inflater.inflate(R.layout.item_total, null)
            (totalView.findViewById(R.id.item_total_title) as TextView).setText(R.string.total_money)
            (totalView.findViewById(R.id.item_total_value) as TextView).text =
                formatAmount(data.totalAmount)
            addView(
                totalView, LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
        }
    }

    // 显示油耗详情
    private fun displayFuelConsumptionDetail(data: LineChartData) {
        vm.apply {
            val min = data.summary.min
            minAmount.set(formatAmount(min.kmAmount))
            minFuelConsumption.set(formatFuelConsumption(min.kmFuelConsumption))

            val max = data.summary.max
            maxAmount.set(formatAmount(max.kmAmount))
            maxFuelConsumption.set(formatFuelConsumption(max.kmFuelConsumption))
        }
    }

    // 显示各类型消费百分比详情
    @SuppressLint("InflateParams")
    private fun displayFuelConsumptionList(summary: FuelConsumptionSummary) {
        fuelConsumptionListView?.apply {
            if (childCount > 0) {
                removeAllViews()
            }
            val inflater = LayoutInflater.from(requireContext())
            val titleView = inflater.inflate(R.layout.item_fuel_detail_title, null)
            addView(titleView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            // 取反序，日期从大到小排列
            summary.list.asReversed().forEachIndexed { index, item ->
                val itemView = inflater.inflate(R.layout.item_fuel_detail, null)
                (itemView.findViewById(R.id.item_fuel_date) as TextView).apply {
                    text = item.time.format(PATTERN_MONTH_DAY)
                    applyFuelTextStyle(this, false, summary.min.time == item.time, summary.max.time == item.time)
                }
                (itemView.findViewById(R.id.item_fuel_days) as TextView).apply {
                    text = item.days.toString()
                    applyFuelTextStyle(this, summary.target.days == item.days, summary.min.days == item.days, summary.max.days == item.days)
                }
                (itemView.findViewById(R.id.item_fuel_mileage) as TextView).apply {
                    text = item.mileage.toString()
                    applyFuelTextStyle(this, summary.target.mileage == item.mileage, summary.min.mileage == item.mileage, summary.max.mileage == item.mileage)
                }
                (itemView.findViewById(R.id.item_fuel_day_mileage) as TextView).apply {
                    text = item.dayMileage.toString()
                    applyFuelTextStyle(this, summary.target.dayMileage == item.dayMileage, summary.min.dayMileage == item.dayMileage, summary.max.dayMileage == item.dayMileage)
                }
                (itemView.findViewById(R.id.item_fuel_consumption) as TextView).apply {
                    text = replace(item.fuelConsumption)
                    applyFuelTextStyle(this, summary.target.fuelConsumption == item.fuelConsumption, summary.min.fuelConsumption == item.fuelConsumption, summary.max.fuelConsumption == item.fuelConsumption)
                }
                (itemView.findViewById(R.id.item_fuel_km_consumption) as TextView).apply {
                    text = replace(item.kmFuelConsumption)
                    applyFuelTextStyle(this, summary.target.kmFuelConsumption == item.kmFuelConsumption, summary.min.kmFuelConsumption == item.kmFuelConsumption, summary.max.kmFuelConsumption == item.kmFuelConsumption)
                }
                (itemView.findViewById(R.id.item_fuel_amount) as TextView).apply {
                    text = replace(item.amount)
                    applyFuelTextStyle(this, summary.target.amount == item.amount, summary.min.amount == item.amount, summary.max.amount == item.amount)
                }
                (itemView.findViewById(R.id.item_fuel_km_amount) as TextView).apply {
                    text = replace(item.kmAmount)
                    applyFuelTextStyle(this, summary.target.kmAmount == item.kmAmount, summary.min.kmAmount == item.kmAmount, summary.max.kmAmount == item.kmAmount)
                }

                itemView.findViewById<View>(R.id.item_table_bottom_divider).visibility =
                    if (index == summary.list.size - 1) View.VISIBLE else View.GONE
                addView(
                    itemView, LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
            }
        }
    }

    private fun applyFuelTextStyle(view: TextView, isTarget: Boolean, isMin: Boolean, isMax: Boolean) {
        val resId = when {
            isMin -> R.color.green
            isTarget -> R.color.inputHint
            isMax -> R.color.red
            else -> R.color.secondary_text
        }
        view.setTextColor(appContext.color(resId))
        if (isTarget || isMin || isMax) {
            view.typeface = Typeface.defaultFromStyle(Typeface.BOLD)
        }
    }

    private fun replace(number: Number): String = number.replace(Const.DEFAULT_SCALE)

    private fun formatAmount(amount: Float): String {
        return getString(R.string.format_amount, amount.toString().replace(scale))
    }
    private fun formatFuelConsumption(value: Float): String {
        return getString(R.string.format_fuel_consumption, value.toString().replace(scale))
    }
    private fun formatPercentage(value: Float): String {
        return value.toString().replace(scale) + "%"
    }
}
