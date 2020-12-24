package com.classic.assistant.car.ui.base

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Deferred

/**
 * ViewModel
 *
 * @author LiuBin
 * @version v1.0, 2020/6/11 4:10 PM
 */
open class AppViewModel : ViewModel() {
    override fun onCleared() {
        super.onCleared()
        unsubscribe()
        for (item in tasks) {
            cancel(item)
        }
    }

    open fun subscribe() {}
    open fun unsubscribe() {}

    private val tasks = mutableListOf<Deferred<*>?>()
    protected fun cancel(deferred: Deferred<*>?) {
        if (null != deferred && !deferred.isCancelled) {
            deferred.cancel()
        }
    }
}