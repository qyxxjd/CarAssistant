@file:Suppress("unused")

package com.classic.assistant.car.extension

import android.Manifest
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Build
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import java.lang.reflect.Field

/**
 * Context extensions
 *
 * @author Classic
 * @version v1.0, 2019-05-20 15:48
 */
val Number.dp: Int
    get() = (toInt() * Resources.getSystem().displayMetrics.density).toInt()

val Number.sp: Float
    get() = toFloat() * Resources.getSystem().displayMetrics.scaledDensity + 0.5f

fun Context.px(dimenResId: Int): Int = resources.getDimensionPixelSize(dimenResId)

fun Context.dp2px(dpValue: Float): Float {
    val scale = resources.displayMetrics.density
    return dpValue * scale + 0.5f
}

fun Context.screenSize(): DisplayMetrics {
    val windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
    val outMetrics = DisplayMetrics()
    windowManager.defaultDisplay.getMetrics(outMetrics)
    return outMetrics
}

fun Context.packageInfo(): PackageInfo = packageManager.getPackageInfo(packageName, 0)
fun Context.versionName(): String = packageInfo().versionName
fun Context.versionCode(): Long = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
    packageInfo().longVersionCode
} else {
    @Suppress("DEPRECATION")
    packageInfo().versionCode.toLong()
}

fun Context.inflate(layoutResId: Int, parent: ViewGroup? = null, attachToRoot: Boolean = false): View =
    LayoutInflater.from(this).inflate(layoutResId, parent, attachToRoot)

fun showToast(context: Context, resId: Int) {
    Toast.makeText(context.applicationContext, resId, Toast.LENGTH_SHORT).show()
}

fun showToast(context: Context, content: String) {
    Toast.makeText(context.applicationContext, content, Toast.LENGTH_SHORT).show()
}

fun showLongToast(context: Context, resId: Int) {
    Toast.makeText(context.applicationContext, resId, Toast.LENGTH_LONG).show()
}

fun showLongToast(context: Context, content: String) {
    Toast.makeText(context.applicationContext, content, Toast.LENGTH_LONG).show()
}

fun Context.toast(resId: Int) {
    showToast(applicationContext, resId)
}

fun Context.toast(content: String) {
    showToast(applicationContext, content)
}


fun Context.longToast(resId: Int) {
    showLongToast(applicationContext, resId)
}

fun Context.longToast(content: String) {
    showLongToast(applicationContext, content)
}

fun Context.customToast(content: String, time: Long = 5000) {
    val toast = Toast.makeText(applicationContext, content, Toast.LENGTH_LONG)
    toast.show()
    globalUITask(time) { toast.cancel() }
}

fun Context.color(resId: Int): Int {
    return ContextCompat.getColor(this, resId)
}

fun Context.drawable(resId: Int): Drawable? {
    return ContextCompat.getDrawable(this, resId)
}

fun Context.string(resId: Int): String {
    return resources.getString(resId)
}

fun Context.string(resId: Int, vararg params: Any): String {
    return resources.getString(resId, *params)
}

// 获取剪切板文本
fun Context.getClipText(): String {
    val clipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager?
    if (null == clipboardManager || !clipboardManager.hasPrimaryClip()) {
        return ""
    }
    val clipData = clipboardManager.primaryClip
    if (null == clipData || clipData.itemCount < 1) {
        return ""
    }
    val clipText = clipData.getItemAt(0)?.text ?: ""
    return clipText.toString()
}

// 清空剪切板文本
fun Context.clearClipText() {
    val clipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager?
    clipboardManager?.setPrimaryClip(ClipData.newPlainText(null, ""))
}

fun Context.isNetworkConnected(): Boolean {
    val manager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val networkInfo: NetworkInfo? = manager.activeNetworkInfo
    @Suppress("DEPRECATION")
    return networkInfo != null && networkInfo.isAvailable
}

fun Context.isPermissionGranted(permission: String): Boolean {
    return ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
}
fun Context.hasExternalPermission(): Boolean {
    return isPermissionGranted(Manifest.permission.READ_EXTERNAL_STORAGE) &&
        isPermissionGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE)
}
fun Context.hasLocationPermission(): Boolean {
    return isPermissionGranted(Manifest.permission.ACCESS_COARSE_LOCATION)
}



/**
 * 修复输入法内存泄漏问题
 */
fun Context.fixInputMethodMemoryLeak() {
    val inputMethodManager: InputMethodManager =
        getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    val viewArr = arrayOf("mCurRootView", "mServedView", "mNextServedView")
    var field: Field?
    var fieldObj: Any?
    for (view in viewArr) {
        try {
            field = inputMethodManager.javaClass.getDeclaredField(view)
            if (null != field && !field.isAccessible) {
                field.isAccessible = true
            }
            fieldObj = field.get(inputMethodManager)
            if (fieldObj != null && fieldObj is View) {
                if (fieldObj.context === this) {
                    //注意需要判断View关联的Context是不是当前Activity，否则有可能造成正常的输入框输入失效
                    field.set(inputMethodManager, null)
                } else {
                    break
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}