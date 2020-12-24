package com.classic.assistant.car.ui.base

import android.content.Context
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.classic.assistant.car.R
import com.classic.assistant.car.extension.toast
import com.classic.assistant.car.util.CarLog
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import pub.devrel.easypermissions.EasyPermissions

/**
 * Base activity
 *
 * @author LiuBin
 * @version v1.0, 2020-01-07 11:13
 */
@Suppress("MemberVisibilityCanBePrivate")
abstract class AppActivity : AppCompatActivity(), EasyPermissions.PermissionCallbacks {
    lateinit var appContext: Context
    var toolbar: Toolbar? = null

    abstract fun layout(): Int

    fun isDestroy() = isFinishing || isDestroyed
    fun fm() = supportFragmentManager
    fun label(): String = javaClass.simpleName
    open fun onObtainParams() {}
    fun toast(resId: Int) { if (!isDestroy()) appContext.toast(resId) }
    fun toast(hint: String) { if (!isDestroy()) appContext.toast(hint) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onObtainParams()
        appContext = applicationContext
        applyContentView()
        setupToolbar()
    }
    override fun onStart() {
        super.onStart()
        try {
            FlurryManager.start(appContext)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    override fun onStop() {
        super.onStop()
        try {
            FlurryManager.end(appContext)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    protected open fun canBack(): Boolean {
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == android.R.id.home) {
            onBackPressed()
            true
        } else {
            super.onOptionsItemSelected(item)
        }
    }

    open fun applyContentView() {
        setContentView(layout())
    }

    fun setupToolbar() {
        findViewById<Toolbar>(R.id.toolbar)?.apply {
            toolbar = this
            setSupportActionBar(this)
            if (canBack()) {
                val actionBar = supportActionBar
                actionBar?.setDisplayHomeAsUpEnabled(true)
            }
        }
    }

    open fun d(content: String) {
        CarLog.d(content)
    }
    open fun e(content: String) {
        CarLog.e(content)
    }

    /**
     * 获取当前显示的Fragment
     */
    private var fragment: Fragment? = null
    /**
     * 切换 Fragment
     *
     * @param container 容器ID
     * @param newFragment 用来替换的Fragment
     */
    open fun changeFragment(container: Int, newFragment: Fragment) {
        if (newFragment == fragment) {
            return
        }
        try {
            val newTag = newFragment::class.java.simpleName
            if (!newFragment.isAdded && null == fm().findFragmentByTag(newTag)) {
                fm().beginTransaction().add(container, newFragment, newTag).commitNowAllowingStateLoss()
            }
            if (newFragment.isHidden) {
                fm().beginTransaction().show(newFragment).commitNowAllowingStateLoss()
            }
            if (fragment != null && fragment!!.isVisible) {
                fm().beginTransaction().hide(fragment!!).commitNowAllowingStateLoss()
            }
            fragment = newFragment
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @Suppress("UNCHECKED_CAST")
    protected fun <T> restoreFragment(tag: String): T? {
        return fm().findFragmentByTag(tag)?.let { it as T }
    }

    /**
     * 基于Deferred的扩展函数创建一个启动协程，该协程将调用await()并将返回的值传递给block()。
     */
    infix fun <T> Deferred<T>.ui(block: suspend (T) -> Unit): Job {
        return lifecycleScope.launch(Dispatchers.Main) {
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