package com.classic.car.ui.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import butterknife.BindView;
import com.classic.car.R;
import com.classic.car.app.CarApplication;
import com.classic.car.db.dao.ConsumerDao;
import com.classic.car.entity.ConsumerDetail;
import com.classic.car.ui.activity.MainActivity;
import com.classic.car.ui.adapter.TimelineAdapter;
import com.classic.car.ui.base.AppBaseFragment;
import com.classic.car.utils.HidingScrollListener;
import com.classic.car.utils.RxUtil;
import java.util.List;
import javax.inject.Inject;
import rx.Subscription;

/**
 * 应用名称: CarAssistant
 * 包 名 称: com.classic.car.ui.fragment
 *
 * 文件描述：时光轴页面
 * 创 建 人：续写经典
 * 创建时间：16/5/29 下午2:21
 */
public class TimelineFragment extends AppBaseFragment {

    @BindView(R.id.timeline_recycler_view) RecyclerView mRecyclerView;
    @Inject ConsumerDao     mConsumerDao;
    private TimelineAdapter mAdapter;

    public static TimelineFragment newInstance() {
        return new TimelineFragment();
    }

    @Override public int getLayoutResId() {
        return R.layout.fragment_timeline;
    }

    @Override public void initView(View parentView, Bundle savedInstanceState) {
        ((CarApplication)mActivity.getApplicationContext()).getAppComponent().inject(this);
        super.initView(parentView, savedInstanceState);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mAppContext));
        mRecyclerView.addOnScrollListener(new HidingScrollListener() {
            @Override public void onHide() {
                ((MainActivity)mActivity).onHide();
            }

            @Override public void onShow() {
                ((MainActivity)mActivity).onShow();
            }
        });
        mAdapter = new TimelineAdapter(mAppContext, R.layout.item_timeline);
        mRecyclerView.setAdapter(mAdapter);

        addSubscription(loadData());
    }

    private Subscription loadData(){
        return mConsumerDao.queryAll()
                           .compose(RxUtil.<List<ConsumerDetail>>applySchedulers(RxUtil.IO_ON_UI_TRANSFORMER))
                           .subscribe(mAdapter, RxUtil.ERROR_ACTION);
    }
}
