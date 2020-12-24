package com.classic.assistant.car.data.manager

import android.content.Context
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.amap.api.location.AMapLocationListener

/**
 * 定位管理
 *
 * @author Classic
 * @version v1.0, 2019-07-28 15:57
 */
class LocationManager private constructor() {

    companion object {
        private var INSTANCE: LocationManager? = null
        fun get() = INSTANCE ?: LocationManager().also { INSTANCE = it }
    }

    private var client: AMapLocationClient? = null
    private var option: AMapLocationClientOption? = null

    fun init(context: Context) {
        client = AMapLocationClient(context)
        option = AMapLocationClientOption()
        option?.apply {
            // 可选，设置定位模式，可选的模式有高精度、仅设备、仅网络。默认为高精度模式
            locationMode = AMapLocationClientOption.AMapLocationMode.Hight_Accuracy
            // 可选，设置网络请求超时时间。默认为30秒。在仅设备模式下无效
            httpTimeOut = 10000L
            // 可选，设置是否单次定位。默认是false
            isOnceLocation = true
            // 可选，设置是否使用缓存定位，默认为true
            isLocationCacheEnable = true
        }
        client?.setLocationOption(option)
    }

    fun start(listener: AMapLocationListener) {
        client?.setLocationListener(listener)
        client?.startLocation()
    }

    fun stop() {
        client?.stopLocation()
    }

    fun destroy() {
        client?.onDestroy()
        client = null
        option = null
    }
}
