package com.classic.car.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import com.classic.adapter.BaseAdapterHelper;
import com.classic.adapter.CommonRecyclerAdapter;
import com.classic.car.R;
import com.classic.car.consts.Consts;
import com.classic.car.entity.ConsumerDetail;
import com.classic.car.utils.Util;
import com.classic.core.fragment.BaseFragment;
import com.classic.core.utils.DateUtil;
import java.util.ArrayList;
import java.util.List;

/**
 * 应用名称: CarAssistant
 * 包 名 称: com.classic.car.fragment
 *
 * 文件描述：TODO
 * 创 建 人：续写经典
 * 创建时间：16/5/29 下午2:21
 */
public class MainFragment extends BaseFragment {

    private RecyclerView mRecyclerView;

    @Override public int getLayoutResId() {
        return R.layout.fragment_main;
    }

    @Override public void initView(View parentView) {
        super.initView(parentView);
        mRecyclerView = (RecyclerView) parentView.findViewById(R.id.main_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(activity));

        mRecyclerView.setAdapter(new CommonRecyclerAdapter<ConsumerDetail>(activity,
                R.layout.item_consumer_detail, getTempData()) {
            @Override public void onUpdate(BaseAdapterHelper helper, ConsumerDetail item, int position) {
                final boolean isNotesEmpty = TextUtils.isEmpty(item.getNotes());
                helper.setText(R.id.item_consumer_detail_money, Util.formatMoney(item.getMoney()))
                        .setText(R.id.item_consumer_detail_tag, Consts.TYPE_MENUS[item.getType()])
                        .setText(R.id.item_consumer_detail_time, DateUtil.formatDate(DateUtil.FORMAT_DATE,item.getConsumptionTime()))
                        .setText(R.id.item_consumer_detail_notes, item.getNotes())
                        .setBackgroundRes(R.id.item_consumer_detail_top_layout, Util.getBackgroundByType(item.getType()))
                        .setTextColorRes(R.id.item_consumer_detail_tag, Util.getColorByType(item.getType()))
                        .setImageResource(R.id.item_consumer_detail_icon, Util.getIconByType(item.getType()))
                        .setVisible(R.id.item_consumer_detail_notes, !isNotesEmpty)
                        .setVisible(R.id.item_consumer_detail_notes_icon, !isNotesEmpty);
            }
        });
    }

    private List<ConsumerDetail> getTempData(){
        List<ConsumerDetail> data = new ArrayList<>();
        for (int i = 0; i<8; i++){
            ConsumerDetail item = new ConsumerDetail(System.currentTimeMillis(),
                    666, i);
            if(i == 1 || i==7){
                item.setNotes("致力于提供世界一流的 Android 和 iOS 应用开发服务, 因一些动画很棒的开源库为大家所熟知。");
            }
            if (i == 3 || i == 5) {
                item.setNotes(
                        "灵感UI设计网是一个致力于设计师UI设计作品分享的网站，提供很多有才华的设计师们的优秀作品，帮助设计师们获得UI方面的设计灵感，提供丰富的设计参考案例，同时还提供收费版的在线设计工具。");
            }
            data.add(item);
        }
        return data;
    }
}
