@file:Suppress("unused")

package com.classic.assistant.car.extension

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

/**
 * Lifecycle extensions
 *
 * @author Classic
 * @version v1.0, 2019-05-20 15:48
 */
fun <T : Any, L : LiveData<T?>> AppCompatActivity.observe(liveData: L, body: (T?) -> Unit) =
    liveData.observe(this, Observer(body))

fun <T : Any, L : LiveData<T?>> Fragment.observe(liveData: L, body: (T?) -> Unit) =
    liveData.observe(viewLifecycleOwner, Observer(body))