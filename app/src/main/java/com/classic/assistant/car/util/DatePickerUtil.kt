package com.classic.assistant.car.util

import android.app.Activity
import android.view.ViewGroup
import com.bigkoo.pickerview.builder.OptionsPickerBuilder
import com.bigkoo.pickerview.builder.TimePickerBuilder
import com.bigkoo.pickerview.listener.OnTimeSelectListener
import com.bigkoo.pickerview.view.TimePickerView
import java.util.Calendar

/**
 * 日期选择工具类
 *
 * @author Classic
 * @version v1.0, 2019-04-20 09:09
 */
object DatePickerUtil {
    private val MIN_YEAR: Int by lazy { com.classic.assistant.car.extension.year() - 10 }
    private val MAX_YEAR: Int by lazy { com.classic.assistant.car.extension.year() + 10 }

    /**
     * 创建日期选择控件
     *
     * @param context Context
     * @param time 默认时间
     * @param listener 监听器
     * @param useTime true:显示日期和时间，false:仅显示日期
     */
    fun create(context: Activity, time: Long, listener: OnTimeSelectListener, useTime: Boolean = true): TimePickerView {
        val decorView: ViewGroup = context.window.decorView.findViewById(android.R.id.content) as ViewGroup
        val type: BooleanArray = if (useTime)
            booleanArrayOf(true, true, true, true, true, false)
        else
            booleanArrayOf(true, true, true, false, false, false)
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = time
        val startDate = Calendar.getInstance()
        startDate.set(MIN_YEAR, 0, 1, 0, 0, 0)
        val endData = Calendar.getInstance()
        endData.set(MAX_YEAR, 11, 31, 23, 59, 59)
        return TimePickerBuilder(context, listener)
            .setDecorView(decorView)
            .isCyclic(false)
            .setOutSideCancelable(false)
            .setRangDate(startDate, endData)
            .setType(type)
            .setDate(calendar)
            .build()
    }

    fun month(context: Activity, time: Long = System.currentTimeMillis(), listener: OnTimeSelectListener) {
        val decorView: ViewGroup = context.window.decorView.findViewById(android.R.id.content) as ViewGroup
        val type: BooleanArray = booleanArrayOf(true, true, false, false, false, false)
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = time
        val startDate = Calendar.getInstance()
        startDate.set(MIN_YEAR, 0, 1, 0, 0, 0)
        val endData = Calendar.getInstance()
        endData.set(MAX_YEAR, 11, 31, 23, 59, 59)

        TimePickerBuilder(context, listener)
            .setDecorView(decorView)
            .isCyclic(false)
            .setTitleText("月份选择")
            .setOutSideCancelable(false)
            .setRangDate(startDate, endData)
            .setType(type)
            .setDate(calendar)
            .build()
            .show()
    }

    private val yearItems = mutableListOf<String>()
    fun year(context: Activity, defaultYear: Int? = null, callback: ChooseCallback<Int>) {
        val decorView: ViewGroup = context.window.decorView.findViewById(android.R.id.content) as ViewGroup
        if (yearItems.isEmpty()) {
            for (i in MIN_YEAR..MAX_YEAR) {
                yearItems.add(i.toString())
            }
        }
        var index = 0
        if (null != defaultYear) {
            val i = yearItems.indexOf(defaultYear.toString())
            if (i != -1) index = i
        }
        val view = OptionsPickerBuilder(context
        ) { options1, _, _, _ ->
            callback.onChoose(yearItems[options1].toInt())
        }
            .setDecorView(decorView)
            .setTitleText("年份选择")
            .setSelectOptions(index)
            .build<String>()
        view.setPicker(yearItems)
        view.show()
    }
}
interface ChooseCallback<T> {
    fun onChoose(t: T)
}