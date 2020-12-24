package com.classic.assistant.car.util

import android.content.Context
import com.afollestad.materialdialogs.DialogCallback
import com.afollestad.materialdialogs.MaterialDialog
import com.classic.assistant.car.R

/**
 * Dialog util
 *
 * @author Classic
 * @version v1.0, 2019-05-09 19:55
 */
object DialogUtil {

    fun default(context: Context, titleResId: Int, contentResId: Int, cancelable: Boolean = false): MaterialDialog {
        return MaterialDialog(context).show {
            cancelable(cancelable)
            title(titleResId)
            message(contentResId)
        }
    }

    fun content(context: Context, contentResId: Int, cancelable: Boolean = false): MaterialDialog {
        return MaterialDialog(context).show {
            cancelable(cancelable)
            message(contentResId)
        }
    }

    fun confirm(context: Context, title: String, contentResId: Int, cancelable: Boolean = false, callback: DialogCallback? = null): MaterialDialog {
        return MaterialDialog(context).show {
            cancelable(cancelable)
            title(text = title)
            message(contentResId)
            positiveButton(R.string.confirm, click = object : DialogCallback {
                override fun invoke(p1: MaterialDialog) {
                    p1.dismiss()
                    callback?.invoke(p1)
                }
            })
        }
    }
}
