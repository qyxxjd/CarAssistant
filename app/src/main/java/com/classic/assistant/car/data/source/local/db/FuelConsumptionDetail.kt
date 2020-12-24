package com.classic.assistant.car.data.source.local.db

/**
 * 油耗详情
 *
 * > 天数、里程 推荐值取最大的
 * > 油耗 推荐值取最小的
 *
 * @author LiuBin
 * @version v1.0, 2020/7/27 9:57 PM
 */
class FuelConsumptionDetail {
    // 日期
    var time: Long = 0
    // 行驶天数
    var days: Int = 0
    // 行驶里程,单位:公里
    var mileage: Long = 0
    // 平均每天里程
    var dayMileage: Long = 0
    // 当前油耗,单位:升
    var fuelConsumption: Float = 0F
    // 百公里油耗,单位:升/100公里
    var kmFuelConsumption: Float = 0F
    // 消费金额,单位:元
    var amount: Float = 0F
    // 百公里消费金额,单位:元/100公里
    var kmAmount: Float = 0F

    override fun toString(): String {
        return "FuelConsumptionDetail(time=$time, days=$days, mileage=$mileage, dayMileage=$dayMileage, fuelConsumption=$fuelConsumption, kmFuelConsumption=$kmFuelConsumption, amount=$amount, kmAmount=$kmAmount)"
    }
}

/** 油耗统计信息 */
class FuelConsumptionSummary {
    // 推荐值
    var target = FuelConsumptionDetail()
    // 最小油耗信息
    var min = FuelConsumptionDetail()
    // 最高油耗信息
    var max = FuelConsumptionDetail()
    val list: MutableList<FuelConsumptionDetail> = mutableListOf()
}