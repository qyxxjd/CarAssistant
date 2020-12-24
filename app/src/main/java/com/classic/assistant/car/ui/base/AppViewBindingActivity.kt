package com.classic.assistant.car.ui.base

import androidx.viewbinding.ViewBinding

/**
 * ViewBinding
 *
 * @author LiuBin
 * @version v1.0, 2020/6/15 4:50 PM
 */
abstract class AppViewBindingActivity<T : ViewBinding> : AppActivity() {
    protected var viewBinding: T? = null
    override fun layout(): Int = 0
    abstract fun createViewBinding()

    override fun applyContentView() {
        createViewBinding()
        setContentView(viewBinding!!.root)
    }

    override fun onDestroy() {
        viewBinding = null
        super.onDestroy()
    }
}