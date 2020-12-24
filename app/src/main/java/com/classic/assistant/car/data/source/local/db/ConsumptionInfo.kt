@file:Suppress("unused")

package com.classic.assistant.car.data.source.local.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.classic.assistant.car.extension.format
import java.io.Serializable

/**
 * 消费信息
 *
 * @author Classic
 * @version v1.0, 2018/7/14 上午10:22
 */
@Entity(tableName = "t_consumption_info")
class ConsumptionInfo : Serializable {

    @PrimaryKey(autoGenerate = true)
    var id: Long? = null
    var createTime: Long = 0
    var lastUpdateTime: Long = 0
    /** 消费时间 */
    var time: Long = 0
    /** 消费金额 */
    var amount: Float = 0F
    /** 消费类型 */
    var type: Int = TYPE_FUEL
    /** 标签 */
    var tag: String = ""
    /** 位置 */
    var location: String = ""
    /** 备注 */
    var remark: String = ""

    /** 油类型 */
    var oilType: Int = FUEL_GASOLINE_92
    /** 油单价 */
    var oilPrice: Float = 0F
    /** 当前的里程表数字 */
    var mileage: Long = 0

    companion object {
        /** 消费类型标签 */
        val TYPE_LABELS = mutableListOf(
            "加油费", "停车费", "过路费", "维修费", "工时费", "手续费", "服务费", "汽车牌照", "汽车保险",
            "汽车保养", "汽车年审", "汽车配件", "汽车购买", "装潢饰品", "交通违章", "事故理赔",
            "纳税", "洗车", "分期付款", "其它"
        )

        /** 加油费 */
        const val TYPE_FUEL: Int = 0
        /** 停车费 */
        const val TYPE_PARKING: Int = 1
        /** 过路费 */
        const val TYPE_ROAD_TOLL: Int = 2
        /** 维修费 */
        const val TYPE_REPAIR: Int = 3
        /** 工时费 */
        const val TYPE_WORKING_HOURS: Int = 4
        /** 手续费 */
        const val TYPE_FORMALITIES: Int = 5
        /** 服务费 */
        const val TYPE_SERVICE: Int = 6
        /** 汽车牌照 */
        const val TYPE_LICENSE_PLATE: Int = 7
        /** 汽车保险 */
        const val TYPE_PREMIUM: Int = 8
        /** 汽车保养 */
        const val TYPE_MAINTENANCE: Int = 9
        /** 汽车年审 */
        const val TYPE_EXAMINATION: Int = 10
        /** 汽车配件 */
        const val TYPE_ACCESSORIES: Int = 11
        /** 汽车购买 */
        const val TYPE_CAR_PURCHASE: Int = 12
        /** 装潢饰品 */
        const val TYPE_DECORATION: Int = 13
        /** 交通违章 */
        const val TYPE_TRAFFIC_VIOLATION: Int = 14
        /** 事故理赔 */
        const val TYPE_INDEMNIFY: Int = 15
        /** 纳税 */
        const val TYPE_TAX: Int = 16
        /** 洗车 */
        const val TYPE_WASH_CAR: Int = 17
        /** 分期付款 */
        const val TYPE_INSTALLMENT: Int = 18
        /** 其它 */
        const val TYPE_OTHER: Int = 100

        /** 燃油类型标签 */
        val FUEL_LABELS = mutableListOf(
            "0#柴油", "89#汽油", "90#汽油", "92#汽油", "93#汽油", "95#汽油", "97#汽油", "98#汽油"
        )

        // 柴油0#
        const val FUEL_DIESEL: Int = 0
        // 汽油
        const val FUEL_GASOLINE_89: Int = 1
        const val FUEL_GASOLINE_90: Int = 2
        const val FUEL_GASOLINE_92: Int = 3
        const val FUEL_GASOLINE_93: Int = 4
        const val FUEL_GASOLINE_95: Int = 5
        const val FUEL_GASOLINE_97: Int = 6
        const val FUEL_GASOLINE_98: Int = 7

        private const val LABEL_PATTERN = "yyyy年MM月"
    }

    fun fastScrollLabel(): String = time.format(LABEL_PATTERN)

    override fun toString(): String {
        return "ConsumptionInfo(id=$id, createTime=$createTime, lastUpdateTime=$lastUpdateTime, " +
            "time=$time, amount=$amount, type=$type, tag='$tag', remark='$remark', " +
            "location='$location', oilType=$oilType, oilPrice=$oilPrice, mileage=$mileage)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ConsumptionInfo

        if (id != other.id) return false
        if (createTime != other.createTime) return false
        if (lastUpdateTime != other.lastUpdateTime) return false
        if (amount != other.amount) return false
        if (type != other.type) return false

        return true
    }
    override fun hashCode(): Int {
        var result = id?.hashCode() ?: 0
        result = 31 * result + createTime.hashCode()
        result = 31 * result + lastUpdateTime.hashCode()
        result = 31 * result + amount.hashCode()
        result = 31 * result + type
        return result
    }
}
