@file:Suppress("unused")

package com.classic.assistant.car.extension

import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DecimalFormat

/**
 * BigDecimal extensions
 *
 * @author Classic
 * @version v1.0, 2019-05-20 15:48
 */

const val DEFAULT_SCALE: Int = 2

/**
 * 四舍五入
 *
 * @param scale 精确到小数点后几位
 * @param mode 舍入的模式
 * @see RoundingMode
 */
fun String.round(scale: Int = DEFAULT_SCALE, mode: Int = BigDecimal.ROUND_HALF_UP): String =
    BigDecimal(this).setScale(scale, mode).toString()
fun Float.round(scale: Int = DEFAULT_SCALE, mode: Int = BigDecimal.ROUND_HALF_UP): Float =
    BigDecimal(this.toString()).setScale(scale, mode).toFloat()
fun Double.round(scale: Int = DEFAULT_SCALE, mode: Int = BigDecimal.ROUND_HALF_UP): Double =
    BigDecimal(this.toString()).setScale(scale, mode).toDouble()

/**
 * 四舍五入，并去掉小数点后无效的0
 *
 * @param scale 精确到小数点后几位
 * @param mode 舍入的模式
 * @see RoundingMode
 */
fun String?.replace(scale: Int = 0, mode: Int = BigDecimal.ROUND_HALF_UP): String {
    if (this.isNullOrEmpty()) return "0"
    return try {
        var result = if (scale > 0) round(scale, mode) else this
        if (result.indexOf(".") > 0) {
            result = result.replace("0+?$".toRegex(), "") // 去掉后面无用的零
            result = result.replace("[.]$".toRegex(), "") // 如小数点后面全是零则去掉小数点
        }
        result
    } catch (e: NumberFormatException) {
        "0"
    }
}

/**
 * 四舍五入，并去掉小数点后无效的0
 *
 * @param scale 精确到小数点后几位
 * @param mode 舍入的模式
 * @see RoundingMode
 */
fun Number.replace(scale: Int = 0, mode: Int = BigDecimal.ROUND_HALF_UP): String = this.toString().replace(scale, mode)

/**
 * 通用金额显示
 */
fun Number.toPrice(): String = "¥${this.toText()}"
/**
 * 通用负金额显示
 */
fun Number.toNegativePrice(): String = "-¥${this.toText()}"

/**
 * 赚多少钱通用显示
 */
fun Number.toMakeMoney(): String = "赚 ¥${this.replace(DEFAULT_SCALE)}"
/**
 * 数字转字符串，精确到小数点后2位，去掉小数点后无效的0
 */
fun Number.toText(): String = this.replace(DEFAULT_SCALE)

/**
 * 排除负金额显示，最小显示0
 *
 * > 支付场景，使用超过支付金额的优惠券时，会出现负数，需要处理
 */
fun Double.avoidNegativePrice(): String {
    return if (this < 0.0) "¥0" else this.toPrice()
}

fun Int.convertW(label: String = ""): String {
    return when {
        this > 1000_0000 -> "${(this / 10000)}w$label"
        this > 10000 -> "${(this / 10000.0).replace(1)}w$label"
        else -> "${this}${label}"
    }
}

fun Long.convertW(label: String = ""): String {
    return when {
        this > 1000_0000 -> "${(this / 10000)}w$label"
        this > 10000 -> "${(this / 10000.0).replace(1)}w$label"
        else -> "${this}${label}"
    }
}
fun String?.convertW(label: String = ""): String {
    if (this.isNullOrEmpty()) return "0${label}"
    return try {
        val number = this.toInt()
        when {
            number > 1000_0000 -> "${(number / 10000)}w$label"
            number > 10000 -> "${(number / 10000.0).replace(1)}w$label"
            else -> "${number}${label}"
        }
    } catch (e: Exception) {
        this
    }
}

/**
 * 使用逗号格式化数字
 *
 * > 1666888999 -> "1,666,888,999"
 */
fun Long?.formatComma(): String {
    if (null == this) return ""
    return DecimalFormat("###,##0").format(this)
}