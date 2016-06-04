package com.classic.car.consts;

/**
 * 应用名称: CarAssistant
 * 包 名 称: com.classic.car.consts
 *
 * 文件描述：常量
 * 创 建 人：续写经典
 * 创建时间：16/5/29 上午10:58
 */
public final class Consts {

    public static final String DB_NAME = "CarAssistant.db";

    public static final String[] TYPE_MENUS =
            { "其它", "加油费", "停车费", "维修费", "过路费", "保险费", "汽车保养费", "交通违章罚款" };

    public static final int TYPE_OTHER             = 0; //其它
    public static final int TYPE_FUEL              = 1; //加油费
    public static final int TYPE_PARKING           = 2; //停车费
    public static final int TYPE_REPAIR            = 3; //维修费
    public static final int TYPE_ROAD_TOLL         = 4; //过路费
    public static final int TYPE_PREMIUM           = 5; //保险费
    public static final int TYPE_MAINTENANCE       = 6; //汽车保养费
    public static final int TYPE_TRAFFIC_VIOLATION = 7; //交通违章罚款

    public static final String FORMAT_MONEY = "$%s";
}
