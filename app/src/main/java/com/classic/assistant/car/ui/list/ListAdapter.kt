package com.classic.assistant.car.ui.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.classic.assistant.car.R
import com.classic.assistant.car.data.source.local.db.ConsumptionInfo
import com.classic.assistant.car.databinding.ItemListBinding
import com.classic.assistant.car.ui.base.BaseAdapter
import com.classic.assistant.car.util.CarLog
import com.simplecityapps.recyclerview_fastscroll.views.FastScrollRecyclerView

/**
 * List adapter
 *
 * @author LiuBin
 * @version v1.0, 2019-08-05 10:25
 */
class ListAdapter : BaseAdapter<ListAdapter.ViewHolder>(), FastScrollRecyclerView.SectionedAdapter {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        CarLog.d("ListAdapter:onCreateViewHolder()")
        return ViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_list, parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        getItem(position).let { entity ->
            CarLog.d("ListAdapter:onBindViewHolder():$entity")
            with(holder) {
                itemView.tag = entity
                bind(entity)
            }
        }

    class ViewHolder(private val binding: ItemListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ConsumptionInfo) {
            with(binding) {
                model = ListItemViewModel(this.root.context, item)
                executePendingBindings()
            }
        }
    }

    override fun getSectionName(position: Int): String {
        return getItem(position).fastScrollLabel()
    }
}
