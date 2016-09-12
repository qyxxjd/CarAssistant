package com.classic.car.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import com.classic.car.consts.Consts;
import com.classic.car.db.dao.ConsumerDao;
import com.classic.car.entity.ConsumerDetail;
import com.classic.core.utils.CloseUtil;
import com.classic.core.utils.DataUtil;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;

public final class DataManager {
    private static final String           SEPARATOR   = " ";
    private static final String           LINE_FEED   = "\n";
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd_HH:mm",
            Locale.CHINA);

    /**
     * 导入数据
     *
     * @param fileName 文件名称
     */
    public void importByAssets(@NonNull final Context context,
                               @NonNull final ConsumerDao mConsumerDao,
                               @NonNull final String fileName) {
        Observable.create(new Observable.OnSubscribe<List<ConsumerDetail>>() {
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
                                    consumerDetails.add(convertConsumerDetail(line));
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
                  .compose(
                          RxUtil.<List<ConsumerDetail>>applySchedulers(RxUtil.IO_ON_UI_TRANSFORMER))
                  .subscribe(new Action1<List<ConsumerDetail>>() {
                      @Override public void call(List<ConsumerDetail> list) {
                          if (!DataUtil.isEmpty(list)) {
                              mConsumerDao.insert(list);
                          }
                      }
                  }, RxUtil.ERROR_ACTION);
    }

    /**
     * 备份数据
     *
     * @param path 文件路径
     * @param fileName 文件名称
     */
    public void backup(@NonNull final ConsumerDao mConsumerDao,
                       @NonNull final String path,
                       @NonNull final String fileName) {
        mConsumerDao.queryAll()
                    .compose(RxUtil.<List<ConsumerDetail>>applySchedulers(RxUtil.IO_TRANSFORMER))
                    .subscribe(new Action1<List<ConsumerDetail>>() {
                        @Override public void call(List<ConsumerDetail> consumerDetails) {
                            FileWriter fileWriter = null;
                            try {
                                fileWriter = new FileWriter(new File(path, fileName));
                                for (ConsumerDetail item : consumerDetails) {
                                    fileWriter.write(convertString(item));
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            } finally {
                                CloseUtil.close(fileWriter);
                            }
                        }
                    }, RxUtil.ERROR_ACTION);
    }

    private ConsumerDetail convertConsumerDetail(String data) {
        final String[] dataItem = data.split(SEPARATOR);
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
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String convertString(ConsumerDetail detail) {
        if (null == detail) return "";
        final String notes = TextUtils.isEmpty(detail.getNotes()) ? "" : detail.getNotes();
        final String timeStr = DATE_FORMAT.format(new Date(detail.getConsumptionTime()));
        if (detail.getType() == Consts.TYPE_FUEL) {
            return new StringBuilder().append(Consts.TYPE_FUEL)
                                      .append(SEPARATOR)
                                      .append(detail.getMoney())
                                      .append(SEPARATOR)
                                      .append(timeStr)
                                      .append(SEPARATOR)
                                      .append(detail.getOilType())
                                      .append(SEPARATOR)
                                      .append(detail.getUnitPrice())
                                      .append(SEPARATOR)
                                      .append(detail.getCurrentMileage())
                                      .append(SEPARATOR)
                                      .append(notes)
                                      .append(LINE_FEED)
                                      .toString();
        }
        return new StringBuilder().append(detail.getType())
                                  .append(SEPARATOR)
                                  .append(detail.getMoney())
                                  .append(SEPARATOR)
                                  .append(timeStr)
                                  .append(SEPARATOR)
                                  .append(notes)
                                  .append(LINE_FEED)
                                  .toString();
    }
}
