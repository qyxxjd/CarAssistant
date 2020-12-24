
package com.classic.assistant.car.app

import android.content.Context

internal class App {
    companion object {
        var DEBUG: Boolean = false
        private val INSTANCE: App by lazy { App() }
        fun get() = INSTANCE
    }
    lateinit var context: Context
}
