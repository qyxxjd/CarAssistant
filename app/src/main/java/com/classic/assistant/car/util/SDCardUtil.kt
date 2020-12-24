package com.classic.assistant.car.util

import android.os.Environment
import java.io.File

/**
 * 存储卡工具类
 *
 * @author Classic
 * @version v1.0, 2019/04/12 10:43 AM
 */
object SDCardUtil {
    // 备份文件目录
    private const val BACKUP_DIR = "data"
    // 临时文件目录
    private const val TEMP_DIR = "temp"

    // Checks if a volume containing external storage is available
    // for read and write.
    fun isExternalStorageWritable(): Boolean {
        return Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED
    }

    // Checks if a volume containing external storage is available to at least read.
    fun isExternalStorageReadable(): Boolean {
        return Environment.getExternalStorageState() in
            setOf(Environment.MEDIA_MOUNTED, Environment.MEDIA_MOUNTED_READ_ONLY)
    }

    /**
     * SD卡是否可用
     *
     * @return true:可用,false:不可用
     */
    @Suppress("unused")
    val isCanUseSD: Boolean
        get() {
            try {
                return Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return false
        }

    /**
     * 获取App根目录
     */
    val rootDir: String
        get() {
            val file = File(sdcard(), File.separator + root())
            if (!file.exists()) {
                file.mkdirs()
            }
            return file.absolutePath
        }

    /**
     * 获取备份文件目录
     */
    val backupDir: String
        get() {
            val file = File(sdcard(), File.separator + root() + File.separator + BACKUP_DIR)
            if (!file.exists()) {
                file.mkdirs()
            }
            return file.absolutePath
        }

    /**
     * 获取临时文件目录
     *
     * > 这里存放自动备份文件 小于 最后一次备份文件大小时，为了避免数据丢失，临时存放的地方。后期处理过之后再删除
     */
    val tempDir: String
        get() {
            val file = File(sdcard(), File.separator + root() + File.separator + TEMP_DIR)
            if (!file.exists()) {
                file.mkdirs()
            }
            return file.absolutePath
        }

    /** 清理空文件夹 */
    fun clearDir() {
        val root = File(sdcard(), root())
        if (root.exists()) {
            root.listFiles()?.forEach {
                val childFiles = it.listFiles()
                if (it.isDirectory && childFiles.isNullOrEmpty()) {
                    it.delete()
                }
            }
        }
    }

    private fun sdcard(): String = Environment.getExternalStorageDirectory().absolutePath

    private fun root() = Const.ROOT_DIR
}
