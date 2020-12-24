@file:Suppress("unused")

package com.classic.assistant.car.extension

import android.os.Bundle
import androidx.fragment.app.Fragment
import java.io.Serializable

/**
 * Fragment extensions
 *
 * @author Classic
 * @version v1.0, 2019-05-20 15:48
 */

/** 添加参数 */
fun Fragment.addStringArgument(key: String, value: String) {
    val bundle = Bundle()
    bundle.putString(key, value)
    this.arguments = bundle
}
fun Fragment.addIntArgument(key: String, value: Int) {
    val bundle = Bundle()
    bundle.putInt(key, value)
    this.arguments = bundle
}
fun Fragment.addLongArgument(key: String, value: Long) {
    val bundle = Bundle()
    bundle.putLong(key, value)
    this.arguments = bundle
}
fun Fragment.addDoubleArgument(key: String, value: Double) {
    val bundle = Bundle()
    bundle.putDouble(key, value)
    this.arguments = bundle
}
fun Fragment.addBooleanArgument(key: String, value: Boolean) {
    val bundle = Bundle()
    bundle.putBoolean(key, value)
    this.arguments = bundle
}
fun Fragment.addSerializableArgument(key: String, value: Serializable) {
    val bundle = Bundle()
    bundle.putSerializable(key, value)
    this.arguments = bundle
}

/** 获取参数 */
fun Fragment.getStringArgument(key: String): String {
    return this.arguments?.getString(key) ?: ""
}
fun Fragment.getIntArgument(key: String, defaultValue: Int = 0): Int {
    return this.arguments?.getInt(key, defaultValue) ?: defaultValue
}
fun Fragment.getLongArgument(key: String, defaultValue: Long = 0): Long {
    return this.arguments?.getLong(key, defaultValue) ?: defaultValue
}
fun Fragment.getDoubleArgument(key: String, defaultValue: Double = 0.0): Double {
    return this.arguments?.getDouble(key, defaultValue) ?: defaultValue
}
fun Fragment.getBooleanArgument(key: String, defaultValue: Boolean = false): Boolean {
    return this.arguments?.getBoolean(key, defaultValue) ?: defaultValue
}
fun Fragment.getSerializableArgument(key: String): Serializable? {
    return this.arguments?.getSerializable(key)
}