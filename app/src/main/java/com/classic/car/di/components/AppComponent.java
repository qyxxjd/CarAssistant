package com.classic.car.di.components;

import com.classic.car.di.modules.AppModule;
import com.classic.car.ui.activity.AddConsumerActivity;
import com.classic.car.ui.activity.ChartActivity;
import com.classic.car.ui.fragment.ChartFragment;
import com.classic.car.ui.fragment.MainFragment;
import com.classic.car.ui.fragment.TimelineFragment;

import javax.inject.Singleton;

import dagger.Component;

/**
 * 应用名称: CarAssistant
 * 包 名 称: com.classic.car.di.components
 *
 * 文件描述：实例注入
 * 创 建 人：续写经典
 * 创建时间：16/6/5 下午2:07
 */
@Singleton @Component(modules = {AppModule.class}) public interface AppComponent {
    void inject(AddConsumerActivity activity);

    void inject(ChartActivity activity);

    void inject(MainFragment fragment);

    void inject(TimelineFragment fragment);

    void inject(ChartFragment fragment);
}
