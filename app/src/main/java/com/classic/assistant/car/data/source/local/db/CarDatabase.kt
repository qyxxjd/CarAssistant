@file:Suppress("unused")

package com.classic.assistant.car.data.source.local.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * DB
 *
 * @author Classic
 * @version v1.0, 2018/7/14 上午11:43
 */
@Database(entities = [(ConsumptionInfo::class)], version = 1)
abstract class CarDatabase : RoomDatabase() {

    abstract fun dao(): ConsumptionDao

    companion object {
        private const val DB_NAME = "CarAssistant.db"
        private var INSTANCE: CarDatabase? = null

        fun get(context: Context): CarDatabase {
            if (INSTANCE == null) {
                synchronized(CarDatabase::class) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                            CarDatabase::class.java, DB_NAME)
                            .build()
                }
            }
            return INSTANCE!!
        }

        fun destroy() {
            INSTANCE = null
        }
    }
}
