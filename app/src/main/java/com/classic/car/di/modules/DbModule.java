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
package com.classic.car.di.modules;

import android.app.Application;
import android.database.sqlite.SQLiteOpenHelper;

import com.classic.car.BuildConfig;
import com.classic.car.db.DbOpenHelper;
import com.classic.car.db.dao.ConsumerDao;
import com.classic.car.utils.LogUtil;
import com.squareup.sqlbrite2.BriteDatabase;
import com.squareup.sqlbrite2.SqlBrite;

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

    @Provides @Singleton SQLiteOpenHelper provideOpenHelper() {
        return new DbOpenHelper(mApplication);
    }

    @Provides @Singleton SqlBrite provideSqlBrite() {
        final SqlBrite.Builder builder = new SqlBrite.Builder();
        if (BuildConfig.DEBUG) {
            //noinspection CheckResult
            builder.logger(new SqlBrite.Logger() {
                @Override public void log(String message) {
                    LogUtil.d(message);
                }
            });
        }
        return builder.build();
    }

    @Provides @Singleton BriteDatabase provideDatabase(SqlBrite sqlBrite, SQLiteOpenHelper helper) {
        BriteDatabase db = sqlBrite.wrapDatabaseHelper(helper, Schedulers.io());
        db.setLoggingEnabled(BuildConfig.DEBUG);
        return db;
    }

    @Provides @Singleton ConsumerDao provideConsumerDao(BriteDatabase briteDatabase) {
        return new ConsumerDao(briteDatabase);
    }
}
