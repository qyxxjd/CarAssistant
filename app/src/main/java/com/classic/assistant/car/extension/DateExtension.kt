@file:Suppress("unused")

package com.classic.assistant.car.extension

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

/**
 * Date extensions
 *
 * @author Classic
 * @version v1.0, 2019-05-20 15:48
 */

const val PATTERN_DATE = "yyyy-MM-dd"
const val PATTERN_MONTH_DAY = "MM-dd"
const val PATTERN_TIME = "HH:mm:ss"
const val PATTERN_MINUTE = "HH:mm"
const val PATTERN_DATE_TIME = "yyyy-MM-dd HH:mm:ss"
@Suppress("SpellCheckingInspection")
const val PATTERN_DATE_TIME_SIMPLE = "yyyyMMddHHmmss"

fun calendar(time: Long = System.currentTimeMillis()): Calendar = Calendar.getInstance().apply { timeInMillis = time }

fun calendar(date: Date): Calendar = Calendar.getInstance().apply { time = date }

fun year(): Int = calendar().get(Calendar.YEAR)
/** Month(0-11) */
fun month(): Int = calendar().get(Calendar.MONTH)
fun day(): Int = calendar().get(Calendar.DAY_OF_MONTH)
fun hour(): Int = calendar().get(Calendar.HOUR_OF_DAY)
fun minute(): Int = calendar().get(Calendar.MINUTE)
fun second(): Int = calendar().get(Calendar.SECOND)
fun week(): Int = calendar().get(Calendar.DAY_OF_WEEK)

fun Calendar.format(pattern: String = PATTERN_DATE_TIME): String {
    return this.time.format(pattern)
}
fun Date.format(pattern: String = PATTERN_DATE_TIME): String = SimpleDateFormat(pattern, Locale.CHINA).format(this)
fun Long?.format(pattern: String = PATTERN_DATE_TIME): String {
    if (null == this) return ""
    return when (this.toString().length) {
        13 -> {
            Date(this).format(pattern)
        }
        10 -> {
            Date(this * 1000).format(pattern)
        }
        else -> ""
    }
}
fun String.parse(pattern: String = PATTERN_DATE_TIME): Date = try {
    SimpleDateFormat(pattern, Locale.CHINA).parse(this)!!
} catch (e: Exception) {
    Date(0L)
}

fun String?.convert(sourcePattern: String, newPattern: String): String {
    if (this.isNullOrEmpty()) return ""
    return try {
        val date = this.parse(sourcePattern)
        date.format(newPattern)
    } catch (e: Exception) {
        ""
    }
}

/** 获取一天的开始时间(00:00:00)  */
fun Date.start(): Long {
    val calendar = calendar(this)
    calendar.set(Calendar.HOUR_OF_DAY, 0)
    calendar.set(Calendar.MINUTE, 0)
    calendar.set(Calendar.SECOND, 0)
    return calendar.timeInMillis
}

/** 获取一天的结束时间(23:59:59)  */
fun Date.end(): Long {
    val calendar = calendar(this)
    calendar.set(Calendar.HOUR_OF_DAY, 23)
    calendar.set(Calendar.MINUTE, 59)
    calendar.set(Calendar.SECOND, 59)
    return calendar.timeInMillis
}

/** 日期加减计算  */
fun Long.add(offset: Int, filed: Int = Calendar.DAY_OF_MONTH): Long {
    val calendar = calendar(this)
    calendar.add(filed, offset)
    return calendar.timeInMillis
}
/** 日期加减计算  */
fun Date.add(offset: Int, filed: Int = Calendar.DAY_OF_MONTH): Long {
    val calendar = calendar(this)
    calendar.add(filed, offset)
    return calendar.timeInMillis
}

/** 获取时间，精确到分钟(秒重置为0) */
fun Long.accurateToMinute(): Long {
    val calendar = calendar(this)
    calendar.set(Calendar.SECOND, 0)
    return calendar.timeInMillis
}

/**
 * 根据日期获得星期
 */
fun Long.getDayOfWeek(): Int {
    val calendar = calendar(this)
    var dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1
    if (dayOfWeek < 0) {
        dayOfWeek = 0
    }
    return dayOfWeek
}
