package com.classic.car.ui.base;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import butterknife.ButterKnife;
import com.classic.core.fragment.BaseFragment;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public abstract class AppBaseFragment extends BaseFragment {
    protected Context               mAppContext;
    private   CompositeSubscription mCompositeSubscription;

    @Override public void initView(View parentView, Bundle savedInstanceState) {
        super.initView(parentView, savedInstanceState);
        ButterKnife.bind(this, parentView);
        mAppContext = activity.getApplicationContext();
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

    protected <T> Observable<T> io(Observable<T> observable) {
        return observable.subscribeOn(Schedulers.io())
                         .observeOn(Schedulers.io())
                         .unsubscribeOn(Schedulers.io());
    }

    protected <T> Observable<T> ui(Observable<T> observable) {
        return observable.subscribeOn(Schedulers.io())
                         .observeOn(AndroidSchedulers.mainThread())
                         .unsubscribeOn(Schedulers.io());
    }
}
