package com.classic.assistant.car.data.source

import android.content.Context
import com.classic.assistant.car.data.manager.RestoreResult
import com.classic.assistant.car.data.source.local.LocalDataSource
import com.classic.assistant.car.data.source.local.db.ConsumptionInfo

/**
 * 数据源
 *
 * @author Classic
 * @version v1.0, 2019-07-22 17:43
 */
interface DataSource {

    fun add(info: ConsumptionInfo): Long?

    fun modify(info: ConsumptionInfo): Int

    fun delete(info: ConsumptionInfo): Int

    fun queryTop(startTime: Long, endTime: Long, size: Int): MutableList<ConsumptionInfo>
    // fun queryAll(desc: Boolean?): MutableList<ConsumptionInfo>

    fun queryAll(startTime: Long, endTime: Long, desc: Boolean?): MutableList<ConsumptionInfo>

    fun queryFuelConsumption(startTime: Long, endTime: Long): MutableList<ConsumptionInfo>

    fun query(type: Int): MutableList<ConsumptionInfo>

    fun restore(list: MutableList<ConsumptionInfo>): RestoreResult

    companion object {
        private var INSTANCE: DataSource? = null
        fun get(context: Context): DataSource {
            if (INSTANCE == null) {
                synchronized(DataSource::class) {
                    INSTANCE = LocalDataSource(context)
                }
            }
            return INSTANCE!!
        }

        fun destroy() {
            INSTANCE = null
        }
    }
}
