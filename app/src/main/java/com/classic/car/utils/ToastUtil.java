package com.classic.car.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.Toast;

/**
 * 应用名称: BaseProject
 * 包 名 称: com.classic.android.utils
 *
 * 文件描述: Toast工具类
 * 创 建 人: 续写经典
 * 创建时间: 2015/11/4 17:26
 */
public final class ToastUtil {

    private ToastUtil() { }

    public static void showToast(@NonNull Context context, @NonNull String text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

    public static void showToast(@NonNull Context context, int resId) {
        Toast.makeText(context, context.getResources().getText(resId), Toast.LENGTH_SHORT).show();
    }
}
