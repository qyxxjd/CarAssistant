package com.classic.car.ui.base;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import butterknife.ButterKnife;
import com.classic.android.base.BaseFragment;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

public abstract class AppBaseFragment extends BaseFragment {
    protected Context               mAppContext;
    private   CompositeSubscription mCompositeSubscription;

    @Override public void initView(View parentView, Bundle savedInstanceState) {
        super.initView(parentView, savedInstanceState);
        ButterKnife.bind(this, parentView);
        mAppContext = mActivity.getApplicationContext();
    }

    @Override public void unRegister() {
        if (null != mCompositeSubscription) {
            mCompositeSubscription.unsubscribe();
        }
    }

    protected void addSubscription(Subscription subscription) {
        if (null == mCompositeSubscription) {
            mCompositeSubscription = new CompositeSubscription();
        }
        mCompositeSubscription.add(subscription);
    }
}
