@file:Suppress("unused")

package com.classic.assistant.car.extension

import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.widget.EditText

/**
 * EditText extensions
 *
 * @author Classic
 * @version v1.0, 2019-05-20 15:48
 */
fun EditText.applyFocus(fillText: String? = null) {
    fillText?.let { if (it.isNotEmpty()) this.setText(it) }
    post {
        isFocusable = true
        isFocusableInTouchMode = true
        requestFocus()
        val content = text()
        if (content.isNotEmpty()) setSelection(content.length)
    }
}

fun EditText?.text(): String {
    if (this == null) return ""
    val editable = text
    if (null != editable) {
        return editable.toString().trim()
    }
    return ""
}

fun EditText.isEmpty(): Boolean = this.text().isEmpty()

fun EditText?.float(): Float {
    if (this == null) return 0F
    val content = text()
    return try {
        content.toFloatOrNull() ?: 0F
    } catch (e: Exception) {
        0F
    }
}

fun EditText?.double(): Double {
    if (this == null) return 0.0
    val content = text()
    return try {
        content.toDoubleOrNull() ?: 0.0
    } catch (e: Exception) {
        0.0
    }
}

fun EditText?.int(): Int {
    if (this == null) return 0
    val content = text()
    return try {
        content.toIntOrNull() ?: 0
    } catch (e: Exception) {
        0
    }
}

fun EditText?.long(): Long {
    if (this == null) return 0L
    val content = text()
    return try {
        content.toLongOrNull() ?: 0L
    } catch (e: Exception) {
        0L
    }
}

/**
 * 文本输入框禁用回车键
 */
fun EditText?.disableEnter() {
    this?.setOnEditorActionListener { _, _, event ->
        return@setOnEditorActionListener KeyEvent.KEYCODE_ENTER == event.keyCode
    }
}

open class AbsTextWatcher: TextWatcher {
    override fun afterTextChanged(s: Editable) {}
    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
}