package com.classic.car.ui.fragment;

import android.os.Bundle;
import android.support.v4.graphics.ColorUtils;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.classic.adapter.BaseAdapterHelper;
import com.classic.adapter.CommonRecyclerAdapter;
import com.classic.car.R;
import com.classic.car.consts.Consts;
import com.classic.car.entity.ConsumerDetail;
import com.classic.car.utils.Util;
import com.classic.car.widget.CircleImageView;
import com.classic.core.fragment.BaseFragment;
import com.classic.core.utils.DateUtil;

/**
 * 应用名称: CarAssistant
 * 包 名 称: com.classic.car.ui.fragment
 *
 * 文件描述：时光轴页面
 * 创 建 人：续写经典
 * 创建时间：16/5/29 下午2:21
 */
public class TimelineFragment extends BaseFragment {

    @BindView(R.id.timeline_recycler_view) RecyclerView mRecyclerView;

    public static TimelineFragment newInstance() {
        return new TimelineFragment();
    }

    @Override public int getLayoutResId() {
        return R.layout.fragment_timeline;
    }

    @Override public void initView(View parentView, Bundle savedInstanceState) {
        super.initView(parentView, savedInstanceState);
        ButterKnife.bind(this, parentView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(activity));
        mRecyclerView.setAdapter(
                new CommonRecyclerAdapter<ConsumerDetail>(activity, R.layout.item_timeline, Util.getTestData()) {
                    @Override public void onUpdate(BaseAdapterHelper helper, ConsumerDetail item, int position) {
                        CircleImageView civ = helper.getView(R.id.item_timeline_icon_bg);
                        int color = getResources().getColor(Util.getColorByType(item.getType()));
                        civ.setFillColor(color);
                        civ.setBorderColor(ColorUtils.setAlphaComponent(color, 100));
                        helper.setText(R.id.item_timeline_time,
                                DateUtil.formatDate(DateUtil.FORMAT_DATE, item.getConsumptionTime()) + "\n" +
                                        DateUtil.formatDate(DateUtil.FORMAT_TIME, item.getConsumptionTime())
                                )
                              .setImageResource(R.id.item_timeline_icon, Util.getIconByType(item.getType()))
                              .setText(R.id.item_timeline_content,
                                      Consts.TYPE_MENUS[item.getType()] + "\t" + Util.formatMoney(item.getMoney()))
                              .setTextColorRes(R.id.item_timeline_content, Util.getColorByType(item.getType()));
                    }
                });
    }
}
