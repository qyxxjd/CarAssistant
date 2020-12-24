@file:Suppress("unused")

package com.classic.assistant.car.extension

import android.os.Looper

/**
 * Thread extensions
 *
 * @author LiuBin
 * @version v1.0, 2019-05-20 15:48
 */
fun isMainThread(): Boolean = Looper.myLooper() == Looper.getMainLooper()