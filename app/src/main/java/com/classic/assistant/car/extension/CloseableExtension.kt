package com.classic.assistant.car.extension

import java.io.Closeable
import java.io.IOException

/**
 * Closeable extensions
 *
 * @author Classic
 * @version v1.0, 2019-05-20 15:48
 */
fun Closeable?.close() {
    if (null != this) {
        try {
            close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}
