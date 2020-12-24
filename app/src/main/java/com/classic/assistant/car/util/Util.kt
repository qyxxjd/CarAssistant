package com.classic.assistant.car.util

import com.tencent.bugly.crashreport.CrashReport

object Util {

    /**
     * 上报异常
     */
    fun report(t: Throwable) {
        if (null != t.message) CarLog.e(t.message!!)
        t.printStackTrace()
        CrashReport.postCatchedException(t)
    }
}
