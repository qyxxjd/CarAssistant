@file:Suppress("unused")

package com.classic.assistant.car.extension

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import androidx.recyclerview.widget.SimpleItemAnimator
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.classic.assistant.car.R
import com.classic.assistant.car.util.GridDividerItemDecoration

/**
 * RecyclerView extensions
 *
 * @author Classic
 * @version v1.0, 2019-05-23 15:48
 */

fun RecyclerView.scrollToPositionWithOffset(position: Int, offset: Int = 0) {
    when (val lm = layoutManager) {
        is StaggeredGridLayoutManager -> lm.scrollToPositionWithOffset(position, offset)
        is LinearLayoutManager -> lm.scrollToPositionWithOffset(position, offset)
    }
}
fun RecyclerView.scrollToTop() {
    scrollToPositionWithOffset(0)
}
fun RecyclerView.scrollToBottom() {
    adapter?.let {
        scrollToPositionWithOffset(it.itemCount - 1)
    }
}

fun RecyclerView.applyLinearConfig(
    layoutManager: RecyclerView.LayoutManager = WrapLinearLayoutManager(context),
    hasFixedSize: Boolean = false,
    supportsChangeAnimations: Boolean = false,
    decoration: ItemDecoration? = null,
    adapter: RecyclerView.Adapter<*>? = null
) {
    setHasFixedSize(hasFixedSize)
    this.layoutManager = layoutManager
    if (!supportsChangeAnimations) (itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
    if (null != decoration) addItemDecoration(decoration)
    if (null != adapter) {
        this.adapter = adapter
    }
}

fun RecyclerView.applyHorizontalLinearConfig(
    hasFixedSize: Boolean = false,
    supportsChangeAnimations: Boolean = false,
    decoration: ItemDecoration? = null,
    adapter: RecyclerView.Adapter<*>? = null
) {
    val lm = WrapLinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
    applyLinearConfig(lm, hasFixedSize, supportsChangeAnimations, decoration, adapter)
}

fun RecyclerView.applyGridConfig(
    spanCount: Int,
    hasFixedSize: Boolean = false,
    supportsChangeAnimations: Boolean = false,
    decoration: ItemDecoration? = null,
    adapter: RecyclerView.Adapter<*>? = null
) {
    setHasFixedSize(hasFixedSize)
    this.layoutManager = GridLayoutManager(context, spanCount)
    if (!supportsChangeAnimations) (itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
    if (null != decoration) addItemDecoration(decoration)
    if (null != adapter) {
        this.adapter = adapter
    }
}

fun RecyclerView.applyStaggeredGridConfig(
    spanCount: Int,
    supportsChangeAnimations: Boolean = false,
    decoration: ItemDecoration? = null,
    adapter: RecyclerView.Adapter<*>? = null
) {
    setHasFixedSize(false)
    this.layoutManager = StaggeredGridLayoutManager(spanCount, StaggeredGridLayoutManager.VERTICAL)
    if (!supportsChangeAnimations) (itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
    if (null != decoration) addItemDecoration(decoration)
    if (null != adapter) {
        this.adapter = adapter
    }
}

fun Context.divider(): ItemDecoration {
    val target = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
    drawable(R.drawable.common_divider)?.apply { target.setDrawable(this) }
    return target
}
// 通用网格分割线
fun Context.commonGridDividerRes(marginResId: Int, colorResId: Int) =
    GridDividerItemDecoration(px(marginResId), color(colorResId))
fun Context.commonGridDivider(margin: Int, color: Int) =
    GridDividerItemDecoration(margin, color)

/**
 * Fix:
 *
 * > java.lang.IndexOutOfBoundsException: Inconsistency detected.
 * > Invalid view holder adapter positionViewHolder
 *
 * - [RecyclerView IndexOutOfBoundsException: Inconsistency detected](https://stackoverflow.com/questions/31759171/recyclerview-and-java-lang-indexoutofboundsexception-inconsistency-detected-in)
 */
class WrapLinearLayoutManager : LinearLayoutManager {
    constructor(context: Context) : super(context)

    constructor(context: Context, orientation: Int, reverseLayout: Boolean):
        super(context, orientation, reverseLayout)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int):
        super(context, attrs, defStyleAttr, defStyleRes)

    override fun onLayoutChildren(recycler: RecyclerView.Recycler?, state: RecyclerView.State) {
        try {
            super.onLayoutChildren(recycler, state)
        } catch (e: IndexOutOfBoundsException) {
            e.printStackTrace()
        }
    }
}