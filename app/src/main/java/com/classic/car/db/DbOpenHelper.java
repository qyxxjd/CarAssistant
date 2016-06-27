package com.classic.car.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.classic.car.db.table.ConsumerTable;

/**
 * 应用名称: CarAssistant
 * 包 名 称: com.classic.car.db
 *
 * 文件描述：数据库管理
 * 创 建 人：续写经典
 * 创建时间：16/6/25 下午2:07
 */
public class DbOpenHelper extends SQLiteOpenHelper {
    private static final String DB_NAME    = "CarAssistant.db";
    private static final int    DB_VERSION = 1;

    public DbOpenHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override public void onCreate(SQLiteDatabase db) {
        db.execSQL(ConsumerTable.create());
    }

    @Override public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
