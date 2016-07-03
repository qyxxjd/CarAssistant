package com.classic.car.ui.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.classic.adapter.CommonRecyclerAdapter;
import com.classic.car.R;
import com.classic.car.app.CarApplication;
import com.classic.car.db.dao.ConsumerDao;
import com.classic.car.db.table.ConsumerTable;
import com.classic.car.entity.ConsumerDetail;
import com.classic.car.ui.activity.AddConsumerActivity;
import com.classic.car.ui.adapter.ConsumerDetailAdapter;
import com.classic.car.utils.TxtHelper;
import com.classic.core.fragment.BaseFragment;
import com.classic.core.utils.DataUtil;
import com.melnykov.fab.FloatingActionButton;
import java.util.List;
import javax.inject.Inject;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * 应用名称: CarAssistant
 * 包 名 称: com.classic.car.ui.fragment
 *
 * 文件描述：TODO
 * 创 建 人：续写经典
 * 创建时间：16/5/29 下午2:21
 */
public class MainFragment extends BaseFragment
        implements CommonRecyclerAdapter.OnItemClickListener, CommonRecyclerAdapter.OnItemLongClickListener {

    @BindView(R.id.main_recycler_view) RecyclerView         mRecyclerView;
    @BindView(R.id.main_fab)           FloatingActionButton mFab;
    @Inject ConsumerDao mConsumerDao;

    private ConsumerDetailAdapter mAdapter;

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Override public void onFirst() {
        super.onFirst();
        Observable.create(new Observable.OnSubscribe<List<ConsumerDetail>>() {
            @Override public void call(Subscriber<? super List<ConsumerDetail>> subscriber) {
                subscriber.onNext(TxtHelper.read(activity.getApplicationContext()));
            }
        })
                  .subscribeOn(Schedulers.io())
                  .observeOn(AndroidSchedulers.mainThread())
                  .unsubscribeOn(Schedulers.io())
                  .subscribe(new Action1<List<ConsumerDetail>>() {
                      @Override public void call(List<ConsumerDetail> list) {
                          if (!DataUtil.isEmpty(list)) {
                              mConsumerDao.insert(list);
                          }
                      }
                  });
    }

    @Override public int getLayoutResId() {
        return R.layout.fragment_main;
    }

    @Override public void initView(View parentView, Bundle savedInstanceState) {
        ((CarApplication)activity.getApplicationContext()).getAppComponent().inject(this);
        super.initView(parentView, savedInstanceState);
        ButterKnife.bind(this, parentView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(activity));
        mAdapter = new ConsumerDetailAdapter(activity, R.layout.item_consumer_detail);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(this);
        mAdapter.setOnItemLongClickListener(this);
        mFab.attachToRecyclerView(mRecyclerView);

        mConsumerDao.queryAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(mAdapter);
    }

    @OnClick(R.id.main_fab) public void onFabClick() {
        AddConsumerActivity.start(activity, AddConsumerActivity.TYPE_ADD, null);
    }

    @Override public void onItemClick(RecyclerView.ViewHolder viewHolder, View view, int position) {
        AddConsumerActivity.start(activity, AddConsumerActivity.TYPE_MODIFY, mAdapter.getItem(position));
    }

    @Override public void onItemLongClick(RecyclerView.ViewHolder viewHolder, View view, int position) {
        //TODO 删除
    }
}
