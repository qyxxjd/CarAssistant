package com.miaoxi.arch.base

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.viewbinding.ViewBinding
import com.classic.assistant.car.extension.toast
import java.lang.ref.WeakReference

/**
 * DialogFragment
 *
 * @author LiuBin
 * @version v1.0, 2020-02-01 08:02
 */
abstract class AppViewBindingDialogFragment<T : ViewBinding> : DialogFragment() {
    protected var viewBinding: T? = null
    protected lateinit var appContext: Context
    private var weakContext: WeakReference<AppCompatActivity>? = null
    abstract fun createViewBinding(inflater: LayoutInflater, container: ViewGroup?)

    override fun onAttach(context: Context) {
        super.onAttach(context)
        appContext = context.applicationContext
        weakContext = WeakReference(context as AppCompatActivity)
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        createViewBinding(inflater, container)
        return viewBinding!!.root
    }
    override fun onStart() {
        super.onStart()
        dialog?.window?.apply {
            val params: WindowManager.LayoutParams = attributes
            customWindow(params)
            attributes = params
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            isCancelable = true
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        viewBinding = null
    }

    fun action(action: (AppCompatActivity) -> Unit) {
        weakContext?.get()?.let {
            if (isAdded) action.invoke(it)
        }
    }

    open fun label(): String = javaClass.simpleName

    fun toast(hint: String) {
        if (isAdded) appContext.toast(hint)
    }

    fun show(manager: FragmentManager) {
        try {
            val ft = manager.beginTransaction()
            ft.add(this, label())
            ft.commitAllowingStateLoss()
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        }
    }

    open fun customWindow(params: WindowManager.LayoutParams) {
        params.gravity = Gravity.BOTTOM
        params.width = WindowManager.LayoutParams.MATCH_PARENT
    }

    fun applyCancelable(value: Boolean) {
        dialog?.setCancelable(value)
        dialog?.setCanceledOnTouchOutside(value)
        isCancelable = value
    }
}