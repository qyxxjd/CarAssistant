package com.classic.car.app;

import android.app.Application;
import com.classic.car.di.components.AppComponent;
import com.classic.car.di.components.DaggerAppComponent;
import com.classic.car.di.modules.AppModule;
import com.classic.car.di.modules.DbModule;

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

        //if (LeakCanary.isInAnalyzerProcess(this)) {
        //    return;
        //}
        //LeakCanary.install(this);
        //BlockCanary.install(this, new CarContext(this)).start();

        mAppComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .dbModule(new DbModule())
                .build();

    }

    public AppComponent getAppComponent() {
        return mAppComponent;
    }

}
