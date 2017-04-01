package com.classic.car.utils;

import java.io.Closeable;
import java.io.IOException;

@SuppressWarnings("WeakerAccess") public final class CloseUtil {

    private CloseUtil() { }

    public static void close(Closeable... params) {
        if (null != params) {
            try {
                for (Closeable closeable : params) {
                    closeable.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
