@file:Suppress("unused", "SpellCheckingInspection")

package com.classic.assistant.car.extension

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * 协程扩展
 *
 * @author Classic
 * @version v1.0, 2019-04-24 10:35
 */
fun AppCompatActivity.task(delayTimeMillis: Long = 0, dispatcher: CoroutineDispatcher = Dispatchers.Default, task: suspend () -> Unit) {
    lifecycleScope.launch(dispatcher) {
        if (delayTimeMillis > 0) delay(delayTimeMillis)
        task()
    }
}
fun AppCompatActivity.ioTask(delayTimeMillis: Long = 0, task: suspend () -> Unit) {
    task(delayTimeMillis, Dispatchers.IO, task)
}
fun AppCompatActivity.uiTask(delayTimeMillis: Long = 0, task: suspend () -> Unit) {
    task(delayTimeMillis, Dispatchers.Main, task)
}

fun Fragment.task(delayTimeMillis: Long = 0, dispatcher: CoroutineDispatcher = Dispatchers.Default, task: suspend () -> Unit) {
    viewLifecycleOwner.lifecycleScope.launch(dispatcher) {
        if (delayTimeMillis > 0) delay(delayTimeMillis)
        task()
    }
}
fun Fragment.ioTask(delayTimeMillis: Long = 0, task: suspend () -> Unit) {
    task(delayTimeMillis, Dispatchers.IO, task)
}
fun Fragment.uiTask(delayTimeMillis: Long = 0, task: suspend () -> Unit) {
    task(delayTimeMillis, Dispatchers.Main, task)
}

fun ViewModel.task(delayTimeMillis: Long = 0, dispatcher: CoroutineDispatcher = Dispatchers.Default, task: suspend () -> Unit) {
    viewModelScope.launch(dispatcher) {
        if (delayTimeMillis > 0) delay(delayTimeMillis)
        task()
    }
}
fun ViewModel.ioTask(delayTimeMillis: Long = 0, task: suspend () -> Unit) {
    task(delayTimeMillis, Dispatchers.IO, task)
}
fun ViewModel.uiTask(delayTimeMillis: Long = 0, task: suspend () -> Unit) {
    task(delayTimeMillis, Dispatchers.Main, task)
}


/**
 * 执行一个全局任务
 *
 * > `Activity、Fragment、ViewModel`慎用，不会随生命周期自动销毁
 *
 * @param delayTimeMillis 可选参数：延迟一定时间后执行任务
 * @param dispatcher 可选参数：协程调度
 */
fun <T> globalTask(delayTimeMillis: Long = 0, dispatcher: CoroutineDispatcher = Dispatchers.Default, task: suspend () -> T): Job {
    return GlobalScope.launch(dispatcher) {
        if (delayTimeMillis > 0) delay(delayTimeMillis)
        task()
    }
}
/**
 * 执行一个全局任务, 在IO线程
 *
 * > `Activity、Fragment、ViewModel`慎用，不会随生命周期自动销毁
 *
 * @param delayTimeMillis 可选参数：延迟一定时间后执行任务
 */
fun <T> globalIOTask(delayTimeMillis: Long = 0, task: suspend () -> T): Job {
    return globalTask(delayTimeMillis, Dispatchers.IO, task)
}
/**
 * 执行一个全局任务, 在主线程
 *
 * > `Activity、Fragment、ViewModel`慎用，不会随生命周期自动销毁
 *
 * @param delayTimeMillis 可选参数：延迟一定时间后执行任务
 */
fun <T> globalUITask(delayTimeMillis: Long = 0, task: suspend () -> T): Job {
    return globalTask(delayTimeMillis, Dispatchers.Main, task)
}

suspend fun <T> with(delayTimeMillis: Long = 0, dispatcher: CoroutineDispatcher = Dispatchers.Default, task: suspend () -> T): T {
    return withContext(dispatcher) {
        if (delayTimeMillis > 0) delay(delayTimeMillis)
        task()
    }
}
/** 协程内部IO操作 */
suspend fun <T> withIO(delayTimeMillis: Long = 0, task: suspend () -> T): T {
    return with(delayTimeMillis, Dispatchers.IO, task)
}
/** 协程内部UI操作 */
suspend fun <T> withUI(delayTimeMillis: Long = 0, task: suspend () -> T): T {
    return with(delayTimeMillis, Dispatchers.Main, task)
}

/**
 * 执行一个异步任务
 *
 * > 任务与 `Activity、Fragment、ViewModel` 声明周期联动
 * > 进行 `IO` 和网络操作时，使用 `Dispatchers.IO` 更高效。
 *
 * @param dispatcher 可选参数：协程调度
 * @return Deferred
 */
fun <T> LifecycleOwner.async(dispatcher: CoroutineDispatcher = Dispatchers.IO, task: suspend () -> T): Deferred<T> {
    return lifecycleScope.async(dispatcher) {
        task()
    }
}