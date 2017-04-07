package com.classic.car.db;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.classic.car.consts.Consts;
import com.classic.car.db.dao.ConsumerDao;
import com.classic.car.entity.ConsumerDetail;
import com.classic.car.utils.CloseUtil;
import com.classic.car.utils.DataUtil;
import com.classic.car.utils.MoneyUtil;
import com.classic.car.utils.RxUtil;
import com.squareup.sqlbrite.BriteDatabase;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Action1;

public final class BackupManager {
    private static final String SEPARATOR      = "----";
    private static final String REPLACE_STRING = "****";
    private static final String EMPTY          = "";
    private static final String LINE_FEED      = "\n";
    private static final String CHART_SET      = "UTF-8";

    /**
     * 导入数据
     *
     * @param fileName 文件名称
     */
    public Subscription importByAssets(@NonNull final Context context, @NonNull final ConsumerDao mConsumerDao,
                                       @NonNull final String fileName) {
        return Observable.unsafeCreate(new Observable.OnSubscribe<List<ConsumerDetail>>() {
            @Override public void call(Subscriber<? super List<ConsumerDetail>> subscriber) {
                List<ConsumerDetail> consumerDetails = new ArrayList<>();
                InputStreamReader inputreader = null;
                BufferedReader buffreader = null;
                try {
                    inputreader = new InputStreamReader(
                            context.getResources().getAssets().open(fileName));
                    buffreader = new BufferedReader(inputreader);
                    String line;
                    while ((line = buffreader.readLine()) != null) {
                        consumerDetails.add(convertConsumerDetail1(line));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    CloseUtil.close(buffreader);
                    CloseUtil.close(inputreader);
                }
                subscriber.onNext(consumerDetails);
            }
        })
                         .compose(RxUtil.<List<ConsumerDetail>>applySchedulers(RxUtil.IO_ON_UI_TRANSFORMER))
                         .subscribe(new Action1<List<ConsumerDetail>>() {
                             @Override public void call(List<ConsumerDetail> list) {
                                 if (!DataUtil.isEmpty(list)) {
                                     mConsumerDao.insert(list);
                                 }
                             }
                         }, RxUtil.ERROR_ACTION);
    }
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd_HH:mm",
                                                                             Locale.CHINA);
    private ConsumerDetail convertConsumerDetail1(String data) {
        final String[] dataItem = data.split(" ");
        final int type = Integer.valueOf(dataItem[0]);
        final long time;
        try {
            time = DATE_FORMAT.parse(dataItem[2]).getTime();
            if (type == Consts.TYPE_FUEL) {
                return new ConsumerDetail(Consts.TYPE_FUEL, Float.valueOf(dataItem[1]), time,
                                          Integer.valueOf(dataItem[3]), Float.valueOf(dataItem[4]),
                                          Long.valueOf(dataItem[5]), (dataItem.length == 7 ? dataItem[6] : ""));
            }
            return new ConsumerDetail(Integer.valueOf(dataItem[0]), Float.valueOf(dataItem[1]),
                                      time, (dataItem.length == 4 ? dataItem[3] : ""));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 备份数据
     *
     * @param path 文件路径
     * @param fileName 文件名称
     */
    // public Subscription backup(@NonNull final ConsumerDao mConsumerDao, @NonNull final String path,
    //                            @NonNull final String fileName) {
    //     return mConsumerDao.queryAll()
    //                        .compose(RxUtil.<List<ConsumerDetail>>applySchedulers(RxUtil.IO_TRANSFORMER))
    //                        .subscribe(new Action1<List<ConsumerDetail>>() {
    //                            @Override public void call(List<ConsumerDetail> consumerDetails) {
    //                                FileWriter fileWriter = null;
    //                                try {
    //                                    fileWriter = new FileWriter(new File(path, fileName));
    //                                    for (ConsumerDetail item : consumerDetails) {
    //                                        fileWriter.write(convertString(item));
    //                                    }
    //                                } catch (IOException e) {
    //                                    e.printStackTrace();
    //                                } finally {
    //                                    CloseUtil.close(fileWriter);
    //                                }
    //                            }
    //                        }, RxUtil.ERROR_ACTION);
    // }

    public int backup(@NonNull ConsumerDao consumerDao, @NonNull String path, @NonNull String fileName) {
        List<ConsumerDetail> list = consumerDao.queryAllSync();
        if (DataUtil.isEmpty(list)) {
            return 0;
        }
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(new File(path, fileName));
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
