package com.classic.car.db.table;

/**
 * 应用名称: CarAssistant
 * 包 名 称: com.classic.car.db
 *
 * 文件描述：消费详情
 * 创 建 人：续写经典
 * 创建时间：16/5/29 上午10:26
 */
public class ConsumerTable {
    public static final String NAME = "t_consumer";

    public static final String COLUMN_ID               = "id";
    public static final String COLUMN_CREATE_TIME      = "createTime";
    public static final String COLUMN_CONSUMPTION_TIME = "consumptionTime";
    public static final String COLUMN_MONEY            = "money";
    public static final String COLUMN_TYPE             = "type";
    public static final String COLUMN_NOTES            = "notes";
    public static final String COLUMN_OIL_TYPE         = "oilType";
    public static final String COLUMN_UNIT_PRICE       = "unitPrice";
    public static final String COLUMN_CURRENT_MILEAGE  = "currentMileage";
    public static final String COLUMN_LAST_UPDATE_TIME = "lastUpdateTime";

    public static String create(){
        //noinspection StringBufferReplaceableByString
        return new StringBuilder("CREATE TABLE ").append(NAME).append("(")
                                                 .append(COLUMN_ID).append(" INTEGER PRIMARY KEY AUTOINCREMENT,")
                                                 .append(COLUMN_CREATE_TIME).append(" INTEGER NOT NULL,")
                                                 .append(COLUMN_CONSUMPTION_TIME).append(" INTEGER NOT NULL,")
                                                 .append(COLUMN_LAST_UPDATE_TIME).append(" INTEGER DEFAULT 0, ")
                                                 .append(COLUMN_MONEY).append(" INTEGER,")
                                                 .append(COLUMN_UNIT_PRICE).append(" INTEGER,")
                                                 .append(COLUMN_TYPE).append(" INTEGER,")
                                                 .append(COLUMN_OIL_TYPE).append(" INTEGER,")
                                                 .append(COLUMN_CURRENT_MILEAGE).append(" INTEGER,")
                                                 .append(COLUMN_NOTES).append(" TEXT")
                                                 .append(")").toString();
    }

    /**
     * 新增最后更新时间字段
     *
     * @return sql
     */
    public static String updateToVersion2(){
        //noinspection StringBufferReplaceableByString
        return new StringBuilder().append("ALTER TABLE ")
                                  .append(NAME)
                                  .append(" ADD COLUMN ")
                                  .append(COLUMN_LAST_UPDATE_TIME)
                                  .append(" INTEGER DEFAULT 0 ")
                                  .toString();
    }
}
