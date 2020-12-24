package com.classic.assistant.car.ui.list

import android.content.Context
import androidx.annotation.ColorInt
import androidx.core.graphics.ColorUtils
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import androidx.lifecycle.ViewModel
import com.classic.assistant.car.data.source.local.db.ConsumptionInfo
import com.classic.assistant.car.extension.PATTERN_DATE
import com.classic.assistant.car.extension.PATTERN_MINUTE
import com.classic.assistant.car.extension.format

class ListItemViewModel(val context: Context, item: ConsumptionInfo) : ViewModel() {
    private val itemType = item.type
    private val targetColor = UIManager.color(context, itemType)

    val time = ObservableField(formatTime(item.time))
    val icon = ObservableField(UIManager.icon(context, itemType))
    val amount = ObservableField(UIManager.amount(item.amount, false))
    val amountColor = ObservableInt(targetColor)
    val type = ObservableField(UIManager.label(itemType))
    val typeColor = ObservableInt(targetColor)
    val fillColor = ObservableInt(targetColor)
    val borderColor = ObservableInt(getBorderAlphaColor())
    // val lineColor = ObservableInt(getBorderAlphaColor())

    private fun formatTime(time: Long): String {
        return time.format(PATTERN_DATE) + "\n" + time.format(PATTERN_MINUTE)
    }

    @ColorInt fun getBorderAlphaColor(): Int =
        ColorUtils.setAlphaComponent(targetColor, 100)
}
