@file:Suppress("unused")

package com.classic.assistant.car.extension

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.Color
import android.net.Uri
import java.util.regex.Pattern

/**
 * String extensions
 *
 * @author Classic
 * @version v1.0, 2019-05-20 15:48
 */

/** 复制文本到剪贴板 */
fun String.copyToClip(activity: Activity): Boolean {
    if (this.isEmpty()) return false

    val clipboardManager = activity.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    return try {
        val clipData = ClipData.newPlainText("text", this)
        clipboardManager.setPrimaryClip(clipData)
        activity.toast("复制成功")
        true
    } catch (e: Exception) {
        false
    }
}

fun String.eq(target: String?): Boolean {
    return target.equals(this, true)
}

// 获取Http链接后面拼接的参数值
fun String?.getUrlParams(key: String, defaultValue: String = ""): String {
    if (this.isNullOrEmpty()) return defaultValue
    val uri = Uri.parse(this)
    return uri.getQueryParameter(key) ?: defaultValue
}
// Http链接后面拼接参数值
fun String?.appendUrlParams(key: String, value: String): String {
    if (this.isNullOrEmpty()) return ""
    return if (this.contains("?")) "${this}&${key}=${value}" else "${this}?${key}=${value}"
}

fun String?.withMaxLength(maxLength: Int, suffix: String = ""): String {
    if (this.isNullOrEmpty()) return ""
    return try {
        if (this.length > maxLength) (this.substring(0, maxLength) + suffix) else this
    } catch (e: Exception) {
        this
    }
}

/** 格式化手机号，隐藏中间4位 */
fun String?.formatPhone(): String {
    if (this.isNullOrEmpty()) return ""
    return try {
        this.substring(0, 3) + "****" + this.substring(7)
    } catch (e: Exception) {
        this
    }
}


fun String?.filterPhone(): String {
    try {
        if (this.isNullOrEmpty()) return ""
        // 找到所有符合正则表达式的字符串
        var list = this.findPhone()
        var result: String
        if (list.isEmpty()) {
            // 如果没找到，替换所有空格后，再查找一遍
            result = this.replace(" ", "")
            list = result.findPhone()
        } else {
            result = this
        }
        list.forEach {
            result = result.replace(it, it.formatPhone())
        }
        return result
    } catch (e: Exception) {
        return this ?: ""
    }
}
fun String?.findPhone(pattern: Pattern = Pattern.compile("1\\d{10}")): MutableList<String> {
    val result = mutableListOf<String>()
    if (this.isNullOrEmpty()) return result
    val matcher = pattern.matcher(this)
    while (matcher.find()) {
        val text = matcher.group()
        result.add(text)
    }
    return result
}

/**
 * 拆分逗号分隔的数据
 */
fun String?.splitComma(): List<String> {
    if (null == this) return mutableListOf()
    return if (this.indexOf(",") != -1) this.split(",") else mutableListOf(this)
}

/**
 * 安全数据转换
 */
fun String?.safeConvertInt(defaultValue: Int = 0): Int {
    if (this.isNullOrEmpty()) return defaultValue
    return try {
        this.toInt()
    } catch (e: Exception) {
        defaultValue
    }
}
fun String?.safeConvertLong(defaultValue: Long = 0): Long {
    if (this.isNullOrEmpty()) return defaultValue
    return try {
        this.toLong()
    } catch (e: Exception) {
        defaultValue
    }
}
fun String?.safeConvertDouble(defaultValue: Double = 0.0): Double {
    if (this.isNullOrEmpty()) return defaultValue
    return try {
        this.toDouble()
    } catch (e: Exception) {
        defaultValue
    }
}
fun String?.safeUrl(): String {
    if (this.isNullOrEmpty()) return ""
    if (this.startsWith("//", true)) return "http:$this"
    return this
}

fun String?.toColor(defaultValue: Int = -1): Int {
    if (this.isNullOrEmpty() || !startsWith("#")) return defaultValue
    return try {
        Color.parseColor(this)
    } catch (e: Exception) {
        defaultValue
    }
}