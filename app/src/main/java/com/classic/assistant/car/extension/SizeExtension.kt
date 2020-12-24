@file:Suppress("unused")

package com.classic.assistant.car.extension

import java.text.DecimalFormat

/**
 * Size extensions
 *
 * @author Classic
 * @version v1.0, 2019-05-20 15:48
 */
/**
 * GB转字节
 */
const val GB_2_BYTE: Long = 1073741824
/**
 * MB转字节
 */
const val MB_2_BYTE: Long = 1048576
/**
 * KB转字节
 */
const val KB_2_BYTE: Long = 1024
/**
 * 秒转毫秒
 */
const val SECOND_2_MS: Long = 1000
/**
 * 分钟转毫秒
 */
const val MINUTE_2_MS: Long = 60000
/**
 * 小时转毫秒
 */
const val HOUR_2_MS: Long = 3600000
/**
 * 天转毫秒
 */
const val DAY_2_MS: Long = 86400000

fun Long.formatByte(): String {
    val df = DecimalFormat("#.00")
    return when {
        this >= GB_2_BYTE -> df.format(this.toDouble() / GB_2_BYTE).replace() + "G"
        this >= MB_2_BYTE -> df.format(this.toDouble() / MB_2_BYTE).replace() + "M"
        this >= KB_2_BYTE -> df.format(this.toDouble() / KB_2_BYTE).replace() + "K"
        else -> df.format(this.toDouble()).replace() + "B"
    }
}