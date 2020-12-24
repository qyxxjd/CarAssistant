package com.classic.assistant.car.ui.base

import com.classic.assistant.car.app.App
import com.classic.assistant.car.data.manager.DataChangeListener
import com.classic.assistant.car.data.manager.DataManager
import com.classic.assistant.car.data.source.DataSource
import com.classic.assistant.car.data.source.local.db.ConsumptionInfo
import com.classic.assistant.car.util.CarLog

open class CommonListViewModel : AppViewModel(), DataChangeListener {
    protected val dataSource: DataSource by lazy { DataSource.get(App.get().context) }
    open fun onRefresh() {}

    override fun subscribe() {
        DataManager.get().addDataChangeListener(this)
    }

    override fun unsubscribe() {
        DataManager.get().removeDataChangeListener(this)
    }

    override fun onAdd(info: ConsumptionInfo) {
        CarLog.d("onAdd(): $info")
        onRefresh()
    }
    override fun onUpdate(info: ConsumptionInfo) {
        CarLog.d("onUpdate(): $info")
        onRefresh()
    }
    override fun onDelete(info: ConsumptionInfo) {
        CarLog.d("onDelete(): $info")
        onRefresh()
    }
    override fun onBackup(list: MutableList<ConsumptionInfo>) {
        CarLog.d("onBackup(): ${list.size}")
    }
    override fun onRestore(insert: MutableList<ConsumptionInfo>, update: MutableList<ConsumptionInfo>) {
        CarLog.d("onRestore(): insert=${insert.size}, update=${update.size}")
        onRefresh()
    }
}
