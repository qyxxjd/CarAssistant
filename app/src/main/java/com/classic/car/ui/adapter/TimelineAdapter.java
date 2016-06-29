package com.classic.car.ui.adapter;

import android.content.Context;
import android.support.v4.graphics.ColorUtils;
import com.classic.adapter.BaseAdapterHelper;
import com.classic.adapter.CommonRecyclerAdapter;
import com.classic.car.R;
import com.classic.car.consts.Consts;
import com.classic.car.entity.ConsumerDetail;
import com.classic.car.utils.Util;
import com.classic.car.widget.CircleImageView;
import com.classic.core.utils.DateUtil;
import java.util.List;
import rx.functions.Action1;

/**
 * 应用名称: CarAssistant
 * 包 名 称: com.classic.car.ui.adapter
 *
 * 文件描述：TODO
 * 创 建 人：续写经典
 * 创建时间：16/6/28 下午6:22
 */
public class TimelineAdapter extends CommonRecyclerAdapter<ConsumerDetail>
        implements Action1<List<ConsumerDetail>> {
    private static final int ALPHA = 100;
    private Context mContext;
    public TimelineAdapter(Context context, int layoutResId) {
        super(context, layoutResId);
        this.mContext = context.getApplicationContext();
    }

    @Override public void onUpdate(BaseAdapterHelper helper, ConsumerDetail item, int position) {
        CircleImageView civ = helper.getView(R.id.item_timeline_icon_bg);
        int color = mContext.getResources().getColor(Util.getColorByType(item.getType()));
        civ.setFillColor(color);
        civ.setBorderColor(ColorUtils.setAlphaComponent(color, ALPHA));
        helper.setText(R.id.item_timeline_time,
                DateUtil.formatDate(DateUtil.FORMAT_DATE, item.getConsumptionTime()) + "\n" +
                        DateUtil.formatDate(DateUtil.FORMAT_TIME, item.getConsumptionTime()))
              .setImageResource(R.id.item_timeline_icon, Util.getIconByType(item.getType()))
              .setText(R.id.item_timeline_content,
                      Consts.TYPE_MENUS[item.getType()] + "\t" + Util.formatMoney(item.getMoney()))
              .setTextColorRes(R.id.item_timeline_content, Util.getColorByType(item.getType()));
    }

    @Override public void call(List<ConsumerDetail> list) {
        replaceAll(list);
    }
}
