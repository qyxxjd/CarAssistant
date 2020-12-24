package com.classic.assistant.car.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.classic.assistant.car.data.source.local.db.ConsumptionInfo
import com.classic.assistant.car.extension.calendar
import com.classic.assistant.car.extension.format
import com.classic.assistant.car.extension.ioTask
import com.classic.assistant.car.extension.year
import com.classic.assistant.car.ui.base.CommonListViewModel
import com.classic.assistant.car.util.BetweenTimeUtil
import com.classic.assistant.car.util.CarLog
import java.util.Calendar

class HomeViewModel : CommonListViewModel() {
    private val _consumptionList = MutableLiveData<List<ConsumptionInfo>>()
    val consumptionList: LiveData<List<ConsumptionInfo>?>
        get() = _consumptionList

    private fun queryConsumptionInfoByDate(year: Int, month: Int? = null) {
        ioTask {
            val time = BetweenTimeUtil.get(year, month)
            val list = dataSource.queryAll(time.start, time.end, true)
            CarLog.d("CommonListViewModel:${year}-${month}:${list.size}")
            _consumptionList.postValue(list)
        }
    }

    private fun queryConsumptionInfoByType(type: Int) {
        ioTask {
            val list = dataSource.query(type)
            CarLog.d("CommonListViewModel:type:${type}:${list.size}")
            _consumptionList.postValue(list)
        }
    }

    override fun onRefresh() {
        loadData()
    }

    fun loadData() {
        when (filterType) {
            HomeFilterMenuPopup.FILTER_MENU_YEAR ->
                queryConsumptionInfoByDate(currentYear)
            HomeFilterMenuPopup.FILTER_MENU_MONTH ->
                queryConsumptionInfoByDate(
                    calendar(currentTime).get(Calendar.YEAR),
                    // 当前月份为：0-11，调用此API需要的月份为：1-12
                    calendar(currentTime).get(Calendar.MONTH) + 1
                )
            HomeFilterMenuPopup.FILTER_MENU_TYPE -> queryConsumptionInfoByType(currentType)
        }
    }

    var filterType = HomeFilterMenuPopup.FILTER_MENU_YEAR
    var currentYear = year()
    var currentTime = System.currentTimeMillis()
    var currentType = ConsumptionInfo.TYPE_FUEL

    fun getFilterTitle(): String {
        return when (filterType) {
            HomeFilterMenuPopup.FILTER_MENU_MONTH -> currentTime.format("yyyy-MM")
            HomeFilterMenuPopup.FILTER_MENU_TYPE -> ConsumptionInfo.TYPE_LABELS[currentType]
            else -> currentYear.toString()
        }
    }
}
