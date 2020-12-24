@file:Suppress("unused")

package com.classic.assistant.car.data.source.local.db

import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE

/**
 * 消费信息数据操作
 *
 * @author Classic
 * @version v1.0, 2018/7/14 上午10:33
 */
@Dao
interface ConsumptionDao {

    /**
     * 按创建时间查询
     */
    @Query("SELECT * FROM t_consumption_info WHERE createTime = :createTime")
    fun queryByTime(createTime: Long): ConsumptionInfo?

    /**
     * 查询所有数据
     */
    @Query("SELECT * FROM t_consumption_info WHERE time BETWEEN :startTime AND :endTime")
    fun queryAll(startTime: Long, endTime: Long): MutableList<ConsumptionInfo>

    /**
     * 查询所有数据（按时间正序排列）
     */
    @Query("SELECT * FROM t_consumption_info WHERE time BETWEEN :startTime AND :endTime ORDER BY time")
    fun queryAllAsc(startTime: Long, endTime: Long): MutableList<ConsumptionInfo>

    /**
     * 查询所有数据（按时间倒序排列）
     */
    @Query("SELECT * FROM t_consumption_info WHERE time BETWEEN :startTime AND :endTime ORDER BY time DESC")
    fun queryAllDesc(startTime: Long, endTime: Long): MutableList<ConsumptionInfo>

    /**
     * 查询油耗数据列表
     */
    @Query("SELECT * FROM t_consumption_info WHERE type = :type AND time BETWEEN :startTime AND :endTime ORDER BY time")
    fun queryFuelConsumption(type: Int, startTime: Long, endTime: Long): MutableList<ConsumptionInfo>

    /**
     * 按类型查询所有数据（按时间倒序排列）
     */
    @Query("SELECT * FROM t_consumption_info WHERE type = :type ORDER BY time DESC")
    fun queryByType(type: Int): MutableList<ConsumptionInfo>

    /**
     * 查询金额最大的前几条数据
     */
    @Query("SELECT * FROM t_consumption_info WHERE time BETWEEN :startTime AND :endTime ORDER BY amount DESC LIMIT :size")
    fun queryTop(startTime: Long, endTime: Long, size: Int): MutableList<ConsumptionInfo>

    /**
     * 按类型查询所有数据（按时间倒序排列）
     */
    @Query("SELECT * FROM t_consumption_info WHERE type = :type ORDER BY time DESC")
    fun queryType(type: Int): MutableList<ConsumptionInfo>

    /**
     * 时间区间查询数据（按时间倒序排列）
     */
    @Query("SELECT * FROM t_consumption_info WHERE time BETWEEN :startTime AND :endTime ORDER BY time DESC")
    fun queryBetweenTime(startTime: Long, endTime: Long): MutableList<ConsumptionInfo>

    // /**
    //  * 按类型、时间区间查询数据（按时间倒序排列）
    //  */
    // @Query("SELECT * FROM t_consumption_info WHERE type = :type AND time BETWEEN :startTime AND :endTime ORDER BY time DESC")
    // fun queryDesc(type: Int, startTime: Long, endTime: Long): MutableList<ConsumptionInfo>

    @Insert(onConflict = REPLACE)
    fun insert(info: ConsumptionInfo): Long?

    @Insert(onConflict = REPLACE)
    fun insert(vararg info: ConsumptionInfo): Array<Long>

    @Update
    fun update(info: ConsumptionInfo): Int

    @Delete
    fun delete(info: ConsumptionInfo): Int
}
