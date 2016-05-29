package com.classic.car.app;

import android.app.Application;
import com.classic.car.consts.Consts;
import com.classic.core.BasicConfig;
import com.litesuits.orm.LiteOrm;

/**
 * 应用名称: CarAssistant
 * 包 名 称: com.classic.car.app
 *
 * 文件描述：TODO
 * 创 建 人：续写经典
 * 创建时间：16/5/29 下午1:53
 */
public class CarApplication extends Application {

    private static LiteOrm sLiteOrm;

    @Override public void onCreate() {
        super.onCreate();

        BasicConfig.getInstance(this)
                .initDir()
                .initLog(true);
        sLiteOrm = LiteOrm.newCascadeInstance(this, Consts.DB_NAME);
        sLiteOrm.setDebugged(true);
    }

    public static LiteOrm getLiteOrm() {
        return sLiteOrm;
    }
}
