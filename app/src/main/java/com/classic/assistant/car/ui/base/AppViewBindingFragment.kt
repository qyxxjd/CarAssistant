package com.classic.assistant.car.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding

/**
 * ViewBinding
 *
 * @author LiuBin
 * @version v1.0, 2020/6/15 4:50 PM
 */
abstract class AppViewBindingFragment<T : ViewBinding> : AppFragment() {
    protected var viewBinding: T? = null
    override fun layout(): Int = 0
    abstract fun createViewBinding(inflater: LayoutInflater, container: ViewGroup?)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        createViewBinding(inflater, container)
        return viewBinding!!.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewBinding = null
    }
}