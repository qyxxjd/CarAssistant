@file:Suppress("unused")

package com.classic.assistant.car.extension

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.Settings

/**
 * Intent extensions
 *
 * @author Classic
 * @version v1.0, 2019-05-20 15:48
 */

/** 打开应用详情页面 */
fun Activity.openAppDetail() {
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
    intent.data = Uri.fromParts("package", packageName, null)
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    startActivity(intent)
}
/** 进入拨号界面 */
fun Activity.openDial(phone: String) {
    val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phone"))
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    startActivity(intent)
}
/** 系统浏览器打开Url */
fun Activity.openUrl(url: String?) {
    if (url.isNullOrEmpty()) {
        toast("url为空")
        return
    }
    try {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

/**
 * 打开Url
 *
 * > 优先查找系统能解析此url的组件
 * > 其次App内打开 `failureUrl`
 * > `failureUrl` 为空时跳转 `downloadAppUrl` 下载App
 */
fun Activity.openUrl(url: String?, failureUrl: String?, downloadAppUrl: String?) {
    if (url.isNullOrEmpty()) {
        toast("url为空")
        return
    }
    try {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}
/**
 * 打开Url
 *
 * > 优先查找系统能解析此url的组件
 * > 其次系统浏览器内打开 `failureUrl`
 * > `failureUrl` 为空时跳转 `downloadAppUrl` 下载App
 */
fun Activity.openUrlWithSystem(url: String?, failureUrl: String?, downloadAppUrl: String?) {
    if (url.isNullOrEmpty()) {
        toast("url为空")
        return
    }
    try {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    } catch (e: Exception) {
        openUrl(failureUrl)
        if (failureUrl.isNullOrEmpty()) {
            openUrl(downloadAppUrl)
        } else openUrl(failureUrl)
    }
}