package com.classic.car.ui.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.classic.adapter.CommonRecyclerAdapter;
import com.classic.android.rx.RxTransformer;
import com.classic.car.R;
import com.classic.car.app.CarApplication;
import com.classic.car.db.dao.ConsumerDao;
import com.classic.car.entity.ConsumerDetail;
import com.classic.car.ui.activity.MainActivity;
import com.classic.car.ui.adapter.TimelineAdapter;
import com.classic.car.ui.base.AppBaseFragment;
import com.classic.car.utils.Util;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import io.reactivex.disposables.Disposable;

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
        ((CarApplication) mAppContext).getDbComponent().inject(this);
        super.initView(parentView, savedInstanceState);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mAppContext));
        mRecyclerView.addOnScrollListener(new CommonRecyclerAdapter.AbsScrollControl() {
            @Override public void onShow() {
                ((MainActivity)mActivity).onShow();
            }

            @Override public void onHide() {
                ((MainActivity)mActivity).onHide();
            }
        });
        mAdapter = new TimelineAdapter(mAppContext, R.layout.item_timeline);
        mRecyclerView.setAdapter(mAdapter);

        recycle(loadData());
    }

    private Disposable loadData(){
        // TODO 检查空数据
        return mConsumerDao.queryAll()
                           .compose(RxTransformer.<List<ConsumerDetail>>applySchedulers(RxTransformer.Observable.IO_ON_UI))
                           .subscribe(mAdapter, Util.ERROR);
    }
}
