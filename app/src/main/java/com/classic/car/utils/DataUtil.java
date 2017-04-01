package com.classic.car.utils;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public final class DataUtil {

    private DataUtil() { }

    /**
     * 检查数组是否为空(去掉前后空格)
     */
    public static boolean isEmpty(String string) {
        return (string == null || string.length() == 0 || string.trim().length() == 0);
    }

    /**
     * 检查数组是否为空
     */
    public static <V> boolean isEmpty(V[] sourceArray) {
        return (sourceArray == null || sourceArray.length == 0);
    }

    /**
     * 检查Collection是否为空
     */
    public static <V> boolean isEmpty(Collection<V> c) {
        return (c == null || c.size() == 0);
    }

    /**
     * 检查List是否为空
     */
    public static <V> boolean isEmpty(List<V> sourceList) {
        return (sourceList == null || sourceList.size() == 0);
    }

    /**
     * 检查Map是否为空
     */
    public static <K, V> boolean isEmpty(Map<K, V> sourceMap) {
        return (sourceMap == null || sourceMap.size() == 0);
    }
}
