@file:Suppress("unused")

package com.classic.assistant.car.extension

import android.content.Context
import android.os.Build
import android.provider.Settings
import java.util.UUID

/**
 * Device info extensions
 *
 * @author Classic
 * @version v1.0, 2019-05-20 15:48
 */
fun uuid(): String = UUID.randomUUID().toString()

/** 设备品牌 */
fun brand(): String = Build.BRAND

/** 设备型号 */
fun model(): String = Build.MODEL

/** SDK版本 */
fun sdkVersion(): Int = Build.VERSION.SDK_INT

/** 用户可见的版本字符串 */
fun userVersion(): String = Build.VERSION.RELEASE

/** 硬件名称 */
fun hardware(): String = Build.HARDWARE

fun Context.androidId(): String {
    return Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID) ?: ""
}

fun Context.id(): String {
    var id = androidId()
    if (id.isEmpty()) id = uuid()
    return brand() + "-" + model() + "-" + sdkVersion() + "-" + userVersion() + "-" + hardware() + "_" + id
}
