package com.classic.car.utils;

import com.classic.car.R;
import com.classic.car.consts.Consts;
import com.classic.core.utils.MoneyUtil;
import java.util.Locale;

/**
 * 应用名称: CarAssistant
 * 包 名 称: com.classic.car.utils
 *
 * 文件描述：TODO
 * 创 建 人：续写经典
 * 创建时间：16/6/4 下午8:51
 */
public class Util {

    public static int getColorByType(int type){
        switch (type){
            case Consts.TYPE_FUEL:
                return R.color.purple_light;
            case Consts.TYPE_PARKING:
                return R.color.orange_light;
            case Consts.TYPE_REPAIR:
                return R.color.pink_light;
            case Consts.TYPE_ROAD_TOLL:
                return R.color.saffron_light;
            case Consts.TYPE_PREMIUM:
                return R.color.blue_light;
            case Consts.TYPE_MAINTENANCE:
                return R.color.green_light;
            case Consts.TYPE_TRAFFIC_VIOLATION:
                return R.color.sienna_light;
            default:
                return R.color.pale_red;
        }
    }
    public static int getIconByType(int type){
        switch (type){
            case Consts.TYPE_FUEL:
                return R.drawable.ic_fuel;
            case Consts.TYPE_PARKING:
                return R.drawable.ic_parking;
            case Consts.TYPE_REPAIR:
                return R.drawable.ic_repair;
            case Consts.TYPE_ROAD_TOLL:
                return R.drawable.ic_road_toll;
            case Consts.TYPE_PREMIUM:
                return R.drawable.ic_premium;
            case Consts.TYPE_MAINTENANCE:
                return R.drawable.ic_maintenance;
            case Consts.TYPE_TRAFFIC_VIOLATION:
                return R.drawable.ic_traffic_violation;
            default:
                return R.drawable.ic_other;
        }
    }
    public static int getBackgroundByType(int type){
        switch (type){
            case Consts.TYPE_FUEL:
                return R.drawable.bg_purple_light;
            case Consts.TYPE_PARKING:
                return R.drawable.bg_orange_light;
            case Consts.TYPE_REPAIR:
                return R.drawable.bg_pink_light;
            case Consts.TYPE_ROAD_TOLL:
                return R.drawable.bg_saffron_light;
            case Consts.TYPE_PREMIUM:
                return R.drawable.bg_blue_light;
            case Consts.TYPE_MAINTENANCE:
                return R.drawable.bg_green_light;
            case Consts.TYPE_TRAFFIC_VIOLATION:
                return R.drawable.bg_sienna_light;
            default:
                return R.drawable.bg_pale_red;
        }
    }
    public static final String formatMoney(float money){
        return MoneyUtil.replace(String.format(Locale.CHINA, Consts.FORMAT_MONEY, money));
    }
}
