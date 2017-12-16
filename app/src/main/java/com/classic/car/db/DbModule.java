/*
 * Copyright (C) 2015 Square, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.classic.car.db;

import android.app.Application;
import android.arch.persistence.db.SupportSQLiteOpenHelper;
import android.arch.persistence.db.framework.FrameworkSQLiteOpenHelperFactory;

import com.classic.car.BuildConfig;
import com.classic.car.consts.Const;
import com.classic.car.db.dao.ConsumerDao;
import com.squareup.sqlbrite3.BriteDatabase;
import com.squareup.sqlbrite3.SqlBrite;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.reactivex.schedulers.Schedulers;

/**
 * 应用名称: CarAssistant
 * 包 名 称: com.classic.car.di.modules
 *
 * 文件描述：数据库相关实例生成
 * 创 建 人：续写经典
 * 创建时间：16/6/5 下午2:07
 */
@SuppressWarnings({"SpellCheckingInspection", "unused"}) @Module public final class DbModule {

    private final Application mApplication;

    public DbModule(Application application) {
        this.mApplication = application;
    }

//    @Provides @Singleton SQLiteOpenHelper provideOpenHelper() {
//        return new DbOpenHelper(mApplication);
//    }

    @Provides @Singleton SqlBrite provideSqlBrite() {
        return new SqlBrite.Builder().build();
    }

    @Provides @Singleton BriteDatabase provideDatabase(SqlBrite sqlBrite) {
        SupportSQLiteOpenHelper.Configuration configuration =
                SupportSQLiteOpenHelper.Configuration.builder(mApplication)
                .name(Const.DB_NAME)
                .callback(new DbCallback())
                .build();
        SupportSQLiteOpenHelper helper = new FrameworkSQLiteOpenHelperFactory().create(configuration);
        BriteDatabase db = sqlBrite.wrapDatabaseHelper(helper, Schedulers.io());
        db.setLoggingEnabled(BuildConfig.DEBUG);
        return db;
    }

    @Provides @Singleton ConsumerDao provideConsumerDao(BriteDatabase briteDatabase) {
        return new ConsumerDao(briteDatabase);
    }
}
