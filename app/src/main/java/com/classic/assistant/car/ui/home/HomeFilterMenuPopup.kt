package com.classic.assistant.car.ui.home

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import android.widget.TextView
import com.classic.assistant.car.R

/**
 * 首页数据过滤菜单
 *
 * @author Classic
 * @version v1.0, 2019/04/12 10:43 AM
 */
class HomeFilterMenuPopup(
    activity: Activity,
    type: Int,
    private val listener: Listener
) : PopupWindow(), View.OnClickListener {

    override fun onClick(v: View) {
        when (v.id) {
            R.id.filter_year -> listener.onFilterTypeChange(FILTER_MENU_YEAR)
            R.id.filter_month -> listener.onFilterTypeChange(FILTER_MENU_MONTH)
            R.id.filter_type -> listener.onFilterTypeChange(FILTER_MENU_TYPE)
        }
        dismiss()
    }

    interface Listener {
        fun onFilterTypeChange(type: Int)
    }

    init {
        val inflater = activity.getSystemService(
            Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        @SuppressLint("InflateParams")
        val view = inflater.inflate(R.layout.pop_home_filter_menu, null)
        val yearMenu = view.findViewById<TextView>(R.id.filter_year)
        val monthMenu = view.findViewById<TextView>(R.id.filter_month)
        val typeMenu = view.findViewById<TextView>(R.id.filter_type)
        yearMenu.setOnClickListener(this)
        monthMenu.setOnClickListener(this)
        typeMenu.setOnClickListener(this)
        val menu: TextView? = when (type) {
            FILTER_MENU_YEAR -> yearMenu
            FILTER_MENU_MONTH -> monthMenu
            FILTER_MENU_TYPE -> typeMenu
            else -> null
        }
        menu?.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_choose, 0, 0, 0)
        this.contentView = view
        this.width = ViewGroup.LayoutParams.WRAP_CONTENT
        this.height = ViewGroup.LayoutParams.WRAP_CONTENT
        this.isFocusable = true
        this.isOutsideTouchable = true
        this.update()
        this.setBackgroundDrawable(ColorDrawable(Color.parseColor("#00000000")))
    }

    fun show(parent: View) {
        if (!this.isShowing) {
            this.showAsDropDown(parent, 0, 0)
        } else {
            this.dismiss()
        }
    }

    companion object {
        const val FILTER_MENU_YEAR = 0
        const val FILTER_MENU_MONTH = 1
        const val FILTER_MENU_TYPE = 2
    }
}
