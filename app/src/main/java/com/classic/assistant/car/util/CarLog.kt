package com.classic.assistant.car.util

import android.util.Log
import com.classic.assistant.car.BuildConfig

object CarLog {
    private const val TAG = "CarAssistant"

    fun d(content: String) {
        if (BuildConfig.DEBUG) Log.d(TAG, content)
    }

    fun w(content: String) {
        if (BuildConfig.DEBUG) Log.w(TAG, content)
    }

    fun e(content: String) {
        if (BuildConfig.DEBUG) Log.e(TAG, content)
    }
}
