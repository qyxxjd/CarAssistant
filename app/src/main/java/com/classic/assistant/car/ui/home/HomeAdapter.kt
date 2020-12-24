package com.classic.assistant.car.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.classic.assistant.car.R
import com.classic.assistant.car.data.source.local.db.ConsumptionInfo
import com.classic.assistant.car.databinding.ItemHomeBinding
import com.classic.assistant.car.ui.base.BaseAdapter
import com.classic.assistant.car.util.CarLog
import com.simplecityapps.recyclerview_fastscroll.views.FastScrollRecyclerView

/**
 * Home adapter
 *
 * @author LiuBin
 * @version v1.0, 2019-08-05 10:25
 */
class HomeAdapter : BaseAdapter<HomeAdapter.ViewHolder>(), FastScrollRecyclerView.SectionedAdapter {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        CarLog.d("HomeAdapter:onCreateViewHolder()")
        return ViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_home, parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position).let { entity ->
            CarLog.d("HomeAdapter:onBindViewHolder():$entity")
            with(holder) {
                itemView.tag = entity
                bind(createOnClickListener(entity), createOnLongClickListener(entity), entity)
            }
        }
    }

    override fun getSectionName(position: Int): String {
        return getItem(position).fastScrollLabel()
    }

    class ViewHolder(private val binding: ItemHomeBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(click: View.OnClickListener, longClick: View.OnLongClickListener, item: ConsumptionInfo) {
            with(binding) {
                clickListener = click
                longClickListener = longClick
                model = HomeItemViewModel(this.root.context, item)
                executePendingBindings()
            }
        }
    }
}
