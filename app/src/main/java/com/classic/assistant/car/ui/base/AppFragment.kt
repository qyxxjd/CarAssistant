package com.classic.assistant.car.ui.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.classic.assistant.car.extension.toast
import com.classic.assistant.car.util.CarLog
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import pub.devrel.easypermissions.EasyPermissions
import java.lang.ref.WeakReference

/**
 * Base Fragment
 *
 * @author LiuBin
 * @version v1.0, 2020-01-07 11:13
 */
abstract class AppFragment : Fragment(), EasyPermissions.PermissionCallbacks {
    lateinit var appContext: Context
    private val isHiddenState = "isHiddenState"
    private var weakContext: WeakReference<AppCompatActivity>? = null

    fun action(action: (AppCompatActivity) -> Unit) {
        weakContext?.get()?.let {
            if (isAdded) action.invoke(it)
        }
    }

    open fun layout(): Int = 0
    open fun onObtainParams() { }
    open fun isEnableShowHideEvent(): Boolean = false

    fun label(): String = javaClass.simpleName

    override fun onAttach(context: Context) {
        super.onAttach(context)
        d("FragmentLifeCycle: onAttach")
        appContext = context.applicationContext
        weakContext = WeakReference(context as AppCompatActivity)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        d("FragmentLifeCycle: onCreateView")
        return inflater.inflate(layout(), container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        d("FragmentLifeCycle: onViewCreated")
        onObtainParams()
        if (isEnableShowHideEvent()) {
            if (savedInstanceState != null) {
                val isHidden = savedInstanceState.getBoolean(isHiddenState)
                val transaction = parentFragmentManager.beginTransaction()
                if (isHidden) {
                    transaction.hide(this)
                } else {
                    transaction.show(this)
                }
                transaction.commitAllowingStateLoss()
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        d("FragmentLifeCycle: onActivityCreated")
    }

    override fun onStart() {
        super.onStart()
        d("FragmentLifeCycle: onStart")
        try {
            FlurryManager.start(appContext)
        } catch (e: Exception) {
        }
    }
    override fun onResume() {
        super.onResume()
        d("FragmentLifeCycle: onResume")
    }

    override fun onPause() {
        super.onPause()
        d("FragmentLifeCycle: onPause")
    }

    override fun onStop() {
        super.onStop()
        d("FragmentLifeCycle: onStop")
        try {
            FlurryManager.end(appContext)
        } catch (e: Exception) {
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        d("FragmentLifeCycle: onDestroyView")
    }

    override fun onDestroy() {
        super.onDestroy()
        d("FragmentLifeCycle: onDestroy")
    }

    override fun onDetach() {
        super.onDetach()
        d("FragmentLifeCycle: onDetach")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        d("FragmentLifeCycle: onSaveInstanceState")
        if (isEnableShowHideEvent()) outState.putBoolean(isHiddenState, isHidden)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        d("FragmentLifeCycle: onViewStateRestored")
    }

    open fun d(content: String) { CarLog.d(content) }
    open fun e(content: String) { CarLog.e(content) }
    fun toast(resId: Int) { if (isAdded) appContext.toast(resId) }
    fun toast(hint: String) { if (isAdded) appContext.toast(hint) }

    /**
     * 基于Deferred的扩展函数创建一个启动协程，该协程将调用await()并将返回的值传递给block()。
     */
    infix fun <T> Deferred<T>.ui(block: suspend (T) -> Unit): Job {
        return viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
            block(this@ui.await())
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }
    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {}
    override fun onPermissionsDenied(requestCode: Int, perms: List<String>) {}
}
