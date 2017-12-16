package com.classic.car.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

import com.classic.android.BasicProject;
import com.classic.android.permissions.AfterPermissionGranted;
import com.classic.android.permissions.AppSettingsDialog;
import com.classic.android.permissions.EasyPermissions;
import com.classic.android.rx.RxUtil;
import com.classic.android.utils.DoubleClickExitHelper;
import com.classic.car.BuildConfig;
import com.classic.car.R;
import com.classic.car.consts.Const;
import com.classic.car.ui.base.AppBaseActivity;
import com.classic.car.ui.fragment.AboutFragment;
import com.classic.car.ui.fragment.ChartFragment;
import com.classic.car.ui.fragment.MainFragment;
import com.classic.car.ui.fragment.TimelineFragment;
import com.classic.car.utils.PgyUtil;
import com.elvishew.xlog.LogLevel;

import java.util.List;

import butterknife.BindColor;
import butterknife.BindView;
import io.reactivex.functions.Action;

public class MainActivity extends AppBaseActivity {
    @BindView(R.id.main_bnv) BottomNavigationView mBottomBar;
    @BindColor(R.color.colorPrimary) int mListNavigationColor;
    @BindColor(R.color.colorAccent) int mChartNavigationColor;
    @BindColor(R.color.material_blue) int mTimelineNavigationColor;
    @BindColor(R.color.orange) int mAboutNavigationColor;
    @BindColor(R.color.bnv_item_selector_list) ColorStateList mListSecondaryColor;
    @BindColor(R.color.bnv_item_selector_chart) ColorStateList mChartSecondaryColor;
    @BindColor(R.color.bnv_item_selector_timeline) ColorStateList mTimelineSecondaryColor;
    @BindColor(R.color.bnv_item_selector_about) ColorStateList mAboutSecondaryColor;

    private MainFragment     mMainFragment;
    private ChartFragment    mChartFragment;
    private TimelineFragment mTimelineFragment;
    private AboutFragment    mAboutFragment;

    private DoubleClickExitHelper mDoubleClickExitHelper;
//    private AppUpdateReceiver     mAppUpdateReceiver;

    @Override public int getLayoutResId() {
        return R.layout.activity_main;
    }

    @Override public void initView(Bundle savedInstanceState) {
        if(BuildConfig.DEBUG) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectAll().penaltyLog().build());
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectAll().penaltyLog().build());
        }
        super.initView(savedInstanceState);
        setTitle(R.string.app_name);
        mDoubleClickExitHelper = new DoubleClickExitHelper(mActivity);

        checkStoragePermissions();
        initBottomBar(savedInstanceState);

        recycle(RxUtil.run(new Action() {
            @Override public void run() throws Exception {
                PgyUtil.register(mAppContext);
                PgyUtil.checkUpdate(mActivity, false);
            }
        }));
    }

    private static final int REQUEST_CODE_STORAGE  = 101;
    private static final int REQUEST_CODE_SETTINGS = 102;
    private static final String[] STORAGE_PERMISSIONS = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };

    @AfterPermissionGranted(REQUEST_CODE_STORAGE)
    private void checkStoragePermissions(){
        if (EasyPermissions.hasPermissions(this, STORAGE_PERMISSIONS)) {
            init();
        } else {
            EasyPermissions.requestPermissions(this, Const.STORAGE_PERMISSIONS_DESCRIBE,
                                               REQUEST_CODE_STORAGE, STORAGE_PERMISSIONS);
        }
    }

    @Override public void onPermissionsGranted(int requestCode, List<String> perms) {
        super.onPermissionsGranted(requestCode, perms);
        if(requestCode == REQUEST_CODE_STORAGE){
            init();
        }
    }

    @Override public void onPermissionsDenied(int requestCode, List<String> perms) {
        super.onPermissionsDenied(requestCode, perms);
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this)
                    .setTitle("权限申请")
                    .setPositiveButton("设置")
                    .setNegativeButton("取消")
                    .setRationale(Const.STORAGE_PERMISSIONS_DESCRIBE)
                    .setRequestCode(REQUEST_CODE_SETTINGS)
                    .build()
                    .show();
        }
    }

    private void init() {
        BasicProject.config(new BasicProject.Builder().setDebug(BuildConfig.DEBUG)
                                                      .setRootDirectoryName(Const.DIR_NAME)
                                                      .setExceptionHandler(mAppContext)
                                                      .setLog(BuildConfig.DEBUG
                                                              ? LogLevel.ALL
                                                              : LogLevel.NONE));
    }
