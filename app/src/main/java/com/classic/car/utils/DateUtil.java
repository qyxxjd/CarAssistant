package com.classic.car.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public final class DateUtil {

    private DateUtil() { }

    public static final String FORMAT_DATE      = "yyyy-MM-dd";
    public static final String FORMAT_TIME      = "HH:mm";

    /**
     * 时间格式化
     */
    public static String formatDate(String format, Long time) {
        return formatDate(new SimpleDateFormat(format, Locale.CHINA), time);
    }

    /**
     * 时间格式化
     */
    @SuppressWarnings("WeakerAccess")
    public static String formatDate(SimpleDateFormat format, Long time) {
        if (null == time || time <= 0) { return ""; }
        return format.format(new Date(String.valueOf(time).length() == 13 ? time : time * 1000));
    }

    public static long getTime(int year) {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.YEAR, year);
        return calendar.getTime().getTime();
    }
}
