package com.classic.assistant.car.ui.base

import android.os.Bundle
import android.view.View
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.viewbinding.ViewBinding
import com.classic.assistant.car.R

/**
 * ViewBinding
 *
 * @author LiuBin
 * @version v1.0, 2020/6/15 4:50 PM
 */
abstract class RefreshFragment<T : ViewBinding> : AppViewBindingFragment<T>() {
    open fun targetRefreshLayout(): SwipeRefreshLayout? = null
    open fun onRefresh() {}

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        targetRefreshLayout()?.apply {
            setColorSchemeResources(R.color.mediumorchid_light, R.color.pale_red, R.color.blue_light)
            setOnRefreshListener { onRefresh() }
        }
    }

    open fun finishRefresh() {
        targetRefreshLayout()?.isRefreshing = false
    }
}