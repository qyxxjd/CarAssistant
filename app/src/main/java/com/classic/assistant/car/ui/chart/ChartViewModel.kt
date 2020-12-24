package com.classic.assistant.car.ui.chart

import android.view.View
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.classic.assistant.car.data.source.local.db.ConsumptionInfo
import com.classic.assistant.car.extension.ioTask
import com.classic.assistant.car.extension.year
import com.classic.assistant.car.ui.base.CommonListViewModel
import com.classic.assistant.car.util.BetweenTimeUtil

class ChartViewModel : CommonListViewModel() {
    private val topSize = 5
    val consumptionInfoSaveVisibility = ObservableInt()
    val consumptionPercentageSaveVisibility = ObservableInt()
    val fuelConsumptionSaveVisibility = ObservableInt()
    val minAmount = ObservableField<String>()
    val minFuelConsumption = ObservableField<String>()
    val maxAmount = ObservableField<String>()
    val maxFuelConsumption = ObservableField<String>()

    private val _consumptionList = MutableLiveData<MutableList<ConsumptionInfo>>()
    private val _topConsumptionList = MutableLiveData<MutableList<ConsumptionInfo>>()
    private val _fuelConsumptionList = MutableLiveData<MutableList<ConsumptionInfo>>()
    private val _yearConsumptionList = MutableLiveData<MutableList<ConsumptionInfo>>()

    val consumptionList: LiveData<MutableList<ConsumptionInfo>?>
        get() = _consumptionList
    val topConsumptionList: LiveData<MutableList<ConsumptionInfo>?>
        get() = _topConsumptionList
    val fuelConsumptionList: LiveData<MutableList<ConsumptionInfo>?>
        get() = _fuelConsumptionList
    val yearConsumptionList: LiveData<MutableList<ConsumptionInfo>?>
        get() = _yearConsumptionList

    var year: Int = year()

    // 获取今年的消费信息
    private fun getCurrentYearConsumptionInfo() {
        ioTask {
            val bt = BetweenTimeUtil.get(year, null)
            val list = dataSource.queryAll(bt.start, bt.end, false)
            _yearConsumptionList.postValue(list)
        }
    }

    private fun getTopConsumptionInfo() {
        ioTask {
            val bt = BetweenTimeUtil.get(year, null)
            val list = dataSource.queryTop(bt.start, bt.end, topSize)
            // val list = dataSource.queryTop(0L, Long.MAX_VALUE, topSize)
            _topConsumptionList.postValue(list)
        }
    }

    private fun getConsumptionInfo() {
        ioTask {
            val bt = BetweenTimeUtil.get(year, null)
            val list = dataSource.queryAll(bt.start, bt.end, false)
            // val list = dataSource.queryAll(0L, Long.MAX_VALUE, false)
            val visibility = if (list.isNotEmpty()) View.VISIBLE else View.GONE
            consumptionInfoSaveVisibility.set(visibility)
            consumptionPercentageSaveVisibility.set(visibility)
            _consumptionList.postValue(list)
        }
    }

    private fun getFuelConsumption() {
        ioTask {
            val bt = BetweenTimeUtil.get(year, null)
            val list = dataSource.queryFuelConsumption(bt.start, bt.end)
            // val list = dataSource.queryFuelConsumption(0L, Long.MAX_VALUE)
            val visibility = if (list.isNotEmpty()) View.VISIBLE else View.GONE
            fuelConsumptionSaveVisibility.set(visibility)
            _fuelConsumptionList.postValue(list)
        }
    }

    override fun onRefresh() {
        getCurrentYearConsumptionInfo()
        getConsumptionInfo()
        getTopConsumptionInfo()
        getFuelConsumption()
    }
}
