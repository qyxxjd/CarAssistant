package com.classic.assistant.car.extension

import android.os.SystemClock
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import com.classic.assistant.car.R

/**
 * View extensions
 *
 * @author Classic
 * @version v1.0, 2019-05-20 15:48
 */

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

fun View.disable() {
    isEnabled = false
}

fun View.enable() {
    isEnabled = true
}

fun View?.applyVisible(visible: Boolean) {
    this?.apply { if (visible) visible() else gone() }
}

fun View.applyPerformClick(itemView: View) {
    setOnTouchListener { _, event ->
        if (MotionEvent.ACTION_UP == event.action) {
            //模拟父控件的点击
            itemView.performClick()
        }
        return@setOnTouchListener false
    }
}

fun View.applySize(width: Int? = null, height: Int? = null) {
    if (null == width && null == height) return
    val lp = this.layoutParams ?: ViewGroup.LayoutParams(width ?: 0, height ?: 0)
    if (null != width) lp.width = width
    if (null != height) lp.width = height
    this.layoutParams = lp
}

fun View.applyHeight(height: Int) {
    val lp = layoutParams
    lp.height = height
    this.layoutParams = lp
}

fun View.applyMargin(left: Int, top: Int, right: Int, bottom: Int) {
    val lp = layoutParams
    if (lp is ViewGroup.MarginLayoutParams) {
        lp.setMargins(left, top, right, bottom)
        this.layoutParams = lp
    }
}
fun View.applyEqualMargin(margin: Int) {
    applyMargin(margin, margin, margin, margin)
}

fun throttleClick(wait: Long = 200, block: ((View) -> Unit)): View.OnClickListener {

    return View.OnClickListener { v ->
        val current = SystemClock.uptimeMillis()
        val lastClickTime = (v.getTag(R.id.click_timestamp) as? Long) ?: 0
        if (current - lastClickTime > wait) {
            v.setTag(R.id.click_timestamp, current)
            block(v)
        }
    }
}

fun debounceClick(wait: Long = 200, block: ((View) -> Unit)): View.OnClickListener {
    return View.OnClickListener { v ->
        var action = (v.getTag(R.id.click_debounce_action) as? DebounceAction)
        if(action == null){
            action = DebounceAction(v, block)
            v.setTag(R.id.click_debounce_action, action)
        }else{
            action.block = block
        }
        v.removeCallbacks(action)
        v.postDelayed(action, wait)
    }
}

class DebounceAction(val view: View,  var block: ((View) -> Unit)): Runnable {
    override fun run() {
        if(view.isAttachedToWindow){
            block(view)
        }
    }
}

fun View.onClick(wait: Long = 200, block: ((View) -> Unit)) {
    setOnClickListener(throttleClick(wait, block))
}

fun View.onDebounceClick(wait: Long = 200, block: ((View) -> Unit)) {
    setOnClickListener(debounceClick(wait, block))
}