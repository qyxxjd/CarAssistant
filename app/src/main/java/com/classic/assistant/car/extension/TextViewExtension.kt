package com.classic.assistant.car.extension

import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.text.Html
import android.widget.TextView

/**
 * TextView extensions
 *
 * @author Classic
 * @version v1.0, 2019-05-20 15:48
 */
// 添加：删除线、中横线
fun TextView.addDeleteLine() {
    val paintFlags = this.paintFlags
    this.paint.flags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
}
// 移除：删除线、中横线
fun TextView.removeDeleteLine() {
    val paintFlags = this.paintFlags
    this.paint.flags = paintFlags and (Paint.STRIKE_THRU_TEXT_FLAG.inv())
}
// 添加：下划线
fun TextView.addUnderLine() {
    val paintFlags = this.paintFlags
    this.paint.flags = paintFlags or Paint.UNDERLINE_TEXT_FLAG
}
// 移除：下划线
fun TextView.removeUnderLine() {
    val paintFlags = this.paintFlags
    this.paint.flags = paintFlags and (Paint.UNDERLINE_TEXT_FLAG.inv())
}
// 移除：下划线
fun TextView.applyDrawable(left: Drawable? = null, top: Drawable? = null,
    right: Drawable? = null, bottom: Drawable? = null) {
    applyDrawableBounds(left)
    applyDrawableBounds(top)
    applyDrawableBounds(right)
    applyDrawableBounds(bottom)
    setCompoundDrawables(left, top, right, bottom)
}
private fun applyDrawableBounds(drawable: Drawable?) {
    drawable?.setBounds(0, 0, drawable.minimumWidth, drawable.minimumHeight)
}

fun TextView.clearDrawable() {
    // setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
    setCompoundDrawables(null, null, null, null)
}

fun TextView.applyHtml(content: String) {
    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
        this.text = Html.fromHtml(content, Html.FROM_HTML_MODE_LEGACY)
    } else {
        @Suppress("DEPRECATION")
        this.text = Html.fromHtml(content)
    }
}