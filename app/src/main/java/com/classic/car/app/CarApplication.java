package com.classic.car.app;

import android.app.Application;
import com.classic.car.BuildConfig;
import com.classic.car.di.components.AppComponent;
import com.classic.car.di.components.DaggerAppComponent;
import com.classic.car.di.modules.AppModule;
import com.classic.car.di.modules.DbModule;
import com.classic.core.utils.SDcardUtil;
import com.github.moduth.blockcanary.BlockCanary;
import com.github.moduth.blockcanary.BlockCanaryContext;

/**
 * 应用名称: CarAssistant
 * 包 名 称: com.classic.car.app
 *
 * 文件描述：汽车助手
 * 创 建 人：续写经典
 * 创建时间：16/5/29 下午1:53
 */
public class CarApplication extends Application {
    private AppComponent mAppComponent;

    @Override public void onCreate() {
        super.onCreate();

        mAppComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .dbModule(new DbModule())
                .build();
        BlockCanary.install(this, new AppBlockCanaryContext()).start();
    }

    public AppComponent getAppComponent() {
        return mAppComponent;
    }

    private class AppBlockCanaryContext extends BlockCanaryContext {
        // override to provide context like app qualifier, uid, network type, block threshold, log save path

        // this is default block threshold, you can set it by phone's performance
        @Override
        public int getConfigBlockThreshold() {
            return 500;
        }

        // if set true, notification will be shown, else only write log file
        @Override
        public boolean isNeedDisplay() {
            return BuildConfig.DEBUG;
        }

        // path to save log file
        @Override
        public String getLogPath() {
            return SDcardUtil.getLogDirPath();
        }
    }
}
