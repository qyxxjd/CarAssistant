@file:Suppress("unused")

package com.classic.assistant.car.extension

import android.app.Activity
import android.content.ComponentName
import android.content.Intent
import android.net.Uri
import android.widget.Toast

/**
 * Share extensions
 *
 * @author Classic
 * @version v1.0, 2019-12-31 15:48
 */

const val MIME_EXCEL_XLS = "application/vnd.ms-excel"
const val MIME_EXCEL_XLSX = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
const val MIME_TEXT = "text/plain"
const val MIME_IMAGE = "image/*"
const val MIME_IMAGE_JPEG = "image/jpg"
const val MIME_AUDIO = "audio/*"
const val MIME_VIDEO = "video/*"
const val MIME_FILE = "*/*"

fun Activity.share(
    contentType: String,
    shareFileUri: Uri? = null,
    contentText: String = "",
    title: String = "",
    requestCode: Int = -1,
    componentPackageName: String = "",
    componentClassName: String = ""
) {
    if (contentType.isEmpty()) return
    if (MIME_TEXT == contentType && contentText.isEmpty()) return

    var shareIntent = Intent()
    shareIntent.action = Intent.ACTION_SEND
    shareIntent.type = contentType
    shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    shareIntent.addCategory(Intent.CATEGORY_DEFAULT)
    if (componentPackageName.isNotEmpty() && componentClassName.isNotEmpty()) {
        shareIntent.component = ComponentName(componentPackageName, componentClassName)
    }
    when (contentType) {
        MIME_TEXT -> {
            shareIntent.putExtra(Intent.EXTRA_TEXT, contentText)
        }
        MIME_IMAGE, MIME_AUDIO, MIME_VIDEO, MIME_EXCEL_XLS, MIME_FILE -> {
            shareIntent.putExtra(Intent.EXTRA_STREAM, shareFileUri)
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        else -> {
            return
        }
    }
    shareIntent = Intent.createChooser(shareIntent, title)
    if (shareIntent.resolveActivity(packageManager) != null) {
        try {
            if (requestCode != -1) {
                startActivityForResult(shareIntent, requestCode)
            } else {
                startActivity(shareIntent)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    } else {
        Toast.makeText(applicationContext, "不支持的文件类型", Toast.LENGTH_SHORT).show()
    }
}