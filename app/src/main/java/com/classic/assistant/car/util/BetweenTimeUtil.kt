package com.classic.assistant.car.util

import com.classic.assistant.car.extension.format
import java.util.Calendar

internal class BetweenTime {
    var start: Long = 0
    var end: Long = 0
}

/**
 * 时间区间工具类
 *
 * @author Classic
 * @version v1.0, 2019-04-19 09:28
 */
internal object BetweenTimeUtil {

    /**
     * 获取当前上个月份的开始、结束时间
     *
     * @param day 可选参数，自定义天数，默认为1
     *
     * 以4月份示例：
     * 2019-03-01 00:00:00
     * 2019-03-31 23:59:59
     */
    fun getLastMonth(day: Int? = null): BetweenTime {
        val betweenTime = BetweenTime()
        val calendar = get()
        if (null != day) {
            calendar.set(Calendar.DAY_OF_MONTH, day)
        }
        calendar.add(Calendar.MONTH, -1) // 上个月，减1
        CarLog.d("上个月份开始时间: ${calendar.format()}")
        betweenTime.start = calendar.timeInMillis
        calendar.add(Calendar.MONTH, 1) // 加1月
        calendar.add(Calendar.SECOND, -1) // 减1秒
        CarLog.d("上个月份结束时间: ${calendar.format()}")
        betweenTime.end = calendar.timeInMillis
        return betweenTime
    }

    /**
     * 获取当前月份的开始、结束时间
     *
     * 以4月份示例：
     * 2019-04-01 00:00:00
     * 2019-04-30 23:59:59
     */
    fun getCurrentMonth(day: Int? = null): BetweenTime {
        val betweenTime = BetweenTime()
        val calendar = get()
        if (null != day) {
            calendar.set(Calendar.DAY_OF_MONTH, day)
        }
        CarLog.d("当前月份开始时间: ${calendar.format()}")
        betweenTime.start = calendar.timeInMillis
        calendar.add(Calendar.MONTH, 1) // 加1月
        calendar.add(Calendar.SECOND, -1) // 减1秒
        CarLog.d("当前月份结束时间: ${calendar.format()}")
        betweenTime.end = calendar.timeInMillis
        return betweenTime
    }

    /**
     * 获取当前年份的开始、结束时间
     *
     * 以2019年示例：
     * 2019-01-01 00:00:00
     * 2019-12-31 23:59:59
     */
    fun getCurrentYear(): BetweenTime {
        val betweenTime = BetweenTime()
        val calendar = get()
        calendar.set(Calendar.MONTH, Calendar.JANUARY) // 1月
        CarLog.d("当前年份开始时间: ${calendar.format()}")
        betweenTime.start = calendar.timeInMillis
        calendar.add(Calendar.YEAR, 1) // 加1年
        calendar.add(Calendar.SECOND, -1) // 减1秒
        CarLog.d("当前年份结束时间: ${calendar.format()}")
        betweenTime.end = calendar.timeInMillis
        return betweenTime
    }

    /**
     * 获取自定义年份、月份的起始时间
     *
     * @param year 年份
     * @param month 月份(1-12)
     */
    fun get(year: Int, month: Int?): BetweenTime {
        val betweenTime = BetweenTime()
        val calendar = get()
        calendar.set(Calendar.YEAR, year)
        if (null != month) {
            calendar.set(Calendar.MONTH, month - 1) // 月份从0开始，需要减1
            CarLog.d("自定义年月开始时间($year, $month): ${calendar.format()}")
            betweenTime.start = calendar.timeInMillis
            calendar.add(Calendar.MONTH, 1) // 加1月
            calendar.add(Calendar.SECOND, -1) // 减1秒
            CarLog.d("自定义年月结束时间($year, $month): ${calendar.format()}")
            betweenTime.end = calendar.timeInMillis
        } else {
            calendar.set(Calendar.MONTH, Calendar.JANUARY) // 1月
            CarLog.d("自定义年月开始时间($year, $month): ${calendar.format()}")
            betweenTime.start = calendar.timeInMillis
            calendar.add(Calendar.YEAR, 1) // 加1年
            calendar.add(Calendar.SECOND, -1) // 减1秒
            CarLog.d("自定义年月结束时间($year, $month): ${calendar.format()}")
            betweenTime.end = calendar.timeInMillis
        }
        return betweenTime
    }

    /**
     * 获取当前时间(天时分秒四个字段重置为开始时间)
     */
    private fun get(): Calendar {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = System.currentTimeMillis()
        // 天数调用clear无效，需要设置。天从1开始
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        // 小时调用clear无效，需要设置。小时从0开始
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.clear(Calendar.MINUTE)
        calendar.clear(Calendar.SECOND)
        return calendar
    }
}
