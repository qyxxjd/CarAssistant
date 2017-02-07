package com.classic.car.utils;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import java.math.BigDecimal;

/**
 * 应用名称: BaseProject
 * 包 名 称: com.classic.android.utils
 *
 * 文件描述: 高精度数据计算工具类
 * 创 建 人: 续写经典
 * 创建时间: 2015/11/3 17:26
 */
@SuppressWarnings({"unused", "WeakerAccess"}) public class MoneyUtil {

    // 默认除法运算精度
    private static final int DEFAULT_SCALE         = 2;
    private static final int DEFAULT_ROUNDING_MODE = BigDecimal.ROUND_HALF_UP;

    private MoneyUtil() { }

    private BigDecimal mBigDecimal;

    /**
     * 对象转BigDecimal
     */
    public static BigDecimal objectToBigDecimal(@NonNull Object number) {
        BigDecimal value;
        if (number instanceof Integer) {
            value = new BigDecimal(Integer.toString((Integer)number));
        } else if (number instanceof Float) {
            value = new BigDecimal(Float.toString((Float)number));
        } else if (number instanceof Double) {
            value = new BigDecimal(Double.toString((Double)number));
        } else if (number instanceof Short) {
            value = new BigDecimal(Short.toString((Short)number));
        } else if (number instanceof Long) {
            value = new BigDecimal(Long.toString((Long)number));
        } else if (number instanceof String) {
            value = new BigDecimal(number.toString());
        } else { //未知的类型
            throw new IllegalArgumentException("unknown type!");
        }
        return value;
    }

    /**
     * 去掉小数点后无效的0
     */
    public static String replace(Number number) {
        return replace(String.valueOf(number));
    }

    /**
     * 去掉小数点后无效的0
     */
    public static String replace(String number) {
        if (TextUtils.isEmpty(number)) { return "0"; }
        if (number.indexOf(".") > 0) {
            number = number.replaceAll("0+?$", ""); //去掉后面无用的零
            number = number.replaceAll("[.]$", ""); //如小数点后面全是零则去掉小数点
        }
        return number;
    }

    /**
     * 去掉小数点后无效的0
     */
    public static String replace(String number, int scale) {
        if (TextUtils.isEmpty(number)) { return "0"; }
        number = newInstance(number).round(scale).create().toString();
        if (number.indexOf(".") > 0) {
            number = number.replaceAll("0+?$", ""); //去掉后面无用的零
            number = number.replaceAll("[.]$", ""); //如小数点后面全是零则去掉小数点
        }
        return number;
    }

    /**
     * 去掉小数点后无效的0
     */
    public static String replace(Number number, int scale) {
        return replace(String.valueOf(number), scale);
    }

    public static MoneyUtil newInstance(@NonNull Object number) {
        MoneyUtil moneyUtil = new MoneyUtil();
        moneyUtil.mBigDecimal = objectToBigDecimal(number);
        return moneyUtil;
    }

    /**
     * 减
     */
    public MoneyUtil subtract(@NonNull Object number) {
        mBigDecimal = mBigDecimal.subtract(objectToBigDecimal(number));
        return this;
    }

    /**
     * 乘
     */
    public MoneyUtil multiply(@NonNull Object number) {
        mBigDecimal = mBigDecimal.multiply(objectToBigDecimal(number));
        return this;
    }

    /**
     * 除
     */
    public MoneyUtil divide(@NonNull Object number) {
        return divide(number, DEFAULT_SCALE, DEFAULT_ROUNDING_MODE);
    }

    /**
     * 除
     *
     * @param scale 精确到小数点后几位数
     */
    public MoneyUtil divide(@NonNull Object number, int scale) {
        return divide(number, scale, DEFAULT_ROUNDING_MODE);
    }

    /**
     * 除
     *
     * @param scale        精确到小数点后几位数
     * @param roundingMode 精确模式
     * @see BigDecimal
     */
    public MoneyUtil divide(@NonNull Object number, int scale, int roundingMode) {
        mBigDecimal = mBigDecimal.divide(objectToBigDecimal(number), scale, roundingMode);
        return this;
    }

    /**
     * 加
     */
    public MoneyUtil add(@NonNull Object number) {
        mBigDecimal = mBigDecimal.add(objectToBigDecimal(number));
        return this;
    }

    /**
     * 四舍五入
     *
     * @param scale 精确到小数点后几位数
     */
    public MoneyUtil round(int scale) {
        return round(scale, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 四舍五入
     *
     * @param scale        精确到小数点后几位数
     * @param roundingMode 精确模式
     * @see BigDecimal
     */
    public MoneyUtil round(int scale, int roundingMode) {
        if (scale >= 0) {
            mBigDecimal = mBigDecimal.divide(new BigDecimal("1"), scale, roundingMode);
        }
        return this;
    }

    public BigDecimal create() {
        return mBigDecimal;
    }


}
