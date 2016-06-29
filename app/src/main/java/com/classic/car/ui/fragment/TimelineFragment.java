package com.classic.car.ui.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.classic.car.R;
import com.classic.car.app.CarApplication;
import com.classic.car.db.dao.ConsumerDao;
import com.classic.car.ui.adapter.TimelineAdapter;
import com.classic.core.fragment.BaseFragment;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

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
    @Inject ConsumerDao     mConsumerDao;
    private TimelineAdapter mAdapter;

    public static TimelineFragment newInstance() {
        return new TimelineFragment();
    }

    @Override public int getLayoutResId() {
        return R.layout.fragment_timeline;
    }

    @Override public void initView(View parentView, Bundle savedInstanceState) {
        ((CarApplication)activity.getApplicationContext()).getAppComponent().inject(this);
        super.initView(parentView, savedInstanceState);
        ButterKnife.bind(this, parentView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(activity));

        mAdapter = new TimelineAdapter(activity, R.layout.item_timeline);
        mRecyclerView.setAdapter(mAdapter);

        mConsumerDao.queryAll()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .unsubscribeOn(Schedulers.io())
                    .subscribe(mAdapter);
    }
}
