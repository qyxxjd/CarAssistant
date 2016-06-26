package com.classic.car.entity;

import java.io.Serializable;

/**
 * 应用名称: CarAssistant
 * 包 名 称: com.classic.car.entity
 *
 * 文件描述：消费详情
 * 创 建 人：续写经典
 * 创建时间：16/5/29 上午10:26
 */
public class ConsumerDetail implements Serializable {
    private static final long serialVersionUID = 6708495983469206253L;

    private long   id;
    private long   createTime;
    private long   consumptionTime;
    private float  money;
    private int    type;
    private String notes;

    //加油费附属信息
    private int   oilType; //油类型
    private float unitPrice; //单价
    private long  currentMileage;//当前里程

    public ConsumerDetail(){}

    public ConsumerDetail(long consumptionTime, float money, int type) {
        this.consumptionTime = consumptionTime;
        this.money = money;
        this.type = type;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getConsumptionTime() {
        return consumptionTime;
    }

    public void setConsumptionTime(long consumptionTime) {
        this.consumptionTime = consumptionTime;
    }

    public float getMoney() {
        return money;
    }

    public void setMoney(float money) {
        this.money = money;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public int getOilType() {
        return oilType;
    }

    public void setOilType(int oilType) {
        this.oilType = oilType;
    }

    public float getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(float unitPrice) {
        this.unitPrice = unitPrice;
    }

    public long getCurrentMileage() {
        return currentMileage;
    }

    public void setCurrentMileage(long currentMileage) {
        this.currentMileage = currentMileage;
    }
}
