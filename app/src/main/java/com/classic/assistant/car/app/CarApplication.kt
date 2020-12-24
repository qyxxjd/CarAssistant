
package com.classic.assistant.car.app

import android.app.Application
import com.classic.assistant.car.BuildConfig

/**
 * Car application
 *
 * @author Classic
 * @version v1.0, 2019-05-22 13:12
 */
class CarApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        App.get().apply {
            context = applicationContext
        }
        App.DEBUG = BuildConfig.DEBUG
    }
}
