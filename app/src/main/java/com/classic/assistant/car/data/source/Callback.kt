package com.classic.assistant.car.data.source

/**
 * 回调
 *
 * @author Classic
 * @version v1.0, 2019-07-22 18:07
 */
interface Callback<T> {

    fun onSuccess(t: T)

    fun onFailure(t: Throwable)
}
