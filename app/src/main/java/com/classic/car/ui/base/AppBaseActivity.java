package com.classic.car.ui.base;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.classic.car.R;
import com.classic.core.activity.BaseActivity;
import com.umeng.analytics.MobclickAgent;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

public abstract class AppBaseActivity extends BaseActivity {
    @BindView(R.id.toolbar) Toolbar               mToolbar;
    protected               Context               mAppContext;
    private                 CompositeSubscription mCompositeSubscription;

    public Toolbar getToolbar() {
        return mToolbar;
    }

    @Override public void initView(Bundle savedInstanceState) {
        ButterKnife.bind(this);
        mAppContext = getApplicationContext();
        if (mToolbar == null) {
            throw new IllegalStateException("No Toolbar");
        }

        setSupportActionBar(mToolbar);

        if (canBack()) {
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    protected boolean canBack() {
        return false;
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override public void unRegister() {
        if (null != mCompositeSubscription) {
            mCompositeSubscription.unsubscribe();
        }
    }

    public void addSubscription(Subscription subscription) {
        if (null == mCompositeSubscription) {
            mCompositeSubscription = new CompositeSubscription();
        }
        mCompositeSubscription.add(subscription);
    }

    @Override protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}
