package com.classic.car.app;

import android.app.Application;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import com.classic.car.db.DaggerDbComponent;
import com.classic.car.db.DbComponent;
import com.classic.car.db.DbModule;
import com.github.moduth.blockcanary.BlockCanary;
import com.github.moduth.blockcanary.BlockCanaryContext;
import com.squareup.leakcanary.LeakCanary;

/**
 * 应用名称: CarAssistant
 * 包 名 称: com.classic.car.app
 *
 * 文件描述：汽车助手
 * 创 建 人：续写经典
 * 创建时间：16/5/29 下午1:53
 */
public class CarApplication extends Application {
    private DbComponent mDbComponent;

    @Override public void onCreate() {
        super.onCreate();
        mDbComponent = DaggerDbComponent.builder().dbModule(new DbModule(this)).build();
        if (!LeakCanary.isInAnalyzerProcess(this)) {
            LeakCanary.install(this);
        }
        BlockCanary.install(this, new BlockContext()).start();
    }

    public DbComponent getDbComponent() {
        return mDbComponent;
    }

    private final class BlockContext extends BlockCanaryContext {

        @SuppressWarnings("WrongConstant") public String provideUid() {
            String uid;
            try {
                ApplicationInfo ai = getPackageManager().getApplicationInfo(getPackageName(),
                                                                            PackageManager.GET_ACTIVITIES);
                uid = Integer.toString(ai.uid, 10);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
                uid = String.valueOf(android.os.Process.myUid());
            }
            return uid;
        }

        public int provideBlockThreshold() {
            return 300;
        }
        public boolean displayNotification() {
            return true;
        }
        // 2G, 3G, 4G, wifi, etc
        public String provideNetworkType() {
            return "wifi";
        }
    }
}
