package com.classic.car.utils;

import android.content.Context;
import com.classic.car.consts.Consts;
import com.classic.car.entity.ConsumerDetail;
import com.classic.core.utils.CloseUtil;
import com.classic.core.utils.DateUtil;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/** 从txt文件读取数据 */
public final class TxtHelper {
    private static final String FILE_NAME = "data.txt";

    public static List<ConsumerDetail> read(Context context){
        List<ConsumerDetail> result = new ArrayList<>();
        InputStreamReader inputreader = null;
        BufferedReader buffreader = null;
        try {
            inputreader = new InputStreamReader(context.getResources().getAssets().open(FILE_NAME));
            buffreader = new BufferedReader(inputreader);
            String line;
            String[] dataItem;
            while (( line = buffreader.readLine()) != null) {
                dataItem = line.split(" ");
                final long time = DateUtil.FORMAT_DATE.parse(dataItem[2]).getTime();
                switch (Integer.valueOf(dataItem[0])){
                    case Consts.TYPE_FUEL:
                        result.add(new ConsumerDetail(
                                Consts.TYPE_FUEL, Float.valueOf(dataItem[1]), time, Integer.valueOf(dataItem[3]),
                                Float.valueOf(dataItem[4]), Long.valueOf(dataItem[5]),
                                (dataItem.length == 7 ? dataItem[6] : "")
                        ));
                        break;
                    default:
                        result.add(new ConsumerDetail(
                                Integer.valueOf(dataItem[0]), Float.valueOf(dataItem[1]), time,
                                (dataItem.length == 4 ? dataItem[3] : "")
                        ));
                        break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } finally {
            CloseUtil.close(buffreader);
            CloseUtil.close(inputreader);
        }
        return result;
    }

}
