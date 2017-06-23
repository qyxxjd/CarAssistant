package com.classic.car.db;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.classic.car.consts.Consts;
import com.classic.car.db.dao.ConsumerDao;
import com.classic.car.entity.ConsumerDetail;
import com.classic.car.utils.CloseUtil;
import com.classic.car.utils.DataUtil;
import com.classic.car.utils.MoneyUtil;
import com.squareup.sqlbrite2.BriteDatabase;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public final class BackupManager {
    private static final String SEPARATOR      = "----";
    private static final String REPLACE_STRING = "****";
    private static final String EMPTY          = "";
    private static final String LINE_FEED      = "\n";
    private static final String CHART_SET      = "UTF-8";

    public int backup(@NonNull ConsumerDao consumerDao, @NonNull String filePath) {
        List<ConsumerDetail> list = consumerDao.queryAllSync();
        if (DataUtil.isEmpty(list)) {
            return 0;
        }
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(new File(filePath));
            for (ConsumerDetail item : list) {
                fileWriter.write(convertString(item));
            }
            return list.size();
        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        } finally {
            CloseUtil.close(fileWriter);
        }
    }

    public boolean restore(@NonNull ConsumerDao consumerDao, @NonNull String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            return false;
        }
        BriteDatabase.Transaction transaction = consumerDao.getDatabase().newTransaction();
        InputStreamReader inputStreamReader = null;
        BufferedReader bufferedReader = null;
        try {
            inputStreamReader = new InputStreamReader(new FileInputStream(filePath), CHART_SET);
            bufferedReader = new BufferedReader(inputStreamReader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                ConsumerDetail item = convertConsumerDetail(line);
                if (null != item) {
                    ConsumerDetail temp = consumerDao.queryByCreateTime(item.getCreateTime());
                    if (null == temp) {
                        consumerDao.insert(item);
                    } else if (item.getLastUpdateTime() > temp.getLastUpdateTime()) {
                        item.setId(temp.getId());
                        consumerDao.update(item);
                    }
                }
            }
            transaction.markSuccessful();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            transaction.end();
            CloseUtil.close(bufferedReader);
            CloseUtil.close(inputStreamReader);
        }
    }

    private ConsumerDetail convertConsumerDetail(String data) {
        if (TextUtils.isEmpty(data)) {
            return null;
        }
        String[] dataItem = data.split(SEPARATOR);
        int type = Integer.valueOf(dataItem[0]);
        if (type != Consts.TYPE_FUEL) {
            String note = dataItem.length == 6 ?
                    (dataItem[5].contains(REPLACE_STRING) ? dataItem[5].replace(REPLACE_STRING, SEPARATOR) :
                            dataItem[5]) : EMPTY;
            return new ConsumerDetail(type, Float.valueOf(dataItem[1]), Long.valueOf(dataItem[2]),
                                      Long.valueOf(dataItem[3]), Long.valueOf(dataItem[4]), note);
        }
        String note = dataItem.length == 9 ?
                (dataItem[8].contains(REPLACE_STRING) ? dataItem[8].replace(REPLACE_STRING, SEPARATOR) :
                        dataItem[8]) : EMPTY;
        return new ConsumerDetail(type, Float.valueOf(dataItem[1]), Long.valueOf(dataItem[2]),
                                  Long.valueOf(dataItem[3]), Long.valueOf(dataItem[4]), Integer.valueOf(dataItem[5]),
                                  Float.valueOf(dataItem[6]), Long.valueOf(dataItem[7]), note);
    }

    private String convertString(ConsumerDetail detail) {
        if (null == detail) return EMPTY;
        final String notes = TextUtils.isEmpty(detail.getNotes()) ? EMPTY :
                (detail.getNotes().contains(SEPARATOR) ? detail.getNotes().replace(SEPARATOR, REPLACE_STRING) :
                                                         detail.getNotes());
        StringBuilder sb = new StringBuilder().append(detail.getType())
                                              .append(SEPARATOR)
                                              .append(MoneyUtil.replace(detail.getMoney()))
                                              .append(SEPARATOR)
                                              .append(detail.getLastUpdateTime())
                                              .append(SEPARATOR)
                                              .append(detail.getConsumptionTime())
                                              .append(SEPARATOR)
                                              .append(detail.getCreateTime());
        if (detail.getType() == Consts.TYPE_FUEL) {
            sb.append(SEPARATOR)
              .append(detail.getOilType())
              .append(SEPARATOR)
              .append(MoneyUtil.replace(detail.getUnitPrice()))
              .append(SEPARATOR)
              .append(detail.getCurrentMileage());
        }
        if (!TextUtils.isEmpty(notes)) {
            sb.append(SEPARATOR).append(notes);

        }
        return sb.append(LINE_FEED)
                 .toString();
    }
}
