package com.classic.assistant.car.data.manager

import com.classic.assistant.car.data.source.local.db.ConsumptionInfo
import java.util.concurrent.CopyOnWriteArrayList

/**
 * 数据管理
 *
 * @author Classic
 * @version v1.0, 2019/9/2 3:02 PM
 */
class DataManager {
    private val dataChangeListeners = CopyOnWriteArrayList<DataChangeListener>()

    fun addDataChangeListener(listener: DataChangeListener) {
        dataChangeListeners.add(listener)
    }

    fun removeDataChangeListener(listener: DataChangeListener) {
        dataChangeListeners.remove(listener)
    }

    fun dispatchAdd(info: ConsumptionInfo) {
        if (dataChangeListeners.isEmpty()) return
        for (listener in dataChangeListeners) {
            listener.onAdd(info)
        }
    }

    fun dispatchUpdate(info: ConsumptionInfo) {
        if (dataChangeListeners.isEmpty()) return
        for (listener in dataChangeListeners) {
            listener.onUpdate(info)
        }
    }

    fun dispatchDelete(info: ConsumptionInfo) {
        if (dataChangeListeners.isEmpty()) return
        for (listener in dataChangeListeners) {
            listener.onDelete(info)
        }
    }

    fun dispatchBackup(list: MutableList<ConsumptionInfo>) {
        if (dataChangeListeners.isEmpty()) return
        for (listener in dataChangeListeners) {
            listener.onBackup(list)
        }
    }

    fun dispatchRestore(insert: MutableList<ConsumptionInfo>, update: MutableList<ConsumptionInfo>) {
        if (dataChangeListeners.isEmpty()) return
        for (listener in dataChangeListeners) {
            listener.onRestore(insert, update)
        }
    }

    companion object {
        private val INSTANCE: DataManager by lazy { DataManager() }
        fun get() = INSTANCE
    }
}

/**
 * 数据改变事件
 */
interface DataChangeListener {
    fun onAdd(info: ConsumptionInfo)
    fun onUpdate(info: ConsumptionInfo)
    fun onDelete(info: ConsumptionInfo)
    fun onBackup(list: MutableList<ConsumptionInfo>)
    fun onRestore(insert: MutableList<ConsumptionInfo>, update: MutableList<ConsumptionInfo>)
}
