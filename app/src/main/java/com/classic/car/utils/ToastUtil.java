package com.classic.car.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.Toast;

public final class ToastUtil {

    private ToastUtil() { }

    public static void showToast(@NonNull Context context, int resId) {
        Toast.makeText(context, context.getResources().getText(resId), Toast.LENGTH_SHORT).show();
    }
}
