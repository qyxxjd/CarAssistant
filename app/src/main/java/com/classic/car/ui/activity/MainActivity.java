package com.classic.car.ui.activity;

import android.Manifest;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.view.KeyEvent;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import butterknife.BindView;
import com.classic.car.BuildConfig;
import com.classic.car.R;
import com.classic.car.ui.base.AppBaseActivity;
import com.classic.car.ui.fragment.AboutFragment;
import com.classic.car.ui.fragment.ChartFragment;
import com.classic.car.ui.fragment.MainFragment;
import com.classic.car.ui.fragment.TimelineFragment;
import com.classic.car.utils.PgyerUtil;
import com.classic.core.BasicConfig;
import com.classic.core.utils.DeviceUtil;
import com.classic.core.utils.DoubleClickExitHelper;
import com.pgyersdk.crash.PgyCrashManager;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;
import com.tbruyelle.rxpermissions.RxPermissions;
import com.umeng.analytics.MobclickAgent;
import rx.functions.Action1;

public class MainActivity extends AppBaseActivity {
    @BindView(R.id.main_bottombar) BottomBar mBottomBar;

    private MainFragment     mMainFragment;
    private ChartFragment    mChartFragment;
    private TimelineFragment mTimelineFragment;
    private AboutFragment    mAboutFragment;

    private DoubleClickExitHelper mDoubleClickExitHelper;

    @Override public int getLayoutResId() {
        return R.layout.activity_main;
    }

    @Override public void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        setTitle(R.string.app_name);
        mDoubleClickExitHelper = new DoubleClickExitHelper(activity);

        if(android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
            init();
        } else {
            RxPermissions.getInstance(mAppContext)
                         .request(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                 Manifest.permission.READ_EXTERNAL_STORAGE,
                                 Manifest.permission.READ_PHONE_STATE)
                         .subscribe(new Action1<Boolean>() {
                             @Override public void call(Boolean granted) {
                                 if (!granted) {
                                     activity.finish();
                                     return;
                                 }
                                 init();
                             }
                         });
        }
        initBottomBar();
        PgyCrashManager.register(getApplicationContext());
        PgyerUtil.checkUpdate(activity, false);
    }

    private void init(){
        if(BuildConfig.DEBUG){
            BasicConfig.getInstance(mAppContext).initDir().initLog(true);
        }else {
            BasicConfig.getInstance(mAppContext).init();
        }
        MobclickAgent.onProfileSignIn(DeviceUtil.getInstance(mAppContext).getID());
    }

    private void initBottomBar(){
        mBottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                switch (tabId) {
                    case R.id.tab_main:
                        if (null == mMainFragment) {
                            mMainFragment = MainFragment.newInstance();
                        }
                        changeFragment(R.id.main_fragment_layout, mMainFragment);
                        break;
                    case R.id.tab_chart:
                        if (null == mChartFragment) {
                            mChartFragment = ChartFragment.newInstance();
                        }
                        changeFragment(R.id.main_fragment_layout, mChartFragment);
                        break;
                    case R.id.tab_timeline:
                        if (null == mTimelineFragment) {
                            mTimelineFragment = TimelineFragment.newInstance();
                        }
                        changeFragment(R.id.main_fragment_layout, mTimelineFragment);
                        break;
                    case R.id.tab_about:
                        if (null == mAboutFragment) {
                            mAboutFragment = AboutFragment.newInstance();
                        }
                        changeFragment(R.id.main_fragment_layout, mAboutFragment);
                        break;
                }
            }
        });
    }

    @Override public void unRegister() {
        super.unRegister();
        PgyCrashManager.unregister();
    }

    @Override public boolean onKeyDown(int keyCode, KeyEvent event) {
        return mDoubleClickExitHelper.onKeyDown(keyCode, event);
    }

    public void onHide() {
        getToolbar().animate().translationY(-getToolbar().getHeight()).setInterpolator(new AccelerateInterpolator(2));
        mBottomBar.animate().translationY(mBottomBar.getHeight()).setInterpolator(new AccelerateInterpolator(2));
    }

    public void onShow() {
        getToolbar().animate().translationY(0).setInterpolator(new DecelerateInterpolator(2));
        mBottomBar.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).start();
    }
}
