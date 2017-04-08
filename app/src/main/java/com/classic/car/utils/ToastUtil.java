package com.classic.car.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.widget.Toast;

public final class ToastUtil {

    private ToastUtil() { }

    public static void showToast(@NonNull Context context, @StringRes int resId) {
        Toast.makeText(context, resId, Toast.LENGTH_SHORT).show();
    }

    public static void showToast(@NonNull Context context, @NonNull String content) {
        Toast.makeText(context, content, Toast.LENGTH_SHORT).show();
    }
}
