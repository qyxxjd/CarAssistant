package com.classic.assistant.car.ui.main

import android.Manifest
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.classic.assistant.car.BuildConfig
import com.classic.assistant.car.R
import com.classic.assistant.car.app.App
import com.classic.assistant.car.data.manager.BackupManager
import com.classic.assistant.car.data.source.DataSource
import com.classic.assistant.car.databinding.ActivityMainBinding
import com.classic.assistant.car.extension.applyVisible
import com.classic.assistant.car.extension.task
import com.classic.assistant.car.ui.add.AddActivity
import com.classic.assistant.car.ui.base.AppViewBindingActivity
import com.classic.assistant.car.ui.chart.ChartFragment
import com.classic.assistant.car.ui.home.HomeFragment
import com.classic.assistant.car.ui.list.ListFragment
import com.classic.assistant.car.ui.settings.SettingsFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.pgyer.pgyersdk.PgyerSDKManager
import com.pgyer.pgyersdk.pgyerenum.FeatureEnum
import com.tencent.bugly.crashreport.CrashReport
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions

class MainActivity : AppViewBindingActivity<ActivityMainBinding>() {
    private var home: HomeFragment? = null
    private var chart: ChartFragment? = null
    private var list: ListFragment? = null
    private var settings: SettingsFragment? = null

    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        viewBinding?.apply {
            // 切换菜单时，展示Toolbar
            // appBar.setExpanded(true)
            // Fab只在首页展示
            fab.applyVisible(item.itemId == R.id.navigation_home ||
                item.itemId == R.id.navigation_list)
        }

        when (item.itemId) {
            R.id.navigation_home -> {
                changeMainFragment(home)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_chart -> {
                changeMainFragment(chart)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_list -> {
                changeMainFragment(list)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_settings -> {
                changeMainFragment(settings)
                return@OnNavigationItemSelectedListener true
            }
            else -> false
        }
    }

    override fun createViewBinding() {
        viewBinding = ActivityMainBinding.inflate(layoutInflater)
    }

    override fun canBack(): Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        home = restoreFragment(HomeFragment::class.simpleName!!) ?: HomeFragment()
        chart = restoreFragment(ChartFragment::class.simpleName!!) ?: ChartFragment()
        list = restoreFragment(ListFragment::class.simpleName!!) ?: ListFragment()
        settings = restoreFragment(SettingsFragment::class.simpleName!!) ?: SettingsFragment()

        viewBinding?.apply {
            mainNav.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
            mainNav.selectedItemId = R.id.navigation_home
            fab.setOnClickListener { AddActivity.start(this@MainActivity) }
        }
        checkAppPermissions()
    }

    override fun onDestroy() {
        viewBinding?.mainNav?.setOnNavigationItemSelectedListener(null)
        DataSource.destroy()
        super.onDestroy()
    }

    private fun changeMainFragment(fragment: Fragment?) {
        if (null != fragment) changeFragment(R.id.main_container, fragment)
    }

    private fun init() {
        FlurryManager.init(appContext, App.DEBUG)
        PgyerSDKManager.InitSdk()
            .setContext(application)
            .enable(FeatureEnum.CHECK_UPDATE)
            .build()
        task(100) {
            CrashReport.initCrashReport(appContext, Const.BUGLY_APP_ID, BuildConfig.DEBUG)
            BackupManager.apply(DataSource.get(appContext))
        }
    }

    @AfterPermissionGranted(REQUEST_CODE_STORAGE)
    private fun checkAppPermissions() {
        if (EasyPermissions.hasPermissions(this, *STORAGE_PERMISSIONS)) {
            init()
        } else {
            EasyPermissions.requestPermissions(this, getString(R.string.permissions_storage_describe),
                REQUEST_CODE_STORAGE, *ALL_PERMISSIONS)
        }
    }
    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {
        super.onPermissionsGranted(requestCode, perms)
        if (perms.contains(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            init()
        }
    }
    override fun onPermissionsDenied(requestCode: Int, perms: List<String>) {
        super.onPermissionsDenied(requestCode, perms)
        if (perms.contains(Manifest.permission.WRITE_EXTERNAL_STORAGE) ||
            perms.contains(Manifest.permission.READ_EXTERNAL_STORAGE)) {
            AppSettingsDialog.Builder(this)
                .setRationale(R.string.permissions_storage_describe)
                .setTitle(R.string.permissions_title)
                .setRequestCode(REQUEST_CODE_SETTINGS)
                .setPositiveButton(R.string.settings)
                .setNegativeButton(R.string.cancel)
                .build()
                .show()
        }
    }
    companion object {
        private const val REQUEST_CODE_STORAGE = 101
        private const val REQUEST_CODE_SETTINGS = 102
        /** 读写权限，必须要有 */
        val STORAGE_PERMISSIONS = arrayOf(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
        /** 申请的所有权限 */
        private val ALL_PERMISSIONS = arrayOf(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_WIFI_STATE,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    }
}
