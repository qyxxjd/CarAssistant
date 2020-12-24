package com.classic.assistant.car.data.manager

import android.content.Context
import android.content.SharedPreferences
import com.classic.assistant.car.app.App

/**
 * 配置管理
 *
 * @author Classic
 * @version v1.0, 2019-05-27 16:59
 */
object Config {
    private val preferences: SharedPreferences by lazy {
        App.get().context.getSharedPreferences(Const.SP_NAME, Context.MODE_PRIVATE)
    }

    fun get() = preferences
}
