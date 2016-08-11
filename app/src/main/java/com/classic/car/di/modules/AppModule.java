package com.classic.car.di.modules;

import android.app.Application;
import com.classic.car.app.RxBus;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;

/**
 * 应用名称: CarAssistant
 * 包 名 称: com.classic.car.di.modules
 *
 * 文件描述：实例生成
 * 创 建 人：续写经典
 * 创建时间：16/6/5 下午2:07
 */
@Module(includes = { DbModule.class})
public class AppModule {

    private final Application mApplication;

    public AppModule(Application application) {
        this.mApplication = application;
    }

    @Provides @Singleton Application provideApplication() {
        return mApplication;
    }

    @Provides @Singleton RxBus provideRxBus() {
        return new RxBus();
    }
}
