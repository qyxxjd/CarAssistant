package com.classic.car.utils;

import android.text.TextUtils;

import com.classic.car.BuildConfig;
import com.elvishew.xlog.XLog;

public final class LogUtil {

    private LogUtil() {
        // no instance
    }

    public static void d(String content) {
        if (BuildConfig.DEBUG && !TextUtils.isEmpty(content)) {
            XLog.d(content);
        }
    }

    public static void e(String content) {
        if (BuildConfig.DEBUG && !TextUtils.isEmpty(content)) {
            XLog.e(content);
        }
    }
}
