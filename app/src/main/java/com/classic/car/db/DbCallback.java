package com.classic.car.db;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.db.SupportSQLiteOpenHelper;
import com.classic.car.db.table.ConsumerTable;

final class DbCallback extends SupportSQLiteOpenHelper.Callback {
    private static final int VERSION = 2;

    DbCallback() {
        super(VERSION);
    }

    @Override
    public void onCreate(SupportSQLiteDatabase db) {
        db.execSQL(ConsumerTable.create());
    }

    @Override
    public void onUpgrade(SupportSQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            db.beginTransaction();
            switch (newVersion) {
                case 2:
                    update2(db);
                    break;
                default:
                    break;
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
        db.setVersion(newVersion);
    }

    private void update2(SupportSQLiteDatabase db) {
        db.execSQL(ConsumerTable.updateToVersion2());
    }
}
