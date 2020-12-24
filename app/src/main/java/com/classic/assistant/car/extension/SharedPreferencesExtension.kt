@file:Suppress("unused")

package com.classic.assistant.car.extension

import android.content.SharedPreferences

/**
 * SharedPreferences extensions
 *
 * @author Classic
 * @version v1.0, 2019-05-20 15:48
 */

inline fun SharedPreferences.edit(operation: (SharedPreferences.Editor) -> Unit) {
    val editor = this.edit()
    operation(editor)
    editor.apply()
}

operator fun SharedPreferences.set(key: String, value: Any) {
    when (value) {
        is String -> edit { it.putString(key, value) }
        is Int -> edit { it.putInt(key, value) }
        is Boolean -> edit { it.putBoolean(key, value) }
        is Float -> edit { it.putFloat(key, value) }
        is Long -> edit { it.putLong(key, value) }
        else -> throw Exception("Unsupported data type")
    }
}

inline operator fun <reified T : Any> SharedPreferences.get(key: String, defaultValue: T): T? {
    return when (T::class) {
        String::class -> getString(key, defaultValue as String) as T?
        Int::class -> getInt(key, defaultValue as Int) as T?
        Boolean::class -> getBoolean(key, defaultValue as Boolean) as T?
        Float::class -> getFloat(key, defaultValue as Float) as T?
        Long::class -> getLong(key, defaultValue as Long) as T?
        else -> throw Exception("Unsupported data type")
    }
}
