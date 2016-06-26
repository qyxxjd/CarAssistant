package com.classic.car.db;

import android.content.ContentValues;
import com.classic.car.entity.ConsumerDetail;
import com.squareup.sqlbrite.BriteDatabase;

/**
 * 应用名称: CarAssistant
 * 包 名 称: com.classic.car.db
 *
 * 文件描述：消费信息数据操作
 * 创 建 人：续写经典
 * 创建时间：16/5/29 上午10:26
 */
public class ConsumerDao {
    private BriteDatabase mDatabase;

    public ConsumerDao(BriteDatabase database) {
        this.mDatabase = database;
    }

    public long insert(ConsumerDetail detail){
        final ContentValues values = new ContentValues();
        values.put(ConsumerTable.COLUMN_CREATE_TIME, detail.getCreateTime());
        values.put(ConsumerTable.COLUMN_CONSUMPTION_TIME, detail.getConsumptionTime());
        values.put(ConsumerTable.COLUMN_TYPE, detail.getType());
        values.put(ConsumerTable.COLUMN_MONEY, detail.getMoney());
        values.put(ConsumerTable.COLUMN_OIL_TYPE, detail.getOilType());
        values.put(ConsumerTable.COLUMN_UNIT_PRICE, detail.getUnitPrice());
        values.put(ConsumerTable.COLUMN_CURRENT_MILEAGE, detail.getCurrentMileage());
        values.put(ConsumerTable.COLUMN_NOTES, detail.getNotes());
        return mDatabase.insert(ConsumerTable.NAME, values);
    }

}