//    private final ColorStateList m1 = getResources().getColorStateList(R.color.bnv_item_selector_list);
//    private final ColorStateList m2 = getResources().getColorStateList(R.color.bnv_item_selector_chart);
//    private final ColorStateList m3 = getResources().getColorStateList(R.color.bnv_item_selector_timeline);
//    private final ColorStateList m4 = getResources().getColorStateList(R.color.bnv_item_selector_about);
    private void initBottomBar(Bundle savedInstanceState){
//        final ColorStateList m1 = getResources().getColorStateList(R.color.bnv_item_selector_list);
//        final ColorStateList m2 = getResources().getColorStateList(R.color.bnv_item_selector_chart);
//        final ColorStateList m3 = getResources().getColorStateList(R.color.bnv_item_selector_timeline);
//        final ColorStateList m4 = getResources().getColorStateList(R.color.bnv_item_selector_about);
        mBottomBar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_list:
                        mBottomBar.setBackgroundColor(mListNavigationColor);
                        mBottomBar.setItemIconTintList(mListSecondaryColor);
                        mBottomBar.setItemTextColor(mListSecondaryColor);
                        if (null == mMainFragment) {
                            mMainFragment = MainFragment.newInstance();
                        }
                        changeFragment(R.id.main_content, mMainFragment);
                        break;
                    case R.id.navigation_chart:
                        mBottomBar.setBackgroundColor(mChartNavigationColor);
                        mBottomBar.setItemIconTintList(mChartSecondaryColor);
                        mBottomBar.setItemTextColor(mChartSecondaryColor);
                        if (null == mChartFragment) {
                            mChartFragment = ChartFragment.newInstance();
                        }
                        changeFragment(R.id.main_content, mChartFragment);
                        break;
                    case R.id.navigation_timeline:
                        mBottomBar.setBackgroundColor(mTimelineNavigationColor);
                        mBottomBar.setItemIconTintList(mTimelineSecondaryColor);
                        mBottomBar.setItemTextColor(mTimelineSecondaryColor);
                        if (null == mTimelineFragment) {
                            mTimelineFragment = TimelineFragment.newInstance();
                        }
                        changeFragment(R.id.main_content, mTimelineFragment);
                        break;
                    case R.id.navigation_about:
                        mBottomBar.setBackgroundColor(mAboutNavigationColor);
                        mBottomBar.setItemIconTintList(mAboutSecondaryColor);
                        mBottomBar.setItemTextColor(mAboutSecondaryColor);
                        if (null == mAboutFragment) {
                            mAboutFragment = AboutFragment.newInstance();
                        }
                        changeFragment(R.id.main_content, mAboutFragment);
                        break;
                    default:
                        return false;
                }
                return true;
            }
        });
        if (null == savedInstanceState) {
            mBottomBar.setSelectedItemId(R.id.navigation_list);
        }
    }

    @Override public void unRegister() {
        super.unRegister();
//        if (null != mAppUpdateReceiver) {
//            unregisterReceiver(mAppUpdateReceiver);
//            mAppUpdateReceiver = null;
//        }
        PgyUtil.destroy();
    }

    @Override public boolean onKeyDown(int keyCode, KeyEvent event) {
        return mDoubleClickExitHelper.onKeyDown(keyCode, event);
    }

    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void onHide() {
        getToolbar().animate().translationY(-getToolbar().getHeight()).setInterpolator(new AccelerateInterpolator(2));
        mBottomBar.animate().translationY(mBottomBar.getHeight()).setInterpolator(new AccelerateInterpolator(2));
    }

    public void onShow() {
        getToolbar().animate().translationY(0).setInterpolator(new DecelerateInterpolator(2));
        mBottomBar.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).start();
    }

//    public void registerAppUpdateReceiver(long downloadId, String apkPath) {
//        mAppUpdateReceiver = new AppUpdateReceiver(downloadId, apkPath);
//        IntentFilter filter = new IntentFilter();
//        filter.addAction(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
//        registerReceiver(mAppUpdateReceiver, filter);
//    }

//    private final class AppUpdateReceiver extends BroadcastReceiver {
//        private long downloadId;
//        private String apkPath;
//
//        public AppUpdateReceiver(long downloadId, String apkPath) {
//            this.downloadId = downloadId;
//            this.apkPath = apkPath;
//        }
//
//        @Override public void onReceive(Context context, Intent intent) {
//            long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
//            if (id == downloadId && !TextUtils.isEmpty(apkPath) && new File(apkPath).exists()) {
//                IntentUtil.installApp(mAppContext, apkPath, getPackageName() + Const.AUTHORITIES_SUFFIX);
//            }
//        }
//    }
}
