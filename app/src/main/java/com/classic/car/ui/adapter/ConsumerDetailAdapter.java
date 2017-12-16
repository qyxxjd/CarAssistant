package com.classic.car.ui.adapter;

import android.content.Context;
import android.text.TextUtils;

import com.classic.adapter.BaseAdapterHelper;
import com.classic.adapter.CommonRecyclerAdapter;
import com.classic.car.R;
import com.classic.car.consts.Const;
import com.classic.car.entity.ConsumerDetail;
import com.classic.car.utils.DateUtil;
import com.classic.car.utils.Util;

import java.util.List;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

/**
 * 应用名称: CarAssistant
 * 包 名 称: com.classic.car.ui.adapter
 *
 * 文件描述：TODO
 * 创 建 人：续写经典
 * 创建时间：16/6/28 下午6:22
 */
public class ConsumerDetailAdapter extends CommonRecyclerAdapter<ConsumerDetail>
        implements Consumer<List<ConsumerDetail>> {

    private final int mLayoutResId;

    public ConsumerDetailAdapter(Context context, int layoutResId) {
        super(context, layoutResId);
        this.mLayoutResId = layoutResId;
    }

    @Override public void onUpdate(BaseAdapterHelper helper, ConsumerDetail item, int position) {
        if (position == 0) { return; }
        final boolean isNotesEmpty = TextUtils.isEmpty(item.getNotes());
        helper.setText(R.id.item_consumer_detail_money, Util.formatMoney(item.getMoney()))
              .setText(R.id.item_consumer_detail_tag, Const.TYPE_MENUS[item.getType()])
              .setText(R.id.item_consumer_detail_time,
                       DateUtil.formatDate(DateUtil.FORMAT_DATE, item.getConsumptionTime()))
              .setText(R.id.item_consumer_detail_notes, item.getNotes())
              .setBackgroundRes(R.id.item_consumer_detail_top_layout, Util.getBackgroundByType(item.getType()))
              .setTextColorRes(R.id.item_consumer_detail_tag, Util.getColorByType(item.getType()))
              .setImageResource(R.id.item_consumer_detail_icon, Util.getIconByType(item.getType()))
              .setVisible(R.id.item_consumer_detail_notes, !isNotesEmpty)
              .setVisible(R.id.item_consumer_detail_notes_icon, !isNotesEmpty);
    }

    @Override public int getLayoutResId(ConsumerDetail item, int position) {
        return position == 0 ? R.layout.item_header : mLayoutResId;
    }

    @Override public void accept(@NonNull List<ConsumerDetail> list) throws Exception {
        list.add(0, new ConsumerDetail());
        replaceAll(list);
    }
}
