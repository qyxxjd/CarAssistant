package com.classic.car.db.dao;

import android.content.ContentValues;
import android.database.Cursor;
import com.classic.car.db.table.ConsumerTable;
import com.classic.car.entity.ConsumerDetail;
import com.classic.car.utils.CursorUtil;
import com.classic.core.log.Logger;
import com.classic.core.utils.CloseUtil;
import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;
import java.util.ArrayList;
import java.util.List;
import rx.Observable;
import rx.functions.Func1;

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

    public int update(ConsumerDetail detail){
        final ContentValues values = new ContentValues();
        values.put(ConsumerTable.COLUMN_CONSUMPTION_TIME, detail.getConsumptionTime());
        values.put(ConsumerTable.COLUMN_TYPE, detail.getType());
        values.put(ConsumerTable.COLUMN_MONEY, detail.getMoney());
        values.put(ConsumerTable.COLUMN_OIL_TYPE, detail.getOilType());
        values.put(ConsumerTable.COLUMN_UNIT_PRICE, detail.getUnitPrice());
        values.put(ConsumerTable.COLUMN_CURRENT_MILEAGE, detail.getCurrentMileage());
        values.put(ConsumerTable.COLUMN_NOTES, detail.getNotes());

        return mDatabase.update(ConsumerTable.NAME, values, ConsumerTable.COLUMN_ID + " = ? ",
                String.valueOf(detail.getId()));
    }

    public Observable<List<ConsumerDetail>> queryAll(){
        final String sql = "SELECT * FROM " + ConsumerTable.NAME;
        return mDatabase.createQuery(ConsumerTable.NAME, sql)
                        .map(new Func1<SqlBrite.Query, List<ConsumerDetail>>() {
                            @Override public List<ConsumerDetail> call(SqlBrite.Query query) {
                                return convert(query.run());
                            }
                        });
    }

    public int delete(long id){
        return mDatabase.delete(ConsumerTable.NAME, ConsumerTable.COLUMN_ID + " = ? ", String.valueOf(id));
    }

    private List<ConsumerDetail> convert(Cursor cursor){
        if(null == cursor){
            return null;
        }
        ArrayList<ConsumerDetail> result = new ArrayList<>();
        try{
            while (cursor.moveToNext()){
                result.add(new ConsumerDetail(
                        CursorUtil.getLong(cursor, ConsumerTable.COLUMN_ID),
                        CursorUtil.getLong(cursor, ConsumerTable.COLUMN_CREATE_TIME),
                        CursorUtil.getLong(cursor, ConsumerTable.COLUMN_CONSUMPTION_TIME),
                        CursorUtil.getFloat(cursor, ConsumerTable.COLUMN_MONEY),
                        CursorUtil.getInt(cursor, ConsumerTable.COLUMN_TYPE),
                        CursorUtil.getString(cursor, ConsumerTable.COLUMN_NOTES),
                        CursorUtil.getInt(cursor, ConsumerTable.COLUMN_OIL_TYPE),
                        CursorUtil.getFloat(cursor, ConsumerTable.COLUMN_UNIT_PRICE),
                        CursorUtil.getLong(cursor, ConsumerTable.COLUMN_CURRENT_MILEAGE)
                ));
            }
        } catch (Exception e){
            e.printStackTrace();
            Logger.e(e.getMessage());
        } finally {
            CloseUtil.close(cursor);
        }
        return result;
    }
}
