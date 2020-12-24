package com.classic.assistant.car.data.source.local

import android.content.Context
import com.classic.assistant.car.data.manager.RestoreResult
import com.classic.assistant.car.data.source.DataSource
import com.classic.assistant.car.data.source.local.db.CarDatabase
import com.classic.assistant.car.data.source.local.db.ConsumptionDao
import com.classic.assistant.car.data.source.local.db.ConsumptionInfo
import com.classic.assistant.car.util.CarLog

/**
 * 本地数据源
 *
 * @author Classic
 * @version v1.0, 2019-07-22 17:45
 */
class LocalDataSource(context: Context) : DataSource {

    private val db: CarDatabase by lazy { CarDatabase.get(context) }
    private val dao: ConsumptionDao by lazy { db.dao() }

    override fun add(info: ConsumptionInfo): Long? = dao.insert(info)

    override fun modify(info: ConsumptionInfo): Int = dao.update(info)

    override fun delete(info: ConsumptionInfo): Int = dao.delete(info)

    override fun queryTop(startTime: Long, endTime: Long, size: Int): MutableList<ConsumptionInfo> {
        return dao.queryTop(startTime, endTime, size)
    }

    override fun queryAll(
        startTime: Long,
        endTime: Long,
        desc: Boolean?
    ): MutableList<ConsumptionInfo> {
        if (null == desc) {
            return dao.queryAll(startTime, endTime)
        }
        return if (desc) dao.queryAllDesc(startTime, endTime) else dao.queryAllAsc(startTime, endTime)
    }

    override fun queryFuelConsumption(
        startTime: Long,
        endTime: Long
    ): MutableList<ConsumptionInfo> {
        return dao.queryFuelConsumption(ConsumptionInfo.TYPE_FUEL, startTime, endTime)
    }

    override fun query(type: Int): MutableList<ConsumptionInfo> {
        return dao.queryByType(type)
    }

    override fun restore(list: MutableList<ConsumptionInfo>): RestoreResult {
        val insertList = mutableListOf<ConsumptionInfo>()
        val updateList = mutableListOf<ConsumptionInfo>()
        db.runInTransaction {
            list.forEach {
                val target = dao.queryByTime(it.createTime)
                when {
                    null == target -> dao.insert(it)?.apply {
                        it.id = this
                        insertList.add(it)
                        CarLog.d("(插入)数据：($this)  $it")
                    }
                    it.lastUpdateTime > target.lastUpdateTime -> {
                        dao.update(it)
                        updateList.add(it)
                        CarLog.d("(更新)要恢复的数据高于DB数据，进行数据更新: $it \n DB数据: $target")
                    }
                    else -> CarLog.d("(跳过)要恢复的数据小于DB数据。旧数据: $it \n DB数据: $target")
                }
            }
        }
        return RestoreResult(insertList, updateList)
    }

    // override fun queryAll(desc: Boolean): MutableList<ConsumptionInfo> {
    //     return if (desc) dao.queryAllDesc() else dao.queryAll()
    // }
    //
    // override fun query(
    //     type: Int?,
    //     startTime: Long?,
    //     endTime: Long?,
    //     desc: Boolean?
    // ): MutableList<ConsumptionInfo> {
    //     val queryAllTime = null == startTime || null == endTime
    //     if (null == type && queryAllTime) {
    //         if (null != desc && desc) {
    //             return dao.queryAllDesc()
    //         } else {
    //             return dao.queryAll()
    //         }
    //     }
    //     // if (null != type)
    //
    // }

    // override fun add(info: ConsumptionInfo, callback: Callback<Long?>) {
    //     execute(callback) { dao.insert(info) }
    // }
    //
    // override fun modify(info: ConsumptionInfo, callback: Callback<Int>) {
    //     execute(callback) { dao.update(info) }
    // }
    //
    // override fun delete(info: ConsumptionInfo, callback: Callback<Int>) {
    //     execute(callback) { dao.delete(info) }
    // }
    //
    // override fun queryAll(callback: Callback<MutableList<ConsumptionInfo>>) {
    //     execute(callback) { dao.queryAllDesc() }
    // }
    //
    //
    // private fun <T> execute(callback: Callback<T>, task: suspend () -> T) {
    //     taskOnIO {
    //         try {
    //             val result = task()
    //             taskOnUI { callback.onSuccess(result) }
    //         } catch (e: Exception) {
    //             taskOnUI { callback.onFailure(e) }
    //         }
    //     }
    // }
}
