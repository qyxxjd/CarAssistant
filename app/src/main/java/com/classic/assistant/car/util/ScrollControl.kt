package com.classic.assistant.car.util

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

/**
 * RecyclerView scroll help
 *
 * @author Classic
 * @version v1.0, 2019-05-11 19:51
 */
abstract class ScrollControl : RecyclerView.OnScrollListener() {
    private var scrollOffset: Int = 0
    private var controlVisible: Boolean = false

    protected abstract fun onShow()
    protected abstract fun onHide()

    /** 滑动隐藏的偏移量  */
    open fun getScrollHideOffset(): Int = 20

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        val layoutManager = recyclerView.layoutManager ?: return
        val firstVisibleItemPosition = findFirstVisibleItemPosition(layoutManager)

        if (firstVisibleItemPosition == 0 && !controlVisible) {
            onShow()
            controlVisible = true
        } else if (firstVisibleItemPosition != 0 && scrollOffset > getScrollHideOffset() &&
            controlVisible) {
            // 向上滚动,并且视图为显示状态
            onHide()
            controlVisible = false
            scrollOffset = 0
        } else if (firstVisibleItemPosition != 0 && scrollOffset < -getScrollHideOffset() &&
            !controlVisible) {
            // 向下滚动,并且视图为隐藏状态
            onShow()
            controlVisible = true
            scrollOffset = 0
        }
        // dy>0:向上滚动
        // dy<0:向下滚动
        if (controlVisible && dy > 0 || !controlVisible && dy < 0) {
            scrollOffset += dy
        }
    }

    private fun findFirstVisibleItemPosition(layoutManager: RecyclerView.LayoutManager): Int {
        return when (layoutManager) {
            is LinearLayoutManager -> layoutManager.findFirstVisibleItemPosition()
            is GridLayoutManager -> layoutManager.findFirstVisibleItemPosition()
            is StaggeredGridLayoutManager -> layoutManager.findFirstVisibleItemPositions(null)[0]
            else -> -1
        }
    }
}
