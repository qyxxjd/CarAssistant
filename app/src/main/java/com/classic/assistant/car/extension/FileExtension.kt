@file:Suppress("unused")

package com.classic.assistant.car.extension

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import java.io.File
import java.io.InputStream

/**
 * File extensions
 *
 * @author Classic
 * @version v1.0, 2019-05-20 15:48
 */

/** 保存 InputStream 到文件 */
fun InputStream.toFile(file: File) {
    file.outputStream().use {
        this.copyTo(it)
    }
}

fun File.deleteAsync() {
    if (!exists()) return
    globalIOTask {
        // 如果是目录，递归删除目录。如果是文件，删除文件
        if (this.isDirectory) deleteRecursively() else delete()
    }
}

fun File?.saveToGallery(context: Context) {
    if (null == this) return
    try {
        MediaStore.Images.Media.insertImage(
            context.contentResolver, this.absolutePath, this.name, null)
        scanFile(context)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}
fun File?.scanFile(context: Context) {
    if (null == this || !this.exists()) return
    context.sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(this)))
}
fun Context.scanFile(file: File?) {
    if (null == file || !file.exists()) return
    sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)))
}
