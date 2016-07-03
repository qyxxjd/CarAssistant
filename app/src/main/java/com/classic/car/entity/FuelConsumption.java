package com.classic.car.entity;

/**
 * 应用名称: CarAssistant
 * 包 名 称: com.classic.car.entity
 *
 * 文件描述：油耗信息
 * 创 建 人：续写经典
 * 创建时间：16/7/3 下午2:50
 */
public class FuelConsumption {
    /** 百公里耗油量,单位:升/公里 */
    private float oilMass;
    /** 百公里花费金额,单位:元/公里 */
    private float money;
    /** 行驶里程数,单位:公里 */
    private long mileage;

    public FuelConsumption() { }

    public FuelConsumption(long mileage, float money, float oilMass) {
        this.mileage = mileage;
        this.money = money;
        this.oilMass = oilMass;
    }

    public float getOilMass() {
        return oilMass;
    }

    public void setOilMass(float oilMass) {
        this.oilMass = oilMass;
    }

    public float getMoney() {
        return money;
    }

    public void setMoney(float money) {
        this.money = money;
    }

    public long getMileage() {
        return mileage;
    }

    public void setMileage(long mileage) {
        this.mileage = mileage;
    }
}
