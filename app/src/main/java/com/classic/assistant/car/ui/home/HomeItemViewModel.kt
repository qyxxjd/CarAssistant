package com.classic.assistant.car.ui.home

import android.content.Context
import android.view.View
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import com.classic.assistant.car.data.source.local.db.ConsumptionInfo
import com.classic.assistant.car.extension.format
import com.classic.assistant.car.ui.base.AppViewModel

class HomeItemViewModel(val context: Context, item: ConsumptionInfo) : AppViewModel() {
    private val itemType = item.type

    val icon = ObservableField(UIManager.icon(context, itemType))
    val topBackground = ObservableField(UIManager.backgroundDrawable(context, itemType))
    val amount = ObservableField(UIManager.amount(item.amount))
    val type = ObservableField(UIManager.label(itemType))
    val typeColor = ObservableInt(UIManager.color(context, itemType))
    val time = ObservableField(formatTime(item.time))

    // 按需显示
    val tag = ObservableField(getTag(item))
    val tagVisibility = ObservableInt(visibilityTag(item))
    val mileage = ObservableField(UIManager.km(item.mileage))
    val mileageVisibility = ObservableInt(visibilityMileage(itemType))
    val location = ObservableField(item.location)
    val locationVisibility = ObservableInt(visibility(item.location))
    val remark = ObservableField(item.remark)
    val remarkVisibility = ObservableInt(visibility(item.remark))

    private fun formatTime(time: Long): String {
        // TODO
        // if (今年) {
        //    MM-dd HH:mm:ss
        // } else {
        //    yyyy-MM-dd HH:mm:ss
        // }
        return time.format()
    }

    private fun getTag(item: ConsumptionInfo): String {
        return when (item.type) {
            ConsumptionInfo.TYPE_FUEL -> ConsumptionInfo.FUEL_LABELS[item.oilType]
            else -> item.tag
        }
    }

    private fun visibilityTag(item: ConsumptionInfo): Int =
        if (item.tag.isNotEmpty() || ConsumptionInfo.TYPE_FUEL == item.type) View.VISIBLE else View.GONE

    private fun visibility(content: String): Int = if (content.isEmpty()) View.GONE else View.VISIBLE

    private fun visibilityMileage(type: Int): Int = if (type == ConsumptionInfo.TYPE_FUEL) View.VISIBLE else View.GONE
}
