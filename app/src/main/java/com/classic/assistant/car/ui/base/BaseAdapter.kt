package com.classic.assistant.car.ui.base

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.MaterialDialog
import com.classic.assistant.car.R
import com.classic.assistant.car.data.manager.DataManager
import com.classic.assistant.car.data.source.DataSource
import com.classic.assistant.car.data.source.local.db.ConsumptionInfo
import com.classic.assistant.car.extension.globalIOTask
import com.classic.assistant.car.ui.add.AddActivity
import com.classic.assistant.car.util.CarLog

class DiffCallback : DiffUtil.ItemCallback<ConsumptionInfo>() {

    override fun areItemsTheSame(old: ConsumptionInfo, new: ConsumptionInfo): Boolean {
        return old.id == new.id
    }

    override fun areContentsTheSame(old: ConsumptionInfo, new: ConsumptionInfo): Boolean {
        return old.createTime == new.createTime && old.type == new.type && old.lastUpdateTime == new.lastUpdateTime
    }
}

abstract class BaseAdapter<VH : RecyclerView.ViewHolder> : ListAdapter<ConsumptionInfo, VH>(DiffCallback()) {

    // var data = mutableListOf<ConsumptionInfo>()
    //
    // override fun submitList(list: MutableList<ConsumptionInfo>?) {
    //     data.clear()
    //     if (null != list && list.isNotEmpty()) {
    //         data.addAll(list)
    //     }
    //     super.submitList(list)
    // }

    fun createOnClickListener(entity: ConsumptionInfo): View.OnClickListener {
        return View.OnClickListener {
            AddActivity.start(it.context, entity)
        }
    }

    private fun deleteItem(context: Context, entity: ConsumptionInfo) {
        globalIOTask {
            val rows = try {
                DataSource.get(context).delete(entity)
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
            if (null != rows && rows > 0) {
                CarLog.e("删除数据成功($rows). $entity")
                DataManager.get().dispatchDelete(entity)
            } else {
                CarLog.e("删除数据失败. $entity")
            }
        }
    }

    fun createOnLongClickListener(entity: ConsumptionInfo): View.OnLongClickListener {
        return View.OnLongClickListener { it ->
            MaterialDialog(it.context).show {
                message(R.string.hint_delete_item)
                positiveButton(R.string.delete) { deleteItem(it.context, entity) }
                negativeButton(R.string.cancel)
            }.show()
            true
        }
    }
}
