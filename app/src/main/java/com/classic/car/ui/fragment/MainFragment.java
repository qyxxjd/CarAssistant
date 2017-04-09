package com.classic.car.ui.fragment;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.classic.adapter.CommonRecyclerAdapter;
import com.classic.car.R;
import com.classic.car.app.CarApplication;
import com.classic.car.db.dao.ConsumerDao;
import com.classic.car.entity.ConsumerDetail;
import com.classic.car.ui.activity.AddConsumerActivity;
import com.classic.car.ui.activity.MainActivity;
import com.classic.car.ui.adapter.ConsumerDetailAdapter;
import com.classic.car.ui.base.AppBaseFragment;
import com.classic.car.utils.RxUtil;
import com.classic.car.utils.ToastUtil;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Subscription;

/**
 * 应用名称: CarAssistant
 * 包 名 称: com.classic.car.ui.fragment
 *
 * 文件描述：TODO
 * 创 建 人：续写经典
 * 创建时间：16/5/29 下午2:21
 */
public class MainFragment extends AppBaseFragment
        implements CommonRecyclerAdapter.OnItemClickListener, CommonRecyclerAdapter.OnItemLongClickListener {

    @BindView(R.id.main_recycler_view) RecyclerView         mRecyclerView;
    @BindView(R.id.main_fab)           FloatingActionButton mFab;
    @Inject                            ConsumerDao          mConsumerDao;

    private ConsumerDetailAdapter mAdapter;
    private int                   mFabOffset;

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Override public int getLayoutResId() {
        return R.layout.fragment_main;
    }

    @TargetApi(Build.VERSION_CODES.DONUT) @Override
    public void initView(View parentView, Bundle savedInstanceState) {
        ((CarApplication) mActivity.getApplicationContext()).getAppComponent().inject(this);
        super.initView(parentView, savedInstanceState);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mAppContext));
        mAdapter = new ConsumerDetailAdapter(mAppContext, R.layout.item_consumer_detail);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(this);
        mAdapter.setOnItemLongClickListener(this);
        mRecyclerView.addOnScrollListener(new CommonRecyclerAdapter.AbsScrollControl() {
            @Override public void onShow() {
                mFab.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).start();
                ((MainActivity)mActivity).onShow();
            }

            @Override public void onHide() {
                if(mFabOffset == 0) {
                    mFabOffset = mFab.getHeight() + mFab.getBottom();
                }
                mFab.animate().translationY(mFabOffset).setInterpolator(new AccelerateInterpolator(2));
                ((MainActivity)mActivity).onHide();
            }
        });
        addSubscription(loadData());
    }

    //@Override public void onConfigurationChanged(Configuration newConfig) {
    //    super.onConfigurationChanged(newConfig);
    //    Logger.d("onConfigurationChanged: " + newConfig.toString());
    //    addSubscription(loadData());
    //}
    //
    //@Override public void onMultiWindowModeChanged(boolean isInMultiWindowMode) {
    //    super.onMultiWindowModeChanged(isInMultiWindowMode);
    //    Logger.d("onMultiWindowModeChanged: " + isInMultiWindowMode);
    //}

    private Subscription loadData(){
        return mConsumerDao.queryAll()
                           .compose(RxUtil.<List<ConsumerDetail>>applySchedulers(RxUtil.IO_ON_UI_TRANSFORMER))
                           .subscribe(mAdapter, RxUtil.ERROR_ACTION);
    }

    @OnClick(R.id.main_fab) public void onFabClick() {
        AddConsumerActivity.start(mActivity, AddConsumerActivity.TYPE_ADD, null);
    }

    @Override public void onItemClick(RecyclerView.ViewHolder viewHolder, View view, int position) {
        AddConsumerActivity.start(mActivity, AddConsumerActivity.TYPE_MODIFY, mAdapter.getItem(position));
    }

    @Override public void onItemLongClick(RecyclerView.ViewHolder viewHolder, View view, final int position) {
        new MaterialDialog.Builder(mActivity).backgroundColorRes(R.color.white)
                                            .content(R.string.delete_dialog_content)
                                            .contentColorRes(R.color.primary_light)
                                            .positiveText(R.string.confirm)
                                            .negativeText(R.string.cancel)
                                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                                @Override
                                                public void onClick(MaterialDialog dialog, DialogAction which) {
                                                    int rows = mConsumerDao.delete(mAdapter.getItem(position).getId());
                                                    ToastUtil.showToast(mAppContext,
                                                            rows > 0 ? R.string.delete_success : R.string.delete_fail);
                                                    dialog.dismiss();
                                                }
                                            })
                                            .show();
    }
}
